package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.servicesExceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final DateTimeFormatter logTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }


    public Film addFilm (Film film) {
        return filmStorage.addFilm(film);
    }

    public Film addLikeFilm(int filmId, int userId) {
        validIdParameters(filmId, userId);
        Film film = filmStorage.storage().get(filmId);
        film.addLiker(userStorage.storage().get(userId));
        film.countLike();
        log.info(LocalDateTime.now().format(logTimeFormat) + " : Пользователь с ID = " + userId +
                " поставил лайк фильму с ID = " + filmId);
        return film;
    }

    public Film deleteLikeFilm(int filmId, int userId) {
        validIdParameters(filmId, userId);
        Film film = filmStorage.storage().get(filmId);
        film.removeLiker(userStorage.storage().get(userId));
        film.countLike();
        log.info(LocalDateTime.now().format(logTimeFormat) + " : Пользователь с ID = " + userId +
                " удалил лайк фильму с ID = " + filmId);
        return film;
    }

    public List<Film> mostPopularFilms(int count) {
        log.info(LocalDateTime.now().format(logTimeFormat) + " : Клиент получил топ " + count + " фильмов");
        return filmStorage.storage().values().stream()
                .sorted((p0, p1) -> p1.getLikes().compareTo(p0.getLikes()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public InMemoryFilmStorage getFilmStorage() {
        return (InMemoryFilmStorage) filmStorage;
    }

    private void validIdParameters(int filmId, int userId) {
        if (!filmStorage.storage().containsKey(filmId) || !userStorage.storage().containsKey(userId)) {
            log.error(LocalDateTime.now().format(logTimeFormat) + " : Клиент передал несуществующие параметры ID = " +
                    filmId + "," + userId);
            throw new FilmNotFoundException("Nonexistent ID was passed");
        } else if (filmId < 0 || userId < 0) {
            log.error(LocalDateTime.now().format(logTimeFormat) + " : Клиент передал отрицательные параметры ID = " +
                    filmId + "," + userId);
            throw new FilmNotFoundException("ID cannot be negative");
        }
    }
}
