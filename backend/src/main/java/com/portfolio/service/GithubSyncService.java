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
            GithubRepo[] repos = restTemplate.getForObject(GITHUB_API_URL, GithubRepo[].class);
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
                    p.setImageUrl("https://images.unsplash.com/photo-1517694712202-14dd9538aa97?auto=format&fit=crop&q=80&w=600&h=400");
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
