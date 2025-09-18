package com.aerodream.Gallery.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {

    private final AmazonS3 amazonS3;
    private final String bucketName;

    public S3Service(AmazonS3 amazonS3, String bucketName) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
    }

    public String uploadFile(MultipartFile file) {
        try {
            // Генерируем уникальное имя файла
            String fileName = generateFileName(file.getOriginalFilename());

            // Конвертируем MultipartFile в File
            File tempFile = convertMultiPartToFile(file);

            // Загружаем файл в S3
            amazonS3.putObject(new PutObjectRequest(bucketName, fileName, tempFile));

            // Удаляем временный файл
            tempFile.delete();

            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    public String getFileUrl(String fileName) {
        if (fileName == null) {
            return null;
        }

        // Генерируем URL для доступа к файлу
        return amazonS3.generatePresignedUrl(
                new GeneratePresignedUrlRequest(bucketName, fileName)
        ).toString();
    }

    public void deleteFile(String fileName) {
        if (fileName != null) {
            amazonS3.deleteObject(bucketName, fileName);
        }
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String generateFileName(String originalFileName) {
        return UUID.randomUUID().toString() + "-" + originalFileName;
    }
}
