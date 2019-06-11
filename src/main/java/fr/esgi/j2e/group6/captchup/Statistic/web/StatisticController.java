package fr.esgi.j2e.group6.captchup.Statistic.web;

import fr.esgi.j2e.group6.captchup.level.model.Level;
import fr.esgi.j2e.group6.captchup.level.service.LevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/statistic")
public class StatisticController {

    @Autowired
    LevelService levelService;

    @GetMapping(path = "/getNumberOfTestedLevels")
    public @ResponseBody
    ResponseEntity<Integer> getNumberOfTestedLevels() {
        List<Level> levels = levelService.getAllLevels();

        return ResponseEntity.status(HttpStatus.OK).body(levels.size());
    }
}
