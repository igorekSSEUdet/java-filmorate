package ru.yandex.practicum.filmorate;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {


    private final FilmStorage storage;
    private final UserStorage userStorage;


    @Test
    public void addFilmTest() {

        Optional<Film> film = storage.addFilm(Film.builder()
                .name("name")
                .description("desc")
                .releaseDate(LocalDate.now().minusYears(2))
                .duration(90)
                .MPA(MPA.builder().id(1).build())
                .build());
        assertThat(film)
                .isPresent()
                .hasValueSatisfying(f -> assertThat(f).hasFieldOrPropertyWithValue("name", "name")
                ).hasValueSatisfying(f -> assertThat(f).hasFieldOrPropertyWithValue("description", "desc")
                ).hasValueSatisfying(f -> assertThat(f).hasFieldOrPropertyWithValue("duration", 90)
                );

    }

    @Test
    public void testFindFilmById() {

        Optional<Film> userOptional = storage.getFilmById(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testFindAllFilms() {

        List<Film> films = storage.getAllFilms();

        assertThat(films).isNotNull().hasSize(1);
    }

    @Test
    public void testUpdateUser() {
        Film film = Film.builder()
                .name("upd")
                .description("upd")
                .releaseDate(LocalDate.now().minusYears(2))
                .duration(80)
                .MPA(MPA.builder().id(1).build())
                .build();
        film.setId(1);
        storage.updateFilm(film);
        Optional<Film> film2 = storage.getFilmById(1);
        assertThat(film2)
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u).hasFieldOrPropertyWithValue("id", 1)

                ).hasValueSatisfying(u ->
                        assertThat(u).hasFieldOrPropertyWithValue("name", "upd")
                ).hasValueSatisfying(u ->
                        assertThat(u).hasFieldOrPropertyWithValue("description", "upd")
                ).hasValueSatisfying(u ->
                        assertThat(u).hasFieldOrPropertyWithValue("duration", 80)
                );
    }

    public void addLikeFilm() {
        userStorage.addUser(User.builder()
                .email("mail@mail.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.now().minusYears(10))
                .build());

        storage.addLikeFilm(1,1);
        Optional<Film> film = storage.getFilmById(1);
        assertThat(film)
                .isPresent()
                .hasValueSatisfying(f -> assertThat(f).hasFieldOrPropertyWithValue("likes",1));

    }

    public void removeLikeFilm() {
        userStorage.addUser(User.builder()
                .email("mail@mail.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.now().minusYears(10))
                .build());

        storage.removeLikeFilm(1,1);
        Optional<Film> film = storage.getFilmById(1);
        assertThat(film)
                .isPresent()
                .hasValueSatisfying(f -> assertThat(f).hasFieldOrPropertyWithValue("likes",0));

    }
}
