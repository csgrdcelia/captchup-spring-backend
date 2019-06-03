package fr.esgi.j2e.group6.captchup.level.model;

import fr.esgi.j2e.group6.captchup.user.model.User;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
public class Level {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    private User creator;

    private URL image;

    public Level() { }

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "level", cascade = CascadeType.ALL )
    private List<LevelPrediction> levelPredictions;

    public Level(URL image, User creator, LevelPrediction... levelPredictions) {
        this.image = image;
        this.creator = creator;
        for(LevelPrediction levelPrediction : levelPredictions) levelPrediction.setLevel(this);
        this.levelPredictions = Stream.of(levelPredictions).collect(Collectors.toList());
    }

    public Level(URL image, User creator, List<LevelPrediction> levelPredictions) {
        this.image = image;
        this.creator = creator;
        for(LevelPrediction levelPrediction : levelPredictions) levelPrediction.setLevel(this);
        this.levelPredictions = levelPredictions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<LevelPrediction> getLevelPredictions() {
        return levelPredictions;
    }

    public void setLevelPredictions(List<LevelPrediction> levelPredictions) {
        this.levelPredictions = levelPredictions;
    }
}
