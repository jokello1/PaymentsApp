package com.stemsence.paymentapp.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stemsence.paymentapp.config.MpesaConfigs;
import com.stemsence.paymentapp.interfaces.TimeoutPayments;
import com.stemsence.paymentapp.interfaces.ValidationRepository;
import com.stemsence.paymentapp.interfaces.WalletRepository;
import com.stemsence.paymentapp.models.Disbursement;
import com.stemsence.paymentapp.models.Validation;
import com.stemsence.paymentapp.models.Wallet;
import com.stemsence.paymentapp.requests.*;
import com.stemsence.paymentapp.responses.*;
import com.stemsence.paymentapp.utils.HelperUtility;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.bson.internal.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Objects;

import static com.stemsence.paymentapp.constants.constants.*;
import static com.stemsence.paymentapp.services.PaymentReceptor.*;

@Service
public class PaymentServices {

    @Autowired
    MpesaConfigs mpesaConfigs;
    @Autowired
    OkHttpClient okHttpClient;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ValidationRepository validationRepository;
    @Autowired
    TimeoutPayments timeoutPayments;
    @Autowired
    AcknowledgeResponse acknowledgeResponse;
    @Autowired
    WalletRepository walletRepository;

    private final Logger log = LoggerFactory.getLogger(PaymentServices.class);

