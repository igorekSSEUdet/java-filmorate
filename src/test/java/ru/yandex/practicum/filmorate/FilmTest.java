package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmTest {


    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldReturnEmptyListOfFilms() throws Exception {
        mvc.perform(get("/films"))
                .andDo(print())
                .andExpect(content().string(containsString("[]")));
    }

    @Test
    public void shouldCreateFilm() throws Exception {
        Film film = new Film("name", "desc", LocalDate.of(2002, 10, 15), 100);
        mvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userToJson(film)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();
    }


    @Test
    public void shouldntCreateFilmBecauseNameIsEmpty() throws Exception {
        Film film = new Film("", "desc", LocalDate.of(2002, 10, 15), 100);
        mvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userToJson(film)))
                .andExpect(content().string("{\"name\":\"The title of the movie cannot be empty\"}"))
                .andDo(print())
                .andReturn();

    }

    @Test
    public void shouldntCreateFilmBecauseDescriptionMoreThan200Characters() throws Exception {
        Film film = new Film("name", "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                "xxxxxxxxxxxxxxxxxxxxxxxxxxxx", LocalDate.of(2002, 10, 15), 100);
        mvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userToJson(film)))
                .andExpect(content().string("{\"description\":\"The maximum length of the description is 200 characters\"}"))
                .andDo(print())
                .andReturn();

    }

    @Test
    public void shouldntCreateFilmBecauseReleaseDateIsBeforeThan1894() throws Exception {
        Film film = new Film("name", "desc", LocalDate.of(1700, 10, 15), 100);
        mvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userToJson(film)))
                .andExpect(content().string("{\"releaseDate\":\"The release date of the film should not be earlier than December 28, 1895\"}"))
                .andDo(print())
                .andReturn();

    }

    private String userToJson(Film film) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.writeValueAsString(film);
    }

}