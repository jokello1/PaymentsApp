package com.stemsence.paymentapp.models;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
public class Disbursement {
    @Id
    private String id;
    private String teacherPhone;
    private String totalAmount;
    private String teacherAmount;
    private Date dateOfTransaction;
    private String dutyAmount;
    private String studentId;
    private String stemAmount;
    private String teacherPayStatus;
    private String conversationID;
    private String originatorConversationID;

}
