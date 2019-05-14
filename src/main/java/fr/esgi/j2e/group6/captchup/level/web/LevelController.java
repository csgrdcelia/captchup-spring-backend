package fr.esgi.j2e.group6.captchup.level.web;

import fr.esgi.j2e.group6.captchup.level.model.Level;
import fr.esgi.j2e.group6.captchup.level.repository.LevelRepository;
import fr.esgi.j2e.group6.captchup.level.service.LevelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/level")
public class LevelController {

    private final LevelRepository levelRepository;
    private final LevelService levelService;

    public LevelController(LevelRepository levelRepository, LevelService levelService) {
        this.levelRepository = levelRepository;
        this.levelService = levelService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Level> getAllLevels() {
        return levelRepository.findAll();
    }

    @GetMapping(path = "/{id}")
    public @ResponseBody
    ResponseEntity<Level> getLevelById(@PathVariable("id") int id) {
        Optional<Level> level = levelRepository.findById(id);

        if(!level.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(level.get());
    }
}
