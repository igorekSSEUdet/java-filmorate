package ru.yandex.practicum.filmorate.storage.user;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exceptions.servicesExceptions.UserStorageException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;
    private final DateTimeFormatter logTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public User getFriendById(int id) {
        if (!users.containsKey(id)) {
            log.error(LocalDateTime.now().format(logTimeFormat) + " : Клиент передал несуществующий ID = " + id);
            throw new UserStorageException("There is no user with this ID");
        }
        log.info(LocalDateTime.now().format(logTimeFormat) + " : Получен пользователь по ID = " + id);
        return users.get(id);

    }

    public Map<Integer, User> storage() {
        return users;
    }

    @Override
    public User addUser(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
        user.setId(id++);
        users.put(user.getId(), user);
        log.info(LocalDateTime.now().format(logTimeFormat) + " : Добавлен новый пользователь: " + user);
        return user;
    }

    @Override
    public User updateUser(@Valid @RequestBody User user) {

        if (user.getName() == null) user.setName(user.getLogin());
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info(LocalDateTime.now().format(logTimeFormat) + " : Пользователь обновлен: " + user);
        } else {
            log.error(LocalDateTime.now().format(logTimeFormat) + " : Не удалось обновить пользователя: " + user);
            throw new UserStorageException("Нет пользователя с таким ID");
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        log.info(LocalDateTime.now().format(logTimeFormat) + " : Клиент получил всех пользователей");
        return new ArrayList<>(users.values());
    }
}
