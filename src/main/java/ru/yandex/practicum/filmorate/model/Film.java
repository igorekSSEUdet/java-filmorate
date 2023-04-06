package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.exceptions.modelValidExceptions.CheckIfBefore1895;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private Set<User> userLikers = new HashSet<>();
    private Integer likes = 0;
    private List<Genre> genres;
    private MPA MPA;

    public Film(String name, String description, LocalDate releaseDate, int duration,
                List<Genre> genres, MPA MPA) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.genres = genres;
        this.MPA = MPA;
    }

    public void countLike() {
        this.likes = userLikers.size();
    }
    public void addLiker(User user) {
        this.userLikers.add(user);
    }
    public void removeLiker(User user) {
        this.userLikers.remove(user);
    }


}
