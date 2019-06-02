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
    public void TEST() throws MalformedURLException {
        Prediction publisherA = new Prediction("Prediction A");
        Prediction publisherB = new Prediction("Prediction B");
        predictionRepository.saveAll(Arrays.asList(publisherA, publisherB));

        levelRepository.save(new Level("Level 1",new URL("http://www.google.com"), user, new LevelPrediction(publisherA, 90.0), new LevelPrediction(publisherB, 91.0)));
        levelRepository.save(new Level("Level 2", new URL("http://www.google.com"), user, new LevelPrediction(publisherA, 87.0)));
    }

    /*@Test
    public void shouldCreatePredictions() {
        predictionRepository.saveAll(getPredictionList());

        Iterable<Prediction> predictionsIterator = predictionRepository.findAll();
        predictionRepository.deleteAll();

        assert(((Collection<?>)predictionsIterator).size() == 2);
    }

    @Test
    public void shouldCreateLevel() throws MalformedURLException {

        predictionRepository.saveAll(getPredictionList());

        Iterable<Prediction> predictionsIterable = predictionRepository.findAll();

        List<Prediction> predictions = new ArrayList<>();

        predictionsIterable.forEach(predictions::add);

        LevelPrediction levelPrediction1 = new LevelPrediction(predictions.get(0), 90.0);
        LevelPrediction levelPrediction2 = new LevelPrediction(predictions.get(1), 95.0);

        Level level = new Level("level",user, new URL("http://www.google.com"), levelPrediction1, levelPrediction2);

        levelRepository.save(level);
        levelRepository.deleteAll();
    }

    public List<Prediction> getPredictionList() {
        List<Prediction> predictions = new ArrayList<>();
        predictions.add(new Prediction("ciel"));
        predictions.add(new Prediction("bleu"));
        return predictions;
    }

    public List<LevelPrediction> getLevelPredictionList() {
        List<Prediction> predictionList = getPredictionList();
        List<LevelPrediction> levelPredictionList = new ArrayList<>();
        levelPredictionList.add(new LevelPrediction(predictionList.get(0), 90.0));
        levelPredictionList.add(new LevelPrediction(predictionList.get(1), 95.0));
        return levelPredictionList;
    }*/

}
