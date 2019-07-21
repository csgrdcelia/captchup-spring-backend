package fr.esgi.j2e.group6.captchup;

import fr.esgi.j2e.group6.captchup.level.model.Level;
import fr.esgi.j2e.group6.captchup.level.model.LevelAnswer;
import fr.esgi.j2e.group6.captchup.level.model.LevelPrediction;
import fr.esgi.j2e.group6.captchup.level.model.Prediction;
import fr.esgi.j2e.group6.captchup.level.repository.LevelAnswerRepository;
import fr.esgi.j2e.group6.captchup.level.repository.LevelRepository;
import fr.esgi.j2e.group6.captchup.level.repository.PredictionRepository;
import fr.esgi.j2e.group6.captchup.user.model.User;
import fr.esgi.j2e.group6.captchup.user.repository.UserRepository;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CaptchupApplication.class)
public class PredictionRepositoryTest {
    @Autowired
    PredictionRepository predictionRepository;
    @Autowired
    LevelRepository levelRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    LevelAnswerRepository levelAnswerRepository;

    List<Prediction> predictions;
    List<LevelPrediction> levelPredictions;
    Level level;
    User user;

    @Before
    public void setUp() throws MalformedURLException {
        user = userRepository.save(new User("usertest", "password"));

        predictions = new ArrayList<>();
        predictions.add(predictionRepository.save(new Prediction("test1")));
        predictions.add(predictionRepository.save(new Prediction("test2")));
        predictions.add(predictionRepository.save(new Prediction("test3")));

        levelPredictions = new ArrayList<>();
        levelPredictions.add(new LevelPrediction(predictions.get(0), 90.0));
        levelPredictions.add(new LevelPrediction(predictions.get(1), 91.0));
        levelPredictions.add(new LevelPrediction(predictions.get(2), 91.0));

        level = levelRepository.save(new Level(new URL("http://www.google.com"), user, levelPredictions));
    }

    @Test
    public void shouldFindSolvedPredictions() {
        LevelAnswer levelAnswer1 = levelAnswerRepository.save(new LevelAnswer(level, predictions.get(0), user, "test1"));
        LevelAnswer levelAnswer2 = levelAnswerRepository.save(new LevelAnswer(level, null, user, "test"));

        List<Prediction> solvedPredictions =  predictionRepository.findSolvedPredictions(level.getId(), user.getId());

        levelAnswerRepository.delete(levelAnswer1);
        levelAnswerRepository.delete(levelAnswer2);

        assert(solvedPredictions.size() == 1);
        assert(solvedPredictions.contains(levelAnswer1.getPrediction()));
    }

    @After
    public void after() {
        levelRepository.delete(level);
        predictionRepository.deleteInBatch(predictions);
        userRepository.delete(user);
    }
}
