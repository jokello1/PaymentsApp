package com.stemsence.paymentapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AcknowledgeResponse{

	@JsonProperty("message")
	private String message;
}