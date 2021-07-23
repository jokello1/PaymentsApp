package com.stemsence.paymentapp.services;


import com.stemsence.paymentapp.interfaces.DisbursementRepository;
import com.stemsence.paymentapp.interfaces.WalletRepository;
import com.stemsence.paymentapp.models.Disbursement;
import com.stemsence.paymentapp.models.Wallet;
import com.stemsence.paymentapp.requests.PurchaseRequest;
import com.stemsence.paymentapp.responses.Result;
import com.stemsence.paymentapp.utils.DisbursementUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.stemsence.paymentapp.constants.constants.*;

public class PaymentReceptor {
    static Logger log = LoggerFactory.getLogger(PaymentReceptor.class);

    @Autowired
    static DisbursementRepository disbursementRepository;

    public static List<Wallet> getAllWallets(WalletRepository walletRepository){
        return walletRepository.findAll();
    }

    public static List<Disbursement> getAllDisbursements(){
        return disbursementRepository.findAll();
    }
    public static void updateWallet(String phone, int amount,WalletRepository walletRepository) {

        Wallet wallet = new Wallet();
        if (walletRepository==null)
        walletRepository.save(wallet);
        Optional<Wallet> optionalWallet = walletRepository.findByPhone(phone);
        if (optionalWallet.isPresent()){
             wallet = optionalWallet.get();
            int balance = wallet.getBalance()+amount;
            wallet.setBalance(balance);
            walletRepository.save(wallet);
            log.info("Wallet updated successfully.");
        }else {
            wallet.setBalance(amount);
            wallet.setLastUpdated(new Date());
            wallet.setPhone(phone);
            walletRepository.save(wallet);
            log.info("Wallet created successfully.");
        }
    }

    public static int purchaseContent(PurchaseRequest purchaseRequest,WalletRepository walletRepository){
        log.info("Purchase of content..");
        Wallet wallet1 = new Wallet();
        if (walletRepository==null)
        walletRepository.save(wallet1);
        Optional<Wallet> optionalWallet = walletRepository.findByPhone(purchaseRequest.getPhone());
        if (optionalWallet.isPresent()){
            Wallet wallet = optionalWallet.get();
            int balance = wallet.getBalance();
            int calculatedAmount = QUESTION_PRICE * purchaseRequest.getQuestions();
            if (balance >= calculatedAmount){
                balance = balance-calculatedAmount;
                wallet.setBalance(balance);
                walletRepository.save(wallet);
                String teacherAmount = disbursePayment(calculatedAmount, purchaseRequest.getTeacherPhone(), purchaseRequest.getPhone());
                return Integer.parseInt(teacherAmount);
            }else {
                return -1;
            }
        }
        return -1;
    }
    public static String disbursePayment(int amount, String teacherPhone, String studentId){
        log.info("Payment disburse..");
        DisbursementUtil disbursementObject = new DisbursementUtil(amount);
        String teacherAmount = String.valueOf(disbursementObject.getTeacherAmount());
        Disbursement disbursement = new Disbursement();
            disbursement.setDutyAmount(String.valueOf(disbursementObject.getDutyAmount()));
            disbursement.setDateOfTransaction(new Date());
            disbursement.setStemAmount(String.valueOf(disbursementObject.getStemAmount()));
            disbursement.setTeacherAmount(teacherAmount);
            disbursement.setStudentId(studentId);
            disbursement.setTeacherPhone(teacherPhone);
            disbursement.setTotalAmount(String.valueOf(amount));
            disbursement.setTeacherPayStatus("Pending");
        disbursementRepository.save(disbursement);
        return teacherAmount;
    }

    public static void confirmTeacherPayment(Result result){
        Optional<Disbursement> optionalDisbursement = disbursementRepository.findByOriginatorConversationID(result.getOriginatorConversationID());
        if (optionalDisbursement.isPresent()){
            log.info("Teacher payment confirmation..");
            optionalDisbursement.get().setTeacherPayStatus("Confirmed");
        disbursementRepository.save(optionalDisbursement.get());
        }
    }

}
