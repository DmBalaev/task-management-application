package com.dm.taskapp.task;

import com.dm.taskapp.account.AccountRepository;
import com.dm.taskapp.app.ApiResponse;
import com.dm.taskapp.exceptions.ApiException;
import com.dm.taskapp.exceptions.ResourceNotFound;
import com.dm.taskapp.task.enums.TaskPriority;
import com.dm.taskapp.task.enums.TaskStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.dm.taskapp.task.enums.TaskPriority.isPriority;
import static com.dm.taskapp.task.enums.TaskStatus.isTaskStatus;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService{
    private final TaskRepository taskRepository;
    private final AccountRepository accountRepository;

    @Override
    public Task updateTask(TaskUpdateRequest request) {
        var task = findTask(request.getId());
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        log.info("Update task with {} id", request.getId());

        return taskRepository.save(task);
    }

    @Override
    public ApiResponse deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
        log.info("Delete task with {} id", taskId);

        return new ApiResponse("You successfully delete task");
    }

    @Override
    public Task assignTask(Long taskId, Long accountId) {
        var account = accountRepository.findById(accountId)
                .orElseThrow(()-> new ResourceNotFound("Account not found"));
        Task task = findTask(taskId);
        task.setAssignee(account);
        task.setStatus(TaskStatus.IN_PROGRESS);
        log.info("Assign task {} -> account {}", taskId, accountId );

        return taskRepository.save(task);
    }

    @Override
    public ApiResponse unsignTask(Long taskId) {
        var task = findTask(taskId);
        task.setAssignee(null);
        taskRepository.save(task);
        log.info("Unsign task {}", taskId );

        return new ApiResponse("You successfully unsign task");
    }

    @Override
    public ApiResponse changeStatus(Long taskId, TaskStatus status, UserDetails userDetails) {
        var task = findTask(taskId);
        var account = accountRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(()-> new ResourceNotFound("Account not found"));

        if (!Objects.equals(account.getId(), task.getAuthor().getId()) && !status.equals(TaskStatus.RESOLVED)){
            throw  new ApiException("Do you not have permissions", HttpStatus.FORBIDDEN);
        }

        task.setStatus(status);
        taskRepository.save(task);
        log.info("Task {} changed status", taskId );

        return new ApiResponse("You successfully change status");
    }

    @Override
    public ApiResponse changePriority(Long taskId, TaskPriority priority) {
        var task = findTask(taskId);
        task.setPriority(priority);
        taskRepository.save(task);

        return new ApiResponse("You successfully change priority");
    }

    @Override
    public Task create(TaskRequest request, UserDetails userDetails ) {
        var account = accountRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        var task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .author(account)
                .status(TaskStatus.OPEN)
                .priority(TaskPriority.LOW)
                .build();
        log.info("Account {} created task", userDetails.getUsername());

        return taskRepository.save(task);
    }

    @Override
    public Task read(Long id) {
        return findTask(id);
    }

    @Override
    public Page<Task> readAll(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    @Override
    public Page<Task> readByStatus(Pageable pageable, String status) {
        if (!isTaskStatus(status)){
            throw new IllegalArgumentException("Wrong status");
        }
        TaskStatus taskStatus = TaskStatus.valueOf(status.toUpperCase());
        return taskRepository.findByStatus(taskStatus, pageable);
    }

    @Override
    public Page<Task> readByPriority(Pageable pageable, String priority) {
        if (!isPriority(priority)){
            throw new IllegalArgumentException("Wrong priority");
        }
        TaskPriority taskPriority = TaskPriority.valueOf(priority.toUpperCase());
        return taskRepository.findByPriority(taskPriority, pageable);

    }

    private Task findTask(Long id){
        return taskRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFound("The task does not exist"));
    }

    @Override
    public Page<Task> tasksByAuthor(Long accountId, Pageable pageable) {
        return taskRepository.findByAuthorId(accountId, pageable);
    }

    @Override
    public Page<Task> tasksByAssignee(Long accountId, Pageable pageable) {
        return taskRepository.findByAssigneeId(accountId, pageable);
    }
}