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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldReturnEmptyListOfUsers() throws Exception {
        mvc.perform(get("/users"))
                .andDo(print())
                .andExpect(content().string(containsString("[]")));
    }

    @Test
    public void shouldCreateUser() throws Exception {
        User user = new User("error@mail.ru", "login", "name",
                LocalDate.of(2002, 10, 15));
        mvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userToJson(user)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();
    }

    @Test
    public void shouldntCreateUserBecauseEmailIsError() throws Exception {
        User user = new User("errormail.ru", "login", "name",
                LocalDate.of(2002, 10, 15));
        mvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userToJson(user)))
                .andExpect(content().string("{\"email\":\"Invalid mail format(example@mail.ru)\"}"))
                .andDo(print())
                .andReturn();

    }

    @Test
    public void shouldntCreateUserBecauseBirthdayInFuture() throws Exception {
        User user = new User("error@mail.ru", "login", "name",
                LocalDate.of(3000, 10, 15));
        mvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userToJson(user)))
                .andExpect(content().string("{\"birthday\":\"The date of birth cannot be in the future.\"}"))
                .andDo(print())
                .andReturn();

    }

    @Test
    public void shouldntCreateUserBecauseLoginIsEmpty() throws Exception {
        User user = new User("error@mail.ru", "", "name",
                LocalDate.of(2000, 10, 15));
        mvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userToJson(user)))
                .andExpect(content().string("{\"login\":\"The login cannot be empty\"}"))
                .andDo(print())
                .andReturn();

    }

    @Test
    public void shouldReturnListOfUsers() throws Exception {
        User user = new User("error@mail.ru", "login", "name",
                LocalDate.of(2000, 10, 15));
        mvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userToJson(user)))
                .andDo(print())
                .andReturn();

        mvc.perform(get("/users"))
                .andDo(print())
                .andExpect(jsonPath("[0]name").value("name"))
                .andExpect(jsonPath("[0]login").value("login"))
                .andExpect(jsonPath("[0]email").value("error@mail.ru"));

    }

    @Test
    public void shouldPutLoginOnNameBecauseNameIsEmpty() throws Exception {

        User user = new User("error@mail.ru", "login", null,
                LocalDate.of(2000, 10, 15));
        mvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userToJson(user)))
                .andDo(print())
                .andReturn();

        mvc.perform(get("/users"))
                .andDo(print())
                .andExpect(jsonPath("[1]name").value("login"));
    }

    private String userToJson(User user) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.writeValueAsString(user);
    }

}