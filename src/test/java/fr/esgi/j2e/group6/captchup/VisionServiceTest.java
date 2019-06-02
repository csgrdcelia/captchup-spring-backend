package fr.esgi.j2e.group6.captchup;

import com.google.cloud.vision.v1.EntityAnnotation;
import fr.esgi.j2e.group6.captchup.vision.service.VisionService;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CaptchupApplication.class)
@AutoConfigureMockMvc
public class VisionServiceTest {

    @Autowired
    private VisionService visionService;

    @Test
    public void callApi_ReturnAnnotationList() throws IOException {

        File file = new File("src/main/resources/test-image.jpg");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("test-image",
                file.getName(), "image/jpeg", IOUtils.toByteArray(input));

        List<EntityAnnotation> annotations = visionService.callAPI(multipartFile);
        assert(annotations.get(0).getDescription().equals("Cactus"));
    }
}
