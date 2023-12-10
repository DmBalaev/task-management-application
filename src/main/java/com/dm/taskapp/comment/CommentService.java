package com.dm.taskapp.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

public interface CommentService {
    Comment add(Long idTask, String text, UserDetails userDetails);
    Page<Comment> getByTask(Long taskId, Pageable pageable);
    Page<Comment> getByAuthor(Long accountId, Pageable pageable);
    void delete(Long idComment, UserDetails userDetails);
}