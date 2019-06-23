package fr.esgi.j2e.group6.captchup.level.repository;

import fr.esgi.j2e.group6.captchup.level.model.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PredictionRepository extends JpaRepository<Prediction, Integer> {
    @Query(value = "select pred.id as id, pred.word as word " +
            "from db.level_prediction as lvl_pred " +
            "join db.prediction pred on lvl_pred.prediction_id = pred.id " +
            "where lvl_pred.level_id = :level_id",
            nativeQuery = true
    )
    List<Prediction> getPredictionsByLevelId(Integer level_id);
}
