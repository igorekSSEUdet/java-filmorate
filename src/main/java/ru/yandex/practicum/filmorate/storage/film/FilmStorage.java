package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface FilmStorage {

    Optional<Film> addFilm(@Valid @RequestBody Film film);

    Optional<Film> updateFilm(@Valid @RequestBody Film film);

    List<Film> getAllFilms();

    Optional<Film> getFilmById(int id);

    void removeFilmById(int id);

    Optional<Film> addLikeFilm(int filmId, int userId);

    Optional<Film> removeLikeFilm(int filmId, int userId);

    List<Film> mostPopularFilms(int count);

    List<Genre> getAllGenres();

    Genre getGenreById(@PathVariable int id);

    List<MPA> getAllMpa();

    MPA getMpaById(int id);
}
