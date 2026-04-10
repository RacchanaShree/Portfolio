package com.portfolio.controller;

import com.portfolio.model.SocialLink;
import com.portfolio.repository.SocialLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/social-links")

public class SocialLinkController {

    @Autowired
    private SocialLinkRepository socialLinkRepository;

    @GetMapping
    public List<SocialLink> getSocialLinks() {
        return socialLinkRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createSocialLink(@RequestBody SocialLink link) {
        return ResponseEntity.ok(socialLinkRepository.save(link));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSocialLink(
            @PathVariable Long id,
            @RequestBody SocialLink linkDetails) {
        
        
        return socialLinkRepository.findById(id).map(link -> {
            link.setPlatformName(linkDetails.getPlatformName());
            link.setUrl(linkDetails.getUrl());
            link.setIconName(linkDetails.getIconName());
            return ResponseEntity.ok(socialLinkRepository.save(link));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSocialLink(@PathVariable Long id) {
        
        
        if (socialLinkRepository.existsById(id)) {
            socialLinkRepository.deleteById(id);
            return ResponseEntity.ok("Deleted");
        }
        return ResponseEntity.notFound().build();
    }
}
