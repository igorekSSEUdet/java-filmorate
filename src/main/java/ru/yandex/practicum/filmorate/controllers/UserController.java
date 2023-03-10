package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

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
    public User addUser(@Valid @RequestBody User user) {
        return userService.getUserStorage().addUser(user);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@Valid @RequestBody User user) {
        return userService.getUserStorage().updateUser(user);
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
    public User getFriendById(@PathVariable int id) {
        return userService.getUserStorage().getFriendById(id);
    }

    @GetMapping("{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsersFriendsById(@PathVariable int id) {
        return userService.getUsersFriendsById(id);
    }

}