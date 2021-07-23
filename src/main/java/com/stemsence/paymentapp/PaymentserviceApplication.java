package com.stemsence.paymentapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stemsence.paymentapp.responses.AcknowledgeResponse;
import okhttp3.OkHttpClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PaymentserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentserviceApplication.class, args);
	}

	@Bean
	public ObjectMapper getObjectMapper(){
		return new ObjectMapper();
	}
	@Bean
	public OkHttpClient getOkHttpClient(){
		return new OkHttpClient();
	}
	@Bean
	public AcknowledgeResponse getAcknowledgement(){
		AcknowledgeResponse acknowledgeResponse = new AcknowledgeResponse();
		acknowledgeResponse.setMessage("success");
		return acknowledgeResponse;

	}
}
