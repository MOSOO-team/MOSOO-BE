package com.team2.mosoo_backend.utils.s3bucket.service;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3BucketService {

    private final AmazonS3Client amazonS3Client;

    @Value("${aws.s3.bucket}")
    private String bucket;

    // 파일 업로드
    public String uploadFile(MultipartFile file) throws IOException {
        // UUID를 활용하여 고유한 파일 이름 생성
        String fileName = "test/" + UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);

        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 여러개의 파일 업로드
    public List<String> uploadFileList(List<MultipartFile> multipartFile) throws IOException {
        List<String> fileNameList = new ArrayList<>();

        for(MultipartFile file : multipartFile) {
            fileNameList.add(uploadFile(file));
        }

        return fileNameList;
    }
}
