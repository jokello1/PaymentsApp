package com.stemsence.paymentapp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "mpesa")
@Configuration
@Data
public class MpesaConfigs {

    private String c2bUrlLink;

    private String b2cUrlLink;

    private String commandId;

    private String commandId2;

    private String shortCode;

    private String authUrlLink;

    private String confirmationUrl;

    private String validationUrl;

    private String consumerKey;

    private String consumerSecret;

    private String grantType;

    private String registerUrl;

    private String oauthEndpoint;

    private String b2cTimeoutUrl;

    private String b2cResultUrl;

    private String b2cUrlEndpoint;

    private String b2cInitiatorPassword;

}
