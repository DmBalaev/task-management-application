package com.dm.taskapp.task;

import com.dm.taskapp.account.Account;
import com.dm.taskapp.account.AccountRepository;
import com.dm.taskapp.app.ApiResponse;
import com.dm.taskapp.exceptions.ResourceNotFound;
import com.dm.taskapp.task.enums.TaskPriority;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class TaskServiceImplTest {
    @Mock
    private  TaskRepository taskRepository;
    @Mock
    private  AccountRepository accountRepository;
    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    public void createTask() {
        TaskRequest request = TaskRequest.builder()
                .title("title")
                .description("description")
                .build();

        Account principal = Account.builder()
                .id(13L)
                .email("example@email.com")
                .build();

        Task task = Task.builder()
                .title("title")
                .description("description")
                .build();

        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(accountRepository.findByEmail(principal.getEmail())).thenReturn(Optional.of(principal));

        Task expected = taskService.create(request, principal);

        assertEquals(expected.getTitle(), task.getTitle());
        assertEquals(expected.getDescription(), request.getDescription());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void findById() {
        Task task = mock(Task.class);
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        Task expected = taskService.read(task.getId());

        assertEquals(expected, task);
    }

    @Test
    void updateTask() {
        TaskUpdateRequest request = TaskUpdateRequest.builder()
                .id(1L)
                .title("newTitle")
                .description("newDescription")
                .build();

        Account userDetails = Account.builder()
                .id(1L)
                .email("test@mail.com")
                .password("password")
                .build();

        Task task = Task.builder()
                .id(1L)
                .title("oldTitle")
                .author(userDetails)
                .description("oldDesc")
                .build();

        when(taskRepository.findById(request.getId())).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task expected = taskService.updateTask(request, userDetails);

        assertEquals(expected.getTitle(), "newTitle");
        assertEquals(expected.getDescription(), "newDescription");
        assertEquals(expected, task);
    }

    @Test
    void deleteTask() {
        Account userDetails = Account.builder()
                .id(1L)
                .email("test@mail.com")
                .password("password")
                .build();
        Task task = Task.builder()
                .id(1L)
                .author(userDetails)
                .build();

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        taskService.deleteTask(task.getId(), userDetails);

        verify(taskRepository).deleteById(task.getId());
    }

    @Test
    void unsignTask() {
        long taskId = 1L;
        Account userDetails = Account.builder()
                .id(1L)
                .email("test@mail.com")
                .password("password")
                .build();
        Task task = Task.builder()
                .id(taskId)
                .title("Test Task")
                .author(userDetails)
                .description("Test Description")
                .assignee(Account.builder().id(123L).email("test@example.com").build())
                .build();

        when(taskRepository.findById(taskId)).thenReturn(java.util.Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ApiResponse result = taskService.unsignTask(taskId, userDetails);

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(any(Task.class));
        assertEquals("You successfully unsign task", result.getMessage());
        assertNull(task.getAssignee());
    }

    @Test
    void unsignTaskTaskNotFound() {
        long taskId = 1L;
        Account userDetails = Account.builder()
                .id(1L)
                .email("test@mail.com")
                .password("password")
                .build();

        when(taskRepository.findById(taskId)).thenReturn(java.util.Optional.empty());

        assertThrows(ResourceNotFound.class, () -> taskService.unsignTask(taskId, userDetails));
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(0)).save(any(Task.class));
    }

    @Test
    void changePriority() {
        long taskId = 1L;
        Account userDetails = Account.builder()
                .id(1L)
                .email("test@mail.com")
                .password("password")
                .build();
        TaskPriority newPriority = TaskPriority.HIGH;
        Task task = Task.builder()
                .id(taskId)
                .title("Test Task")
                .description("Test Description")
                .author(userDetails)
                .priority(TaskPriority.LOW)
                .build();

        when(taskRepository.findById(taskId)).thenReturn(java.util.Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ApiResponse result = taskService.changePriority(taskId, newPriority, userDetails);

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(any(Task.class));
        assertEquals("You successfully change priority", result.getMessage());
        assertEquals(newPriority, task.getPriority()); // Ensure priority is updated
    }

    @Test
    void readTaskFound() {
        long taskId = 1L;
        Task task = Task.builder()
                .id(taskId)
                .title("Test Task")
                .description("Test Description")
                .build();

        when(taskRepository.findById(taskId)).thenReturn(java.util.Optional.of(task));

        Task result = taskService.read(taskId);

        verify(taskRepository, times(1)).findById(taskId);
        assertEquals(task, result);
    }

    @Test
    void readTaskNotFound() {
        long taskId = 1L;

        when(taskRepository.findById(taskId)).thenReturn(java.util.Optional.empty());

        assertThrows(ResourceNotFound.class, () -> taskService.read(taskId));
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    void tasksByAuthor() {
        long accountId = 1L;
        Pageable pageable = Pageable.unpaged();
        List<Task> expectedPage = mock(List.class);

        when(taskRepository.findByAuthorId(accountId, pageable)).thenReturn(expectedPage);

        List<Task> result = taskService.tasksByAuthor(accountId, pageable);

        verify(taskRepository, times(1)).findByAuthorId(accountId, pageable);
        assertEquals(expectedPage, result);
    }

    @Test
    void tasksByAssignee() {
        long accountId = 1L;
        Pageable pageable = Pageable.unpaged();
        List<Task> expectedPage = mock(List.class);

        when(taskRepository.findByAssigneeId(accountId, pageable)).thenReturn(expectedPage);

        List<Task> result = taskService.tasksByAssignee(accountId, pageable);

        verify(taskRepository, times(1)).findByAssigneeId(accountId, pageable);
        assertEquals(expectedPage, result);
    }
}