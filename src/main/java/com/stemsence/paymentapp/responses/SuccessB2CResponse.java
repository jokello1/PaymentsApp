package com.stemsence.paymentapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SuccessB2CResponse{

	@JsonProperty("Result")
	private Result result;
}