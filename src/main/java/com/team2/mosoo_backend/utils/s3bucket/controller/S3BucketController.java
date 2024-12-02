package com.team2.mosoo_backend.utils.s3bucket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.team2.mosoo_backend.utils.s3bucket.service.S3BucketService;

import java.io.IOException;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class S3BucketController {
        private final S3BucketService s3BucketService;

        // 파일 업로드
        @PostMapping
        public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
            try {
                String fileUrl = s3BucketService.uploadFile(file);
                return ResponseEntity.ok(fileUrl);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
}
