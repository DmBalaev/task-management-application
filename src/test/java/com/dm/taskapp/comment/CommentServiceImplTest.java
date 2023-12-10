package com.dm.taskapp.comment;

import com.dm.taskapp.account.Account;
import com.dm.taskapp.account.AccountRepository;
import com.dm.taskapp.exceptions.ApiException;
import com.dm.taskapp.task.Task;
import com.dm.taskapp.task.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void addComment() {
        Task task = Task.builder()
                .id(1L)
                .build();
        Comment comment = Comment.builder()
                .id(1L)
                .author(Account.builder().id(999L).build())
                .build();
        Account userDetails = Account.builder()
                .id(13L)
                .email("example@test.com")
                .build();

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(accountRepository.findByEmail(userDetails.getEmail())).thenReturn(Optional.of(userDetails));
        when(commentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Comment addedComment = commentService.add(task.getId(), "text", userDetails);

        assertNotNull(addedComment);
        verify(commentRepository, times(1)).save(any());
    }

    @Test
    void getByTask() {
        Long taskId = 1L;
        Pageable pageable = mock(Pageable.class);

        when(commentRepository.findByTaskId(taskId, pageable)).thenReturn(mock(Page.class));

        Page<Comment> comments = commentService.getByTask(taskId, pageable);

        assertNotNull(comments);
        verify(commentRepository, times(1)).findByTaskId(taskId, pageable);
    }

    @Test
    void getByAuthor() {
        Long accountId = 1L;
        Pageable pageable = mock(Pageable.class);

        when(commentRepository.findByAuthorId(accountId, pageable)).thenReturn(mock(Page.class));

        Page<Comment> comments = commentService.getByAuthor(accountId, pageable);

        assertNotNull(comments);
        verify(commentRepository, times(1)).findByAuthorId(accountId, pageable);
    }

    @Test
    void deleteComment() {
        Account userDetails = Account.builder()
                .id(13L)
                .email("example@test.com")
                .build();
        Comment comment = Comment.builder()
                .id(1L)
                .author(userDetails)
                .build();

        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        when(accountRepository.findByEmail(userDetails.getEmail())).thenReturn(Optional.of(userDetails));

        assertDoesNotThrow(() -> commentService.delete(comment.getId(), userDetails));

        verify(commentRepository, times(1)).deleteById(comment.getId());
    }

    @Test
    void deleteCommentUnauthorized() {
        Comment comment = Comment.builder()
                .id(1L)
                .author(Account.builder().id(999L).build())
                .build();
        Account userDetails = Account.builder()
                .id(13L)
                .build();

        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        when(accountRepository.findByEmail(userDetails.getUsername())).thenReturn(Optional.of(userDetails));

        ApiException apiException = assertThrows(ApiException.class, () -> commentService.delete(comment.getId(), userDetails));

        assertEquals("You not have permissions for delete comment", apiException.getMessage());
        verify(commentRepository, never()).deleteById(comment.getId());
    }
}