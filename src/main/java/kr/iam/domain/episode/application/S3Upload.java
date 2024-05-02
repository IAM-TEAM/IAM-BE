package kr.iam.domain.episode.application;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.rometools.rome.feed.synd.SyndFeed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Upload {
    private final AmazonS3Client amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public void uploadRssFeed(String keyName, String rssFeedXml) {
        InputStream stream = new ByteArrayInputStream(rssFeedXml.getBytes());
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("application/rss+xml");
        metadata.addUserMetadata("title", "RSS Feed");
        metadata.setContentEncoding("UTF-8");

        amazonS3.putObject(new PutObjectRequest(bucket, keyName, stream, metadata));
    }
}
