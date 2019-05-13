package fr.esgi.j2e.group6.captchup;
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

    //not tested, wait for delete function
    @Test
    public void signUp_shouldReturnOk() throws Exception {

        final ResultActions result = mockMvc.perform(
          post("/user/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\t\"username\": \"temporary\",\n" +
                        "\t\"password\": \"temporary\"\n" +
                        "}")
        );

        result.andExpect(status().isOk());

        //TODO: delete created user

    }


}
