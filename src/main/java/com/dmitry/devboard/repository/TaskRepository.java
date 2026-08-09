package com.dmitry.devboard.repository;

import com.dmitry.devboard.entity.Task;
import com.dmitry.devboard.entity.TaskStatus;
import com.dmitry.devboard.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser_Id(Long id);
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByUser(User user);
    List<Task> findByUserAndStatus(User user, TaskStatus status);
    Page<Task> findByUser(User user,Pageable pageable);
    Page<Task> findByUserAndStatus(User user, TaskStatus status, Pageable pageable);
}
