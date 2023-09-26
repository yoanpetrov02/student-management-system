package com.yoanpetrov.studentmanagementsystem.service;

import com.yoanpetrov.studentmanagementsystem.model.User;
import com.yoanpetrov.studentmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository repository;

    public User createUser(User user) {
        return repository.save(user);
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public Optional<User> getUserById(int id) {
        return repository.findById(id);
    }

    public User updateUser(int id, User userDetails) {
        Optional<User> user = repository.findById(id);
        if (user.isEmpty()) {
            return null;
        }
        User existingUser = user.get();
        existingUser.setFirstName(userDetails.getFirstName());
        existingUser.setLastName(userDetails.getLastName());
        existingUser.setEmail(userDetails.getEmail());
        existingUser.setPassword(userDetails.getPassword());
        existingUser.setRole(userDetails.getRole());

        return repository.save(existingUser);
    }

    public void deleteAllUsers() {
        repository.deleteAll();
    }

    public void deleteUser(int id) {
        repository.deleteById(id);
    }
}
