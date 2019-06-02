package fr.esgi.j2e.group6.captchup.level.model;

import fr.esgi.j2e.group6.captchup.user.model.User;

import javax.persistence.*;
import java.net.URL;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
public class Level {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @OneToOne
    private User creator;

    private URL image;

    public Level() { }

    @OneToMany(mappedBy = "level", cascade = CascadeType.ALL)
    private Set<LevelPrediction> levelPredictions;

    public Level(String name, URL image, User creator, LevelPrediction... levelPredictions) {
        this.name = name;
        this.image = image;
        this.creator = creator;
        for(LevelPrediction levelPrediction : levelPredictions) levelPrediction.setLevel(this);
        this.levelPredictions = Stream.of(levelPredictions).collect(Collectors.toSet());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<LevelPrediction> getLevelPredictions() {
        return levelPredictions;
    }

    public void setLevelPredictions(Set<LevelPrediction> levelPredictions) {
        this.levelPredictions = levelPredictions;
    }
}
