package com.dm.taskapp.comment;

import com.dm.taskapp.account.Account;
import com.dm.taskapp.account.AccountRepository;
import com.dm.taskapp.exceptions.ApiException;
import com.dm.taskapp.exceptions.ResourceNotFound;
import com.dm.taskapp.task.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final AccountRepository accountRepository;

    @Override
    public Comment add(Long idTask, String text, UserDetails userDetails){
        var task = taskRepository.findById(idTask)
                .orElseThrow(()-> new ResourceNotFound("Task not found"));
        var account = findAccount(userDetails.getUsername());

        var comment = Comment.builder()
                .author(account)
                .content(text)
                .task(task)
                .build();

        return commentRepository.save(comment);
    }

    @Override
    public Page<Comment> getByTask(Long taskId, Pageable pageable){
        return commentRepository.findByTaskId(taskId, pageable);
    }

    @Override
    public Page<Comment> getByAuthor(Long accountId, Pageable pageable){
        return commentRepository.findByAuthorId(accountId, pageable);
    }

    @Override
    public void delete(Long idComment, UserDetails userDetails){
        var account = findAccount(userDetails.getUsername());
        var comment = commentRepository.findById(idComment)
                .orElseThrow(()-> new ResourceNotFound("Comment not found"));

        if (!Objects.equals(account.getId(), comment.getAuthor().getId())){
            throw new ApiException("You not have permissions for delete comment", HttpStatus.FORBIDDEN);
        }

        commentRepository.deleteById(idComment);
    }

    private Account findAccount(String email){
        return  accountRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFound("Resource not found"));
    }
}