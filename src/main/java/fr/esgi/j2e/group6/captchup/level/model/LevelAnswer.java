package fr.esgi.j2e.group6.captchup.level.model;

import javax.persistence.*;

@Entity
public class LevelAnswer  {

    @EmbeddedId
    private LevelAnswerKey id;

    @ManyToOne
    @MapsId("level_id")
    @JoinColumn(name = "level_id")
    private Level level;

    @ManyToOne
    @MapsId("answer_id")
    @JoinColumn(name = "answer_id")
    private Answer answer;

    private Double pertinence;

    public LevelAnswer(LevelAnswerKey id, Level level, Answer answer, Double pertinence) {
        this.id = id;
        this.level = level;
        this.answer = answer;
        this.pertinence = pertinence;
    }

    public LevelAnswerKey getId() {
        return id;
    }

    public void setId(LevelAnswerKey id) {
        this.id = id;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public Double getPertinence() {
        return pertinence;
    }

    public void setPertinence(Double pertinence) {
        this.pertinence = pertinence;
    }
}
