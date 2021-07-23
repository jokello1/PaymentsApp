package com.stemsence.paymentapp.requests;

import lombok.Data;

import java.util.Date;

@Data
public class LoadWalletRequest {
    private String studentId;
    private String teacherId;
    private String elcId;
    private String amount;
    private String phone;
    private String billRefNumber;
}
