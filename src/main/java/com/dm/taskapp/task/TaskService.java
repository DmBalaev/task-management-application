package com.dm.taskapp.task;

import com.dm.taskapp.app.ApiResponse;
import com.dm.taskapp.task.enums.TaskPriority;
import com.dm.taskapp.task.enums.TaskStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface TaskService {
    Task create(TaskRequest request, UserDetails userDetails);
    Task read(Long id);
    List<Task> readAll(Pageable pageable);
    List<Task> readByStatus(Pageable pageable, String status);
    List<Task> readByPriority (Pageable pageable, String priority);
    Task updateTask(TaskUpdateRequest request, UserDetails userDetails);
    ApiResponse deleteTask(Long taskId, UserDetails userDetails);
    Task assignTask(Long taskId, Long accountId, UserDetails userDetails);
    ApiResponse unsignTask(Long taskId, UserDetails userDetails);
    ApiResponse changeStatus(Long taskId, TaskStatus status, UserDetails userDetails);
    ApiResponse changePriority(Long taskId, TaskPriority priority, UserDetails userDetails);
    List<Task> tasksByAuthor(Long accountId, Pageable pageable);
    List<Task> tasksByAssignee(Long accountId, Pageable pageable);
}
