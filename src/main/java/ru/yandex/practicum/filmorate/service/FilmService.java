package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.exceptions.servicesExceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }


    public void removeFilmById(int id) {
        filmStorage.removeFilmById(id);
    }

    public Optional<Film> updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Optional<Film> addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Optional<Film> getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    public Optional<Film> addLikeFilm(int filmId, int userId) {
        return filmStorage.addLikeFilm(filmId, userId);
    }

    public Optional<Film> deleteLikeFilm(int filmId, int userId) {
        return filmStorage.removeLikeFilm(filmId, userId);
    }

    public List<Film> mostPopularFilms(int count) {
        return filmStorage.mostPopularFilms(count);
    }

    public List<Genre> getAllGenres() {
        return filmStorage.getAllGenres();
    }

    public Genre getGenreById(@PathVariable int id) {
        return filmStorage.getGenreById(id);
    }

    public List<MPA> getAllMpa() {
        return filmStorage.getAllMpa();
    }

    public MPA getMpaById(int id) {
        return filmStorage.getMpaById(id);
    }


}
