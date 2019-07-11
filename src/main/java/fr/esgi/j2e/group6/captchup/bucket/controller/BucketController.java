package fr.esgi.j2e.group6.captchup.bucket.controller;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import fr.esgi.j2e.group6.captchup.bucket.service.AmazonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/storage/")
public class BucketController {

    private AmazonClient amazonClient;

    @Autowired
    BucketController(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
    }

    @GetMapping("/getFile")
    public S3ObjectInputStream downloadFile(@RequestPart(value = "key") String fileUrl) {
        return this.amazonClient.getFileFromS3Bucket(fileUrl);
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestPart(value = "file") MultipartFile file) {
        return this.amazonClient.uploadFile(file);
    }

    @DeleteMapping("/deleteFile")
    public String deleteFile(@RequestPart(value = "url") String fileUrl) {
        return this.amazonClient.deleteFileFromS3Bucket(fileUrl);
    }
}
