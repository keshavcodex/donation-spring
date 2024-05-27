package com.example.donation.repository;

import com.example.donation.model.TempUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TempUserRepository extends MongoRepository<TempUser, String> {
    TempUser findByEmail(String email);
}
