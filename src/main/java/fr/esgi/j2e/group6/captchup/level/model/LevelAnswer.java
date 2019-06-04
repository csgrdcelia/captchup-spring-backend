package fr.esgi.j2e.group6.captchup.level.model;

import com.google.api.client.util.DateTime;
import fr.esgi.j2e.group6.captchup.user.model.User;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class LevelAnswer implements Serializable {
    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn
    private Level level;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "level_id", insertable=false, updatable=false, referencedColumnName = "level_id"),
            @JoinColumn(name = "prediction_id", insertable=false, updatable=false, referencedColumnName = "prediction_id")
    })
    private LevelPrediction levelPrediction;

    @ManyToOne
    @JoinColumn
    private User user;

    private DateTime submittedDate;

    public LevelAnswer() {
    }

    public LevelAnswer(Level level, LevelPrediction levelPrediction, User user, DateTime submittedDate) {
        this.level = level;
        this.levelPrediction = levelPrediction;
        this.user = user;
        this.submittedDate = submittedDate;
    }

    public LevelAnswer(Integer id, Level level, LevelPrediction levelPrediction, User user, DateTime submittedDate) {
        this.id = id;
        this.level = level;
        this.levelPrediction = levelPrediction;
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

    public LevelPrediction getLevelPrediction() {
        return levelPrediction;
    }

    public void setLevelPrediction(LevelPrediction levelPrediction) {
        this.levelPrediction = levelPrediction;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DateTime getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(DateTime submittedDate) {
        this.submittedDate = submittedDate;
    }
}
