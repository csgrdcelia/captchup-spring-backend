package fr.esgi.j2e.group6.captchup.level.repository;

import fr.esgi.j2e.group6.captchup.level.model.Level;
import fr.esgi.j2e.group6.captchup.level.model.LevelAnswer;
import fr.esgi.j2e.group6.captchup.level.model.Prediction;
import fr.esgi.j2e.group6.captchup.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LevelAnswerRepository extends JpaRepository<LevelAnswer, Integer> {
    Optional<LevelAnswer> findByUserAndLevelAndWord(User user, Level level, String word);
    List<LevelAnswer> findAllByUser(User user);

    @Query(
            value = "SELECT COUNT(nb) FROM(SELECT COUNT(id) as nb FROM level_answer " +
                    "WHERE prediction_id IS NOT NULL GROUP BY level_id, user_id HAVING COUNT(*) = 3) " +
                    "as finished_level_list",
            nativeQuery = true
    )
    Integer numberOfSolvedLevels();

    @Query(
            value = "SELECT COUNT(nb) FROM(SELECT COUNT(id) as nb FROM level_answer " +
                    "WHERE prediction_id IS NOT NULL AND user_id = ?1 GROUP BY level_id, user_id HAVING COUNT(*) = 3) " +
                    "as finished_level_list",
            nativeQuery = true
    )
    Integer numberOfSolvedLevelsByUser(int id);
}
