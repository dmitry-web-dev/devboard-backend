package com.dmitry.devboard.repository;

import com.dmitry.devboard.entity.Task;
import com.dmitry.devboard.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser_Id(Long id);
    List<Task> findByStatus(TaskStatus status);
}
