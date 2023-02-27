package ru.yandex.practicum.filmorate.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptios.HandleValidation;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController extends HandleValidation {

    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@Valid @RequestBody User user) {
        if (user.getName().isEmpty()) user.setName(user.getLogin());
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("Добавлен новый пользователь: " + user);
        return user;
    }


    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User updateUser( @RequestBody User user) {

        if (user.getName().isEmpty()) user.setName(user.getLogin());
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Пользователь обновлен: " + user);
        } else {
            user.setId(id++);
            users.put(user.getId(), user);
            log.info("Добавлен новый пользователь: " + user);
        }
        return user;
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        log.info("Клиент получил всех пользователей");
        return new ArrayList<>(users.values());
    }


}
