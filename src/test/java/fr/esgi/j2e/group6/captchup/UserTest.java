package fr.esgi.j2e.group6.captchup;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.esgi.j2e.group6.captchup.user.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.MimeTypeUtils;

import javax.validation.constraints.AssertTrue;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CaptchupApplication.class)
@AutoConfigureMockMvc
public class UserTest {

    @Autowired
    private MockMvc mockMvc;

    private String token;

    @Before
    public void setUp() throws Exception {
        final ResultActions result = mockMvc.perform(
                post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "\t\"username\": \"testuser\",\n" +
                                "\t\"password\": \"password\"\n" +
                                "}")
        );

        token = result.andReturn().getResponse().getHeader("Authorization");
    }

    @Test
    public void getAllUsers_notLoggedIn_shouldReturnForbidden() throws Exception {
        final ResultActions result = mockMvc.perform(
                get("/user/all")
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        result.andExpect(status().isForbidden());
    }

    @Test
    public void getAllUsers_LoggedIn_shouldReturnOk() throws Exception {
        final ResultActions result = mockMvc.perform(
                get("/user/all")
                        .header("Authorization",token)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());
    }

    @Test
    public void getUserById_LoggedIn_shouldReturnOkAndUser() throws Exception {
        JacksonJsonParser jsonParser = new JacksonJsonParser();

        final ResultActions result = mockMvc.perform(
                get("/user/{id}", 1)
                        .header("Authorization",token)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());

        String resultContent = result.andReturn().getResponse().getContentAsString();
        String username = jsonParser.parseMap(resultContent).get("username").toString();

        assert(username.equals("celia"));
    }

    @Test
    public void getUserById_notLoggedIn_shouldReturnForbidden() throws Exception {
        JacksonJsonParser jsonParser = new JacksonJsonParser();

        final ResultActions result = mockMvc.perform(
                get("/user/{id}", 1)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        result.andExpect(status().isForbidden());
    }

    @Test
    public void signUp_withExistingUsername_shouldReturnConflict() throws Exception {
        final ResultActions result = mockMvc.perform(
                post("/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "\t\"username\": \"celia\",\n" +
                                "\t\"password\": \"password\"\n" +
                                "}")
        );

        result.andExpect(status().isConflict());
    }

    @Test
    public void signUpAndDelete_shouldReturnOkAndDeleted() throws Exception {

        JacksonJsonParser jsonParser = new JacksonJsonParser();

        // signup
        final ResultActions result = mockMvc.perform(
          post("/user/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\t\"username\": \"temporary\",\n" +
                        "\t\"password\": \"temporary\"\n" +
                        "}")
        );

        result.andExpect(status().isOk());
        String resultContent = result.andReturn().getResponse().getContentAsString();
        String id = jsonParser.parseMap(resultContent).get("id").toString();

        // deletion
        final ResultActions deletionResult = mockMvc.perform(
                delete("/user/delete/" + id)
                        .header("Authorization",token)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        deletionResult.andExpect(status().isOk());

        // verify its deleted
        final ResultActions resultAfterDeletion = mockMvc.perform(
                get("/user/all")
                        .header("Authorization",token)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        ObjectMapper mapper = new ObjectMapper();
        List<User> users = mapper.readValue(resultAfterDeletion.andReturn().getResponse().getContentAsString(), new TypeReference<List<User>>(){});

        assert(users.stream().filter(x -> String.valueOf(x.getId()) == id).count() == 0);
    }




}
