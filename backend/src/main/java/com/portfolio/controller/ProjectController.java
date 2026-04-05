package com.portfolio.controller;

import com.portfolio.model.Project;
import com.portfolio.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")

public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private com.portfolio.service.GithubSyncService githubSyncService;

    // Rate limiting (60 minute cooldown)
    private static long lastSyncTime = 0;
    private static final long SYNC_COOLDOWN = 60 * 60 * 1000;

    @GetMapping("/sync")
    public ResponseEntity<String> syncGithub() {
        githubSyncService.syncRepos();
        lastSyncTime = System.currentTimeMillis();
        return ResponseEntity.ok("Sync initiated.");
    }

    @Value("${app.admin.token:admin123}")
    private String adminToken;

    @GetMapping
    public List<Project> getAllProjects() {
        return projectRepository.findAll().stream()
                .filter(p -> !p.isDeleted())
                .toList();
    }

    @GetMapping("/trash")
    public List<Project> getTrashedProjects(
            @RequestHeader(value = "X-Admin-Token", required = false) String token) {
        if (token == null || !token.equals(adminToken)) {
            return List.of();
        }
        return projectRepository.findAll().stream()
                .filter(p -> p.isDeleted())
                .toList();
    }

    @PostMapping
    public ResponseEntity<?> createProject(
            @RequestBody Project project,
            @RequestHeader(value = "X-Admin-Token", required = false) String token) {
        
        if (token == null || !token.equals(adminToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        return ResponseEntity.ok(projectRepository.save(project));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(
            @PathVariable Long id,
            @RequestBody Project projectDetails,
            @RequestHeader(value = "X-Admin-Token", required = false) String token) {
        
        if (token == null || !token.equals(adminToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        
        return projectRepository.findById(id).map(project -> {
            project.setTitle(projectDetails.getTitle());
            project.setDescription(projectDetails.getDescription());
            project.setTechStack(projectDetails.getTechStack());
            project.setImageUrl(projectDetails.getImageUrl());
            project.setCodeUrl(projectDetails.getCodeUrl());
            project.setDemoUrl(projectDetails.getDemoUrl());
            project.setFeatured(projectDetails.isFeatured());
            return ResponseEntity.ok(projectRepository.save(project));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(
            @PathVariable Long id,
            @RequestHeader(value = "X-Admin-Token", required = false) String token) {
        
        if (token == null || !token.equals(adminToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        
        return projectRepository.findById(id).map(project -> {
            project.setDeleted(true);
            projectRepository.save(project);
            return ResponseEntity.ok().body("Deleted");
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<?> restoreProject(
            @PathVariable Long id,
            @RequestHeader(value = "X-Admin-Token", required = false) String token) {
        
        if (token == null || !token.equals(adminToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        
        return projectRepository.findById(id).map(project -> {
            project.setDeleted(false);
            projectRepository.save(project);
            return ResponseEntity.ok().body("Restored");
        }).orElse(ResponseEntity.notFound().build());
    }
}
