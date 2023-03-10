package ru.yandex.practicum.filmorate.exceptions.servicesExceptions;

public class UserStorageException extends RuntimeException{
    public UserStorageException(String message) {
        super(message);
    }
}
