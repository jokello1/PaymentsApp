package com.stemsence.paymentapp.requests;

import lombok.Data;

@Data
public class PurchaseRequest {
    private int questions;
    private String phone;
    private String teacherPhone;
}
