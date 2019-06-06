package fr.esgi.j2e.group6.captchup.level.model;

import fr.esgi.j2e.group6.captchup.user.model.User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class LevelAnswer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn
    private Level level;

    @ManyToOne
    /*
    @JoinColumns({
            @JoinColumn(name = "level_id", insertable=false, updatable=false, referencedColumnName = "level_id"),
            @JoinColumn(name = "prediction_id", insertable=false, updatable=false, referencedColumnName = "prediction_id")
    })*/
    @JoinColumn
    private Prediction prediction;

    @ManyToOne
    @JoinColumn
    private User user;

    private LocalDateTime submittedDate;

    private String word;

    public LevelAnswer() {
    }

    public LevelAnswer(Level level, Prediction prediction, User user, String word) {
        this.level = level;
        this.prediction = prediction;
        this.user = user;
        this.word = word;
    }

    public LevelAnswer(Level level, Prediction prediction, User user, LocalDateTime submittedDate) {
        this.level = level;
        this.prediction = prediction;
        this.user = user;
        this.submittedDate = submittedDate;
    }

    public LevelAnswer(Integer id, Level level, Prediction prediction, User user, LocalDateTime submittedDate) {
        this.id = id;
        this.level = level;
        this.prediction = prediction;
        this.user = user;
        this.submittedDate = submittedDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(LocalDateTime submittedDate) {
        this.submittedDate = submittedDate;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        return "LevelAnswer{" +
                "id=" + id +
                ", level=" + level +
                ", Prediction=" + prediction +
                ", user=" + user +
                ", submittedDate=" + submittedDate +
                ", word=" + word +
                '}';
    }
}
