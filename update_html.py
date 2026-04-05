import os
import re

filepath = r'c:\Users\THRIVENI GK\OneDrive\Desktop\Portfolio_web\index.html'
with open(filepath, 'r', encoding='utf-8') as f:
    content = f.read()

# 1. Skills
skills_target = re.compile(r'(<div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">.*?)(</div>\s*</div>\s*</section>)', re.DOTALL)
skills_replacement = r'<div id="skills-container" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">\n            <!-- Skills will be loaded here dynamically -->\n        </div>\n    \2'
content = skills_target.sub(skills_replacement, content, count=1)

# 2. Projects
projects_target = re.compile(r'(<div class="grid grid-cols-1 md:grid-cols-2 gap-8">.*?)(</div>\s*</div>\s*</section>)', re.DOTALL)
projects_replacement = r'<div id="projects-container" class="grid grid-cols-1 md:grid-cols-2 gap-8">\n            <!-- Projects will be loaded here dynamically -->\n        </div>\n    \2'
content = projects_target.sub(projects_replacement, content, count=1)

# 3. Experience
exp_target = re.compile(r'(<div class="relative border-l-2 border-primary-container/30 pl-8 space-y-16">.*?)(</div>\s*</div>\s*</section>)', re.DOTALL)
exp_replacement = r'<div id="experience-container" class="relative border-l-2 border-primary-container/30 pl-8 space-y-16">\n            <!-- Experience will be loaded here dynamically -->\n        </div>\n    \2'
content = exp_target.sub(exp_replacement, content, count=1)

