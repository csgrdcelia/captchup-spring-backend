package fr.esgi.j2e.group6.captchup.level.service;

import fr.esgi.j2e.group6.captchup.level.repository.LevelRepository;
import org.springframework.stereotype.Service;

@Service
public class LevelService {
    private LevelRepository levelRepository;

    public LevelService(LevelRepository levelRepository) {
        this.levelRepository = levelRepository;
    }
}
