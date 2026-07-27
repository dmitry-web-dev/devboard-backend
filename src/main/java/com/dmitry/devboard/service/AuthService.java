package com.dmitry.devboard.service;

import com.dmitry.devboard.dto.LoginRequest;
import com.dmitry.devboard.dto.RegisterRequest;
import com.dmitry.devboard.dto.UserResponse;
import com.dmitry.devboard.entity.User;
import com.dmitry.devboard.exception.IncorrectPasswordException;
import com.dmitry.devboard.exception.UserAlreadyExistsException;
import com.dmitry.devboard.exception.UserNotFoundException;
import com.dmitry.devboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse registerUser(RegisterRequest request){
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new UserAlreadyExistsException("Пользователь с такой почтой уже существует");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);

        return new UserResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(), savedUser.getCreatedAt());
    }

    public UserResponse authUser(LoginRequest request){
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UserNotFoundException("Пользователя не существует"));
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new IncorrectPasswordException("Неверный пароль");
        }
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getCreatedAt());
    }


}
