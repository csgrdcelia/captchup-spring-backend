package fr.esgi.j2e.group6.captchup;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.esgi.j2e.group6.captchup.user.model.User;
import fr.esgi.j2e.group6.captchup.user.repository.UserRepository;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CaptchupApplication.class)
@AutoConfigureMockMvc
public class LevelControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    private User connectedUser;
    private ObjectMapper mapper;
    private String token;

    @Before
    public void before() throws Exception {
        mapper = new ObjectMapper();

        final ResultActions resultSignUp = mockMvc.perform(
                post("/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new User("testUser", "testUser")))
        );

        connectedUser = userRepository.findByUsername("testUser");

        final ResultActions result = mockMvc.perform(
                post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new User("testUser", "testUser")))
        );

        token = result.andReturn().getResponse().getHeader("Authorization");
    }

    @After
    public void after() {
        userRepository.delete(connectedUser);
    }

    @Ignore("api isn't free")
    @Test
    public void shouldCreateLevel() throws Exception {
        File file = new File("src/main/resources/test-image.jpg");
        FileInputStream input = new FileInputStream(file);
        MockMultipartFile multipartFile = new MockMultipartFile("image",
                file.getName(), "image/jpeg", IOUtils.toByteArray(input));


        mockMvc .perform(MockMvcRequestBuilders.multipart("/level/create")
                        .file("image", multipartFile.getBytes())
                        .header("Authorization",token))
                .andExpect(status().isCreated());
    }


}
