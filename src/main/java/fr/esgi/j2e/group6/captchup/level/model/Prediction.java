package fr.esgi.j2e.group6.captchup.level.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Prediction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String word;

    @OneToMany(mappedBy = "prediction", cascade = CascadeType.ALL)
    private Set<LevelPrediction> levelPredictions = new HashSet<>();

    public Prediction() {}
    public Prediction(String word) {
        this.word = word;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Set<LevelPrediction> getLevelPredictions() {
        return levelPredictions;
    }

    public void setLevelPredictions(Set<LevelPrediction> levelPredictions) {
        this.levelPredictions = levelPredictions;
    }
}
