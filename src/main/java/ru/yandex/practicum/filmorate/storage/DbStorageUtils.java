package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.servicesExceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.servicesExceptions.FilmStorageException;
import ru.yandex.practicum.filmorate.exceptions.servicesExceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class DbStorageUtils {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DbStorageUtils(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder().id(resultSet.getInt("ID"))
                .name(resultSet.getString("NAME"))
                .description(resultSet.getString("DESCRIPTION"))
                .releaseDate(resultSet.getDate("RELEASEDATE").toLocalDate())
                .duration(resultSet.getInt("DURATION"))
                .likes(resultSet.getInt("LIKES"))
                .genres(getListOfGenres(resultSet.getInt("ID")))
                .MPA(getMpa(resultSet.getInt("MPA_ID"))).build();
    }

    public User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder().id(resultSet.getInt("ID"))
                .email(resultSet.getString("EMAIL"))
                .login(resultSet.getString("LOGIN"))
                .name(resultSet.getString("NAME"))
                .birthday(resultSet.getDate("BIRTHDAY").toLocalDate()).build();

    }

    public MPA mapRowToMPA(ResultSet resultSet, int rowNum) throws SQLException {
        return MPA.builder()
                .id(resultSet.getInt("MPA_ID"))
                .name(resultSet.getString("MPA")).build();
    }

    public Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("ID"))
                .name(resultSet.getString("GENRE")).build();
    }

    public void checkMpaId(int id) {
        String sql = "SELECT MAX(MPA_ID) FROM MPA";
        if (id > jdbcTemplate.queryForObject(sql, Integer.class)) throw new FilmStorageException("MPA id error");
    }

    public void checkGenreId(int id) {
        String sql = "SELECT MAX(ID) FROM GENRES";
        if (id > jdbcTemplate.queryForObject(sql, Integer.class)) throw new FilmStorageException("Genre ID error");
    }

    public void checkUserId(List<Integer> listId) {
        String sql = "SELECT ID,EMAIL,LOGIN,NAME,BIRTHDAY " +
                "FROM USERS WHERE ID = ?";
        listId.forEach(id -> {
            if (id < 0) throw new UserNotFoundException("ID error");
            jdbcTemplate.queryForObject(sql, this::mapRowToUser, id);
        });
    }

    public void checkFilmId(List<Integer> listId) {
        String sql = "SELECT ID,NAME,DESCRIPTION,RELEASEDATE,DURATION,LIKES,MPA_ID " +
                "FROM FILMS WHERE ID = ?";
        listId.forEach(id -> {
            if (id < 0) throw new FilmNotFoundException("ID error");
            jdbcTemplate.queryForObject(sql, this::mapRowToFilm, id);
        });
    }

    private List<Genre> getListOfGenres(int filmId) {
        String sql = "SELECT id,GENRE FROM GENRES WHERE id IN (SELECT GENRE_ID from film_genres join films f on film_genres.film_id = f.id where f.id = " + filmId + ")";
        return jdbcTemplate.query(sql, this::mapRowToGenre);
    }

    private MPA getMpa(int id) {
        String sql = "SELECT MPA_ID,MPA FROM MPA WHERE MPA_ID = " + id;
        return jdbcTemplate.queryForObject(sql, this::mapRowToMPA);
    }

}
