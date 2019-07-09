package fr.esgi.j2e.group6.captchup.level.service;

import fr.esgi.j2e.group6.captchup.level.model.Level;
import fr.esgi.j2e.group6.captchup.level.model.LevelAnswer;
import fr.esgi.j2e.group6.captchup.level.repository.LevelAnswerRepository;
import fr.esgi.j2e.group6.captchup.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LevelAnswerService {
    @Autowired
    LevelAnswerRepository levelAnswerRepository;

    public Optional<LevelAnswer> findByUserAndLevelAndWord(User user, Level level, String word) {
        return levelAnswerRepository.findByUserAndLevelAndWord(user, level, word);
    }

    public LevelAnswer save(LevelAnswer levelAnswer) {
        return levelAnswerRepository.save(levelAnswer);
    }

    public List<LevelAnswer> getAllLevelAnswers() {
        return levelAnswerRepository.findAll();
    }

    public List<LevelAnswer> getAllLevelAnswersById(User user) {
        return levelAnswerRepository.findAllByUser(user);
    }

    public Integer getNumberOfSolvedLevels() {
        return levelAnswerRepository.numberOfSolvedLevels();
    }

    public Integer getNumberOfSolvedLevelsByUser(int id) {
        return levelAnswerRepository.numberOfSolvedLevelsByUser(id);
    }

    public Double getAverageNumberOfAnswersPerCompletedLevels(int id) {
        return levelAnswerRepository.averageNumberOfAnswersAndCompletedLevels(id);
    }

    public List<LevelAnswer> getAllLevelAnswerByUserAndPredictionNotNull(User user) {
        return levelAnswerRepository.findAllByUserAndPredictionNotNull(user);
    }
}
