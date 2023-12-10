package com.dm.taskapp.task;

import com.dm.taskapp.task.enums.TaskPriority;
import com.dm.taskapp.task.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{
    Page<Task>findByStatus(TaskStatus status, Pageable pageable);
    Page<Task>findByPriority(TaskPriority priority, Pageable pageable);
    Page<Task> findByAssigneeId(Long id, Pageable pageable);
    Page<Task> findByAuthorId(Long id, Pageable pageable);
}