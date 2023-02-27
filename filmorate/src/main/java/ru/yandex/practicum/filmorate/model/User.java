package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.yandex.practicum.filmorate.exceptios.springValidAnnotations.CheckHasBlank;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class User {
    private int id;
    @Email(message = "Invalid mail format(example@mail.ru)")
    private final String email;
    @NotBlank(message = "The login cannot be empty")
    @CheckHasBlank
    private final String login;
    private  String name;
        @JsonFormat(pattern = "yyyy-MM-dd")
    @Past(message = "The date of birth cannot be in the future.")
    private final LocalDate birthday;

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
//
//    public User(String email, String login,  LocalDate birthday) {
//        this.email = email;
//        this.login = login;
//        this.birthday = birthday;
//    }


}
