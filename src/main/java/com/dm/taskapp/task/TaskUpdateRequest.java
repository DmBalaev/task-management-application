package com.dm.taskapp.task;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpdateRequest {
    @NotBlank
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
}
