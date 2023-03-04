package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.exceptions.CheckIfBefore1895;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {
    private int id;
    @NotBlank(message = "The title of the movie cannot be empty")
    private final String name;
    @Size(max = 200, message = "The maximum length of the description is 200 characters")
    private final String description;
    @CheckIfBefore1895
    private final LocalDate releaseDate;
    @Min(value = 0, message = "The duration of the film should be positive")
    private final int duration;
}
