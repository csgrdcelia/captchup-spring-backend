package fr.esgi.j2e.group6.captchup.vision.service;


import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import fr.esgi.j2e.group6.captchup.config.model.Config;
import fr.esgi.j2e.group6.captchup.config.repository.ConfigRepository;
import fr.esgi.j2e.group6.captchup.level.model.Level;
import fr.esgi.j2e.group6.captchup.level.repository.LevelRepository;
import fr.esgi.j2e.group6.captchup.user.model.User;
import fr.esgi.j2e.group6.captchup.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.vision.CloudVisionTemplate;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.IntegerSyntax;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@Service
public class VisionService {

    @Autowired
    private CloudVisionTemplate cloudVisionTemplate;

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private LevelRepository levelRepository;

    public List<EntityAnnotation> callAPI(MultipartFile resource)
    {
        AnnotateImageResponse response = this.cloudVisionTemplate.analyzeImage(resource.getResource(), Feature.Type.LABEL_DETECTION);

        return response.getLabelAnnotationsList();
    }

    public boolean maxAmountOfCallsIsReached(LocalDate date, User creator) {
        Config config = configRepository.findByName("max_vision_calls");
        List<Level> levels = levelRepository.findByCreationDateAndCreator(date, creator);

        if(Integer.parseInt(config.getValue()) <= levels.size())
            return true;

        return false;
    }
}
