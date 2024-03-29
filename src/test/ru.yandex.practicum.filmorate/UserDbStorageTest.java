package ru.yandex.practicum.filmorate;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {

    private final UserStorage userStorage;

    @Test
    public void testAddUser() {

        Optional<User> userOptional = userStorage.addUser(User.builder()
                .email("mail@mail.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.now().minusYears(10))
                .build());

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "name")
                ).hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "login")
                ).hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("email", "mail@mail.ru")
                );

    }

    @Test
    public void testFindUserById() {

        Optional<User> userOptional = userStorage.getUserById(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }


    @Test
    public void testUpdateUser() {
        User user = User.builder()
                .email("mail@mail.ru")
                .login("update")
                .name("update")
                .birthday(LocalDate.now().minusYears(10))
                .build();
        user.setId(1);
        userStorage.updateUser(user);
        Optional<User> user2 = userStorage.getUserById(1);
        assertThat(user2)
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u).hasFieldOrPropertyWithValue("id", 1)

                ).hasValueSatisfying(u ->
                        assertThat(u).hasFieldOrPropertyWithValue("login", "update")
                ).hasValueSatisfying(u ->
                        assertThat(u).hasFieldOrPropertyWithValue("name", "update")
                );
    }


    @Test
    public void testAddFriend() {

        Optional<User> userOptional = userStorage.addUser(User.builder()
                .email("new@mail.ru")
                .login("new")
                .name("new")
                .birthday(LocalDate.now().minusYears(10))
                .build());
        userStorage.addUser(userOptional.get());
        userStorage.addToFriend(1, 3);
        List<User> users = userStorage.getUsersFriendsById(1);

        assertThat(users).isNotNull().hasSize(1);

    }

    @Test
    public void testRemoveUser() {

        Optional<User> userOptional = userStorage.addUser(User.builder()
                .email("new@mail.ru")
                .login("new")
                .name("new")
                .birthday(LocalDate.now().minusYears(10))
                .build());
        userStorage.removeUser(2);
        List<User> users = userStorage.getAllUsers();

        assertThat(users).isNotNull().hasSize(3);

    }

    @Test
    public void testgetAllUsers() {
        List<User> users = userStorage.getAllUsers();
        assertThat(users).isNotNull().hasSize(3);
    }

    @Test
    public void testRemoveFromFriend() {
        userStorage.removeFromFriend(1, 3);
        List<User> users = userStorage.getUsersFriendsById(1);

        assertThat(users).isEmpty();

    }


}
