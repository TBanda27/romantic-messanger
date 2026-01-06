package com.romantic_messenger.romanticmessenger.claude.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;

@Service
@Slf4j
public class S3Service {

    private final S3Client s3Client;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    public String uploadAudioFile(byte[] audioBytes){
        String fileName = "romantic-messages/" + UUID.randomUUID() + ".mp3";
        log.info("Uploading file {} to S3", fileName);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType("audio/mpeg")
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(audioBytes));
        String publicUrl = String.format("https://%s.s3.%s.amazonaws.com/%s",
                bucketName, region, fileName);

        log.info("File uploaded successfully. Public URL: {}", publicUrl);
        return publicUrl;
    }
}
