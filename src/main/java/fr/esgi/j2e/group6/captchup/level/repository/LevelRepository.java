package fr.esgi.j2e.group6.captchup.level.repository;

import fr.esgi.j2e.group6.captchup.level.model.Level;
import fr.esgi.j2e.group6.captchup.level.model.Prediction;
import fr.esgi.j2e.group6.captchup.user.model.User;
import fr.esgi.j2e.group6.captchup.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.List;

public interface LevelRepository extends JpaRepository<Level, Integer> {
    List<Level> findByCreationDateAndCreator(LocalDate creationDate, User user);

    @Query(
            value = "SELECT * FROM level as L " +
                    "WHERE L.id in \t(\tSELECT level_id FROM level_answer " +
                    "WHERE prediction_id IS NOT NULL " +
                    "AND user_id = ?1 " +
                    "GROUP BY level_id " +
                    "HAVING COUNT(*) = 3 );",
            nativeQuery = true
    )
    List<Level> findFinishedLevelsBy(int userId);

    @Query(
            value = "SELECT * FROM level as L " +
                    "WHERE L.id in \t(\tSELECT level_id FROM level_answer " +
                    "WHERE user_id = ?1 " +
                    "GROUP BY level_id " +
                    "HAVING COUNT(*) != 3 );",
            nativeQuery = true
    )
    List<Level> findUnfinishedLevelsBy(int userId);

    @Query(
            value = "SELECT * FROM level as L " +
                    "WHERE L.creator_id != ?1 " +
                    "AND L.id NOT IN " +
                    "(SELECT level_id FROM `level_answer` AS LA " +
                    "WHERE user_id = ?1);",
            nativeQuery = true
    )
    List<Level> findAllUntestedLevels(int userId);
    List<Level> findAllByCreator(User user);
}
