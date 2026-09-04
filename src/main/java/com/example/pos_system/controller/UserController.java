package com.example.pos_system.controller;

import com.example.pos_system.dto.UserRequest;
import com.example.pos_system.dto.UserResponse;
import com.example.pos_system.entity.User;
import com.example.pos_system.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Slf4j
@RequiredArgsConstructor

public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
        log.info("POST /api/users - Creating new user: {}", request.getUsername());
        
        User user = userService.createUser(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getFullName(), 
                request.getRole()
        );
        return  ResponseEntity.status(HttpStatus.CREATED).body(toResponse(user));
    }


    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        log.info("GET /api/users - Fetching all users");

        List<User> users = userService.getAllUsers();
        List<UserResponse> responses = users.stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        log.info("GET /api/users/{} - Fetching user by ID", id);

        User user = userService.getUserById(id);
        return ResponseEntity.ok(toResponse(user));
    }


    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        log.info("GET /api/users/username/{} - Fetching user by username", username);

        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(toResponse(user));
    }

//    @GetMapping("/search")
//    public ResponseEntity<List<UserResponse>> searchUsers(@RequestParam String keyword) {
//        log.info("GET /api/users/search - Searching users with keyword: {}", keyword);
//
//        List<User> users = userService.searchUsers(keyword);
//        List<UserResponse> responses = users.stream()
//                .map(this::toResponse)
//                .toList();
//
//        return ResponseEntity.ok(responses);
//    }


    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequest request) {
        log.info("PUT /api/users/{} - Updating user", id);

        User user = userService.updateUser(
                id,
                request.getEmail(),
                request.getFullName()
        );

        return ResponseEntity.ok(toResponse(user));
    }

//    @PatchMapping("/{id}/role")
//    public ResponseEntity<UserResponse> changeUserRole(
//            @PathVariable Long id,
//            @RequestParam UserRole role) {
//        log.info("PATCH /api/users/{}/role - Changing user role to: {}", id, role);
//
//        User user = userService.changeUserRole(id, role);
//        return ResponseEntity.ok(toResponse(user));
//    }


    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        log.info("PATCH /api/users/{}/deactivate - Deactivating user", id);

        userService.deactivateUser(id);
        return ResponseEntity.noContent().build();  // HTTP 204
    }


//    @PatchMapping("/{id}/activate")
//    public ResponseEntity<Void> activateUser(@PathVariable Long id) {
//        log.info("PATCH /api/users/{}/activate - Activating user", id);
//
//        userService.activateUser(id);
//        return ResponseEntity.noContent().build();  // HTTP 204
//    }



    private UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        response.setRole(user.getRole());
        response.setActive(user.isActive());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }

}