    public AccessTokenResponse authenticate(){
        log.info("Authentication..");
        String encodedCredentials = HelperUtility.toBase64String(String.format("%s:%s",mpesaConfigs.getConsumerKey(),mpesaConfigs.getConsumerSecret()));
        Request request = new Request.Builder()
                .url(mpesaConfigs.getAuthUrlLink())
                .get()
                .addHeader(AUTHORIZATION_HEADER_STRING,String.format("%s %s",BASIC_AUTH_STRING,encodedCredentials))
                .addHeader(CACHE_CONTROL_HEADER, CACHE_CONTROL_HEADER_VALUE)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            return objectMapper.readValue(Objects.requireNonNull(response.body()).string(),AccessTokenResponse.class);
        } catch (IOException e) {
            log.info(e.getLocalizedMessage());
            return null;
        }
    }
    public ResponseEntity<C2BResponse> c2bIntegration(LoadWalletRequest loadWalletRequest) {
        log.info("loading wallet..");
        AccessTokenResponse accessTokenResponse = authenticate();
        //log.info(Objects.requireNonNull(registerUrl(accessTokenResponse).getBody()).toString());
        C2BRequest c2BRequest = new C2BRequest();
        String phone = loadWalletRequest.getPhone();
        if (phone.contains("+"))
            phone = phone.substring(1);
            c2BRequest.setAmount(loadWalletRequest.getAmount());
            c2BRequest.setShortCode(mpesaConfigs.getShortCode());
            c2BRequest.setBillRefNumber(loadWalletRequest.getBillRefNumber());
            c2BRequest.setMsisdn(phone);
            c2BRequest.setCommandID(mpesaConfigs.getCommandId());
        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, Objects.requireNonNull(HelperUtility.toJson(c2BRequest)));

        Request request = new Request.Builder()
                .url(mpesaConfigs.getC2bUrlLink())
                .post(body)
                .addHeader(AUTHORIZATION_HEADER_STRING,String.format("%s %s",BEARER_AUTH_STRING,accessTokenResponse.getAccessToken()))
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            return ResponseEntity.ok(objectMapper.readValue(Objects.requireNonNull(response.body()).string(),C2BResponse.class));
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
        return null;
    }
    public ResponseEntity<RegisterUrlResponse> registerUrl(){
        log.info("Registering c2b url..");
        AccessTokenResponse accessTokenResponse = authenticate();
        RegisterUrlRequest registerUrlRequest = new RegisterUrlRequest();
            registerUrlRequest.setConfirmationURL(mpesaConfigs.getConfirmationUrl());
            registerUrlRequest.setValidationURL(mpesaConfigs.getValidationUrl());
            registerUrlRequest.setShortCode(mpesaConfigs.getShortCode());
            registerUrlRequest.setResponseType("Completed");

            RequestBody requestBody = RequestBody.create(JSON_MEDIA_TYPE, Objects.requireNonNull(HelperUtility.toJson(registerUrlRequest)));
            Request request = new Request.Builder()
                    .url(mpesaConfigs.getRegisterUrl())
                    .post(requestBody)
                    .addHeader(AUTHORIZATION_HEADER_STRING,String.format("%s %s",BEARER_AUTH_STRING,accessTokenResponse.getAccessToken()))
                    .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            return ResponseEntity.ok(objectMapper.readValue(Objects.requireNonNull(response.body()).string(),RegisterUrlResponse.class));
        }catch (Exception e){
            log.error(e.getLocalizedMessage());
            return null;
        }
    }

    public ResponseEntity<AcknowledgeResponse> validation(ValidationRequest validationRequest) {
        log.info("Validating ..");
        ValidationResponse validationResponse = new ValidationResponse();
        if (validationRequest.getTransAmount()==null){
            log.info("Validation request is null");
            return ResponseEntity.ok(null);
        }
        int amount = Integer.parseInt(validationRequest.getTransAmount());
        if (amount>0){

            Validation validation = new Validation();
            String phone =validationRequest.getMSISDN();
            if (phone.contains("+"))
                phone = phone.substring(1);
                validation.setFirstName(validationRequest.getFirstName());
                validation.setLastName(validationRequest.getLastName());
                validation.setBillRefNumber(validationRequest.getBillRefNumber());
                validation.setInvoiceNumber(validationRequest.getInvoiceNumber());
                validation.setMSISDN(phone);
                validation.setOrgAccountBalance(validationRequest.getOrgAccountBalance());
                validation.setMiddleName(validationRequest.getMiddleName());
                validation.setTransAmount(validationRequest.getTransAmount());
                validation.setTransID(validationRequest.getTransID());
                validation.setTransTime(validationRequest.getTransTime());
                validation.setThirdPartyTransID(validationRequest.getThirdPartyTransID());
            validationRepository.save(validation);

            updateWallet(phone,amount,walletRepository);
                validationResponse.setResultCode(0);
                validationResponse.setResultDesc("Success");
            return ResponseEntity.ok(acknowledgeResponse);
        }
            validationResponse.setResultDesc("Failed");
            validationResponse.setResultCode(1);
        return ResponseEntity.ok(null);
    }

    public ResponseEntity<B2CResponse> purchaseQuestions(PurchaseRequest purchaseRequest){
        PayTeacherRequest payTeacherRequest = new PayTeacherRequest();
        String totalAmount = String.valueOf(purchaseContent(purchaseRequest,walletRepository));
        if (totalAmount != null && !totalAmount.equals("-1")) {
            payTeacherRequest.setTeacherPhone(purchaseRequest.getTeacherPhone());
            payTeacherRequest.setAmount(totalAmount);
        return payTeacher(payTeacherRequest);
        }
        return null;
    }
    public ResponseEntity<B2CResponse> payTeacher(PayTeacherRequest payTeacherRequest){
        log.info("Teacher payment..");
        String teacherPhone = payTeacherRequest.getTeacherPhone();
        String encodedCredentials = HelperUtility.toBase64String(String.format("%s:%s",mpesaConfigs.getConsumerKey(),mpesaConfigs.getConsumerSecret()));
        if(teacherPhone.contains("+")){
            teacherPhone=teacherPhone.substring(1);
        }
        log.info(teacherPhone);
        AccessTokenResponse accessTokenResponse = authenticate();
        B2CRequest b2CRequest = new B2CRequest();
            b2CRequest.setAmount(payTeacherRequest.getAmount());
            b2CRequest.setCommandID("BusinessPayment");
            b2CRequest.setInitiatorName(INITIATOR_NAME);
            b2CRequest.setOccassion("BusinessPayment");
            b2CRequest.setRemarks("Paying teacher for the content purchase");
            b2CRequest.setPartyA(mpesaConfigs.getShortCode());
            b2CRequest.setPartyB(teacherPhone);
            b2CRequest.setQueueTimeOutURL(mpesaConfigs.getB2cTimeoutUrl());
            b2CRequest.setResultURL(mpesaConfigs.getB2cResultUrl());
            b2CRequest.setSecurityCredential(encodedCredentials);

        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, Objects.requireNonNull(HelperUtility.toJson(b2CRequest)));
        Request request = new Request.Builder()
                .url(mpesaConfigs.getB2cUrlEndpoint())
                .post(body)
                .addHeader(AUTHORIZATION_HEADER_STRING,String.format("%s %s",BEARER_AUTH_STRING,accessTokenResponse.getAccessToken()))
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();

            return ResponseEntity.ok(objectMapper.readValue(Objects.requireNonNull(response.body()).string(),B2CResponse.class));
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
            return null;
        }
    }

    public void timeoutHook(Object object) {
        log.info(object.toString());
        //TODO save a timeout response.
        //timeoutPayments.save(errorResponse);
    }

    public ResponseEntity<AcknowledgeResponse> resultHook(SuccessB2CResponse successB2CResponse) {
        log.info(successB2CResponse.toString());
        Result result = successB2CResponse.getResult();
        if (result.getResultCode() == 0&& result.getResultType() == 2001){
            confirmTeacherPayment(result);
        }
        return ResponseEntity.ok(acknowledgeResponse);
    }
    public ResponseEntity<List<Wallet>> getAllWallet(){
        return ResponseEntity.ok(getAllWallets(walletRepository));
    }
    public ResponseEntity<List<Disbursement>> getAllDisbursement(){
        return ResponseEntity.ok(getAllDisbursements());
    }
    public String getSecurityCredentials(String initiatorPassword){

        String encryptedPassword;
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            byte[] input = initiatorPassword.getBytes();
            Resource resource = new ClassPathResource("cert.cer");
            InputStream inputStream = resource.getInputStream();
            FileInputStream fin = new FileInputStream(resource.getFile());
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding","BC");
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate certificate = (X509Certificate)cf.generateCertificate(fin);
            PublicKey publicKey = certificate.getPublicKey();
            cipher.init(Cipher.ENCRYPT_MODE,publicKey);
            byte[] cipherText = cipher.doFinal(input);
            encryptedPassword = Base64.encode(cipherText).trim();
            return encryptedPassword;
        } catch (IOException | NoSuchPaddingException | IllegalBlockSizeException | CertificateException
                | NoSuchAlgorithmException | BadPaddingException | NoSuchProviderException | InvalidKeyException e) {
            log.error(e.getLocalizedMessage());
            return null;
        }
    }
}
