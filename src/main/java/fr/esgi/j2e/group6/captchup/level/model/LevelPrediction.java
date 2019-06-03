package fr.esgi.j2e.group6.captchup.level.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Transactional
public class LevelPrediction implements Serializable {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @JsonBackReference
    private Level level;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Prediction prediction;

    private Double pertinence;

    public LevelPrediction() {
    }

    public LevelPrediction(Prediction prediction, Double pertinence) {
        this.prediction = prediction;
        this.pertinence = pertinence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LevelPrediction)) return false;
        LevelPrediction that = (LevelPrediction) o;
        return Objects.equals(level.getId(), that.level.getId()) &&
                Objects.equals(prediction.getWord(), that.prediction.getWord()) &&
                Objects.equals(pertinence, that.pertinence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(level.getId(), prediction.getWord(), pertinence);
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Prediction getPrediction() {
        return prediction;
    }

    public void setPrediction(Prediction prediction) {
        this.prediction = prediction;
    }

    public Double getPertinence() {
        return pertinence;
    }

    public void setPertinence(Double pertinence) {
        this.pertinence = pertinence;
    }
}

