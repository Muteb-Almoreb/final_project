package org.example.trucksy.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final S3Client s3;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.s3.public-base-url}")
    private String publicBaseUrl;

    private static final Set<String> ALLOWED = Set.of("image/jpeg","image/png","image/webp");

    public record UploadResult(String key, String url) {}

    public UploadResult upload(MultipartFile file, String key) {
        try {
            String ct = file.getContentType();
            if (ct == null || !ALLOWED.contains(ct.toLowerCase())) {
                throw new IllegalArgumentException("Only PNG/JPEG/WEBP images are allowed");
            }
            byte[] bytes = file.getBytes();
            if (bytes.length == 0) throw new IllegalArgumentException("Empty file");

            PutObjectRequest req = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(ct)
                    .cacheControl("public, max-age=31536000")
                    .build();

            s3.putObject(req, RequestBody.fromBytes(bytes));

            String url = publicBaseUrl + "/" + key;
            return new UploadResult(key, url);

        } catch (IOException e) {
            throw new RuntimeException("Failed to read file", e);
        }
    }

    public void deleteIfExists(String key) {
        if (key == null || key.isBlank()) return;
        try {
            s3.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(key).build());
        } catch (NoSuchKeyException ignored) {}
        catch (Exception ex) {
           //just for logs
            System.err.println("S3 delete failed: " + ex.getMessage());
        }
    }


    public static String buildKey(String prefix, String originalFilename, String forcedExtIfMissing) {
        String clean = UUID.randomUUID().toString().replace("-", "");
        String ext = null;
        if (originalFilename != null && originalFilename.contains(".")) {
            ext = originalFilename.substring(originalFilename.lastIndexOf('.')+1).toLowerCase();
        }
        if (ext == null || ext.length() > 5) ext = forcedExtIfMissing; // fallback
        return prefix + "/" + clean + "." + ext;
    }
}

