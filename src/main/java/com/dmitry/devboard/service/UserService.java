package com.dmitry.devboard.service;

import com.dmitry.devboard.dto.CreateUserRequest;
import com.dmitry.devboard.dto.UpdateUserRequest;
import com.dmitry.devboard.dto.UserResponse;
import com.dmitry.devboard.entity.User;
import com.dmitry.devboard.exception.UserAlreadyExistsException;
import com.dmitry.devboard.exception.UserNotFoundException;
import com.dmitry.devboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
    public class UserService {
    private final UserRepository userRepository;


    public UserResponse createUser(CreateUserRequest request){
        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new UserAlreadyExistsException("Email пользователя уже занят");
        }


        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(), savedUser.getCreatedAt());

    }

    public UserResponse getUserById(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь с id " + id + " не найден"));
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getCreatedAt());
    }

    public UserResponse updateUser(Long id, UpdateUserRequest request){
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь с id " + id + " не найден"));
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if(existingUser.isPresent() && !existingUser.get().getId().equals(id)){
            throw new UserAlreadyExistsException("Email пользователя уже занят");
        }
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        userRepository.save(user);
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getCreatedAt());
    }

    public void deleteUser(Long id){
        if(!userRepository.existsById(id)){
            throw new UserNotFoundException("Пользователь с id " + id + " не найден");
        }
        userRepository.deleteById(id);
    }

    public List<UserResponse> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getCreatedAt()))
                .toList();
    }

}
