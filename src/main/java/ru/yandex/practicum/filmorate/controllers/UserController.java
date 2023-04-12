package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.swing.text.html.Option;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Optional<User> addUser(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().equals("")) user.setName(user.getLogin());
        return userService.addUser(user);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Optional<User> updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean removeUser(@PathVariable int id) {
        return userService.removeUser(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        return userService.getUserStorage().getAllUsers();
    }

    @PutMapping("{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void addToFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.addToFriend(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeFromFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.removeFromFriend(id, friendId);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getCommonFriends(id, otherId);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<User> getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @GetMapping("test")
    @ResponseStatus(HttpStatus.OK)
    public User test() {
        return User.builder().email("mail@mail.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.now().minusYears(5)).build();
    }

    @GetMapping("{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsersFriendsById(@PathVariable int id) {
        return userService.getUsersFriendsById(id);
    }

}