package fr.esgi.j2e.group6.captchup.level.model;

import fr.esgi.j2e.group6.captchup.user.model.User;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
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

    private LocalDate creationDate;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "level", cascade = CascadeType.ALL )
    private List<LevelPrediction> levelPredictions;

    public Level() { }

    public Level(URL image, User creator, LevelPrediction... levelPredictions) {
        this.image = image;
        this.creator = creator;
        this.creationDate = LocalDate.now();
        for(LevelPrediction levelPrediction : levelPredictions) levelPrediction.setLevel(this);
        this.levelPredictions = Stream.of(levelPredictions).collect(Collectors.toList());
    }

    public Level(URL image, User creator, List<LevelPrediction> levelPredictions) {
        this.image = image;
        this.creator = creator;
        this.creationDate = LocalDate.now();
        for(LevelPrediction levelPrediction : levelPredictions) levelPrediction.setLevel(this);
        this.levelPredictions = levelPredictions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public URL getImage() {
        return image;
    }

    public void setImage(URL image) {
        this.image = image;
    }

    public List<LevelPrediction> getLevelPredictions() {
        return levelPredictions;
    }

    public void setLevelPredictions(List<LevelPrediction> levelPredictions) {
        this.levelPredictions = levelPredictions;
    }

    @Override
    public String toString() {
        return "Level{" +
                "id=" + id +
                ", creator=" + creator +
                ", image=" + image +
                '}';
    }
}
