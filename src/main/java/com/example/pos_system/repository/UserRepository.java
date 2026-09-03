package com.example.pos_system.repository;

import com.example.pos_system.entity.User;
import com.example.pos_system.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find by username
    Optional<User> findByUsername(String username);

    // Find by email
    Optional<User> findByEmail(String email);

    // Find all users by role
    List<User> findAllByRole(UserRole role);

    // Find all active users
    List<User> findAllByActiveTrue();

    // Check if username exists
    boolean existsByUsername(String username);

    // Check if email exists
    boolean existsByEmail(String email);

    // Custom query with LIKE
    @Query("SELECT u FROM User u WHERE u.fullName LIKE %:name%")
    List<User> searchByFullName(String name);
}