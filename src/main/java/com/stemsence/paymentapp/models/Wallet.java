package com.stemsence.paymentapp.models;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
public class Wallet {
    @Id
    private String id;
    private String phone;
    private int balance;
    private Date lastUpdated;
}
