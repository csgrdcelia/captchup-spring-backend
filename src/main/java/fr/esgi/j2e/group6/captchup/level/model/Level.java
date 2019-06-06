package fr.esgi.j2e.group6.captchup.level.model;

import fr.esgi.j2e.group6.captchup.user.model.User;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
public class Level {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne
    private User creator;

    private URL image;

    private int difficulty;

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
        this.difficulty = difficultyFromPredictions();
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
    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int difficultyFromPredictions(){
        List<Double> pertinence = new ArrayList<>();
        for (LevelPrediction levelPrediction : this.levelPredictions) {
            pertinence.add(levelPrediction.getPertinence());
        }
        OptionalDouble average = pertinence.stream().mapToDouble(a -> a).average();
        Double doubleAverage = average.getAsDouble();
        if(doubleAverage > 0.95){
            return 1;
        }
        else if(doubleAverage > 0.90){
            return 2;
        }
        else{
            return 3;
        }
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
