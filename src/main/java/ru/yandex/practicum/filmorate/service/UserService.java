package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.servicesExceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;
    private final DateTimeFormatter logTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }

    public void addToFriend(int userId, int friendId) {
        validId(userId, friendId);
        userStorage.storage().get(userId).requestFriend(userStorage.storage().get(friendId).getId());
        //userStorage.storage().get(friendId).addFriend(userStorage.storage().get(userId).getId());
        log.info(LocalDateTime.now().format(logTimeFormat) + " : Пользователь с ID = " + userId +
                " добавил в друзья пользователя с ID = " + friendId);
    }

    public void removeFromFriend(int userId, int friendId) {
        validId(userId, friendId);
        userStorage.storage().get(userId).removeFriend(userStorage.storage().get(friendId).getId());
        userStorage.storage().get(friendId).removeFriend(userStorage.storage().get(userId).getId());
        log.info(LocalDateTime.now().format(logTimeFormat) + " : Пользователь с ID = " + userId +
                " удалил из друзей пользователя с ID = " + friendId);
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        validId(userId, otherId);
        List<Integer> generalList = Stream.concat
                (userStorage.storage().get(userId).getFriends().keySet().stream(),
                        userStorage.storage().get(otherId).getFriends().keySet().stream()).collect(Collectors.toList());
        Set<Integer> commonId = generalList.stream()
                .filter(i -> Collections.frequency(generalList, i) > 1)
                .collect(Collectors.toSet());
        List<User> commonFriends = new ArrayList<>();
        for (Integer id : commonId) {
            commonFriends.add(userStorage.storage().get(id));
        }
        return commonFriends;
    }

    public List<User> getUsersFriendsById(int userId) {
        if (!userStorage.storage().containsKey(userId)) {
            log.error(LocalDateTime.now().format(logTimeFormat) + " : Клиент передал несуществующий ID = " + userId);
            throw new UserNotFoundException("Nonexistent ID was passed");
        }
        else if (userId < 0) {
            log.error(LocalDateTime.now().format(logTimeFormat) + " : Клиент передал отрицательный ID = " + userId);
            throw new UserNotFoundException("ID cannot be negative");
        }
        log.info(LocalDateTime.now().format(logTimeFormat) + " : Клиент получил список своих друзей");
        List<User> usersFriends = new ArrayList<>();

        for (Integer id : userStorage.storage().get(userId).getFriends().keySet()) {
            usersFriends.add(userStorage.storage().get(id));
        }
        return usersFriends;
    }

    private void validId(int userId, int friendId) {
        if (!userStorage.storage().containsKey(userId) || !userStorage.storage().containsKey(friendId)) {
            log.error(LocalDateTime.now().format(logTimeFormat) + " : Клиент передал несуществующие ID: " +
                    userId + "," + friendId);
            throw new UserNotFoundException("Nonexistent ID was passed");
        } else if (userId < 0 || friendId < 0) {
            log.error(LocalDateTime.now().format(logTimeFormat) + " : Клиент передал отрицательные ID = " +
                    userId + "," + friendId);
            throw new UserNotFoundException("ID cannot be negative");
        } else if (userId == friendId) {
            log.error(LocalDateTime.now().format(logTimeFormat) + " : Клиент попытался добавить в друзья сам себя");
            throw new UserNotFoundException("You can't add yourself as a friend");
        }
    }

}