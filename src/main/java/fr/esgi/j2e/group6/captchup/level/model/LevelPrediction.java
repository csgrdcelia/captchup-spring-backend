package fr.esgi.j2e.group6.captchup.level.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class LevelPrediction implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn
    private Level level;

    @Id
    @ManyToOne
    @JoinColumn
    private Prediction prediction;

    private Double pertinence;

    public LevelPrediction() { }

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
                Objects.equals(prediction.getId(), that.prediction.getId()) &&
                Objects.equals(pertinence, that.pertinence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(level.getId(), prediction.getId(), pertinence);
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
