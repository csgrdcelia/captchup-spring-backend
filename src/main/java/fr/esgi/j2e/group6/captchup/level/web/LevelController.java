package fr.esgi.j2e.group6.captchup.level.web;

import fr.esgi.j2e.group6.captchup.level.model.Level;
import fr.esgi.j2e.group6.captchup.level.repository.LevelRepository;
import fr.esgi.j2e.group6.captchup.level.service.LevelService;
import fr.esgi.j2e.group6.captchup.user.model.User;
import fr.esgi.j2e.group6.captchup.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.Optional;

@Controller
@RequestMapping("/level")
public class LevelController {

    @Autowired LevelRepository levelRepository;
    @Autowired LevelService levelService;
    @Autowired UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Level> getAllLevels() {
        return levelRepository.findAll();
    }

    @GetMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<Level> getLevelById(@PathVariable("id") int id) {
        Optional<Level> level = levelRepository.findById(id);

        if(!level.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(level.get());
    }

    @PostMapping(path = "/create")
    public @ResponseBody ResponseEntity<Level> createLevel(@RequestBody MultipartFile image) {
        try {
            User user = userService.getCurrentLoggedInUser();
            Level level = levelService.createLevel(image, user);
            return ResponseEntity.status(HttpStatus.CREATED).body(level);
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
