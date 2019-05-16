package fr.esgi.j2e.group6.captchup;

import com.google.cloud.vision.v1.EntityAnnotation;
import fr.esgi.j2e.group6.captchup.vision.service.VisionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.google.cloud.vision.v1.ImageAnnotatorClient;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CaptchupApplication.class)
@AutoConfigureMockMvc
public class VisionServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VisionService visionService;

    @Test
    public void callApi_ReturnAnnotationList(){
        List<EntityAnnotation> annotations = visionService.callAPI("test-image.jpg");
    }

}
