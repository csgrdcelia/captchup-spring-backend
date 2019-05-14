package fr.esgi.j2e.group6.captchup.level.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String word;

    @OneToMany(mappedBy = "answer")
    private List<LevelAnswer> levels;

    public Answer(String word, List<LevelAnswer> levels) {
        this.word = word;
        this.levels = levels;
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

    public List<LevelAnswer> getLevels() {
        return levels;
    }

    public void setLevels(List<LevelAnswer> levels) {
        this.levels = levels;
    }
}
