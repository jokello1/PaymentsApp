package com.stemsence.paymentapp.interfaces;

import com.stemsence.paymentapp.models.Validation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ValidationRepository extends MongoRepository<Validation,String> {
}
