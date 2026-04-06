# Portfolio Project Architecture & Developer FAQ

This document serves as a comprehensive reference for the design, implementation, and deployment of your Java Full Stack Portfolio Website.

---

## 🏗️ Overall Architecture Summary

Your portfolio is built as a **Spring Boot Monolith** that follows modern full-stack development patterns. Here is how the different layers interact:

### 1. Frontend (The User Interface)
*   **Tech Stack**: HTML5, Tailwind CSS, and Vanilla JavaScript.
*   **Dynamic Loading**: The site uses `fetch` APIs to retrieve data from the backend dynamically.
*   **Animations**: **AOS (Animate On Scroll)** is used for entry animations, and **Font Awesome** provides the iconography.
*   **Splash Screen**: A cinematic intro that serves two purposes:
    *   Branding (Your "RSR" logo).
    *   Hiding the "cold start" lag while the backend wakes up on Render.

### 2. Backend (The Logic)
*   **Tech Stack**: **Java** with **Spring Boot**.
*   **Role**: It provides RESTful API endpoints for the frontend to consume. It also serves as the host for the static frontend files (located in `src/main/resources/public` or `static`).
*   **Data Management**: It uses **Spring Data JPA** to interact with the database.
*   **CMS Dashboard**: A built-in Administrative Panel that lets you update your skills and projects without touching the code.

### 3. Supabase (The Database)
*   **PostgreSQL**: A professional-grade, relational database managed by **Supabase**.
*   **Persistence**: Unlike local databases, Supabase remains active even when your Render server is sleeping.
*   **Schema Updates**: The backend is configured with `spring.jpa.hibernate.ddl-auto=update`, meaning your database tables automatically adjust to changes in your Java code.

---

## ❓ Developer FAQ & Solutions

### Q1: Why does the screen go blank after the splash screen?
**Solution**: This was caused by a synchronization gap between the splash screen removal and the initialization of scroll animations (AOS). 
*   **The Fix**: I implemented a **Split-Screen animation** that slides two panels out to the sides. I also ensured that **AOS.init()** is called exactly when the splash screen is fully removed, ensuring the main content is visible immediately.

### Q2: Why use Supabase over a "normal" database?
**Solution**: Supabase is essentially **Postgres-as-a-Service**. It offers:
*   **Zero-Maintenance**: No need to manage your own database server.
*   **Integrated Storage**: Provides a built-in place to store your Resume PDF and Project Images.
*   **Reliability**: It stays online 24/7, even when your Render backend goes to sleep on the free tier.

### Q3: What should I tell recruiters about this architecture?
**Solution**: Focus on your **design decisions**.
*   Mention that you chose **PostgreSQL** for data integrity and **Spring Boot** for its powerful, enterprise-grade backend capabilities.
*   Highlight your use of **managed cloud services** (Supabase/Render) to showcase your familiarity with modern DevOps and deployment workflows.

### Q4: How do my GitHub changes get to the website?
**Solution**: Through **Continuous Deployment (CD)**.
1.  You `git push origin main`.
2.  **Render** detects the push and starts a new **Build**.
3.  Render compiles your new code and **Redeploys** the backend.
4.  The **Backend** (Logic) is updated with the new code, while **Supabase** (Data) remains persistent and untouched.

### Q5: Does a new Render build affect my data in Supabase?
**Solution**: **No.** 
*   Render only updates the **code** and logic of your application. Your data (skills, projects, messages) is stored safely on Supabase’s servers and remains intact across builds and deployments.

---

## ✅ Recent Updates Implemented
- [x] **Split-Screen Splash**: Replaces the old circle-fade with a modern side-split transition.
- [x] **Real-time Status**: "Waking up server..." status now appears instantly during retries.
- [x] **Static Resource Sync**: Ensured all `index.html` versions (root, public, and static) are identical and up-to-date.

---
*Created by Rachana S Reddy & Antigravity AI*
