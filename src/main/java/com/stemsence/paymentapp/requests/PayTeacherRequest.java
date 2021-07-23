package com.stemsence.paymentapp.requests;

import lombok.Data;

@Data
public class PayTeacherRequest {
    private String amount;
    private String teacherPhone;
}
