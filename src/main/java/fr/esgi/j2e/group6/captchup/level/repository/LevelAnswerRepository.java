package fr.esgi.j2e.group6.captchup.level.repository;

import fr.esgi.j2e.group6.captchup.level.model.Level;
import fr.esgi.j2e.group6.captchup.level.model.LevelAnswer;
import fr.esgi.j2e.group6.captchup.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LevelAnswerRepository extends JpaRepository<LevelAnswer, Integer> {
    Optional<LevelAnswer> findByUserAndLevelAndWord(User user, Level level, String word);
    List<LevelAnswer> findAllByUser(User user);
}
