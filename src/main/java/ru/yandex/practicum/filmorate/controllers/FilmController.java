package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
@Slf4j

public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }


    @PostMapping("/films")
    @ResponseStatus(HttpStatus.CREATED)
    public Optional<Film> addFilm(@Valid @RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping("/films")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Film> updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @DeleteMapping("films/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void removeFilmById(@PathVariable int id) {
        filmService.removeFilmById(id);
    }

    @GetMapping("/films")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @PutMapping("films/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Film> addLikeFilm(@PathVariable int filmId, @PathVariable int userId) {
        return filmService.addLikeFilm(filmId, userId);
    }

    @DeleteMapping("films/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Film> removeLikeFilm(@PathVariable int filmId, @PathVariable int userId) {
        return filmService.deleteLikeFilm(filmId, userId);

    }

    @GetMapping("films/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> mostPopularFilms(@RequestParam(required = false, defaultValue = "10") int count) {
        return filmService.mostPopularFilms(count);
    }

    @GetMapping("films/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Film> getFilmById(@PathVariable int id) {
        return filmService.getFilmById(id);
    }


    @RequestMapping("/genres")
    @GetMapping
    public List<Genre> getAllGenres() {
        return filmService.getAllGenres();
    }

    @RequestMapping("/genres/{id}")
    @GetMapping
    public Genre getGenreById(@PathVariable int id) {
        return filmService.getGenreById(id);
    }

    @RequestMapping("/mpa")
    @GetMapping
    public List<MPA> getAllMpa() {
        return filmService.getAllMpa();
    }

    @GetMapping
    @RequestMapping("/mpa/{id}")
    public MPA getMpaById(@PathVariable int id) {
        return filmService.getMpaById(id);
    }

}