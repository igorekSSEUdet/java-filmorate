package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.servicesExceptions.FilmStorageException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;
    private final DateTimeFormatter logTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    public Map<Integer, Film> storage() {
        return films;
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(id++);
        films.put(film.getId(), film);
        log.info(LocalDateTime.now().format(logTimeFormat) + " : Добавлен фильм: " + film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info(LocalDateTime.now().format(logTimeFormat) + " : Обновлен фильм: " + film);
        } else {
            log.error(LocalDateTime.now().format(logTimeFormat) + " : Не удалось обновить фильм с несуществующим ID = " + film.getId());
            throw new FilmStorageException("There is no film with this ID");
        }

        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        log.info(LocalDateTime.now().format(logTimeFormat) + " : Клиент получил все фильмы");
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(int id) {
        if (!films.containsKey(id)) {
            log.error(LocalDateTime.now().format(logTimeFormat) + " : Клиент передал несуществующий ID = " + id);
            throw new FilmStorageException("There is no film with this ID");
        }
        log.info(LocalDateTime.now().format(logTimeFormat) + " : Клиент получил фильм по ID = " + id);
        return films.get(id);
    }
}
