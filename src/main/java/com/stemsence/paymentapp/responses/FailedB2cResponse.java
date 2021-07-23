package com.stemsence.paymentapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FailedB2cResponse {

	@JsonProperty("Result")
	private Result result;
}