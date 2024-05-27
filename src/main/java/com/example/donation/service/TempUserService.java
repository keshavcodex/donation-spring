package com.example.donation.service;

import com.example.donation.model.TempUser;
//import com.example.donation.model.User;
import com.example.donation.repository.TempUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TempUserService {

    @Autowired
    private TempUserRepository userRepository;

    public List<TempUser> getAllUsers() {
        return userRepository.findAll();
    }

    public TempUser getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public TempUser getUserById(String id) {
        Optional<TempUser> userOptional = userRepository.findById(id);
        return userOptional.orElse(null);
    }

    public TempUser editUserById(String id, TempUser updatedUser) {
        Optional<TempUser> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            TempUser existingUser = userOptional.get();
            // Update user fields with new values
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPhone(updatedUser.getPhone());
            // Save the changes to the database
            return userRepository.save(existingUser);
        } else {
            return null; // Return null if user with the given ID does not exist
        }
    }

    public TempUser saveUser(TempUser user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusMinutes(10); // expires in 10 minutes
        user.setExpiresAt(expiresAt);

        TempUser existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser != null) {
            // Update the existing user's information
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setPassword(user.getPassword());
            existingUser.setPhone(user.getPhone());
            existingUser.setExpiresAt(expiresAt);
            return userRepository.save(existingUser);
        } else {
            // Save as a new user
            return userRepository.save(user);
        }
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
