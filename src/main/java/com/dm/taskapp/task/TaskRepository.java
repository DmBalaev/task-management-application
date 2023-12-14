package com.dm.taskapp.task;

import com.dm.taskapp.task.enums.TaskPriority;
import com.dm.taskapp.task.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{
    List<Task> findByStatus(TaskStatus status, Pageable pageable);
    List<Task>findByPriority(TaskPriority priority, Pageable pageable);
    List<Task> findByAssigneeId(Long id, Pageable pageable);
    List<Task> findByAuthorId(Long id, Pageable pageable);
}