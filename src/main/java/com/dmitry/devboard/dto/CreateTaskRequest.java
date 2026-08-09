package com.dmitry.devboard.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class CreateTaskRequest {
    @NotBlank
    @Size(min = 3)
    private String title;

    @NotBlank
    @Size(min = 5)
    private String description;
}
