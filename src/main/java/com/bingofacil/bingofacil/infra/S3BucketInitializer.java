package com.bingofacil.bingofacil.infra;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Component
public class S3BucketInitializer {

    @Autowired
    private S3Client s3Client;

    @Value("${aws.s3.bucket-name:bucket-bingo-facil}")
    private String bucketName;

    @PostConstruct
    public void init() {
        try {
            s3Client.headBucket(HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build());

            System.out.println("S3: Bucket já existe: " + bucketName);
        } catch (S3Exception e) {
            if (e.statusCode() == 400) {
                System.out.println("S3: Criando um bucket inicial: " + bucketName);
                s3Client.createBucket(CreateBucketRequest.builder()
                        .bucket(bucketName)
                        .build());
            } else {
                System.err.println("S3: Erro inesperado ao verificar bucket: " + e.getMessage());
            }
        }
    }

}
