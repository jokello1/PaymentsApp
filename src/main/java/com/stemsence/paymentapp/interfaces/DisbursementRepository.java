package com.stemsence.paymentapp.interfaces;

import com.stemsence.paymentapp.models.Disbursement;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DisbursementRepository extends MongoRepository<Disbursement,String> {
    Optional<Disbursement> findByOriginatorConversationID(String originatorConversationID);
}
