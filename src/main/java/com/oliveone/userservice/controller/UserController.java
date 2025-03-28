package com.oliveone.userservice.controller;

import com.oliveone.userservice.dto.UserDto;
import com.oliveone.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto.UserInfoResponse> registerUser(@Valid @RequestBody UserDto.UserRegistrationRequest request) {
        UserDto.UserInfoResponse registeredUser = userService.registerUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto.UserInfoResponse> getUserById(@PathVariable Long userId) {
        UserDto.UserInfoResponse user = userService.getUserById(userId);

        return ResponseEntity.ok(user);
    }
}
