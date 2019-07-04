package fr.esgi.j2e.group6.captchup;

import com.google.cloud.vision.v1.EntityAnnotation;
import fr.esgi.j2e.group6.captchup.config.model.Config;
import fr.esgi.j2e.group6.captchup.config.repository.ConfigRepository;
import fr.esgi.j2e.group6.captchup.level.model.Level;
import fr.esgi.j2e.group6.captchup.level.model.LevelPrediction;
import fr.esgi.j2e.group6.captchup.level.repository.LevelRepository;
import fr.esgi.j2e.group6.captchup.user.model.User;
import fr.esgi.j2e.group6.captchup.user.repository.UserRepository;
import fr.esgi.j2e.group6.captchup.vision.service.VisionService;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CaptchupApplication.class)
@AutoConfigureMockMvc
public class VisionServiceTest {

    @Autowired private VisionService visionService;
    @Autowired private ConfigRepository configRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private LevelRepository levelRepository;

    private User user;

    @Before
    public void setUp() {
        user = userRepository.save(new User("user4", "user1"));
    }

    @After
    public void after() {
        userRepository.delete(user);

    }

    @Ignore("api isn't free")
    @Test
    public void callApi_ReturnAnnotationList() throws IOException {

        File file = new File("src/main/resources/test-image.jpg");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("test-image",
                file.getName(), "image/jpeg", IOUtils.toByteArray(input));

        List<EntityAnnotation> annotations = visionService.callAPI(multipartFile);
        assert(annotations.get(0).getDescription().equals("Cactus"));
    }

    @Test
    public void maxAmountOfCallsIsReached_shouldReturnTrue() throws MalformedURLException {
        Config config = configRepository.findByName("max_vision_calls");
        List<Level> levels = new ArrayList<>();

        for (int i = 0; i < Integer.parseInt(config.getValue()); i++) {
            levels.add(new Level(new URL("http://www.google.com"), user, new ArrayList<LevelPrediction>()));
            levelRepository.save(levels.get(i));
        }

        boolean isReached = visionService.maxAmountOfCallsIsReached(LocalDate.now(), user);

        for(Level level : levels)
        {
            levelRepository.delete(level);
        }

        assert(isReached);
    }

    @Test
    public void maxAmountOfCallsIsReached_shouldReturnFalse() throws MalformedURLException {
        Config config = configRepository.findByName("max_vision_calls");
        List<Level> levels = new ArrayList<>();

        for (int i = 0; i < (Integer.parseInt(config.getValue()) - 1); i++) {
            levels.add(new Level(new URL("http://www.google.com"), user, new ArrayList<LevelPrediction>()));
            levelRepository.save(levels.get(i));
        }

        boolean isReached = visionService.maxAmountOfCallsIsReached(LocalDate.now(), user);

        for(Level level : levels)
        {
            levelRepository.delete(level);
        }

        assert(isReached == false);
    }
}
