package ru.yandex.practicum.filmorate.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.yandex.practicum.filmorate.exceptions.modelValidExceptions.CheckHasBlank;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {

    private int id;
    @Email(message = "Invalid mail format(example@mail.ru)")
    private final String email;
    @NotBlank(message = "The login cannot be empty")
    @CheckHasBlank
    private final String login;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past(message = "The date of birth cannot be in the future.")
    private final LocalDate birthday;
    private Set<Integer> friends = new HashSet<>();


    public void addFriend(Integer id) {
        friends.add(id);
    }

    public void removeFriend(Integer id) {
        friends.remove(id);
    }

    public User( String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}

