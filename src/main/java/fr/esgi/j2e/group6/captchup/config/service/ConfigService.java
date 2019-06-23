package fr.esgi.j2e.group6.captchup.config.service;

import fr.esgi.j2e.group6.captchup.config.model.Config;
import fr.esgi.j2e.group6.captchup.config.repository.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigService {

    @Autowired
    ConfigRepository repository;

    public void insertConfigIfNotExist(String configName, String configValue) {
        if(repository.findByName(configName) == null) {
            repository.save(new Config(configName, configValue));
        }
    }
}
