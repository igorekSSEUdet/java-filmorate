package ru.yandex.practicum.filmorate.exceptions.servicesExceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
