package ru.yandex.practicum.filmorate.exceptions.servicesExceptions;

public class FilmNotFoundException extends RuntimeException{
    public FilmNotFoundException(String message) {
        super(message);
    }
}
