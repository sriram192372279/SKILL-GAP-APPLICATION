package com.sriram.skillgap.utils

data class JobRole(
    val title: String,
    val requiredSkills: List<String>,
    val atsKeywords: List<String>,
    val interviewQuestions: List<String>,
    val recommendedCerts: List<String>,
    val youtubeCourses: List<String>,
    val roadmapPhase1: List<String>,
    val roadmapPhase2: List<String>,
    val roadmapPhase3: List<String>,
    val futureTrendingSkills: List<String> = emptyList(),
    val outdatedSkillsReplacements: Map<String, String> = emptyMap()
)

object JobData {
    val roles = listOf(
        JobRole(
            title = "Android Developer",
            requiredSkills = listOf("Kotlin", "Jetpack Compose", "MVVM", "Retrofit", "Room", "Dagger Hilt", "Unit Testing", "Coroutines", "Git"),
            atsKeywords = listOf("Native Mobile", "Architecture Components", "Lifecycle", "Clean Architecture", "REST API"),
            interviewQuestions = listOf("What is the difference between Val and Var?", "Explain MVVM architecture.", "How do Coroutines work?"),
            recommendedCerts = listOf("Google Associate Android Developer", "Meta Android Developer Professional Certificate"),
            youtubeCourses = listOf("Philipp Lackner - Android Development", "Android Developers - Jetpack Compose Basics"),
            roadmapPhase1 = listOf("Kotlin Fundamentals", "Android SDK Basics"),
            roadmapPhase2 = listOf("Jetpack Compose UI", "MVVM Architecture"),
            roadmapPhase3 = listOf("Dependency Injection (Hilt)", "Unit Testing"),
            futureTrendingSkills = listOf("Kotlin Multiplatform (KMP)", "Jetpack Compose Multiplatform", "AI-assisted Mobile Coding (Gemini Nano)", "Advanced Performance Profiling & Trace"),
            outdatedSkillsReplacements = mapOf("Java (for Android)" to "Kotlin", "XML Layouts" to "Jetpack Compose", "ActionBar" to "Material Toolbar", "Shared Preferences" to "Proto DataStore", "SQLiteOpenHelper" to "Room Database")
        ),
        JobRole(
            title = "Data Scientist",
            requiredSkills = listOf("Python", "R", "SQL", "Machine Learning", "Pandas", "Scikit-Learn", "Deep Learning", "Statistics"),
            atsKeywords = listOf("Predictive Modeling", "NLP", "Big Data", "Data Visualization", "Feature Engineering"),
            interviewQuestions = listOf("Explain Overfitting vs Underfitting.", "How does a Random Forest work?", "What is p-value?"),
            recommendedCerts = listOf("IBM Data Science Professional Certificate", "Google Data Analytics Certificate"),
            youtubeCourses = listOf("Sentdex - ML with Python", "StatQuest with Josh Starmer"),
            roadmapPhase1 = listOf("Python for Data Science", "Exploratory Data Analysis"),
            roadmapPhase2 = listOf("Statistical Modeling", "Machine Learning Basics"),
            roadmapPhase3 = listOf("Deep Learning (PyTorch/TF)", "MLOps Basics"),
            futureTrendingSkills = listOf("LLM Fine-Tuning & Prompt Engineering", "Vector Databases (Milvus/Pinecone)", "RAG Architecture patterns", "MLOps Lifecycle (MLflow/Kubeflow)"),
            outdatedSkillsReplacements = mapOf("Excel Modeling" to "Python/Pandas", "Hadoop MapReduce" to "Apache Spark / Snowflake", "Local CPU training" to "GPU Cloud Clusters", "Static Charts" to "Interactive Dashboards")
        ),
        JobRole(
            title = "Full Stack Developer",
            requiredSkills = listOf("React", "Node.js", "Express", "MongoDB", "SQL", "JavaScript", "TypeScript", "Docker", "Git"),
            atsKeywords = listOf("MERN Stack", "RESTful API", "Frontend/Backend Integration", "State Management"),
            interviewQuestions = listOf("What is Virtual DOM?", "Explain Middleware in Express.", "How do you handle JWT authentication?"),
            recommendedCerts = listOf("Meta Full Stack Developer Certificate", "IBM Full Stack Software Developer"),
            youtubeCourses = listOf("FreeCodeCamp - Full Stack Development", "Programming with Mosh - Node.js"),
            roadmapPhase1 = listOf("HTML/CSS/JS Mastery", "Frontend Frameworks (React)"),
            roadmapPhase2 = listOf("Backend Systems (Node/Express)", "Database Design (SQL/NoSQL)"),
            roadmapPhase3 = listOf("DevOps & Deployment", "System Architecture"),
            futureTrendingSkills = listOf("Serverless Edge Runtime functions", "Next.js 14 App Router & Server Actions", "WebSockets / Realtime syncing", "AI Agent tool integration"),
            outdatedSkillsReplacements = mapOf("jQuery" to "React / TypeScript", "REST APIs only" to "GraphQL / tRPC", "Manual Server Config" to "Docker / CI-CD pipeline", "CSS Hacks" to "Tailwind CSS / Component libraries")
        ),
        JobRole(
            title = "DevOps Engineer",
            requiredSkills = listOf("AWS", "Docker", "Kubernetes", "Jenkins", "Terraform", "Linux", "CI/CD", "Shell Scripting", "Python"),
            atsKeywords = listOf("Infrastructure as Code", "Containerization", "Automation", "Cloud Architecture"),
            interviewQuestions = listOf("Difference between Docker and VM?", "How does a CI/CD pipeline work?", "What is Kubernetes Pod?"),
            recommendedCerts = listOf("AWS Certified DevOps Engineer", "Docker Certified Associate"),
            youtubeCourses = listOf("TechWorld with Nana - DevOps", "Simplilearn - DevOps Course"),
            roadmapPhase1 = listOf("Linux Administration", "Shell Scripting"),
            roadmapPhase2 = listOf("Docker Containerization", "CI/CD Pipeline Automation"),
            roadmapPhase3 = listOf("Kubernetes Orchestration", "Terraform (IaC)")
        ),
        JobRole(
            title = "UI/UX Designer",
            requiredSkills = listOf("Figma", "Adobe XD", "User Research", "Prototyping", "Wireframing", "Color Theory", "Typography", "Usability Testing"),
            atsKeywords = listOf("Interaction Design", "Visual Design", "Design Thinking", "User Persona"),
            interviewQuestions = listOf("What is your design process?", "Explain the difference between UI and UX.", "How do you handle feedback on your designs?"),
            recommendedCerts = listOf("Google UX Design Professional Certificate", "Interaction Design Foundation (IxDF)"),
            youtubeCourses = listOf("DesignCourse - UI Design", "The Futur - UX Fundamentals"),
            roadmapPhase1 = listOf("Design Fundamentals", "Figma/XD Tool Mastery"),
            roadmapPhase2 = listOf("User Research & Information Architecture", "High-Fidelity Prototyping"),
            roadmapPhase3 = listOf("Usability Testing", "Design Systems")
        ),
        JobRole(
            title = "Cybersecurity Analyst",
            requiredSkills = listOf("Network Security", "Penetration Testing", "Ethical Hacking", "SIEM", "Firewalls", "Cryptography", "Linux", "Risk Assessment"),
            atsKeywords = listOf("Information Security", "Threat Intelligence", "Incident Response", "Vulnerability Management", "Compliance"),
            interviewQuestions = listOf("What is the CIA triad?", "Explain SQL Injection.", "What is the difference between Hashing and Encryption?"),
            recommendedCerts = listOf("CompTIA Security+", "Certified Ethical Hacker (CEH)"),
            youtubeCourses = listOf("The Cyber Mentor", "NetworkChuck - Cybersecurity"),
            roadmapPhase1 = listOf("Networking Fundamentals", "Linux for Security"),
            roadmapPhase2 = listOf("Security Tools (Nmap, Wireshark)", "Ethical Hacking Basics"),
            roadmapPhase3 = listOf("Incident Response", "Security Compliance & Auditing")
        ),
        JobRole(
            title = "Cloud Engineer",
            requiredSkills = listOf("AWS", "Azure", "GCP", "Cloud Security", "Networking", "Storage", "Compute", "Serverless", "Python"),
            atsKeywords = listOf("Cloud Computing", "Virtualization", "Scalability", "Reliability", "Cost Optimization"),
            interviewQuestions = listOf("Explain AWS S3 vs EBS.", "What is Serverless computing?", "How do you secure a cloud environment?"),
            recommendedCerts = listOf("AWS Solutions Architect Associate", "Azure Administrator Associate"),
            youtubeCourses = listOf("AWS Cloud Practitioner Essentials", "Andrew Brown - Cloud Training"),
            roadmapPhase1 = listOf("Cloud Basics (AWS/Azure/GCP)", "Virtualization & Networking"),
            roadmapPhase2 = listOf("Storage & Compute Services", "Cloud Security Implementation"),
            roadmapPhase3 = listOf("Serverless Architecture", "Cost Optimization Strategies")
        ),
        JobRole(
            title = "AI/ML Engineer",
            requiredSkills = listOf("Python", "TensorFlow", "PyTorch", "Deep Learning", "NLP", "Computer Vision", "Machine Learning", "Mathematics"),
            atsKeywords = listOf("Deep Learning Models", "Neural Networks", "NLP Processing", "Computer Vision", "Model Deployment"),
            interviewQuestions = listOf("What is a neural network?", "Explain Supervised vs Unsupervised learning.", "What is Gradient Descent?"),
            recommendedCerts = listOf("Google Cloud ML Engineer", "DeepLearning.AI TensorFlow Developer"),
            youtubeCourses = listOf("3Blue1Brown - Neural Networks", "Stanford CS229 - Machine Learning"),
            roadmapPhase1 = listOf("Linear Algebra & Calculus", "Python ML Fundamentals"),
            roadmapPhase2 = listOf("Deep Learning Frameworks (PyTorch/TF)", "Computer Vision & NLP"),
            roadmapPhase3 = listOf("MLOps & Model Deployment", "Reinforcement Learning")
        ),
        JobRole(
            title = "Data Analyst",
            requiredSkills = listOf("SQL", "Excel", "Tableau", "Power BI", "Python", "Data Visualization", "Statistics", "Pandas"),
            atsKeywords = listOf("Business Intelligence", "Data Cleaning", "Dashboard Creation", "Statistical Analysis", "Reporting"),
            interviewQuestions = listOf("What is a JOIN in SQL?", "How do you handle missing values?", "Difference between Power BI and Tableau?"),
            recommendedCerts = listOf("Google Data Analytics Professional", "Microsoft Certified: Power BI Data Analyst"),
            youtubeCourses = listOf("Alex The Analyst - Portfolio Project", "freeCodeCamp - SQL Tutorial"),
            roadmapPhase1 = listOf("Excel & SQL Basics", "Data Cleaning Techniques"),
            roadmapPhase2 = listOf("Power BI or Tableau Basics", "Python for Data Analysis (Pandas)"),
            roadmapPhase3 = listOf("Advanced SQL & Statistics", "Creating Executive Dashboards")
        ),
        JobRole(
            title = "Product Manager",
            requiredSkills = listOf("Product Roadmap", "User Research", "Agile/Scrum", "A/B Testing", "Market Analysis", "Wireframing", "Product Metrics", "SQL"),
            atsKeywords = listOf("Product Lifecycle", "User Personas", "Agile Methodologies", "Feature Prioritization", "Stakeholder Management"),
            interviewQuestions = listOf("How do you prioritize features?", "Design an elevator for a blind person.", "What is a KPI?"),
            recommendedCerts = listOf("Product School PM Certificate", "OneWeekPM Product Manager Certification"),
            youtubeCourses = listOf("Product School - Learn PM", "Dan Olsen - The Lean Product Playbook"),
            roadmapPhase1 = listOf("Product Principles", "User Research & Personas"),
            roadmapPhase2 = listOf("Wireframing & Prototyping", "Agile/Scrum & Dev Collaboration"),
            roadmapPhase3 = listOf("A/B Testing & Product Analytics", "Product Strategy & Launching")
        ),
        JobRole(
            title = "Software QA/Testing Engineer",
            requiredSkills = listOf("Selenium", "JUnit", "TestNG", "Manual Testing", "Automation Testing", "Java", "Python", "API Testing (Postman)", "Jira"),
            atsKeywords = listOf("Test Automation", "Bug Tracking", "Regression Testing", "CI/CD Integration", "Test Case Design"),
            interviewQuestions = listOf("Difference between Severity and Priority?", "What is Selenium Grid?", "Explain Boundary Value Analysis."),
            recommendedCerts = listOf("ISTQB Certified Tester", "Selenium Automation Certification"),
            youtubeCourses = listOf("SDET-QA Automation Techie", "freeCodeCamp - QA Engineering"),
            roadmapPhase1 = listOf("Manual Testing Principles", "SDLC & Bug Life Cycle"),
            roadmapPhase2 = listOf("Automation Testing (Selenium/Java)", "API Testing (Postman)"),
            roadmapPhase3 = listOf("Mobile Testing (Appium)", "CI/CD Integration (Jenkins)")
        )
    )
}
