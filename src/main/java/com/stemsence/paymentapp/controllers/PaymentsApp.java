package com.stemsence.paymentapp.controllers;

import com.stemsence.paymentapp.models.Disbursement;
import com.stemsence.paymentapp.models.Wallet;
import com.stemsence.paymentapp.requests.LoadWalletRequest;
import com.stemsence.paymentapp.requests.PayTeacherRequest;
import com.stemsence.paymentapp.requests.PurchaseRequest;
import com.stemsence.paymentapp.requests.ValidationRequest;
import com.stemsence.paymentapp.responses.*;
import com.stemsence.paymentapp.services.PaymentServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/stemscence")
@RestController
public class PaymentsApp {

    @Autowired
    PaymentServices paymentServices;

    @GetMapping("/hello")
    public String hello() {
        return "Hello RESTEasy";
    }

    @GetMapping("/allWallets")
    public ResponseEntity<List<Wallet>> getAllWallets() {
        return paymentServices.getAllWallet();
    }

    @GetMapping("/allDisbursements")
    public ResponseEntity<List<Disbursement>> getAllDisbursements() {
        return paymentServices.getAllDisbursement();
    }

    @PostMapping
    public ResponseEntity<C2BResponse> c2bIntegration(@RequestBody LoadWalletRequest loadWalletRequest){
        return paymentServices.c2bIntegration(loadWalletRequest);
    }
    @PostMapping("/validation")
    public ResponseEntity<AcknowledgeResponse> validations(@RequestBody ValidationRequest validationRequest){
        return paymentServices.validation(validationRequest);
    }
    @PostMapping("/confirmation")
    public ResponseEntity<AcknowledgeResponse> confirmation(@RequestBody ValidationRequest validationRequest){
        return paymentServices.validation(validationRequest);
    }
    @PostMapping("/registerUrl")
    public ResponseEntity<RegisterUrlResponse> registerUrl(){
        return paymentServices.registerUrl();
    }
    @PostMapping("/purchaseQuestion")
    public ResponseEntity<B2CResponse> purchaseQuestions(@RequestBody PurchaseRequest purchaseRequest){
        return paymentServices.purchaseQuestions(purchaseRequest);
    }
    @PostMapping("/timeout")
    public void timeoutHook(@RequestBody Object response){
         paymentServices.timeoutHook(response);
    }

    @PostMapping("/result")
    public ResponseEntity<AcknowledgeResponse> resultsHook(@RequestBody SuccessB2CResponse b2CResponse){
        return paymentServices.resultHook(b2CResponse);
    }
}