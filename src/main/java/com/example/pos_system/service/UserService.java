package com.example.pos_system.service;

import com.example.pos_system.entity.UserRole;
import com.example.pos_system.entity.User;
import com.example.pos_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    // CREATE - Save a new user
    public User createUser(String username, String email, String password,
                           String fullName, UserRole role) {
        log.info("Creating new user: {}", username);

        // Business validations
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists: " + username);
        }

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists: " + email);
        }

        // Create user object
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);  // TODO: Encrypt password later!
        user.setFullName(fullName);
        user.setRole(role != null ? role : UserRole.CASHIER);

        // Save to database
        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {}", savedUser.getId());

        return savedUser;
    }

    // READ - Get all users
    @Transactional(readOnly = true)  // Read-only transaction (better performance)
    public List<User> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAllByActiveTrue();
    }

    // READ - Get user by ID
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        log.info("Fetching user by ID: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    // READ - Get user by username
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        log.info("Fetching user by username: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    // UPDATE - Update user details
    public User updateUser(Long id, String email, String fullName) {
        log.info("Updating user: {}", id);

        User user = getUserById(id);  // Reuse the getter

        if (email != null && !email.isEmpty()) {
            // Check if email is taken by another user
            if (!email.equals(user.getEmail()) && userRepository.existsByEmail(email)) {
                throw new RuntimeException("Email already exists: " + email);
            }
            user.setEmail(email);
        }

        if (fullName != null && !fullName.isEmpty()) {
            user.setFullName(fullName);
        }

        return userRepository.save(user);
    }

    // UPDATE - Change user role
    public User changeUserRole(Long id, UserRole newRole) {
        log.info("Changing role for user {} to: {}", id, newRole);

        User user = getUserById(id);
        user.setRole(newRole);

        return userRepository.save(user);
    }

    // DELETE - Soft delete (deactivate)
    public void deactivateUser(Long id) {
        log.info("Deactivating user: {}", id);

        User user = getUserById(id);
        user.setActive(false);
        userRepository.save(user);

        log.info("User deactivated successfully");
    }
}