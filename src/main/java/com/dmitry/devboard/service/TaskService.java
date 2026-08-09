package com.dmitry.devboard.service;

import com.dmitry.devboard.dto.CreateTaskRequest;
import com.dmitry.devboard.dto.TaskResponse;
import com.dmitry.devboard.dto.UpdateTaskRequest;
import com.dmitry.devboard.dto.UpdateTaskStatusRequest;
import com.dmitry.devboard.entity.Task;
import com.dmitry.devboard.entity.TaskStatus;
import com.dmitry.devboard.entity.User;
import com.dmitry.devboard.exception.TaskNotFoundException;
import com.dmitry.devboard.exception.UserNotFoundException;
import com.dmitry.devboard.repository.TaskRepository;
import com.dmitry.devboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskResponse createTask(CreateTaskRequest request){
        Task task = new Task();
        User user = getCurrentUser();
        task.setUser(user);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(TaskStatus.TODO);

        Task savedTask = taskRepository.save(task);
        return new TaskResponse(savedTask.getId(),savedTask.getTitle(), savedTask.getDescription(), savedTask.getStatus(), savedTask.getCreatedAt());
    }

    public TaskResponse getTaskById(Long id){
        User user = getCurrentUser();
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Задача с id: " + id + " не найдена"));
        if(!user.getId().equals(task.getUser().getId())){
            throw new AccessDeniedException("Ошибка доступа");
        }
        return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(),task.getStatus() ,task.getCreatedAt());
    }
    
    public Page<TaskResponse> getUserTasks(Pageable pageable){
        User user = getCurrentUser();
        return taskRepository.findByUser(user, pageable)
                .map(task -> new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), task.getCreatedAt()));
    }

    public TaskResponse updateTask(Long id, UpdateTaskRequest request){
        User user = getCurrentUser();
        Task currTask = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Задача с id: " + id + " не найдена"));
        if(!currTask.getUser().getId().equals(user.getId())){
            throw new AccessDeniedException("Неккоректный айди пользователя");
        }
        currTask.setTitle(request.getTitle());
        currTask.setDescription(request.getDescription());
        taskRepository.save(currTask);
        return new TaskResponse(currTask.getId(), currTask.getTitle(), currTask.getDescription(), currTask.getStatus(), currTask.getCreatedAt());
    }

    public void deleteTask(Long id){
        User currUser = getCurrentUser();
        Task currTask = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Задача с id: " + id + " не найдена"));
        if(!currUser.getId().equals(currTask.getUser().getId())){
            throw new AccessDeniedException("Ошибка доступа");
        }
        taskRepository.delete(currTask);
    }

    public List<TaskResponse> getByUserId(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь с id " + id + " не найден"));
        return user.getTasks()
                .stream()
                .map(task -> new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), task.getCreatedAt()))
                .toList();
    }

    public TaskResponse updateStatusById(Long id, UpdateTaskStatusRequest request){
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Задача с id: " + id + " не найдена"));
        task.setStatus(request.getStatus());
        Task savedTask = taskRepository.save(task);

        return new TaskResponse(
                savedTask.getId(),
                savedTask.getTitle(),
                savedTask.getDescription(),
                savedTask.getStatus(),
                savedTask.getCreatedAt()
        );
    }

    public Page<TaskResponse> getByStatus(TaskStatus status, Pageable pageable){
        User user = getCurrentUser();
        return taskRepository.findByUserAndStatus(user ,status, pageable)
                .map(task -> new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), task.getCreatedAt()));
    }

    public Page<TaskResponse> getPages(Pageable pageable){
        User user = getCurrentUser();
        return taskRepository.findByUser(user, pageable)
                .map(task -> new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), task.getCreatedAt()));
    }

    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication.getPrincipal() == null){
             throw new AuthenticationCredentialsNotFoundException("Ошибка аунтетификации");
        }
        return (User) authentication.getPrincipal();
    }
}
