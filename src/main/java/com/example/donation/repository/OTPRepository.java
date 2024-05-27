package com.example.donation.repository;

import com.example.donation.model.OTP;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OTPRepository extends MongoRepository<OTP, String> {
    Optional<OTP> findByUserEmail(String email);
    Optional<OTP> findByUserEmailAndOtp(String email, String otp);
}
