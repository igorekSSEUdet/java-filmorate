package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.exceptions.modelValidExceptions.CheckIfBefore1895;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class Film {
    private int id;
    @NotBlank(message = "The title of the movie cannot be empty")
    private String name;
    @Size(max = 200, message = "The maximum length of the description is 200 characters")
    private String description;
    @CheckIfBefore1895
    private LocalDate releaseDate;
    @Min(value = 0, message = "The duration of the film should be positive")
    private int duration;
    private Integer likes = 0;
    private List<Genre> genres;
    private MPA MPA;

}
