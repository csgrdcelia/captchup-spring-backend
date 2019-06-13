package fr.esgi.j2e.group6.captchup.config.repository;

import fr.esgi.j2e.group6.captchup.config.model.Config;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigRepository extends JpaRepository<Config, Integer> {
    Config findByName(String name);
}
