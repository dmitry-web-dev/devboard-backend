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
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskResponse createTask(CreateTaskRequest request){
        Task task = new Task();
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new UserNotFoundException("Пользователь с id " + request.getUserId() + " не найден"));
        task.setUser(user);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(TaskStatus.TODO);

        Task savedTask = taskRepository.save(task);
        return new TaskResponse(savedTask.getId(),savedTask.getTitle(), savedTask.getDescription(), savedTask.getStatus(), savedTask.getCreatedAt());
    }

    public TaskResponse getTaskById(Long id){
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Задача с id: " + id + " не найдена"));
        return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(),task.getStatus() ,task.getCreatedAt());
    }
    
    public List<TaskResponse> getAllTasks(){
        return taskRepository.findAll()
                .stream()
                .map(task -> new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), task.getCreatedAt()))
                .toList();
    }

    public TaskResponse updateTask(Long id, UpdateTaskRequest request){
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Задача с id: " + id + " не найдена"));
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        taskRepository.save(task);
        return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), task.getCreatedAt());
    }

    public void deleteTask(Long id){
        if(taskRepository.findById(id).isEmpty()){
            throw new TaskNotFoundException("Задача с id: " + id + " не найдена");
        }
        taskRepository.deleteById(id);
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

    public List<TaskResponse> getByStatus(TaskStatus status){
        return taskRepository.findByStatus(status).stream()
                .map(task -> new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), task.getCreatedAt()))
                .toList();
    }

    public Page<TaskResponse> getPages(Pageable pageable){
        return taskRepository.findAll(pageable)
                .map(task -> new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), task.getCreatedAt()));
    }
}
