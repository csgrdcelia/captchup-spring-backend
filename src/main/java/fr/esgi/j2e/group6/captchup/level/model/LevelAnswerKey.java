package fr.esgi.j2e.group6.captchup.level.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class LevelAnswerKey implements Serializable {
    @Column(name = "level_id")
    private Long levelId;

    @Column(name = "answer_id")
    private Long answerId;

    public LevelAnswerKey(Long levelId, Long answerId) {
        this.levelId = levelId;
        this.answerId = answerId;
    }

    public Long getLevelId() {
        return levelId;
    }

    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LevelAnswerKey that = (LevelAnswerKey) o;
        return Objects.equals(levelId, that.levelId) &&
                Objects.equals(answerId, that.answerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(levelId, answerId);
    }
}