# 4. JavaScript
js_target = re.compile(r'<script>\s*// Initialize AOS.*?</script>', re.DOTALL)
js_replacement = r"""<script>
    // Base API URL
    const API_BASE_URL = 'http://localhost:8081/api';

    // Icons map for skill categories
    const skillIcons = {
        'Backend': 'database',
        'Frontend': 'web',
        'Infrastructure': 'construction'
    };

    // Icons/Colors map for experience types
    const expConfig = {
        'WORK': { iconBg: 'bg-secondary', shadow: 'shadow-[0_0_15px_rgba(0,212,170,0.5)]', borderHover: 'hover:border-secondary', textHover: 'group-hover:text-secondary' },
        'EDUCATION': { iconBg: 'bg-primary-container group-hover:bg-primary', shadow: 'shadow-[0_0_15px_rgba(123,47,190,0)] group-hover:shadow-[0_0_15px_rgba(123,47,190,0.7)]', borderHover: 'hover:border-primary', textHover: 'group-hover:text-primary' },
        'CERTIFICATION': { iconBg: 'bg-primary-container group-hover:bg-primary', shadow: 'shadow-[0_0_15px_rgba(123,47,190,0)] group-hover:shadow-[0_0_15px_rgba(123,47,190,0.7)]', borderHover: 'hover:border-primary', textHover: 'group-hover:text-primary' }
    };

    // Initialize AOS
    function initAOS() {
        AOS.init({
            duration: 800,
            easing: 'ease-out-cubic',
            once: true,
            offset: 50,
        });
    }

    // Custom script to animate skill bars on scroll
    function initSkillBars() {
        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    const bar = entry.target;
                    bar.style.width = bar.getAttribute('data-width');
                    bar.style.transition = 'width 1.5s ease-out 0.5s'; 
                    observer.unobserve(bar);
                }
            });
        }, { threshold: 0.5 });

        document.querySelectorAll('.aos-animate-bar').forEach(bar => {
            bar.style.width = '0%'; // Reset to 0 initially
            observer.observe(bar);
        });
    }

    async function fetchData(endpoint) {
        try {
            const response = await fetch(`${API_BASE_URL}/${endpoint}`);
            if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
            return await response.json();
        } catch (error) {
            console.error(`Could not fetch data from ${endpoint}:`, error);
            return [];
        }
    }

    async function loadSkills() {
        const skills = await fetchData('skills');
        const container = document.getElementById('skills-container');
        if (!container || !skills.length) return;

        // Group skills by category
        const groupedSkills = skills.reduce((acc, skill) => {
            if (!acc[skill.category]) acc[skill.category] = [];
            acc[skill.category].push(skill);
            return acc;
        }, {});

        container.innerHTML = '';
        let delay = 100;
        
        for (const [category, categorySkills] of Object.entries(groupedSkills)) {
            const icon = skillIcons[category] || 'code';
            
            let skillsHtml = categorySkills.map(skill => `
                <div class="space-y-2">
                    <div class="flex justify-between font-label text-sm">
                        <span>${skill.name}</span>
                        <span class="text-secondary">${skill.proficiency}%</span>
                    </div>
                    <div class="w-full bg-surface-container-highest h-1.5 rounded-full overflow-hidden">
                        <div class="bg-gradient-to-r from-primary-container to-secondary h-full w-[${skill.proficiency}%] aos-animate-bar" data-width="${skill.proficiency}%"></div>
                    </div>
                </div>
            `).join('');

            container.innerHTML += `
                <div data-aos="fade-up" data-aos-delay="${delay}" class="glass-card p-8 rounded-2xl border-glow transform transition-transform hover:-translate-y-2">
                    <div class="flex items-center gap-4 mb-6">
                        <span class="material-symbols-outlined text-secondary text-3xl">${icon}</span>
                        <h3 class="text-xl font-headline font-bold text-white">${category}</h3>
                    </div>
                    <div class="space-y-6">
                        ${skillsHtml}
                    </div>
                </div>
            `;
            delay += 200;
        }
    }

    async function loadProjects() {
        const projects = await fetchData('projects');
        const container = document.getElementById('projects-container');
        if (!container || !projects.length) return;

        container.innerHTML = '';
        let delay = 100;

        projects.forEach(project => {
            const techStackHtml = project.techStack ? project.techStack.map(tech => `
                <span class="font-label text-xs bg-primary-container/20 text-primary px-3 py-1 rounded transition-colors group-hover:bg-primary-container group-hover:text-white">${tech}</span>
            `).join('') : '';

            container.innerHTML += `
                <div data-aos="zoom-in-up" data-aos-delay="${delay}" class="glass-card group p-8 rounded-3xl transition-all duration-500 hover:-translate-y-2 hover:shadow-[0_20px_40px_rgba(123,47,190,0.15)] border-transparent hover:border-primary-container">
                    <div class="aspect-video mb-6 rounded-xl overflow-hidden bg-surface-container relative">
                        <img alt="${project.title}" class="w-full h-full object-cover group-hover:scale-110 transition-transform duration-700" src="${project.imageUrl || 'https://via.placeholder.com/600x400'}"/>
                        <div class="absolute inset-0 bg-gradient-to-t from-surface-dim to-transparent opacity-60"></div>
                    </div>
                    <h3 class="text-2xl font-headline font-bold text-white mb-3 group-hover:text-primary transition-colors">${project.title}</h3>
                    <p class="text-on-surface-variant mb-6 line-clamp-2">${project.description}</p>
                    <div class="flex flex-wrap gap-2 mb-8">
                        ${techStackHtml}
                    </div>
                    <div class="flex gap-4">
                        <a href="${project.codeUrl}" target="_blank" class="flex-1 py-3 glass-card rounded-lg font-medium hover:bg-secondary/10 transition-colors flex justify-center items-center gap-2">
                            <span class="material-symbols-outlined text-sm">code</span> Code
                        </a>
                        <a href="${project.demoUrl}" target="_blank" class="flex-1 py-3 bg-primary-container text-white rounded-lg font-medium hover:brightness-110 hover:shadow-[0_0_15px_rgba(123,47,190,0.6)] transition-all flex justify-center items-center gap-2">
                            <span class="material-symbols-outlined text-sm">visibility</span> Live Demo
                        </a>
                    </div>
                </div>
            `;
            delay += 200;
        });
    }

    async function loadExperience() {
        const experiences = await fetchData('experience');
        const container = document.getElementById('experience-container');
        if (!container || !experiences.length) return;

        container.innerHTML = '';
        let delay = 100;

        experiences.forEach(exp => {
            const config = expConfig[exp.type] || expConfig['WORK'];
            
            container.innerHTML += `
                <div class="relative group" data-aos="fade-left" data-aos-delay="${delay}">
                    <div class="absolute -left-[41px] top-0 w-5 h-5 ${config.iconBg} rounded-full transition-transform duration-300 group-hover:scale-125 ${config.shadow}"></div>
                    <div class="glass-card p-8 rounded-2xl transform transition-transform duration-300 hover:translate-x-2 border-transparent ${config.borderHover}">
                        <div class="flex flex-col md:flex-row justify-between items-start mb-4 gap-4">
                            <div>
                                <h3 class="text-xl font-headline font-bold text-white ${config.textHover} transition-colors">${exp.role}</h3>
                                <p class="text-secondary font-medium">${exp.organization}</p>
                            </div>
                            <span class="font-label text-xs bg-primary/10 text-primary px-3 py-1 rounded">${exp.period}</span>
                        </div>
                        <p class="text-on-surface-variant leading-relaxed">
                            ${exp.description}
                        </p>
                    </div>
                </div>
            `;
            delay += 200;
        });
    }

    document.addEventListener("DOMContentLoaded", async () => {
        // Load dynamic data
        await Promise.all([
            loadSkills(),
            loadProjects(),
            loadExperience()
        ]);
        
        // Initialize animations after DOM is updated
        initSkillBars();
        initAOS();
    });
</script>"""
content = js_target.sub(js_replacement, content)

with open(filepath, 'w', encoding='utf-8') as f:
    f.write(content)
