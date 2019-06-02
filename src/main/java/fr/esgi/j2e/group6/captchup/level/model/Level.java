package fr.esgi.j2e.group6.captchup.level.model;

import fr.esgi.j2e.group6.captchup.user.model.User;

import javax.persistence.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
public class Level {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private User creator;

    private URL image;

    @OneToMany(mappedBy = "level", cascade = CascadeType.ALL)
    private Set<LevelPrediction> predictions = new HashSet<>();

    public Level() { }

    public Level(User creator, URL image, LevelPrediction... bookPublishers) {
        this.creator = creator;
        this.image = image;
        for(LevelPrediction bookPublisher : bookPublishers) bookPublisher.setLevel(this);
        this.predictions = Stream.of(bookPublishers).collect(Collectors.toSet());
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<LevelPrediction> getPredictions() {
        return predictions;
    }

    public void setPredictions(Set<LevelPrediction> predictions) {
        this.predictions = predictions;
    }
}
