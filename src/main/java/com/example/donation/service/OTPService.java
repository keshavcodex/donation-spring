package com.example.donation.service;

import com.example.donation.model.OTP;
import com.example.donation.repository.OTPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OTPService {
    @Autowired
    private OTPRepository otpRepository;

    public OTP generateOTP(String email) {
        String otp = new DecimalFormat("000000").format(new Random().nextInt(1000000));
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusMinutes(10); // OTP valid for 10 minutes

        Optional<OTP> existingOtp = otpRepository.findByUserEmail(email);

        OTP otpEntity;
        if (existingOtp.isPresent()) {
            otpEntity = existingOtp.get();
            otpEntity.setOtp(otp);
            otpEntity.setExpiresAt(expiresAt);
        } else {
            otpEntity = new OTP(otp, email, expiresAt);
        }

        otpRepository.save(otpEntity);
        return otpEntity;
    }

    public boolean validateOTP(String email, String otp) {
        OTP otpEntity = otpRepository.findByUserEmailAndOtp(email, otp)
                .orElse(null);

        if (otpEntity == null) {
            return false;
        }

        if (otpEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            return false;
        }

        return true;
    }

    public void deleteOTP(String email) {
        OTP otpEntity = otpRepository.findByUserEmail(email)
                .orElse(null);

        if (otpEntity != null) {
            otpRepository.delete(otpEntity);
        }
    }
}
