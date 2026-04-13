window.PORTFOLIO_DATA = {
    skills: [
        { category: "Backend", name: "Spring Boot", proficiency: 90 },
        { category: "Backend", name: "Java (J2EE)", proficiency: 95 },
        { category: "Backend", name: "Python", proficiency: 85 },
        { category: "Frontend", name: "HTML/CSS/JS", proficiency: 85 },
        { category: "Infrastructure", name: "MySQL", proficiency: 88 },
        { category: "Infrastructure", name: "Git & Docker", proficiency: 80 }
    ],
    projects: [
        {
            title: "Deepfake Detection Framework",
            description: "Advanced machine learning techniques to analyze images, videos, and audio to identify AI-generated content and ensure digital media authenticity.",
            techStack: ["Python", "Machine Learning"],
            imageUrl: "https://images.unsplash.com/photo-1550751827-4bd374c3f58b?q=80&w=600",
            codeUrl: "https://github.com/RacchanaShree/Deepfake-Detection-Framework-for-AI-Generated-Multimedia-Data",
            demoUrl: "#",
            featured: true
        },
        {
            title: "Jarvis AI Assistant",
            description: "Voice-controlled virtual assistant that executes commands, fetches news, and answers questions using LLMs. Features modular design and secure API handling.",
            techStack: ["Python", "AI"],
            imageUrl: "https://images.unsplash.com/photo-1677442136019-21780ecad995?q=80&w=600",
            codeUrl: "https://github.com/RacchanaShree/Jarvis-voice-activated-virtual-assistant",
            demoUrl: "#",
            featured: true
        },
        {
            title: "Multilingual Translator",
            description: "Interactive language translation web app built with Streamlit. Supports automatic detection, swap languages, and translation history.",
            techStack: ["Python", "Streamlit"],
            imageUrl: "https://images.unsplash.com/photo-1456513080510-7bf3a84b82f8?q=80&w=600",
            codeUrl: "https://github.com/RacchanaShree/Multilingual--Language-Translator",
            demoUrl: "#",
            featured: false
        },
        {
            title: "Text Generation (GPT-2)",
            description: "Exploring state-of-the-art natural language processing with GPT-2 for creative and coherent text generation.",
            techStack: ["Python", "NLP"],
            imageUrl: "https://images.unsplash.com/photo-1517694712202-14dd9538aa97?q=80&w=600",
            codeUrl: "https://github.com/RacchanaShree/PRODIGY_GA_01",
            demoUrl: "#",
            featured: false
        },
        {
            title: "Neural Style Transfer",
            description: "Artistic image processing using neural networks to blend style and content of different images.",
            techStack: ["Python", "Computer Vision"],
            imageUrl: "https://images.unsplash.com/photo-1550751827-4bd374c3f58b?q=80&w=600",
            codeUrl: "https://github.com/RacchanaShree/PRODIGY_GA_05",
            demoUrl: "#",
            featured: false
        }
    ],
    experience: [
        {
            role: "GenAI Intern",
            organization: "Prodigy Infotech",
            period: "2026",
            description: "Developing innovative Generative AI solutions and integrating large language models (LLMs) into specialized software environments.",
            type: "WORK"
        },
        {
            role: "Full Stack Development",
            organization: "Personal Projects",
            period: "Ongoing",
            description: "Actively building and deploying personal projects to master modern backend architectures and frameworks like Spring Boot.",
            type: "WORK"
        },
        {
            role: "Bachelor of Engineering (BE)",
            organization: "Sai Vidya Institute of Technology, Bangalore",
            period: "Present",
            description: "Pursuing comprehensive engineering studies with a focus on Computer Science and specialized software engineering principles.",
            type: "EDUCATION"
        },
        {
            role: "PU College",
            organization: "St Ann's PU College",
            period: "Completed",
            description: "Completed Pre-University education with a focus on science and mathematics.",
            type: "EDUCATION"
        },
        {
            role: "Schooling",
            organization: "St Ann's School",
            period: "Completed",
            description: "Completed primary and secondary education with a strong academic foundation.",
            type: "EDUCATION"
        }
    ],
    socialLinks: [
        { platformName: "GitHub", url: "https://www.github.com/RacchanaShree", iconName: "fa-brands fa-github" },
        { platformName: "LinkedIn", url: "https://www.linkedin.com/in/racahanasreddy", iconName: "fa-brands fa-linkedin" },
        { platformName: "Email", url: "mailto:racchanashree@gmail.com", iconName: "fa-solid fa-envelope" },
        { platformName: "Instagram", url: "https://www.instagram.com/ph0enix_1411", iconName: "fa-brands fa-instagram" }
    ],
    resumeExists: true
};

// Export for use in index.html
if (typeof module !== 'undefined' && module.exports) {
    module.exports = PORTFOLIO_DATA;
}
