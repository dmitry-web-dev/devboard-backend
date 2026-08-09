package com.dmitry.devboard.controller;

import com.dmitry.devboard.dto.CreateTaskRequest;
import com.dmitry.devboard.dto.TaskResponse;
import com.dmitry.devboard.dto.UpdateTaskRequest;
import com.dmitry.devboard.dto.UpdateTaskStatusRequest;
import com.dmitry.devboard.entity.TaskStatus;
import com.dmitry.devboard.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public TaskResponse createTask(@Valid @RequestBody CreateTaskRequest request){
        return taskService.createTask(request);
    }

    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Long id){
        return taskService.getTaskById(id);
    }

    @PutMapping("/{id}")
    public TaskResponse updateTask(@PathVariable Long id, @Valid @RequestBody UpdateTaskRequest request){
        return taskService.updateTask(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public TaskResponse updateStatusById(@PathVariable Long id,@Valid @RequestBody UpdateTaskStatusRequest request){
        return taskService.updateStatusById(id, request);
    }

    @GetMapping
    public Page<TaskResponse> getAllOrFilteredTasks(@RequestParam(required = false) TaskStatus status, Pageable pageable){
        if(status == null){
            return taskService.getUserTasks(pageable);
        }
        return taskService.getByStatus(status, pageable);
    }

    @GetMapping("/pages")
    public Page<TaskResponse> getPages(@PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        return taskService.getPages(pageable);
    }
}
