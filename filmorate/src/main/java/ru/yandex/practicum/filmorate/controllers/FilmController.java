package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptios.HandleValidation;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/film")
public class FilmController extends HandleValidation {

    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film addFilm(@Valid @RequestBody Film film) {
        film.setId(id++);
        films.put(film.getId(), film);
        log.info("Добавлен фильм: " + film);
        return film;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Обновлен фильм: " + film);
        } else {
            film.setId(id++);
            films.put(film.getId(), film);
            log.info("Добавлен фильм: " + film);
        }

        return film;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getAllFilms() {
        log.info("Клиент получил все фильмы");
        return new ArrayList<>(films.values());
    }


}
