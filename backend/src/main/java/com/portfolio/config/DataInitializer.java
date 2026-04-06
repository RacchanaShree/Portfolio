package com.portfolio.config;

import com.portfolio.model.Experience;
import com.portfolio.model.Project;
import com.portfolio.model.Skill;
import com.portfolio.model.SocialLink;
import com.portfolio.repository.ExperienceRepository;
import com.portfolio.repository.ProjectRepository;
import com.portfolio.repository.SkillRepository;
import com.portfolio.repository.SocialLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private com.portfolio.service.GithubSyncService githubSyncService;

    @Autowired
    private ExperienceRepository experienceRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private SocialLinkRepository socialLinkRepository;

    @Override
    public void run(String... args) throws Exception {
        try {
            // Seed Skills - Check if skills already exist (idempotent)
            if (skillRepository.count() == 0) {
                seedSkills();
                System.out.println("Skills seeded successfully.");
            } else {
                System.out.println("Skills already present, skipping initial seed.");
            }

            // Seed Projects from GitHub - Always run on startup (logic handles duplicates internally)
            try {
                System.out.println("Starting GitHub repository sync...");
                githubSyncService.syncRepos();
                System.out.println("GitHub repositories synced successfully.");
            } catch (Exception e) {
                System.err.println("Error syncing GitHub repositories: " + e.getMessage());
            }

            // Seed Experience - Only if empty to preserve manual edits
            if (experienceRepository.count() == 0) {
                try {
                    seedExperience();
                    System.out.println("Experience data seeded successfully.");
                } catch (Exception e) {
                    System.err.println("Error seeding experience data: " + e.getMessage());
                }
            }

            // Seed Social Links
            if (socialLinkRepository.count() == 0) {
                seedSocialLinks();
                System.out.println("Social links seeded successfully.");
            }
        } catch (Exception e) {
            System.err.println("FATAL error during database initialization: " + e.getMessage());
            e.printStackTrace();
            // Don't rethrow to avoid application crash
        }
    }


    private void seedSkills() {
        skillRepository.saveAll(Arrays.asList(
            createSkill("Backend", "Spring Boot", 90),
            createSkill("Backend", "Java (J2EE)", 95),
            createSkill("Frontend", "HTML/CSS/JS", 85),
            createSkill("Frontend", "React", 75),
            createSkill("Infrastructure", "MySQL", 88),
            createSkill("Infrastructure", "Git & Docker", 80)
        ));
    }

    private Skill createSkill(String category, String name, int proficiency) {
        Skill skill = new Skill();
        skill.setCategory(category);
        skill.setName(name);
        skill.setProficiency(proficiency);
        return skill;
    }


    private void seedExperience() {
        // Work Experience
        Experience e1 = new Experience();
        e1.setRole("GenAI Intern");
        e1.setOrganization("Prodigy Infotech");
        e1.setPeriod("2026");
        e1.setDescription("Developing innovative Generative AI solutions and integrating large language models (LLMs) into specialized software environments.");
        e1.setType(Experience.ExperienceType.WORK);

        Experience e2 = new Experience();
        e2.setRole("Full Stack Development");
        e2.setOrganization("Personal Projects");
        e2.setPeriod("Ongoing");
        e2.setDescription("Actively building and deploying personal projects to master modern backend architectures and frameworks like Spring Boot.");
        e2.setType(Experience.ExperienceType.WORK);

        // Education
        Experience e3 = new Experience();
        e3.setRole("Bachelor of Engineering (BE)");
        e3.setOrganization("Sai Vidya Institute of Technology, Bangalore");
        e3.setPeriod("Present");
        e3.setDescription("Pursuing comprehensive engineering studies with a focus on Computer Science and specialized software engineering principles.");
        e3.setType(Experience.ExperienceType.EDUCATION);

        Experience e4 = new Experience();
        e4.setRole("PU College");
        e4.setOrganization("St Ann's PU College");
        e4.setPeriod("Completed");
        e4.setDescription("Completed Pre-University education with a focus on science and mathematics.");
        e4.setType(Experience.ExperienceType.EDUCATION);

        Experience e5 = new Experience();
        e5.setRole("Schooling");
        e5.setOrganization("St Ann's School");
        e5.setPeriod("Completed");
        e5.setDescription("Foundation education covering primary and secondary school levels.");
        e5.setType(Experience.ExperienceType.EDUCATION);

        experienceRepository.saveAll(Arrays.asList(e1, e2, e3, e4, e5));
    }

    private void seedSocialLinks() {
        SocialLink s1 = new SocialLink();
        s1.setPlatformName("GitHub");
        s1.setUrl("https://www.github.com/RacchanaShree");
        s1.setIconName("fa-brands fa-github");

        SocialLink s2 = new SocialLink();
        s2.setPlatformName("LinkedIn");
        s2.setUrl("https://www.linkedin.com/in/racahanasreddy");
        s2.setIconName("fa-brands fa-linkedin");

        SocialLink s3 = new SocialLink();
        s3.setPlatformName("Email");
        s3.setUrl("mailto:racchanashree@gmail.com");
        s3.setIconName("fa-solid fa-envelope");

        SocialLink s4 = new SocialLink();
        s4.setPlatformName("Instagram");
        s4.setUrl("https://www.instagram.com/ph0enix_1411");
        s4.setIconName("fa-brands fa-instagram");

        socialLinkRepository.saveAll(Arrays.asList(s1, s2, s3, s4));
    }
}
