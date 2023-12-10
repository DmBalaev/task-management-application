package com.dm.taskapp.task;

import com.dm.taskapp.task.enums.TaskPriority;
import com.dm.taskapp.task.enums.TaskStatus;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/task")
@Tag(name = "Task management")
public class TaskController {
    private final TaskService taskService;
    private final TaskRepository repository;

    @PostMapping
    @Operation(
            summary = "Create Task",
            description = "Endpoint to create a new task",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Request body for creating a task",
                    content = @Content(schema = @Schema(implementation = TaskRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Task created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Task.class)))
            },
            security = {@SecurityRequirement(name = "BearerJWT")}
    )
    public ResponseEntity<Task> createTask(
            @RequestBody TaskRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        Task task = taskService.create(request, userDetails);

        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Read Task",
            description = "Endpoint to retrieve a task by ID",
            parameters = {
                    @Parameter(name = "id", description = "ID of the task to retrieve", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Task.class))),
                    @ApiResponse(responseCode = "404", description = "Task not found")
            },
            security = {@SecurityRequirement(name = "BearerJWT")}
    )
    public ResponseEntity<Task> readTask(@PathVariable Long id) {
        Task task = taskService.read(id);
        return ResponseEntity.ok(task);
    }


    @GetMapping
    @Operation(
            summary = "Read All Tasks",
            description = "Endpoint to retrieve all tasks",
            parameters = {
                    @Parameter(name = "page", description = "Optional. Page number (zero-based) to retrieve."),
                    @Parameter(name = "size", description = "Optional. Number of items per page."),
                    @Parameter(name = "status", description = "Optional. Filter by task status ('OPEN', 'IN_PROGRESS', 'RESOLVED','CLOSE')."),
                    @Parameter(name = "priority", description = "Optional. Filter by task priority ('LOW', 'HIGH', 'MEDIUM').")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Task.class)))
            },
            security = {@SecurityRequirement(name = "BearerJWT")}
    )
    public ResponseEntity<Page<Task>> readAllTasks(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size,
            @RequestParam Optional<String> status,
            @RequestParam Optional<String> priority
    ) {
        Pageable pageable = PageRequest.of(page.orElse(0), size.orElse(10));
        Page<Task> tasks;

        if (status.isPresent()) {
            tasks = taskService.readByStatus(pageable, status.get());
        } else if (priority.isPresent()) {
            tasks = taskService.readByPriority(pageable, priority.get());
        } else {
            tasks = taskService.readAll(pageable);
        }
        return ResponseEntity.ok(tasks);
    }

    @PutMapping
    @Operation(
            summary = "Update Task",
            description = "Endpoint to update a task",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Request body for updating a task",
                    content = @Content(schema = @Schema(implementation = TaskUpdateRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Task.class))),
                    @ApiResponse(responseCode = "404", description = "Task not found")
            },
            security = {@SecurityRequirement(name = "BearerJWT")}
    )
    public ResponseEntity<Task> updateTask(@RequestBody TaskUpdateRequest request) {
        Task updatedTask = taskService.updateTask(request);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{taskId}")
    @Operation(
            summary = "Delete Task",
            description = "Endpoint to delete a task by ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Task not found")
            },
            security = {@SecurityRequirement(name = "BearerJWT")}
    )
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{taskId}/assign/{accountId}")
    @Operation(
            summary = "Assign Task",
            description = "Endpoint to assign a task to a user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task assigned successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Task.class))),
                    @ApiResponse(responseCode = "404", description = "Task or user not found")
            },
            security = {@SecurityRequirement(name = "BearerJWT")}
    )
    public ResponseEntity<Task> assignTask(@PathVariable Long taskId, @PathVariable Long accountId) {
        Task task = taskService.assignTask(taskId, accountId);
        return ResponseEntity.ok(task);
    }

    @PostMapping("/{taskId}/unsign")
    @Operation(
            summary = "Unsign Task",
            description = "Endpoint to unsign a task",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Task unsigned successfully"),
                    @ApiResponse(responseCode = "404", description = "Task not found or not assigned")
            },
            security = {@SecurityRequirement(name = "BearerJWT")}
    )
    public ResponseEntity<?> unsignTask(@PathVariable Long taskId) {
        taskService.unsignTask(taskId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{taskId}/change_status")
    @Operation(
            summary = "Change Task Status",
            description = "Endpoint to change the status of a task",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "This is the request body",
                    content = @Content(schema = @Schema)
            ),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Task status changed successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid status specified"),
                    @ApiResponse(responseCode = "404", description = "Task not found or not assigned"),
                    @ApiResponse(responseCode = "403", description = "Permission denied")
            },
            security = {@SecurityRequirement(name = "BearerJWT")}
    )
    public ResponseEntity<?> changeStatus(
            @PathVariable Long taskId,
            @RequestBody JsonNode json,
            @AuthenticationPrincipal UserDetails userDetails) {
        TaskStatus status = TaskStatus.valueOf(json.get("taskStatus").asText());
        taskService.changeStatus(taskId, status, userDetails);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{taskId}/change_priority")
    @Operation(
            summary = "Change Task Priority",
            description = "Endpoint to change the priority of a task",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "This is the request body",
                    content = @Content(schema = @Schema)
            ),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Task priority changed successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid priority specified"),
                    @ApiResponse(responseCode = "404", description = "Task not found")
            },
            security = {@SecurityRequirement(name = "BearerJWT")}
    )
    public ResponseEntity<?> changePriority(
            @PathVariable Long taskId,
            @RequestBody JsonNode json) {
        TaskPriority priority = TaskPriority.valueOf(json.get("taskPriotiry").asText());
        taskService.changePriority(taskId, priority);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/author/{accountId}")
    @Operation(
            summary = "Get Tasks by Author",
            description = "Endpoint to retrieve tasks for a specific author",

            parameters = {
                    @Parameter(name = "accountId", description = "ID of the author to get tasks for", required = true),
                    @Parameter(name = "page", description = "Optional. Page number (zero-based) to retrieve."),
                    @Parameter(name = "size", description = "Optional. Number of items per page."),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Task.class))),
                    @ApiResponse(responseCode = "404", description = "Author not found")
            },
            security = {@SecurityRequirement(name = "BearerJWT")}
    )
    public ResponseEntity<Page<Task>> tasksByAuthor(
            @PathVariable Long accountId,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size)
    {
        Pageable pageable = PageRequest.of(page.orElse(0), size.orElse(10));
        var tasks = taskService.tasksByAuthor(accountId, pageable);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/assignee/{accountId}")
    @Operation(
            summary = "Get Tasks by Assignee",
            description = "Endpoint to retrieve tasks assigned to a specific user",
            parameters = {
                    @Parameter(name = "accountId", description = "ID of the assignee to get tasks for", required = true),
                    @Parameter(name = "page", description = "Optional. Page number (zero-based) to retrieve."),
                    @Parameter(name = "size", description = "Optional. Number of items per page."),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Task.class))),
                    @ApiResponse(responseCode = "404", description = "Assignee not found")
            },
            security = {@SecurityRequirement(name = "BearerJWT")}
    )
    public ResponseEntity<Page<Task>> tasksByAssignee(
            @PathVariable Long accountId,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size)
    {
        Pageable pageable = PageRequest.of(page.orElse(0), size.orElse(10));
        var tasks = taskService.tasksByAssignee(accountId, pageable);
        return ResponseEntity.ok(tasks);
    }
}