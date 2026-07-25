package com.dmitry.devboard.dto;


import com.dmitry.devboard.entity.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateTaskStatusRequest {

    @NotNull
    private TaskStatus status;
}
