package com.portfolio.model;

import jakarta.persistence.*;

@Entity
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String role;
    private String organization;
    private String period; // e.g., 2026 - Present
    @Column(length = 1000)
    private String description;
    
    @Enumerated(EnumType.STRING)
    private ExperienceType type;

    public enum ExperienceType {
        WORK, EDUCATION, CERTIFICATION
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getOrganization() { return organization; }
    public void setOrganization(String organization) { this.organization = organization; }
    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public ExperienceType getType() { return type; }
    public void setType(ExperienceType type) { this.type = type; }
}
