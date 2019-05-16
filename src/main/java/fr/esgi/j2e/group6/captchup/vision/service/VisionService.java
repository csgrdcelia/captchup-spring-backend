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

import java.util.List;


@Service
public class VisionService {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private CloudVisionTemplate cloudVisionTemplate;

    public List<EntityAnnotation> callAPI(String resourceName)
    {
        resourceLoader = new DefaultResourceLoader();
        Resource imageResource = this.resourceLoader.getResource(resourceName);
        AnnotateImageResponse response = this.cloudVisionTemplate.analyzeImage(imageResource, Feature.Type.LABEL_DETECTION);

        return response.getLabelAnnotationsList();


    }
}
