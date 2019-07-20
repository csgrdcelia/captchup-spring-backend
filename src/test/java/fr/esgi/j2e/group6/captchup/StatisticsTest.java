package fr.esgi.j2e.group6.captchup;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.esgi.j2e.group6.captchup.level.model.Level;
import fr.esgi.j2e.group6.captchup.level.model.LevelAnswer;
import fr.esgi.j2e.group6.captchup.level.model.LevelPrediction;
import fr.esgi.j2e.group6.captchup.level.model.Prediction;
import fr.esgi.j2e.group6.captchup.level.repository.LevelAnswerRepository;
import fr.esgi.j2e.group6.captchup.level.repository.LevelRepository;
import fr.esgi.j2e.group6.captchup.level.service.LevelAnswerService;
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
    private User user2;
    private ObjectMapper mapper;
    private List<LevelAnswer> levelAnswers;
    private List<Prediction> predictions;
    private Level level1;
    private List<LevelPrediction> levelPredictions;

    @Before
    public void before() throws Exception {
        mapper = new ObjectMapper();
        final ResultActions resultSignUp = mockMvc.perform(
                post("/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new User("testedUser", "testedUser")))
        );

        final ResultActions resultSignUp2 = mockMvc.perform(
                post("/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new User("testedUser2", "testedUser2")))
        );

        user = userRepository.findByUsername("testedUser");
        user2 = userRepository.findByUsername("testedUser2");

        predictions = new ArrayList<>();
        predictions.add(predictionService.save(new Prediction("test1")));
        predictions.add(predictionService.save(new Prediction("test2")));
        predictions.add(predictionService.save(new Prediction("test3")));

        levelPredictions = new ArrayList<>();
        levelPredictions.add(new LevelPrediction(predictions.get(0), 90.0));
        levelPredictions.add(new LevelPrediction(predictions.get(1), 91.0));
        levelPredictions.add(new LevelPrediction(predictions.get(2), 92.0));

        level1 = levelRepository.save(new Level(new URL("http://www.google.com"), user, levelPredictions));

        levelAnswers = new ArrayList<>();
        levelAnswers.add(levelAnswerService.save(new LevelAnswer(level1, predictions.get(0), user, "test")));
        levelAnswers.add(levelAnswerService.save(new LevelAnswer(level1, predictions.get(1), user, "test1")));
        levelAnswers.add(levelAnswerService.save(new LevelAnswer(level1, predictions.get(2), user, "test2")));
        levelAnswers.add(levelAnswerService.save(new LevelAnswer(level1, null, user, "test3")));
        levelAnswers.add(levelAnswerService.save(new LevelAnswer(level1, null, user, "test4")));
        levelAnswers.add(levelAnswerService.save(new LevelAnswer(level1, null, user, "test5")));

        levelAnswers.add(levelAnswerService.save(new LevelAnswer(level1, predictions.get(0), user2, "test")));
        levelAnswers.add(levelAnswerService.save(new LevelAnswer(level1, predictions.get(1), user2, "test1")));
        levelAnswers.add(levelAnswerService.save(new LevelAnswer(level1, predictions.get(2), user2, "test2")));
        levelAnswers.add(levelAnswerService.save(new LevelAnswer(level1, null, user2, "test3")));
    }

    @After
    public void after() {
        levelAnswerRepository.deleteAll(levelAnswers);
        predictionService.deleteAll(predictions);
        levelRepository.delete(level1);
        userRepository.delete(user);
        userRepository.delete(user2);
    }

    @Test
    public void shouldReturnNumberOfSolvedLevelByUser() {
        Integer value = levelAnswerRepository.numberOfSolvedLevelsByUser(user.getId());
        assert(value == 1);
    }

    @Test
    public void shouldReturnNumberOfGoodAnswerByUser() {
        List<LevelAnswer> values = levelAnswerRepository.findAllByUserAndPredictionNotNull(user);
        assert(values.size() == 3);
    }

    @Test
    public void shouldReturnNumberOfLevelAnswerByUser() {
        List<LevelAnswer> values = levelAnswerRepository.findAllByUser(user);
        assert(values.size() == 6);
    }

    @Test
    public void shouldReturnAverageNumberOfAnswersAndCompletedLevels() {
        Double averageNumber = levelAnswerRepository.averageNumberOfAnswersAndCompletedLevels(level1.getId());
        Double resultExpected = 5d;

        assert(averageNumber.equals(resultExpected));
    }

    @Test
    public void shouldReturnAllLevelsCreatedByUser() {
        List<Level> levels = levelRepository.findAllByCreator(user);
        assert(levels.size() == 1);
    }
}
