package fr.esgi.j2e.group6.captchup;

import com.javaetmoi.core.persistence.hibernate.JpaLazyLoadingUtil;
import fr.esgi.j2e.group6.captchup.level.model.*;
import fr.esgi.j2e.group6.captchup.level.repository.LevelAnswerRepository;
import fr.esgi.j2e.group6.captchup.level.repository.LevelRepository;
import fr.esgi.j2e.group6.captchup.level.repository.PredictionRepository;
import fr.esgi.j2e.group6.captchup.user.model.User;
import fr.esgi.j2e.group6.captchup.user.repository.UserRepository;
import org.hibernate.Hibernate;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CaptchupApplication.class)
public class LevelRepositoryTest {

    @Autowired private LevelRepository levelRepository;

    @Autowired PredictionRepository predictionRepository;

    @Autowired UserRepository userRepository;

    @Autowired LevelAnswerRepository levelAnswerRepository;

    @PersistenceContext
    private EntityManager em;

    private User user;

    @Before
    public void setUp() {
        user = userRepository.save(new User("user1", "user1"));
    }

    @After
    public void after() {
        userRepository.delete(user);
    }

    @Transactional
    @Test
    public void shouldCreateLevel() throws MalformedURLException {
        Prediction predictionA = new Prediction("Prediction A");
        Prediction predictionB = new Prediction("Prediction B");
        predictionRepository.saveAll(Arrays.asList(predictionA, predictionB));

        List<LevelPrediction> levelPredictions = new ArrayList<>();
        levelPredictions.add(new LevelPrediction(predictionA, 90.0));
        levelPredictions.add(new LevelPrediction(predictionB, 91.0));

        Level level = levelRepository.save(new Level(new URL("http://www.google.com"), user, levelPredictions));

        Optional<Level> optionalLevel = levelRepository.findById(level.getId());

        assert(optionalLevel.isPresent());

        Level foundLevel = optionalLevel.get();

        assert(foundLevel.getLevelPredictions().get(0).getPrediction().getWord().equals("Prediction A"));
        assert(foundLevel.getLevelPredictions().get(0).getPertinence().equals(90.0));
        assert(foundLevel.getLevelPredictions().get(1).getPrediction().getWord().equals("Prediction B"));
        assert(foundLevel.getLevelPredictions().get(1).getPertinence().equals(91.0));

        levelRepository.delete(foundLevel);
    }

    @Test
    public void findLevelsByCreationDateAndCreator_shouldReturn2() throws MalformedURLException {
        Level level1 = new Level(new URL("http://www.image1.com"), user, new ArrayList<LevelPrediction>());
        Level level2 = new Level(new URL("http://www.image2.com"), user, new ArrayList<LevelPrediction>());
        levelRepository.save(level1);
        levelRepository.save(level2);

        int createdLevelsToday = levelRepository.findByCreationDateAndCreator(LocalDate.now(), user).size();

        levelRepository.delete(level1);
        levelRepository.delete(level2);

        assert(createdLevelsToday == 2);
    }

    @Test
    public void shouldFindUnfinishedLevels() throws MalformedURLException {
        List<Prediction> predictions = new ArrayList<>();
        predictions.add(predictionRepository.save(new Prediction("test1")));
        predictions.add(predictionRepository.save(new Prediction("test2")));
        predictions.add(predictionRepository.save(new Prediction("test3")));

        List<LevelPrediction> levelPredictions = new ArrayList<>();
        levelPredictions.add(new LevelPrediction(predictions.get(0), 90.0));
        levelPredictions.add(new LevelPrediction(predictions.get(1), 91.0));
        levelPredictions.add(new LevelPrediction(predictions.get(2), 91.0));

        Level level = levelRepository.save(new Level(new URL("http://www.google.com"), user, levelPredictions));

        LevelAnswer levelAnswer1 = levelAnswerRepository.save(new LevelAnswer(level, predictions.get(0), user, "test1"));

        List<Level> unfinishedLevels = levelRepository.findUnfinishedLevelsBy(user.getId());

        levelAnswerRepository.delete(levelAnswer1);
        levelRepository.delete(level);
        predictionRepository.deleteAll(predictions);

        assert(unfinishedLevels.size() == 1);
        assert(unfinishedLevels.get(0).getId() == level.getId());
    }

    @Test
    public void shouldFindFinishedLevels() throws MalformedURLException {
        List<Prediction> predictions = new ArrayList<>();
        predictions.add(predictionRepository.save(new Prediction("test1")));
        predictions.add(predictionRepository.save(new Prediction("test2")));
        predictions.add(predictionRepository.save(new Prediction("test3")));

        List<LevelPrediction> levelPredictions = new ArrayList<>();
        levelPredictions.add(new LevelPrediction(predictions.get(0), 90.0));
        levelPredictions.add(new LevelPrediction(predictions.get(1), 91.0));
        levelPredictions.add(new LevelPrediction(predictions.get(2), 91.0));

        Level level = levelRepository.save(new Level(new URL("http://www.google.com"), user, levelPredictions));

        List<LevelAnswer> levelAnswers = new ArrayList<>();
        levelAnswers.add(levelAnswerRepository.save(new LevelAnswer(level, predictions.get(0), user, "test1")));
        levelAnswers.add(levelAnswerRepository.save(new LevelAnswer(level, predictions.get(1), user, "test2")));
        levelAnswers.add(levelAnswerRepository.save(new LevelAnswer(level, predictions.get(2), user, "test3")));

        List<Level> finishedLevels = levelRepository.findFinishedLevelsBy(user.getId());

        levelAnswerRepository.deleteAll(levelAnswers);
        levelRepository.delete(level);
        predictionRepository.deleteAll(predictions);

        assert(finishedLevels.size() == 1);
        assert(finishedLevels.get(0).getId() == level.getId());
    }

}
