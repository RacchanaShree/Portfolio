package com.portfolio.service;

import com.portfolio.model.Project;
import com.portfolio.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Optional;

@Service
public class GithubSyncService {

    @Autowired
    private ProjectRepository projectRepository;

    private final String GITHUB_API_URL = "https://api.github.com/users/RacchanaShree/repos";
    private final RestTemplate restTemplate = new RestTemplate();

    public void syncRepos() {
        try {
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set("User-Agent", "Portfolio-App");
            org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(headers);
            
            org.springframework.http.ResponseEntity<GithubRepo[]> response = restTemplate.exchange(
                GITHUB_API_URL, 
                org.springframework.http.HttpMethod.GET, 
                entity, 
                GithubRepo[].class
            );
            
            GithubRepo[] repos = response.getBody();
            if (repos == null) return;
            
            for (GithubRepo repo : repos) {
                // Skip forks if desired
                if (repo.fork) continue;

                Optional<Project> existingProject = projectRepository.findAll().stream()
                        .filter(p -> repo.html_url.equals(p.getCodeUrl()))
                        .findFirst();

                Project project = existingProject.orElseGet(() -> {
                    Project p = new Project();
                    p.setDeleted(false); 
                    
                    // Assign a smart placeholder image based on name keywords
                    String name = repo.name.toLowerCase();
                    String imgUrl = "https://images.unsplash.com/photo-1517694712202-14dd9538aa97?q=80&w=600"; // Default: Code
                    if (name.contains("assist") || name.contains("jarvis") || name.contains("ai")) imgUrl = "https://images.unsplash.com/photo-1677442136019-21780ecad995?q=80&w=600"; // AI
                    else if (name.contains("detec") || name.contains("framework")) imgUrl = "https://images.unsplash.com/photo-1550751827-4bd374c3f58b?q=80&w=600"; // Tech/Security
                    else if (name.contains("translat") || name.contains("ling")) imgUrl = "https://images.unsplash.com/photo-1456513080510-7bf3a84b82f8?q=80&w=600"; // Language/Book
                    else if (name.contains("portfolio")) imgUrl = "Rachu_proff.jpeg"; // Personal
                    
                    p.setImageUrl(imgUrl);
                    p.setFeatured(false);
                    return p;
                });
                
                if (project.getTitle() == null || project.getTitle().isEmpty()) {
                    project.setTitle(repo.name);
                }
                
                project.setDeleted(false); // Ensure projects are visible since delete feature was disabled
                
                if (project.getDescription() == null || project.getDescription().equals("No description provided.") || project.getDescription().isEmpty()) {
                    project.setDescription(repo.description != null ? repo.description : "No description provided.");
                }
                
                project.setCodeUrl(repo.html_url);
                
                if (project.getDemoUrl() == null || project.getDemoUrl().equals("#")) {
                    project.setDemoUrl(repo.homepage != null && !repo.homepage.trim().isEmpty() ? repo.homepage : "#");
                }
                
                if (repo.language != null && (project.getTechStack() == null || project.getTechStack().isEmpty())) {
                    project.setTechStack(Arrays.asList(repo.language));
                }

                projectRepository.save(project);
            }
        } catch (Exception e) {
            System.err.println("Error syncing with GitHub: " + e.getMessage());
        }
    }

    // Static inner class for mapping
    public static class GithubRepo {
        public String name;
        public String description;
        public String html_url;
        public String homepage;
        public String language;
        public boolean fork;
    }
}
