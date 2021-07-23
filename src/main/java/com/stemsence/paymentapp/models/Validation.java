package com.stemsence.paymentapp.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Validation {

    @JsonProperty("BillRefNumber")
    private String billRefNumber;

    @JsonProperty("MSISDN")
    private String mSISDN;

    @JsonProperty("FirstName")
    private String firstName;

    @JsonProperty("MiddleName")
    private String middleName;

    @JsonProperty("OrgAccountBalance")
    private String orgAccountBalance;

    @JsonProperty("TransAmount")
    private String transAmount;

    @JsonProperty("ThirdPartyTransID")
    private String thirdPartyTransID;

    @JsonProperty("InvoiceNumber")
    private String invoiceNumber;

    @JsonProperty("LastName")
    private String lastName;

    @JsonProperty("TransID")
    private String transID;

    @JsonProperty("TransTime")
    private String transTime;
}
