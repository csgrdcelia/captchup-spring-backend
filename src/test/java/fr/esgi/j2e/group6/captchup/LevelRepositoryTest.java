package fr.esgi.j2e.group6.captchup;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.esgi.j2e.group6.captchup.level.model.Level;
import fr.esgi.j2e.group6.captchup.level.model.LevelPrediction;
import fr.esgi.j2e.group6.captchup.level.model.Prediction;
import fr.esgi.j2e.group6.captchup.level.repository.LevelRepository;
import fr.esgi.j2e.group6.captchup.level.repository.PredictionRepository;
import fr.esgi.j2e.group6.captchup.user.model.User;
import fr.esgi.j2e.group6.captchup.user.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
    public void shouldCreatePredictions() {
        predictionRepository.saveAll(getPredictionList());

        Iterable<Prediction> predictionsIterator = predictionRepository.findAll();
        predictionRepository.deleteAll();

        assert(((Collection<?>)predictionsIterator).size() == 2);
    }

    @Test
    public void shouldCreateLevel() throws MalformedURLException {

        predictionRepository.saveAll(getPredictionList());

        Level level = new Level(user, new URL("http://www.google.com"), getLevelPredictionList().get(0), getLevelPredictionList().get(1));

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
    }

}
