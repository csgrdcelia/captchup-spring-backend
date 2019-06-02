package fr.esgi.j2e.group6.captchup.level.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Prediction {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String word;

    @OneToMany(mappedBy = "prediction", cascade = CascadeType.ALL)
    private Set<LevelPrediction> predictions = new HashSet<>();

    public Prediction() { }

    public Prediction(String word) {
        this.word = word;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
