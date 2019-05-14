package fr.esgi.j2e.group6.captchup.level.model;

import fr.esgi.j2e.group6.captchup.user.model.User;

import javax.persistence.*;
import java.net.URL;
import java.util.List;

@Entity
public class Level {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private User creator;

    private URL image;

    @ManyToMany
    private List<Answer> answers;

    public Level() { }

    public Level(User creator, URL image, List<Answer> answers) {
        this.creator = creator;
        this.image = image;
        this.answers = answers;
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

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
