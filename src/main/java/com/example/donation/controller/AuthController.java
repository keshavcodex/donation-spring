package com.example.donation.controller;

import com.example.donation.model.OTP;
import com.example.donation.model.TempUser;
import com.example.donation.model.User;
import com.example.donation.model.UserResponse;
import com.example.donation.service.EmailService;
import com.example.donation.service.OTPService;
import com.example.donation.service.TempUserService;
import com.example.donation.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://192.168.29.215:3000"})
public class AuthController {


    @Autowired
    private UserService userService;

    @Autowired
    private TempUserService tempUserService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OTPService otpService;

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody User loginRequest) {
        try {
            User user = userService.getUserByEmail(loginRequest.getEmail().toLowerCase());
            if (user == null || !loginRequest.getPassword().equals(user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new UserResponse(null, "Invalid email or password!"));
            }
            user.setPassword("");
            return ResponseEntity.ok(new UserResponse(user));
        } catch (Exception e) {
            // Log the exception (logging framework would be used in a real application)
            return ResponseEntity.internalServerError().body(new UserResponse(null, "Internal server error"));
        }
    }


    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@RequestBody TempUser user) {
        try {
            user.setEmail(user.getEmail().toLowerCase());
            User existingUser = userService.getUserByEmail(user.getEmail());
            if (existingUser != null) {
                return ResponseEntity.badRequest().body(new UserResponse(null, "Email address already exists!"));
            }
            tempUserService.saveUser(user);
            OTP otp = otpService.generateOTP(user.getEmail());
            emailService.sendEmail(user.getEmail(), "Donation app OTP", "Let's take a step together to save animals.\n\nYour OTP code is: " + otp.getOtp());
            return ResponseEntity.ok(new UserResponse(null, "OTP sent to your email address, it will expire in 10 minutes", true));
        } catch (Exception e) {
            // Log the exception (logging framework would be used in a real application)
            return ResponseEntity.internalServerError().body(new UserResponse(null, "User registration failed!"));
        }
    }

    @PostMapping("/validate-login")
    public ResponseEntity<UserResponse> validateLogin(@RequestBody OTP request) {
        System.out.println("body: " + request.getEmail() + ", " + request.getOtp());
        boolean isValid = otpService.validateOTP(request.getEmail(), request.getOtp());

        if (isValid) {
            otpService.deleteOTP(request.getEmail());
            User user = userService.getUserByEmail(request.getEmail().toLowerCase());
            return ResponseEntity.ok(new UserResponse(user, ""));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UserResponse(null, "Invalid OTP or OTP has expired"));
        }
    }

    @PostMapping("/validate-signup")
    public ResponseEntity<UserResponse> validateSignUp(@RequestBody OTP request) {
        request.setEmail(request.getEmail().toLowerCase());
        System.out.println("body: " + request.getEmail() + ", " + request.getOtp());
        boolean isValid = otpService.validateOTP(request.getEmail(), request.getOtp());

        TempUser tempUser = tempUserService.getUserByEmail(request.getEmail());

        if (isValid && tempUser != null) {
            User user = new User(tempUser);
            userService.saveUser(user);
            tempUserService.deleteUser(tempUser.getId());
            otpService.deleteOTP(request.getEmail());
            return ResponseEntity.ok(new UserResponse(user, ""));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UserResponse(null, "Invalid OTP or OTP has expired"));
        }
    }


    @GetMapping("/getUser/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") String id) {
        try {
            User user = userService.getUserById(id);
            if (user != null) {
                user.setPassword("");
                return ResponseEntity.ok(new UserResponse(user, ""));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UserResponse(null, "User not found"));
            }
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UserResponse(null, "Oops we did something wrong!"));
        }
    }

    @PutMapping("/editUser/{id}")
    public ResponseEntity<UserResponse> editUser(@PathVariable("id") String id, @RequestBody User updatedUser) {
        try {
            User editedUser = userService.editUserById(id, updatedUser);
            if (editedUser != null) {
                return ResponseEntity.ok(new UserResponse(editedUser, ""));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UserResponse(null, "User not found"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            for (User user : users) user.setPassword("");
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable("id") String id) {
        try {
            userService.deleteUser(id); // Call the deleteUser service method
            return ResponseEntity.ok(new UserResponse(null, "", true)); // Return 204 No Content if deletion is successful
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UserResponse(null, "Failed to delete user" + e, false));
            // Return 500 Internal Server Error if an unexpected error occurs
        }
    }

}
