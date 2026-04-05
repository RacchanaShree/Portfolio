package com.portfolio.controller;

import org.springframework.beans.factory.annotation.Value;
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
            if (!Files.exists(uploadDir)) {
                return ResponseEntity.notFound().build();
            }

            // Resolve and normalize the path to the fixed resume file
            Path filePath = uploadDir.resolve(RESUME_FILENAME).normalize();

            // SECURITY: Ensure the resolved path is strictly inside the upload directory
            // This blocks any path traversal attacks (e.g., ../../etc/passwd)
            Path canonicalUploadDir = uploadDir.toRealPath();
            Path canonicalFilePath = filePath.toRealPath();
            if (!canonicalFilePath.startsWith(canonicalUploadDir)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // SECURITY: Double-check the filename matches exactly — no dynamic filenames
            if (!canonicalFilePath.getFileName().toString().equals(RESUME_FILENAME)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            UrlResource resource = new UrlResource(canonicalFilePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"resume.pdf\"")
                        // Prevent browser from caching the file so old versions aren't served
                        .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
        return ResponseEntity.ok(Collections.singletonMap("exists", Files.exists(filePath)));
    }
}
