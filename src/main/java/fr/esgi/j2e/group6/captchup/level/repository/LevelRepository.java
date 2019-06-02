package fr.esgi.j2e.group6.captchup.level.repository;

import fr.esgi.j2e.group6.captchup.level.model.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface LevelRepository extends JpaRepository<Level, Integer> {

}
