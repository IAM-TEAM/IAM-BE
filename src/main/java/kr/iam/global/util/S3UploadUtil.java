package kr.iam.global.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3UploadUtil {
    private final AmazonS3Client amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadRssFeed(String keyName, String rssFeedXml) {
        String modifiedKeyName = keyName + "-rss";
        InputStream stream = new ByteArrayInputStream(rssFeedXml.getBytes(StandardCharsets.UTF_8));
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("application/rss+xml; charset=UTF-8");
        metadata.setContentLength(rssFeedXml.getBytes(StandardCharsets.UTF_8).length);

        amazonS3.putObject(new PutObjectRequest(bucket, modifiedKeyName, stream, metadata));
        return amazonS3.getUrl(bucket, modifiedKeyName).toString();
    }

    public String saveProfileImage(MultipartFile multipartFile, Long memberId) throws IOException {
        String extension = extractExtension(multipartFile);
        ObjectMetadata metadata = new ObjectMetadata();
//        if(!Objects.equals(multipartFile.getContentType(), "image/png") && !Objects.equals(multipartFile.getContentType(), "image/jpg")) {
//            throw new IOException();
//        }
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());
        String key = "Member_Profile" + memberId + extension;
        amazonS3.putObject(bucket, key, multipartFile.getInputStream(), metadata);
        log.info("{}타입 {} 업로드 완료", multipartFile.getContentType(), key);
        return amazonS3.getUrl(bucket, key).toString();
    }

    public String saveFile(MultipartFile multipartFile, LocalDateTime uploadTime, Long memberId) throws IOException {
        String extension = extractExtension(multipartFile);
        ObjectMetadata metadata = new ObjectMetadata();
        String originalFilename = multipartFile.getOriginalFilename();
        String type = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());
        if(extension.equals("mov")) {
            extension = "mp4";
        }
        String key = "Member" + memberId + "_" + type + "_" + uploadTime + extension;
        amazonS3.putObject(bucket, key, multipartFile.getInputStream(), metadata);
        log.info("{}타입 {} 업로드 완료", multipartFile.getContentType(), key);
        return amazonS3.getUrl(bucket, key).toString();
    }

    public void deleteFile(String key)  {
        key = key.substring(key.lastIndexOf("/") + 1);
        key = URLDecoder.decode(key, StandardCharsets.UTF_8);
        amazonS3.deleteObject(bucket, key);
    }

    private String extractExtension(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        return extension;
    }
}
