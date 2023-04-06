package ru.yandex.practicum.filmorate.exceptions.servicesExceptions;

public class FilmStorageException extends RuntimeException{
    public FilmStorageException(String message) {
        super(message);
    }
}
