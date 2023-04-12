package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exceptions.servicesExceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getUsersFriendsById(int id) {
        return userStorage.getUsersFriendsById(id);
    }

    public List<User> getCommonFriends(int id, int otherId) {
        return userStorage.getCommonFriends(id, otherId);
    }

    public Optional<User> getUserById(@PathVariable int id) {
        return userStorage.getUserById(id);
    }

    public void removeFromFriend(int id, int friendId) {
        userStorage.removeFromFriend(id, friendId);
    }

    public void addToFriend(@PathVariable int id, @PathVariable int friendId) {
        userStorage.addToFriend(id, friendId);
    }

    public boolean removeUser(int id) {
        return userStorage.removeUser(id);
    }

    public Optional<User> addUser(User user) {
        return userStorage.addUser(user);
    }

    public Optional<User> updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }

}
