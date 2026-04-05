package com.portfolio.controller;

import com.portfolio.model.ContactMessage;
import com.portfolio.repository.ContactMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/contact")

public class ContactController {

    @Autowired
    private ContactMessageRepository contactMessageRepository;

    @Value("${app.admin.token:admin123}")
    private String adminToken;

    


    // 2. Submit Contact Form
    @PostMapping
    public ResponseEntity<?> submitMessage(@RequestBody ContactFormRequest request) {

        // Save Message
        ContactMessage message = new ContactMessage();
        message.setName(request.getName());
        message.setEmail(request.getEmail());
        message.setMessage(request.getMessage());
        contactMessageRepository.save(message);

        return ResponseEntity.ok(Collections.singletonMap("message", "Message sent successfully!"));
    }

    // --- ADMIN ENDPOINTS BELOW ---

    @GetMapping("/messages")
    public ResponseEntity<?> getAllMessages(@RequestHeader(value = "X-Admin-Token", required = false) String token) {
        if (token == null || !token.equals(adminToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Unauthorized"));
        }
        return ResponseEntity.ok(contactMessageRepository.findAllByOrderByTimestampDesc());
    }

    @DeleteMapping("/messages/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable Long id, @RequestHeader(value = "X-Admin-Token", required = false) String token) {
        if (token == null || !token.equals(adminToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Unauthorized"));
        }
        if (contactMessageRepository.existsById(id)) {
            contactMessageRepository.deleteById(id);
            return ResponseEntity.ok(Collections.singletonMap("message", "Deleted"));
        }
        return ResponseEntity.notFound().build();
    }

    // --- Helper Classes ---


    public static class ContactFormRequest {
        private String name;
        private String email;
        private String message;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
