package fr.esgi.j2e.group6.captchup.level.repository;

import fr.esgi.j2e.group6.captchup.level.model.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface PredictionRepository extends JpaRepository<Prediction, Integer> {

}
