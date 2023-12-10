package com.dm.taskapp.task;

import com.dm.taskapp.app.ApiResponse;
import com.dm.taskapp.task.enums.TaskPriority;
import com.dm.taskapp.task.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

public interface TaskService {
    Task create(TaskRequest request, UserDetails userDetails);
    Task read(Long id);
    Page<Task> readAll(Pageable pageable);
    Page<Task> readByStatus(Pageable pageable, String status);
    Page<Task> readByPriority (Pageable pageable, String priority);
    Task updateTask(TaskUpdateRequest request);
    ApiResponse deleteTask(Long taskId);
    Task assignTask(Long taskId, Long accountId);
    ApiResponse unsignTask(Long taskId);
    ApiResponse changeStatus(Long taskId, TaskStatus status, UserDetails userDetails);
    ApiResponse changePriority(Long taskId, TaskPriority priority);
    Page<Task> tasksByAuthor(Long accountId, Pageable pageable);
    Page<Task> tasksByAssignee(Long accountId, Pageable pageable);
}
