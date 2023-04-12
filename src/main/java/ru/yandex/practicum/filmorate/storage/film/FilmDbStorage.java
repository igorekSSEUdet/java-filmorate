package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.servicesExceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.DbStorageUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Component
@Qualifier("FilmDbStorage")
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final DbStorageUtils utils;

    private final DateTimeFormatter logTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");


    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, DbStorageUtils utils) {
        this.jdbcTemplate = jdbcTemplate;
        this.utils = utils;
    }

    @Override
    public Optional<Film> addFilm(Film film) {
        utils.checkMpaId(film.getId());
        String sql = "INSERT INTO FILMS(NAME,DESCRIPTION,RELEASEDATE,DURATION,LIKES,MPA_ID) " +
                "VALUES(?,?,?,?,?,?)";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                0,
                film.getMPA().getId());
        log.info(LocalDateTime.now().format(logTimeFormat) + " : Добавлен новый фильм " + film);
        return Optional.of(addFilmsGenres(film));
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT ID,NAME,DESCRIPTION,RELEASEDATE,DURATION,LIKES,MPA_ID " +
                "FROM FILMS";
        log.info(LocalDateTime.now().format(logTimeFormat) + " : Пользователь получил список всех фильмов");
        return jdbcTemplate.query(sql, utils::mapRowToFilm);
    }

    @Override
    public Optional<Film> getFilmById(int id) {
        utils.checkFilmId(List.of(id));
        String sql = "SELECT ID,NAME,DESCRIPTION,RELEASEDATE,DURATION,LIKES,MPA_ID " +
                "FROM FILMS WHERE ID = ?";
        log.info(LocalDateTime.now().format(logTimeFormat) + " : Пользователь получил фильм с id = " + id);
        return Optional.of(jdbcTemplate.queryForObject(sql, utils::mapRowToFilm, id));
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        utils.checkFilmId(List.of(film.getId()));
        utils.checkMpaId(film.getId());
        String sql = "UPDATE FILMS SET " +
                "NAME = ?,DESCRIPTION = ?, RELEASEDATE = ?, DURATION = ?, LIKES = ?, MPA_ID =? " +
                "WHERE ID = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getLikes(),
                film.getMPA().getId(),
                film.getId());
        updateFilmsGenres(film);
        log.info(LocalDateTime.now().format(logTimeFormat) + " : Обновлен фильм " + film);
        return Optional.of(getFilm(film.getId())).get();
    }

    public void removeFilmById(int id) {
        utils.checkFilmId(List.of(id));
        String sql = "DELETE FROM FILMS WHERE ID = ?";
        log.info(LocalDateTime.now().format(logTimeFormat) + " : Удален фильм с id = " + id);
        jdbcTemplate.update(sql, id);
    }

    public List<Film> mostPopularFilms(int count) {
        String sql = "SELECT ID,NAME,DESCRIPTION,RELEASEDATE,DURATION,LIKES,MPA_ID FROM films ORDER BY likes DESC LIMIT ?";
        log.info(LocalDateTime.now().format(logTimeFormat) + " : Получен список " + count + " самых популярных фильмов");
        return jdbcTemplate.query(sql, utils::mapRowToFilm, count);
    }

    public Optional<Film> addLikeFilm(int filmId, int userId) {
        utils.checkFilmId(List.of(filmId));
        utils.checkUserId(List.of(userId));
        String sql1 = "INSERT INTO FILM_LIKERS(FILM_ID,USER_ID) " +
                "VALUES(?,?)";
        jdbcTemplate.update(sql1, filmId, userId);

        String sql2 = "UPDATE FILMS SET LIKES = LIKES + 1 WHERE ID = ?";
        jdbcTemplate.update(sql2, filmId);
        log.info(LocalDateTime.now().format(logTimeFormat) + " : Пользователь с id = " + userId + " поставил лайк фильму с id = " + filmId);
        return Optional.of(getFilm(filmId)).get();
    }

    public Optional<Film> removeLikeFilm(int filmId, int userId) {
        utils.checkFilmId(List.of(filmId));
        utils.checkUserId(List.of(userId));
        String sql = "DELETE FROM FILM_LIKERS WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sql, filmId, userId);

        String sql2 = "UPDATE FILMS SET LIKES = LIKES - 1 WHERE ID = ?";
        jdbcTemplate.update(sql2, filmId);
        log.info(LocalDateTime.now().format(logTimeFormat) + " : Пользователь с id = " + userId + " убрал лайк с фильма с id = " + filmId);
        return Optional.of(getFilm(filmId)).get();
    }

    public Genre getGenreById(int id) {
        utils.checkGenreId(id);
        String sql = "SELECT ID,GENRE FROM GENRES WHERE ID = ?";
        log.info(LocalDateTime.now().format(logTimeFormat) + " : Получен жанр с id = " + id);

        return jdbcTemplate.queryForObject(sql, utils::mapRowToGenre, id);
    }

    public List<Genre> getAllGenres() {
        String sql = "SELECT ID,GENRE FROM GENRES";
        log.info(LocalDateTime.now().format(logTimeFormat) + " : Получен список всех жанров");
        return jdbcTemplate.query(sql, utils::mapRowToGenre);
    }

    public MPA getMpaById(int id) {
        utils.checkMpaId(id);
        String sql = "SELECT MPA_ID,MPA FROM MPA WHERE MPA_ID = ?";
        log.info(LocalDateTime.now().format(logTimeFormat) + " : Получен возрастной рейтинг фильма с id = " + id);
        return jdbcTemplate.queryForObject(sql, utils::mapRowToMPA, id);
    }

    public List<MPA> getAllMpa() {
        String sql = "SELECT MPA_ID,MPA FROM MPA";
        log.info(LocalDateTime.now().format(logTimeFormat) + " : Получен список всех возрастных рейтингов");
        return jdbcTemplate.query(sql, utils::mapRowToMPA);
    }

    private Film addFilmsGenres(Film film) {
        int id = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM FILMS", Integer.class);
        if (film.getGenres() != null) {
            List<Genre> genres_id = film.getGenres();
            String sql2 = "INSERT INTO FILM_GENRES(FILM_ID,GENRE_ID) " +
                    "VALUES(?,?)";
            genres_id.forEach(genre -> {
                jdbcTemplate.update(sql2, id, genre.getId());
            });
        }
        return getFilm(id).get();
    }

    private Film updateFilmsGenres(Film film) {
        int id = film.getId();
        if (film.getGenres() != null) {
            String deleteSql = "DELETE FROM FILM_GENRES WHERE FILM_ID = ?";
            jdbcTemplate.update(deleteSql, film.getId());
            List<Genre> genres_id = film.getGenres();
            String sql2 = "INSERT INTO FILM_GENRES(FILM_ID,GENRE_ID) " +
                    "VALUES(?,?)";
            genres_id.forEach(genre -> {
                jdbcTemplate.update(sql2, id, genre.getId());
            });
        }
        return getFilm(id).orElseThrow(() -> new FilmNotFoundException("Нет фильма с таким ID"));
    }

    private Optional<Film> getFilm(int id) {
        utils.checkFilmId(List.of(id));
        String sql = "SELECT ID,NAME,DESCRIPTION,RELEASEDATE,DURATION,LIKES,MPA_ID " +
                "FROM FILMS WHERE ID = ?";
        return Optional.of(jdbcTemplate.queryForObject(sql, utils::mapRowToFilm, id));
    }

}
