package com.stemsence.paymentapp.models;

import lombok.Data;

import java.util.Date;

@Data
public class Deposit {
    private String studentId;
    private Date dateOfTransaction;
    private String id;
    private String amount;
    private String balance;
}
