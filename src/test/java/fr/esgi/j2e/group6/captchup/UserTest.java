package fr.esgi.j2e.group6.captchup;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.esgi.j2e.group6.captchup.user.model.User;
import fr.esgi.j2e.group6.captchup.user.repository.UserRepository;
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
    @Autowired
    private UserRepository userRepository;

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
    public void follow_shouldReturnOk() throws Exception {
        JacksonJsonParser jsonParser = new JacksonJsonParser();

        final ResultActions result = mockMvc.perform(
                patch("/user/follow/{id}", 1)
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk());

        // the response body does not returns the followings so we retrieve it
        User currentUser = userRepository.findByUsername(jsonParser.parseMap(result.andReturn().getResponse().getContentAsString()).get("username").toString());
        assert(currentUser.getFollowed() != null);

        User userFollowed = currentUser.getFollowed().stream().filter(x -> x.getId() == 1).findFirst().orElse(null);
        assert(userFollowed != null);
        currentUser.getFollowed().remove(userFollowed);
        userRepository.save(currentUser);
    }

    @Test
    public void follow_inexistantUser_shouldReturnNotFound() throws Exception {
        JacksonJsonParser jsonParser = new JacksonJsonParser();

        final ResultActions result = mockMvc.perform(
                patch("/user/follow/{id}", 999999)
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNotFound());
    }

    @Test
    public void unfollow_inexistantUser_shouldReturnNotFound() throws Exception {
        JacksonJsonParser jsonParser = new JacksonJsonParser();

        final ResultActions result = mockMvc.perform(
                patch("/user/unfollow/{id}", 999999)
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNotFound());
    }

    @Test
    public void follow_itself_shouldReturnBadRequest() throws Exception {
        JacksonJsonParser jsonParser = new JacksonJsonParser();

        final ResultActions result = mockMvc.perform(
                patch("/user/follow/{id}", 13)
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void unfollow_shouldReturnOk() throws Exception {
        JacksonJsonParser jsonParser = new JacksonJsonParser();

        final ResultActions followResult = mockMvc.perform(
                patch("/user/follow/{id}", 1)
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        followResult.andExpect(status().isOk());

        final ResultActions unfollowResult = mockMvc.perform(
                patch("/user/unfollow/{id}", 1)
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        unfollowResult.andExpect(status().isOk());

        // the response body does not returns the followings so we retrieve it
        User currentUser = userRepository.findByUsername(jsonParser.parseMap(unfollowResult.andReturn().getResponse().getContentAsString()).get("username").toString());
        assert(currentUser.getFollowed() == null || currentUser.getFollowed().size() == 0);

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
    public void signUp_shouldReturnOk() throws Exception {

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

        String username = jsonParser.parseMap(result.andReturn().getResponse().getContentAsString()).get("username").toString();
        assert(userExists(username));

        deleteUser(username);
    }

    @Test
    public void deleteUser_shouldReturnOk() throws Exception {
        User user = userRepository.save(new User("temporary", "temporary"));

        final ResultActions deletionResult = mockMvc.perform(
                delete("/user/delete/{id}", user.getId())
                        .header("Authorization",token)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        deletionResult.andExpect(status().isOk());

        assert(!userExists("temporary"));
    }

    public boolean userExists(String username) {
        User user = userRepository.findByUsername(username);
        return user != null;
    }

    public void deleteUser(String username) {
        userRepository.delete(userRepository.findByUsername(username));
    }




}
