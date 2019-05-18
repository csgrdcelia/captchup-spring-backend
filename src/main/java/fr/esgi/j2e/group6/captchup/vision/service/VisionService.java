package fr.esgi.j2e.group6.captchup.vision.service;


import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.vision.CloudVisionTemplate;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
public class VisionService {

    @Autowired
    private CloudVisionTemplate cloudVisionTemplate;

    public List<EntityAnnotation> callAPI(MultipartFile resource)
    {
        AnnotateImageResponse response = this.cloudVisionTemplate.analyzeImage(resource.getResource(), Feature.Type.LABEL_DETECTION);

        return response.getLabelAnnotationsList();
    }
}
