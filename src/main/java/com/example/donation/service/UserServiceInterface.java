package com.example.donation.service;
import com.example.donation.model.User;
import java.util.List;

public interface UserServiceInterface {
    List<User> getAllUsers();

    User getUserByEmail(String email);

    User saveUser(User user);

    void deleteUser(String id);
}
