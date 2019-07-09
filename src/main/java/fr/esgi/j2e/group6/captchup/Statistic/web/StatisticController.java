package fr.esgi.j2e.group6.captchup.Statistic.web;

import fr.esgi.j2e.group6.captchup.level.model.Level;
import fr.esgi.j2e.group6.captchup.level.model.LevelAnswer;
import fr.esgi.j2e.group6.captchup.level.service.LevelAnswerService;
import fr.esgi.j2e.group6.captchup.level.service.LevelService;
import fr.esgi.j2e.group6.captchup.user.model.User;
import fr.esgi.j2e.group6.captchup.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/statistic")
public class StatisticController {

    @Autowired
    LevelAnswerService levelAnswerService;

    @Autowired
    UserService userService;

    @Autowired
    LevelService levelService;

    @GetMapping(path = "/getNumberOfLevelAnswer")
    public @ResponseBody
    ResponseEntity<Integer> getNumberOfTestedLevels() {
        List<LevelAnswer> levelAnswers = levelAnswerService.getAllLevelAnswers();

        return ResponseEntity.status(HttpStatus.OK).body(levelAnswers.size());
    }

    @GetMapping(path = "/getNumberOfLevelAnswerByUser")
    public @ResponseBody
    ResponseEntity<Integer> getNumberOfTestedLevelsByUser() {
        try {
            User user = userService.getCurrentLoggedInUser();
            List<LevelAnswer> levelAnswers = levelAnswerService.getAllLevelAnswersById(user);
            return ResponseEntity.status(HttpStatus.OK).body(levelAnswers.size());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(path = "/getNumberOfSolvedLevels")
    public @ResponseBody
    ResponseEntity<Integer> getNumberOfLevelSolved() {
        try {
            Integer levelAnswersSolvedNumber = levelAnswerService.getNumberOfSolvedLevels();
            return ResponseEntity.status(HttpStatus.OK).body(levelAnswersSolvedNumber);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(path = "/getNumberOfSolvedLevelsByUser")
    public @ResponseBody
    ResponseEntity<Integer> getNumberOfSolvedLevelsByUser() {
        try {
            User user = userService.getCurrentLoggedInUser();
            Integer levelAnswersSolvedNumber = levelAnswerService.getNumberOfSolvedLevelsByUser(user.getId());
            return ResponseEntity.status(HttpStatus.OK).body(levelAnswersSolvedNumber);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(path = "/getAverageNumberOfAnswersPerCompletedLevels/{levelId}")
    public @ResponseBody
    ResponseEntity<Double> getAverageNumberOfAnswersPerCompletedLevels(@PathVariable("levelId") int levelId) {
        try {
            Double averageLevelTrial = levelAnswerService.getAverageNumberOfAnswersPerCompletedLevels(levelId);
        return ResponseEntity.status(HttpStatus.OK).body(averageLevelTrial);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(path = "/getNumberOfLevelTestedByUser")
    public @ResponseBody
    ResponseEntity<Integer> getNumberOfLevelTestedByUser() {
        ArrayList<LevelAnswer> levelAnswersList = new ArrayList<>();
        boolean levelAlreadyAdded = false;

        try {
            User user = userService.getCurrentLoggedInUser();
            List<LevelAnswer> levelAnswers = levelAnswerService.getAllLevelAnswersById(user);

            for (LevelAnswer levelAnswer: levelAnswers) {
                for (LevelAnswer answer : levelAnswersList) {
                    if (answer.getLevel().getId() == levelAnswer.getLevel().getId()) {
                        levelAlreadyAdded = true;
                        break;
                    }
                }
                if(!levelAlreadyAdded) {
                    levelAnswersList.add(levelAnswer);
                }
            }

            return ResponseEntity.status(HttpStatus.OK).body(levelAnswersList.size());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(path = "/getGoodAnswersRateByUser")
    public @ResponseBody
    ResponseEntity<Double> getGoodAnswersRateByUser() {
        try {
            User user = userService.getCurrentLoggedInUser();
            List<LevelAnswer> levelAnswersPredictionNotNull = levelAnswerService.getAllLevelAnswerByUserAndPredictionNotNull(user);
            List<LevelAnswer> levelAnswers = levelAnswerService.getAllLevelAnswersById(user);

            return ResponseEntity.status(HttpStatus.OK).body((double)levelAnswersPredictionNotNull.size() / (double)levelAnswers.size());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(path = "/getGoodAnswersRate")
    public @ResponseBody
    ResponseEntity<Double> getGoodAnswersRate() {
        try {
            List<LevelAnswer> levelAnswersPredictionNotNull = levelAnswerService.getAllLevelAnswerByPredictionNotNull();
            List<LevelAnswer> levelAnswers = levelAnswerService.getAllLevelAnswers();

            return ResponseEntity.status(HttpStatus.OK).body((double)levelAnswersPredictionNotNull.size() / (double)levelAnswers.size());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(path = "/getNumberOfCreatedLevels")
    public @ResponseBody
    ResponseEntity<Integer> getNumberOfCreatedLevels() {
        try {
            List<Level> levels = levelService.getAllLevels();

            return ResponseEntity.status(HttpStatus.OK).body(levels.size());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
