package fr.esgi.j2e.group6.captchup.level.model;

import fr.esgi.j2e.group6.captchup.user.model.User;

import java.net.URL;
import java.util.List;

public class Level {
    private User creator;

    private URL image;

    private List<String> answers;

    public Level() { }

    public Level(User creator, URL image, List<String> answers) {
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

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }
}
