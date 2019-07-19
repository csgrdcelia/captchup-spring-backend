package fr.esgi.j2e.group6.captchup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.esgi.j2e.group6.captchup.config.model.Config;
import fr.esgi.j2e.group6.captchup.config.repository.ConfigRepository;
import fr.esgi.j2e.group6.captchup.user.model.User;
import fr.esgi.j2e.group6.captchup.user.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CaptchupApplication.class)
@AutoConfigureMockMvc
public class ConfigControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private ConfigRepository configRepository;

    private User connectedUser;
    private ObjectMapper mapper;
    private String token;
    private Config config;

    @Before
    public void before() throws Exception {
        mapper = new ObjectMapper();

        signUpUser();
        loginUser();
        saveTestConfig();

    }

    @After
    public void after() {
        userRepository.delete(connectedUser);
        configRepository.delete(config);
    }

    @Test
    public void shouldConfig_BePatched() throws Exception {

        String newTestValue = "newTestValue";

        config.setValue(newTestValue);

        final ResultActions result = mockMvc.perform(
                patch("/config")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization",token)
                        .content(mapper.writeValueAsString(config))
        );

        Config config = mapper.readValue(result.andReturn().getResponse().getContentAsString(), Config.class);

        assert(config.getValue().equals(newTestValue));
    }

    private void signUpUser() throws Exception {
        mockMvc.perform( post("/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new User("testUser", "testUser")))
        );
    }

    private void loginUser() throws Exception {
        final ResultActions result = mockMvc.perform(
                post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new User("testUser", "testUser")))
        );
        token = result.andReturn().getResponse().getHeader("Authorization");
        connectedUser = userRepository.findByUsername("testUser");
    }

    private void saveTestConfig() {
        config = new Config("testName", "testValue");
        configRepository.save(config);
    }
}
