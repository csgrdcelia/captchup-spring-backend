package fr.esgi.j2e.group6.captchup.level.service;

import fr.esgi.j2e.group6.captchup.level.model.Prediction;
import fr.esgi.j2e.group6.captchup.level.repository.PredictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PredictionService {

    @Autowired
    PredictionRepository predictionRepository;

    public List<Prediction> getPredictionsByLevelId(Integer levelId) {
        return predictionRepository.getPredictionsByLevelId(levelId);
    }

    public Prediction save(Prediction prediction) {
        return predictionRepository.save(prediction);
    }

    public void deleteAll(List<Prediction> predictions) {
        predictionRepository.deleteAll(predictions);
    }
}
