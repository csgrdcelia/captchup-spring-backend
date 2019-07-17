package fr.esgi.j2e.group6.captchup.bucket.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

@Service
public class AmazonClient {

    private AmazonS3 s3client;

    @Value("${aws_endpoint_url}")
    private String endpointUrl;
    @Value("${aws_bucket_name}")
    private String bucketName;
    @Value("${aws_access_key_id}")
    private String accessKey;
    @Value("${aws_secret_access_key}")
    private String secretKey;
    @Value("${aws_region}")
    private String region;

    @PostConstruct
    private void initializeAmazon() {
//        AWSCredentials creds = null;
//        try {
//            creds = new ProfileCredentialsProvider().getCredentials();
//        }
//        catch(Exception e){
//            throw new AmazonClientException("Credentials error", e);
//        }
//
//        s3client = AmazonS3ClientBuilder.standard()
//                .withCredentials(new AWSStaticCredentialsProvider(creds))
//                .withRegion(this.region).build();
        BasicAWSCredentials creds = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = AmazonS3ClientBuilder.standard().withRegion(this.region).withCredentials(new AWSStaticCredentialsProvider(creds)).build();
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    private void uploadFileTos3bucket(String fileName, File file) {
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.
                        PublicRead));
    }

    public String uploadFile(MultipartFile multipartFile) {

        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
            uploadFileTos3bucket(fileName, file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }

    public String deleteFileFromS3Bucket(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        s3client.deleteObject(new DeleteObjectRequest(bucketName + "/", fileName));
        return "Successfully deleted";
    }

    public S3ObjectInputStream getFileFromS3Bucket(String fileName) {
        S3Object object = s3client.getObject(new GetObjectRequest(bucketName , fileName));
        return object.getObjectContent();
    }
}
