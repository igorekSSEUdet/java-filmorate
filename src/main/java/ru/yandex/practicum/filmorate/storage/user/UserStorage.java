package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface UserStorage {

    Optional<User> addUser(@Valid @RequestBody User user);

    Optional<User> updateUser(@Valid @RequestBody User user);

    List<User> getAllUsers();

    Optional<User> getUserById(int id);

    boolean removeUser(int id);

    void addToFriend(int id, int friendId);

    void removeFromFriend(int id, int friendId);

    List<User> getCommonFriends(int id, int otherId);

    List<User> getUsersFriendsById(int id);
}
