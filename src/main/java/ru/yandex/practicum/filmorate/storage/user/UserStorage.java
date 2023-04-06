package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

public interface UserStorage {

    public User addUser(@Valid @RequestBody User user);

    User updateUser(@Valid @RequestBody User user);

    List<User> getAllUsers();

    public Map<Integer, User> storage();

    User getFriendById(int id);
}
