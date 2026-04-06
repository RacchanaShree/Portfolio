package com.portfolio.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    @Value("${app.resume.storage-path:./uploads/resumes}")
    private String storagePath;

    @Value("${app.admin.token:admin123}")
    private String adminToken;

    // The ONLY file that can ever be served — hardcoded, never dynamic
    private static final String RESUME_FILENAME = "resume.pdf";

    @PostMapping("/upload")
    public ResponseEntity<?> uploadResume(
            @RequestParam("file") MultipartFile file,
            @RequestHeader(value = "X-Admin-Token", required = false) String token) {

        if (token == null || !token.equals(adminToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "Unauthorized"));
        }

        // Only allow PDF uploads
        String contentType = file.getContentType();
        if (contentType == null || !contentType.equals("application/pdf")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Only PDF files are allowed"));
        }

        try {
            Path uploadDir = Paths.get(storagePath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            // Always save as the fixed filename — never use the uploaded filename
            Path targetLocation = uploadDir.resolve(RESUME_FILENAME);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return ResponseEntity.ok(Collections.singletonMap("message", "Resume uploaded successfully"));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Upload failed"));
        }
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadResume() {
        try {
            Path uploadDir = Paths.get(storagePath);
            Path filePath = uploadDir.resolve(RESUME_FILENAME).normalize();

            // 1. Try serving from filesystem (Uploads)
            if (Files.exists(filePath)) {
                // SECURITY: Ensure the resolved path is strictly inside the upload directory
                Path canonicalUploadDir = uploadDir.toRealPath();
                Path canonicalFilePath = filePath.toRealPath();
                if (canonicalFilePath.startsWith(canonicalUploadDir) && 
                    canonicalFilePath.getFileName().toString().equals(RESUME_FILENAME)) {
                    
                    UrlResource resource = new UrlResource(canonicalFilePath.toUri());
                    if (resource.exists() && resource.isReadable()) {
                        return serveResource(resource);
                    }
                }
            }

            // 2. Fallback: Try serving from classpath (src/main/resources/static/)
            ClassPathResource staticResource = new ClassPathResource("static/" + RESUME_FILENAME);
            if (staticResource.exists()) {
                return serveResource(staticResource);
            }

            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private ResponseEntity<Resource> serveResource(Resource resource) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"resume.pdf\"")
                .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                .body(resource);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteResume(
            @RequestHeader(value = "X-Admin-Token", required = false) String token) {

        if (token == null || !token.equals(adminToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "Unauthorized"));
        }

        try {
            Path filePath = Paths.get(storagePath).resolve(RESUME_FILENAME);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                return ResponseEntity.ok(Collections.singletonMap("message", "Resume deleted"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("error", "No resume found"));
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Delete failed"));
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Boolean>> getStatus() {
        Path filePath = Paths.get(storagePath).resolve(RESUME_FILENAME);
        boolean existsOnFs = Files.exists(filePath);
        boolean existsOnClasspath = new ClassPathResource("static/" + RESUME_FILENAME).exists();
        
        return ResponseEntity.ok(Collections.singletonMap("exists", existsOnFs || existsOnClasspath));
    }
}
