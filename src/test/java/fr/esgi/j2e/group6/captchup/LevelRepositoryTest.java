package fr.esgi.j2e.group6.captchup;

import fr.esgi.j2e.group6.captchup.level.model.*;
import fr.esgi.j2e.group6.captchup.level.repository.LevelRepository;
import fr.esgi.j2e.group6.captchup.level.repository.PredictionRepository;
import fr.esgi.j2e.group6.captchup.user.model.User;
import fr.esgi.j2e.group6.captchup.user.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CaptchupApplication.class)
public class LevelRepositoryTest {

    @Autowired
    private LevelRepository levelRepository;

    @Autowired
    PredictionRepository predictionRepository;

    @Autowired
    UserRepository userRepository;

    private User user;

    @Before
    public void setUp() {
        user = userRepository.save(new User("user1", "user1"));
    }

    @After
    public void after() {
        userRepository.delete(user);
    }

    @Test
    public void shouldCreateLevel() throws MalformedURLException {
        Prediction predictionA = new Prediction("Prediction A");
        Prediction predictionB = new Prediction("Prediction B");
        predictionRepository.saveAll(Arrays.asList(predictionA, predictionB));

        List<LevelPrediction> levelPredictions = new ArrayList<>();
        levelPredictions.add(new LevelPrediction(predictionA, 90.0));
        levelPredictions.add(new LevelPrediction(predictionB, 91.0));

        levelRepository.save(new Level(new URL("http://www.google.com"), user, levelPredictions));

        List<Level> levels = levelRepository.findAll();
        assert(levels.size() == 1);
        assert(levels.get(0).getLevelPredictions().get(0).getPrediction().getWord().equals("Prediction A"));
        assert(levels.get(0).getLevelPredictions().get(0).getPertinence().equals(90.0));
        assert(levels.get(0).getLevelPredictions().get(1).getPrediction().getWord().equals("Prediction B"));
        assert(levels.get(0).getLevelPredictions().get(1).getPertinence().equals(91.0));

        levelRepository.deleteAll();
    }
}
