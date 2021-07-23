package com.stemsence.paymentapp.interfaces;

import com.stemsence.paymentapp.responses.ErrorResponse;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TimeoutPayments extends MongoRepository<ErrorResponse,String> {
}
