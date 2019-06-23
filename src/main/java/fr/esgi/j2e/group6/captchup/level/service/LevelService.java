package fr.esgi.j2e.group6.captchup.level.service;

import com.google.api.gax.rpc.InvalidArgumentException;
import com.google.cloud.vision.v1.EntityAnnotation;
import fr.esgi.j2e.group6.captchup.level.model.Level;
import fr.esgi.j2e.group6.captchup.level.model.LevelAnswer;
import fr.esgi.j2e.group6.captchup.level.model.LevelPrediction;
import fr.esgi.j2e.group6.captchup.level.model.Prediction;
import fr.esgi.j2e.group6.captchup.level.repository.LevelAnswerRepository;
import fr.esgi.j2e.group6.captchup.level.repository.LevelRepository;
import fr.esgi.j2e.group6.captchup.level.repository.PredictionRepository;
import fr.esgi.j2e.group6.captchup.user.model.User;
import fr.esgi.j2e.group6.captchup.user.service.UserService;
import fr.esgi.j2e.group6.captchup.vision.service.VisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LevelService {

    @Autowired private LevelRepository levelRepository;
    @Autowired private PredictionRepository predictionRepository;
    @Autowired private VisionService visionService;
    @Autowired private UserService userService;
    @Autowired private LevelService levelService;
    @Autowired private PredictionService predictionService;

    @Autowired LevelAnswerService levelAnswerService;

    public List<Level> getUnfinishedLevelsBy(User user) {
        return null;
    }

    /**
     * @return level if created, null if not
     */
    public Level createLevel(MultipartFile multipartFile, User user) throws MalformedURLException {

        List<EntityAnnotation> annotations = visionService.callAPI(multipartFile);

        List<LevelPrediction> levelPredictions = getFirstThreePredictions(annotations);

        if (levelPredictions != null) {
            //TODO: save image and get image url
            return levelRepository.save(new Level(new URL("http://www.google.com"), user, levelPredictions));
        }

        return null;
    }

    public List<LevelPrediction> getFirstThreePredictions(List<EntityAnnotation> annotations) {
        if(annotations.size() > 3) {

            List<Prediction> predictions = predictionRepository.findAll();
            List<LevelPrediction> levelPredictions = new ArrayList<>();
            for (int i = 0; i < 3; i++) {

                Prediction prediction = getPrediction(annotations, predictions, i);
                levelPredictions.add(new LevelPrediction(prediction, (double)annotations.get(i).getScore()));
            }
            return levelPredictions;
        }
        return null;
    }

    public Prediction getPrediction(List<EntityAnnotation> annotations, List<Prediction> predictions, int predictionNumber) {
        Prediction prediction = new Prediction(annotations.get(predictionNumber).getDescription());
        int predictionIndex = predictions.indexOf(prediction);
        if(predictionIndex != -1) {
            prediction = predictions.get(predictionIndex);
        } else {
            prediction = predictionRepository.save(prediction);
        }
        return prediction;
    }

    public Optional<Level> getLevelById(Integer id) {
        return levelRepository.findById(id);
    }

    public LevelAnswer solveLevel(Integer id, String answer) throws IllegalArgumentException {
        if(answer.equals("")) {
            throw new IllegalArgumentException();
        }

        LevelAnswer levelAnswer = new LevelAnswer(levelService.getLevelById(id).get(),
                null,
                userService.getCurrentLoggedInUser(),
                answer);

        levelAnswer.setPrediction(getMatchingLevelPredictionFromWord(id, answer));

        Optional<LevelAnswer> existingLevelAnswer = levelAnswerService.findByUserAndLevelAndWord(
                levelAnswer.getUser(), levelAnswer.getLevel(), levelAnswer.getWord()
        );

        if(!existingLevelAnswer.isPresent()) {
            levelAnswer.setSubmittedDate(LocalDateTime.now());
            levelAnswer = levelAnswerService.save(levelAnswer);
        }

        return levelAnswer;
    }

    private Prediction getMatchingLevelPredictionFromWord(Integer id, String word) {
        List<Prediction> predictionList = predictionService.getPredictionsByLevelId(id);

        for(Prediction prediction: predictionList) {
            if(prediction.getWord().toLowerCase().equals(word.toLowerCase())) {
                return prediction;
            }
        }

        return null;
    }
}
