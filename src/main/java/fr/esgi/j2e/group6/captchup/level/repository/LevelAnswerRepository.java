package fr.esgi.j2e.group6.captchup.level.repository;

import fr.esgi.j2e.group6.captchup.level.model.Level;
import fr.esgi.j2e.group6.captchup.level.model.LevelAnswer;
import fr.esgi.j2e.group6.captchup.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LevelAnswerRepository extends JpaRepository<LevelAnswer, Integer> {
    Optional<LevelAnswer> findByUserAndLevelAndWord(User user, Level level, String word);
    List<LevelAnswer> findAllByUser(User user);
    List<LevelAnswer> findAllByUserAndPredictionNotNull(User user);
    List<LevelAnswer> findAllByPredictionNotNull();

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

    @Query(
            value = "SELECT (COUNT(level_answer.id) / COUNT(DISTINCT level_answer.user_id)) FROM (" +
                        "SELECT user_id FROM (" +
                            "SELECT * FROM level_answer WHERE level_id = ?1) as levels " +
                        "WHERE level_id = ?1 AND prediction_id IS NOT NULL " +
                        "GROUP BY user_id " +
                        "HAVING COUNT(*) = 3) as result " +
                    "INNER JOIN level_answer " +
                    "WHERE result.user_id = level_answer.user_id " +
                    "AND level_answer.level_id = ?1",
            nativeQuery = true
    )
    Double averageNumberOfAnswersAndCompletedLevels(int level_id);
}
