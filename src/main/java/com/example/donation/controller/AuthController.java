package com.example.donation.controller;

import com.example.donation.model.User;
import com.example.donation.model.UserResponse;
import com.example.donation.service.EmailService;
import com.example.donation.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class AuthController {


    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

//    @CrossOrigin(origins = {"http://localhost:3000", "http://192.168.29.215:3000"})

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody User loginRequest) {
        try {
            User user = userService.getUserByEmail(loginRequest.getEmail());
            if (user == null || !loginRequest.getPassword().equals(user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new UserResponse(null, "Invalid email or password!"));
            }
            user.setPassword("");
//            emailService.sendEmail(user.getEmail(), "Welcome to Donation App", "Lets give animals a voice. Donate today.");
            return ResponseEntity.ok(new UserResponse(user, ""));
        } catch (Exception e) {
            // Log the exception (logging framework would be used in a real application)
            return ResponseEntity.internalServerError().body(new UserResponse(null, "Internal server error"));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@RequestBody User user) {
        try {
            userService.saveUser(user);
//            emailService.sendEmail(user.getEmail(), "Welcome to Donation App", "Thank you for signing up!");
            return ResponseEntity.ok(new UserResponse(user, ""));
        } catch (DuplicateKeyException e) {  // Example: Catch duplicate email
            return ResponseEntity.badRequest().body(new UserResponse(null, "Email address already exists!"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new UserResponse(null, "User registration failed!"));
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
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String id) {
        try {
            userService.deleteUser(id); // Call the deleteUser service method
            return ResponseEntity.noContent().build(); // Return 204 No Content if deletion is successful
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            // Return 500 Internal Server Error if an unexpected error occurs
        }
    }

}
