package com.stemsence.paymentapp.interfaces;

import com.stemsence.paymentapp.models.Wallet;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface WalletRepository extends MongoRepository<Wallet,String> {
    Optional<Wallet> findByPhone(String phone);
}
