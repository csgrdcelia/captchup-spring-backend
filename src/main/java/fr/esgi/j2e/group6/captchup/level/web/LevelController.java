package fr.esgi.j2e.group6.captchup.level.web;

import com.google.api.client.util.DateTime;
import fr.esgi.j2e.group6.captchup.level.model.Level;
import fr.esgi.j2e.group6.captchup.level.model.LevelAnswer;
import fr.esgi.j2e.group6.captchup.level.model.Prediction;
import fr.esgi.j2e.group6.captchup.level.repository.LevelAnswerRepository;
import fr.esgi.j2e.group6.captchup.level.repository.LevelRepository;
import fr.esgi.j2e.group6.captchup.level.service.LevelAnswerService;
import fr.esgi.j2e.group6.captchup.level.service.LevelService;
import fr.esgi.j2e.group6.captchup.level.service.PredictionService;
import fr.esgi.j2e.group6.captchup.user.model.User;
import fr.esgi.j2e.group6.captchup.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/level")
public class LevelController {

    @Autowired LevelRepository levelRepository;
    @Autowired LevelService levelService;
    @Autowired UserService userService;
    @Autowired PredictionService predictionService;
    @Autowired LevelAnswerRepository levelAnswerRepository;
    @Autowired LevelAnswerService levelAnswerService;

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

    @PostMapping(path = "/{id}/solve")
    public @ResponseBody ResponseEntity<LevelAnswer> solveLevel(@PathVariable("id") Integer id, @RequestParam("answer") String answer) {
        LevelAnswer levelAnswer = levelService.solveLevel(id, answer);
        return ResponseEntity.status(HttpStatus.OK).body(levelAnswer);
    }
}
