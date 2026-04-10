package com.portfolio.controller;

import com.portfolio.model.Experience;
import com.portfolio.repository.ExperienceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/experience")

public class ExperienceController {

    @Autowired
    private ExperienceRepository experienceRepository;

    @GetMapping
    public List<Experience> getAllExperiences() {
        return experienceRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createExperience(@RequestBody Experience experience) {
        return ResponseEntity.ok(experienceRepository.save(experience));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateExperience(
            @PathVariable Long id,
            @RequestBody Experience expDetails) {
        
        
        return experienceRepository.findById(id).map(exp -> {
            exp.setRole(expDetails.getRole());
            exp.setOrganization(expDetails.getOrganization());
            exp.setPeriod(expDetails.getPeriod());
            exp.setDescription(expDetails.getDescription());
            exp.setType(expDetails.getType());
            return ResponseEntity.ok(experienceRepository.save(exp));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExperience(@PathVariable Long id) {
        
        
        if (experienceRepository.existsById(id)) {
            experienceRepository.deleteById(id);
            return ResponseEntity.ok("Deleted");
        }
        return ResponseEntity.notFound().build();
    }
}
