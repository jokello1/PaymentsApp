package com.stemsence.paymentapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResultParameterItem{

	@JsonProperty("Value")
	private int value;

	@JsonProperty("Key")
	private String key;
}