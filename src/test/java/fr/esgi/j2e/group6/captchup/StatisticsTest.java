package fr.esgi.j2e.group6.captchup;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.esgi.j2e.group6.captchup.level.model.Level;
import fr.esgi.j2e.group6.captchup.level.model.LevelAnswer;
import fr.esgi.j2e.group6.captchup.level.model.LevelPrediction;
import fr.esgi.j2e.group6.captchup.level.model.Prediction;
import fr.esgi.j2e.group6.captchup.level.repository.LevelAnswerRepository;
import fr.esgi.j2e.group6.captchup.level.repository.LevelRepository;
import fr.esgi.j2e.group6.captchup.level.service.LevelAnswerService;
import fr.esgi.j2e.group6.captchup.level.service.LevelService;
import fr.esgi.j2e.group6.captchup.level.service.PredictionService;
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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CaptchupApplication.class)
@AutoConfigureMockMvc
public class StatisticsTest {
    @Autowired private MockMvc mockMvc;

    @Autowired
    private LevelAnswerRepository levelAnswerRepository;

    @Autowired
    private LevelRepository levelRepository;

    @Autowired
    private LevelAnswerService levelAnswerService;

    @Autowired
    PredictionService predictionService;

    @Autowired
    UserRepository userRepository;

    private User user;
    private ObjectMapper mapper;
    private LevelAnswer levelAnswer1;
    private LevelAnswer levelAnswer2;
    private LevelAnswer levelAnswer3;
    private List<Prediction> predictions;
    private Level level;
    private List<LevelPrediction> levelPredictions;

    @Before
    public void before() throws Exception {
        mapper = new ObjectMapper();
        final ResultActions resultSignUp = mockMvc.perform(
                post("/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new User("testedUser", "testedUser")))
        );

        user = userRepository.findByUsername("testedUser");
        predictions = new ArrayList<>();
        predictions.add(predictionService.save(new Prediction("test1")));
        predictions.add(predictionService.save(new Prediction("test2")));
        predictions.add(predictionService.save(new Prediction("test3")));

        levelPredictions = new ArrayList<>();
        levelPredictions.add(new LevelPrediction(predictions.get(0), 90.0));
        levelPredictions.add(new LevelPrediction(predictions.get(1), 91.0));
        levelPredictions.add(new LevelPrediction(predictions.get(2), 91.0));

        level = levelRepository.save(new Level(new URL("http://www.google.com"), user, levelPredictions));

        levelAnswer1 = levelAnswerService.save(new LevelAnswer(level, predictions.get(0), user, "test"));
        levelAnswer2 = levelAnswerService.save(new LevelAnswer(level, predictions.get(1), user, "test1"));
        levelAnswer3 = levelAnswerService.save(new LevelAnswer(level, predictions.get(2), user, "test2"));
    }

    @After
    public void after() {
        levelAnswerRepository.delete(levelAnswer1);
        levelAnswerRepository.delete(levelAnswer2);
        levelRepository.delete(level);
        predictionService.deleteAll(predictions);
        userRepository.delete(user);
    }

    @Test
    public void shouldReturnNumberOfLevelAnswerByUser() throws Exception {
        Integer value = levelAnswerRepository.numberOfSolvedLevelsByUser(user.getId());
        assert(value == 1);
    }
}
