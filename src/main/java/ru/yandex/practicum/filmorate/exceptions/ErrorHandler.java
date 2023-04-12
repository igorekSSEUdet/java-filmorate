package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.servicesExceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.servicesExceptions.FilmStorageException;
import ru.yandex.practicum.filmorate.exceptions.servicesExceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.servicesExceptions.UserStorageException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.DbStorageUtils;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(assignableTypes =
        {FilmController.class, UserController.class, FilmService.class, UserService.class, User.class, Film.class,
                UserDbStorage.class, FilmDbStorage.class, DbStorageUtils.class, })
public class ErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ExceptionHandler({UserStorageException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> UserStorageException(final UserStorageException e) {
        return Map.of("User Error: ", e.getMessage());
    }


    @ExceptionHandler({FilmStorageException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> FilmStorageException(final FilmStorageException e) {
        return Map.of("User Error: ", e.getMessage());
    }

    @ExceptionHandler({FilmNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> FilmNotFoundException(final FilmNotFoundException e) {
        return Map.of("Not found error: ", e.getMessage());
    }

    @ExceptionHandler({NullPointerException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> NotFoundException(final NullPointerException e) {
        return Map.of("Not found error: ", e.getMessage());
    }


    @ExceptionHandler({UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> UserNotFoundException(final UserNotFoundException e) {
        return Map.of("Not found error: ", e.getMessage());
    }

    @ExceptionHandler({EmptyResultDataAccessException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> NotFoundException(final EmptyResultDataAccessException e) {
        return Map.of("Not found error: ", e.getMessage());
    }
}
