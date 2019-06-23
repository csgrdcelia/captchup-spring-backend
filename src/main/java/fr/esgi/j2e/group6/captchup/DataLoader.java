package fr.esgi.j2e.group6.captchup;

import fr.esgi.j2e.group6.captchup.config.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    private ConfigService configService;

    public void run(ApplicationArguments args) {
        configService.insertConfigIfNotExist("max_vision_calls", "5");
    }
}
