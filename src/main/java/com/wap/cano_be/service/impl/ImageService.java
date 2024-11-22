package com.wap.cano_be.service.impl;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    /**
     * 단일 이미지 업로드
     * @param image Multipart 이미지 파일
     * @return 업로드 된 이미지 URL 주소
     */
    public String uploadImage(MultipartFile image) {
        String fileName = "images/" + UUID.randomUUID() + "_" + image.getOriginalFilename();
        File convertedFile = convertMultiPartFileToFile(image);

        amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, convertedFile).withCannedAcl(CannedAccessControlList.PublicRead));
        if (!convertedFile.delete()) {
            throw new RuntimeException("파일 삭제 실패");
        }
        return amazonS3Client.getUrl(bucketName, fileName).toString();
    }

    /**
     * 이미지 삭제
     * @param imageUrl S3 이미지 URL
     */
    public void deleteImage(String imageUrl) {
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        fileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
        amazonS3Client.deleteObject(bucketName, "images/" + fileName);
    }

    /**
     * MultipartFile -> File 로 변환
     * */
    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Error converting multipart file to file", e);
        }
        return convertedFile;
    }
}
