package fr.esgi.j2e.group6.captchup.level.web;

import fr.esgi.j2e.group6.captchup.level.model.Level;
import fr.esgi.j2e.group6.captchup.level.repository.LevelRepository;
import fr.esgi.j2e.group6.captchup.level.service.LevelService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/level")
public class LevelController {

    private final LevelRepository levelRepository;
    private final LevelService levelService;

    public LevelController(LevelRepository levelRepository, LevelService levelService) {
        this.levelRepository = levelRepository;
        this.levelService = levelService;
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Level> getAllLevels() {
        return levelRepository.findAll();
    }
}
