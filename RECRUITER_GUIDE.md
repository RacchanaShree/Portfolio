# 🚀 Tech Stack & Interview Preparation Guide

This portfolio isn't just a static website; it’s a full-stack engineering solution designed for **high availability on zero-budget infrastructure.** Here is the breakdown of how it works and how to talk about it to recruiters.

---

## 🛠️ The Tech Stack

| Layer | Technology | Why we used it |
| :--- | :--- | :--- |
| **Backend** | **Java / Spring Boot 3** | Provides a robust, enterprise-grade architecture. Highly scalable and the industry standard for backend development. |
| **Frontend** | **HTML5, CSS3, Vanilla JS** | Prioritized performance and "raw" coding ability. Avoided heavy frameworks (like React) to keep the initial load time fast on mobile devices. |
| **Styling** | **Tailwind CSS** | Enabled rapid, modern UI development with utility classes, ensuring perfect responsiveness across all screen sizes. |
| **Database** | **PostgreSQL (Supabase)** | A powerful relational database. We used it to store dynamic data like skills, work experience, and social links. |
| **Hosting** | **Render (Free Tier)** | Reliable cloud hosting that supports the Spring Boot monolith architecture. |
| **Automation**| **GitHub Actions** | Used for CI/CD and our custom "Keep-Alive" heartbeat system. |

---

## 🏗️ Key Implementation Highlights (The "impressive" parts)

### 1. Parallel Data Loading & Resilience
**How:** We implemented an optimized fetching strategy using `Promise.allSettled` to load skills, projects, and social links simultaneously.
**Why:** This approach minimizes the "Time to Interactive" (TTI) for the user. Even if one API call is slow, the rest of the site populates immediately, providing a snappier experience while the backend warms up.

### 2. Autonomous "Keep-Alive" Heartbeat
**How:** We created a GitHub Action (`.github/workflows/keep-alive.yml`) that pings your backend every 2 days.
**Why:** To prevent Supabase from "pausing" your database and Render from going into a "deep sleep." This ensures your portfolio is always ready for a recruiter's click.

### 3. Permanent Data Fallback Strategy
**How:** For the Resume, if the "live" upload is wiped (a common issue on ephemeral free hosting), the Java backend automatically falls back to a permanent PDF stored in the project's static resources.
**Why:** This demonstrates "Defensive Programming"—anticipating failure and having a backup ready so the user experience never breaks.

---

## 👔 Recruiter Q&A (Interview Ready)

Use these answers to showcase your technical depth:

### Q1: "Why did you choose a Spring Boot monolith instead of a microservices architecture?"
> **High-Impact Answer:** "For a personal portfolio, a monolith is much more efficient. It reduces network latency, simplifies deployment on a single cloud instance, and keeps the codebase maintainable. It follows the principle of **KISS (Keep It Simple, Stupid)**, focusing on performance rather than over-engineering."

### Q2: "How did you handle the challenges of deploying a Java app on a free-tier hosting service?"
> **High-Impact Answer:** "I addressed two main challenges: **Cold Starts** and **Ephemeral Storage**. I implemented an automated 'Heartbeat' system using GitHub Actions to prevent hibernation. I also designed a specialized 'Fetch Retry' mechanism with **Exponential Backoff** on the frontend to gracefully wait for the JVM to initialize without crashing the UI."

### Q3: "Talk to me about your database schema. Why PostgreSQL?"
> **High-Impact Answer:** "I chose PostgreSQL for its strong ACID compliance and relational capabilities. Even for a portfolio, data integrity matters. I used **Spring Data JPA** for the persistence layer to maintain a clean separation between my business logic and the database, making it easy to swap databases in the future if needed."

### Q4: "I noticed your site has a custom CMS. How is it secured?"
> **High-Impact Answer:** "The CMS is protected by a custom **Pre-Shared Key (X-Admin-Token)** header system. All sensitive POST/PUT/DELETE operations are intercepted by the backend and validated. This provides a lightweight but effective security layer without the overhead of a full OAuth2 server for a single-admin use case."

---

## 💡 Final Tip for Recruiter Calls
When they ask "What are you most proud of?", talk about the **Automation**. Mention that your portfolio is "self-maintaining"—it pings itself, updates its own projects from GitHub, and has built-in safety nets for its files. This shows you think about **Operations (DevOps)** as much as you do **Coding**.

Good luck! You're now technically ready to explain every inch of this system. 🚀
