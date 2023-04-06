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
import java.time.LocalDate;
import java.util.List;


@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.getFilmStorage().updateFilm(film);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getAllFilms() {
        return filmService.getFilmStorage().getAllFilms();
    }

    @PutMapping("{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Film addLikeFilm(@PathVariable int id, @PathVariable int userId) {
        return filmService.addLikeFilm(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Film removeLikeFilm(@PathVariable int id, @PathVariable int userId) {
        return filmService.deleteLikeFilm(id, userId);

    }

    @GetMapping("popular")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> mostPopularFilms(@RequestParam(required = false, defaultValue = "10") int count) {
        return filmService.mostPopularFilms(count);
    }

    @GetMapping("test")
    @ResponseStatus(HttpStatus.OK)
    public Film test1() {
        return new Film("name","desc", LocalDate.now(),100,
                List.of(new Genre(1,"Comedy"),new Genre(1,"Drama"),new Genre(1,"Crime")
                ),new MPA(1,"NC-17"));
    }

    @PutMapping("test")
    @ResponseStatus(HttpStatus.OK)
    public Film test2(@RequestBody Film film) {
       filmService.addFilm(film);
        System.out.println(film.toString());
        return film;
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film getFriendById(@PathVariable int id) {
        return filmService.getFilmStorage().getFilmById(id);
    }


}