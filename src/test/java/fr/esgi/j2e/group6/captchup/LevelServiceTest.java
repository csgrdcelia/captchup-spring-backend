package fr.esgi.j2e.group6.captchup;

import fr.esgi.j2e.group6.captchup.level.model.Level;
import fr.esgi.j2e.group6.captchup.level.repository.LevelRepository;
import fr.esgi.j2e.group6.captchup.level.repository.PredictionRepository;
import fr.esgi.j2e.group6.captchup.level.service.LevelService;
import fr.esgi.j2e.group6.captchup.user.model.User;
import fr.esgi.j2e.group6.captchup.user.repository.UserRepository;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CaptchupApplication.class)
public class LevelServiceTest {
    @Autowired private LevelService levelService;

    @Autowired UserRepository userRepository;

    @Autowired LevelRepository levelRepository;

    User user;

    @Before
    public void before() {
        user = userRepository.save(new User("user5", "user1"));
    }

    @After
    public void after() {
        userRepository.delete(user);
    }

    @Ignore("api isn't free")
    @Test
    public void shouldCreateLevel() throws IOException {
        MultipartFile imageFile = getImageFile();
        Level level = levelService.createLevel(imageFile, user);

        assertThat(level != null);
        assertThat(level.getLevelPredictions().get(0).getPrediction().getWord()).isEqualTo("Cactus");

        levelRepository.delete(level);
    }

    public static MultipartFile getImageFile() throws IOException {
        File file = new File("src/main/resources/test-image.jpg");
        FileInputStream input = new FileInputStream(file);
        return new MockMultipartFile("test-image",
                file.getName(), "image/jpeg", IOUtils.toByteArray(input));

    }
}
