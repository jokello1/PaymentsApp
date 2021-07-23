package com.stemsence.paymentapp.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegisterUrlRequest{

	@JsonProperty("ShortCode")
	private String shortCode;

	@JsonProperty("ConfirmationURL")
	private String confirmationURL;

	@JsonProperty("ValidationURL")
	private String validationURL;

	@JsonProperty("ResponseType")
	private String responseType;
}