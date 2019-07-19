package fr.esgi.j2e.group6.captchup.config.web;

import fr.esgi.j2e.group6.captchup.config.model.Config;
import fr.esgi.j2e.group6.captchup.config.repository.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/config")
public class ConfigController {
    @Autowired private ConfigRepository configRepository;

    @PatchMapping()
    public ResponseEntity<Config> updateConfig(@RequestBody Config config) {

        Optional<Config> optionalConfig = configRepository.findById(config.getId());

        if(optionalConfig.isPresent()) {
            Config newConfig = optionalConfig.get();
            newConfig.setValue(config.getValue());
            return ResponseEntity.status(HttpStatus.OK).body(configRepository.save(newConfig));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
