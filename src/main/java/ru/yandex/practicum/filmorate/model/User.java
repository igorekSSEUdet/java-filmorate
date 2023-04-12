package ru.yandex.practicum.filmorate.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.exceptions.AddFriendException;
import ru.yandex.practicum.filmorate.exceptions.modelValidExceptions.CheckHasBlank;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
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

}

