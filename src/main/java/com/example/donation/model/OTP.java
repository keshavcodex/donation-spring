package com.example.donation.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "otp")
public class OTP {
    @Id
    private String id;
    private String otp;

    @Indexed(unique = true)
    private String userEmail;

    private String email;

    @Indexed(name = "expiresAt", expireAfterSeconds = 0)
    private LocalDateTime expiresAt;

    public OTP(String otp, String userEmail,  LocalDateTime expiresAt) {
        this.otp = otp;
        this.userEmail = userEmail;
        this.expiresAt = expiresAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    public String getEmail() {
        return userEmail;
    }

    public void setEmail(String userEmail) {
        this.userEmail = userEmail;
    }


    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OTP{");
        sb.append("id='").append(id).append('\'');
        sb.append(", otp='").append(otp).append('\'');
        sb.append(", email='").append(userEmail).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
