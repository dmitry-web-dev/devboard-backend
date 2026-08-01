package com.dmitry.devboard.controller;

import com.dmitry.devboard.dto.LoginRequest;
import com.dmitry.devboard.dto.LoginResponse;
import com.dmitry.devboard.dto.RegisterRequest;
import com.dmitry.devboard.dto.UserResponse;
import com.dmitry.devboard.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public UserResponse registerUser(@Valid @RequestBody RegisterRequest request){
        return authService.registerUser(request);
    }

    @PostMapping("/login")
    public LoginResponse authUser(@Valid @RequestBody LoginRequest request){
        return authService.authUser(request);
    }

}
