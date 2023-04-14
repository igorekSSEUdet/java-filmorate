package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.servicesExceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Status;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.DbStorageUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Component
@Qualifier("UserDbStorage")
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final DbStorageUtils utils;

    private final DateTimeFormatter logTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate, DbStorageUtils utils) {
        this.jdbcTemplate = jdbcTemplate;
        this.utils = utils;
    }

    @Override
    public Optional<User> addUser(User user) {
        String sql = "INSERT INTO USERS(EMAIL,LOGIN,NAME,BIRTHDAY) " +
                "VALUES(?,?,?,?)";
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());

        int id = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM USERS", Integer.class);
        log.info(LocalDateTime.now().format(logTimeFormat) + " : Добавлен новый пользователь id = " + id);
        return Optional.of(getUser(id).orElseThrow(() -> {
            log.error(LocalDateTime.now().format(logTimeFormat) + " : Ошибка добавления пользователя с id = " + id);
            return new UserNotFoundException("Ошибка добавления юзера с id= " + id);
        }));
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT ID,EMAIL,LOGIN,NAME,BIRTHDAY " +
                "FROM USERS";
        log.info(LocalDateTime.now().format(logTimeFormat) + " : Получен список всех пользователей");
        return jdbcTemplate.query(sql, utils::mapRowToUser);
    }

    @Override
    public Optional<User> getUserById(int id) {
        utils.checkUserId(List.of(id));
        String sql = "SELECT ID,EMAIL,LOGIN,NAME,BIRTHDAY " +
                "FROM USERS WHERE ID = ?";
        log.info(LocalDateTime.now().format(logTimeFormat) + " : Получен пользователь с id = " + id);
        return Optional.of(jdbcTemplate.queryForObject(sql, utils::mapRowToUser, id));
    }

    @Override
    public Optional<User> updateUser(User user) {
        if (checkHasUser(user)) {
            String sql = "UPDATE USERS SET " +
                    "EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ?" +
                    "WHERE ID = ?";
            jdbcTemplate.update(sql,
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday(),
                    user.getId());
            log.info(LocalDateTime.now().format(logTimeFormat) + " : Обновлен пользователь: " + user);
            return Optional.of(user);
        } else {
            log.error(LocalDateTime.now().format(logTimeFormat) + " : Произошла ошибка обновления пользователя " + user);
            throw new UserNotFoundException("Нет юзера с таким ID");
        }
    }

    public boolean removeUser(int id) {
        utils.checkUserId(List.of(id));
        String sql = "DELETE FROM USERS WHERE ID = ?";
        log.info(LocalDateTime.now().format(logTimeFormat) + " : Удален пользователь с id = " + id);
        return jdbcTemplate.update(sql, id) > 1;
    }

    public void addToFriend(int id, int friendId) {
        utils.checkUserId(List.of(id, friendId));
        String sql = "INSERT INTO USER_FRIENDS(USER_ID,FRIEND_ID,STATUS) " +
                "VALUES(?,?,?)";
        log.info(LocalDateTime.now().format(logTimeFormat) + " : Пользователь с id = " + id +
                " отправил заявку в друзья пользователю с id = " + friendId);
        jdbcTemplate.update(sql, id, friendId, Status.REQUEST.toString());
    }

    public void removeFromFriend(int id, int friendId) {
        utils.checkUserId(List.of(id, friendId));
        String sql = "DELETE FROM USER_FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
        log.info(LocalDateTime.now().format(logTimeFormat) + " : Пользователь с id = " + id +
                " удалил из друзей пользователя с id = " + friendId);
        jdbcTemplate.update(sql, id, friendId);
    }

    public List<User> getUsersFriendsById(int id) {
        utils.checkUserId(List.of(id));
        String sql = "SELECT ID,EMAIL,LOGIN,NAME,BIRTHDAY " +
                "FROM USERS WHERE ID IN (SELECT FRIEND_ID FROM USER_FRIENDS " +
                "WHERE USER_ID = ?)";
        log.info(LocalDateTime.now().format(logTimeFormat) + " : Пользователь с id = " + id +
                " получил список своих друзей");
        return jdbcTemplate.query(sql, utils::mapRowToUser, id);
    }

    public List<User> getCommonFriends(int id, int otherId) {
        utils.checkUserId(List.of(id, otherId));
        String sql = "SELECT ID,EMAIL,LOGIN,NAME,BIRTHDAY FROM USERS WHERE ID IN " +
                "(SELECT uf2.FRIEND_ID " +
                "FROM USER_FRIENDS uf1 " +
                "JOIN USER_FRIENDS uf2 on uf1.FRIEND_ID = uf2.FRIEND_ID " +
                "WHERE uf1.user_id = ? AND uf2.user_id = ?);";
        log.info(LocalDateTime.now().format(logTimeFormat) + " : Пользователь с id = " + id +
                " получил список общих друзей с пользователем id = " + otherId);
        return jdbcTemplate.query(sql, utils::mapRowToUser, id, otherId);
    }

    private boolean checkHasUser(User user) {
        String sql = "SELECT ID,EMAIL,LOGIN,NAME,BIRTHDAY " +
                "FROM USERS WHERE ID = ?";
        return jdbcTemplate.queryForObject(sql, utils::mapRowToUser, user.getId()) != null;
    }

    private Optional<User> getUser(int id) {
        utils.checkUserId(List.of(id));
        String sql = "SELECT ID,EMAIL,LOGIN,NAME,BIRTHDAY " +
                "FROM USERS WHERE ID = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, utils::mapRowToUser, id));
    }

}
