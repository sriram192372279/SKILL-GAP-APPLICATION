package com.sriram.skillgap.utils

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.sriram.skillgap.data.model.Company
import com.sriram.skillgap.data.model.Job
import com.sriram.skillgap.data.model.InterviewQuestion
import com.sriram.skillgap.data.model.JobRoleModel

object JobMatcherData {

    val supportedRoles = listOf(
        "Software Developer",
        "Java Full Stack Developer",
        "Data Analyst",
        "Data Scientist",
        "AI Engineer",
        "Cloud Engineer",
        "Cybersecurity Analyst",
        "DevOps Engineer",
        "Android Developer",
        "Product Manager"
    )

    fun getOfflineCompanies(): List<Company> = listOf(
        Company(
            companyId = "google",
            companyName = "Google",
            logoUrl = "G",
            location = "Mountain View, CA, USA",
            website = "https://careers.google.com",
            industry = "Tech Giant",
            workCulture = "High autonomy, focus on innovation, 20% project time, excellent benefits.",
            salaryRange = "₹18 - ₹55 LPA",
            technologiesUsed = listOf("Java", "C++", "Python", "Go", "Kotlin", "TensorFlow", "GCP"),
            eligibilityCriteria = "CGPA 7.5+, CS background.",
            hiringProcess = listOf("OA (Hard)", "Coding Round 1 (DSA)", "Coding Round 2 (System)", "Googleyness"),
            interviewDifficulty = "Hard"
        ),
        Company(
            companyId = "microsoft",
            companyName = "Microsoft",
            logoUrl = "MS",
            location = "Redmond, WA, USA",
            website = "https://careers.microsoft.com",
            industry = "Tech Giant",
            workCulture = "Growth mindset, highly collaborative, emphasis on learning, work-life balance.",
            salaryRange = "₹16 - ₹48 LPA",
            technologiesUsed = listOf("C#", "C++", "TypeScript", "Python", "Azure", "SQL Server"),
            eligibilityCriteria = "Strong OOPs & DSA skills.",
            hiringProcess = listOf("OA (Codility)", "Technical DSA", "LLD/System Design", "Asappropriate Round"),
            interviewDifficulty = "Hard"
        ),
        Company(
            companyId = "amazon",
            companyName = "Amazon",
            logoUrl = "AMZ",
            location = "Seattle, WA, USA",
            website = "https://careers.amazon.com",
            industry = "E-commerce & Cloud",
            workCulture = "Customer obsession, ownership mindset, fast-paced execution, 16 Leadership Principles.",
            salaryRange = "₹15 - ₹45 LPA",
            technologiesUsed = listOf("Java", "C++", "Python", "AWS", "SQL", "NoSQL"),
            eligibilityCriteria = "Strong problem-solving & scalability mindset.",
            hiringProcess = listOf("OA (DSA + LP)", "Tech DSA Round 1", "System Design Round", "Bar Raiser"),
            interviewDifficulty = "Hard"
        ),
        Company(
            companyId = "tcs",
            companyName = "TCS",
            logoUrl = "TCS",
            location = "Mumbai, Maharashtra, India",
            website = "https://www.tcs.com/careers",
            industry = "IT Services",
            workCulture = "Stable careers, structured training (ILP), high job security.",
            salaryRange = "₹3.3 - ₹7.5 LPA",
            technologiesUsed = listOf("Java", "Python", "SQL", "HTML", "CSS", "JS", "C++"),
            eligibilityCriteria = "CGPA 6.0+, no backlogs.",
            hiringProcess = listOf("TCS NQT Exam", "Technical Interview", "Managerial Round", "HR Round"),
            interviewDifficulty = "Easy"
        ),
        Company(
            companyId = "infosys",
            companyName = "Infosys",
            logoUrl = "INF",
            location = "Bengaluru, Karnataka, India",
            website = "https://www.infosys.com/careers",
            industry = "IT Services",
            workCulture = "Continuous learning (Lex platform), iconic Mysore campus, structured processes.",
            salaryRange = "₹3.6 - ₹9.5 LPA",
            technologiesUsed = listOf("Java", "Python", "SQL", "JavaScript", "HTML", "CSS", "Cloud basics"),
            eligibilityCriteria = "60% throughout academics.",
            hiringProcess = listOf("InfyTQ / OA Test", "Technical Interview", "HR/Communication"),
            interviewDifficulty = "Easy"
        ),
        Company(
            companyId = "cognizant",
            companyName = "Cognizant",
            logoUrl = "CTS",
            location = "Teaneck, NJ, USA",
            website = "https://careers.cognizant.com",
            industry = "IT Services & Consulting",
            workCulture = "Collaborative, client-centric, vast domain exposure, progressive training.",
            salaryRange = "₹4.0 - ₹12.0 LPA",
            technologiesUsed = listOf("Java", "C#", "Python", "SQL", "JavaScript", "React"),
            eligibilityCriteria = "B.Tech/MCA degrees.",
            hiringProcess = listOf("AMCAT OA Test", "Technical Interview", "HR Round"),
            interviewDifficulty = "Medium"
        ),
        Company(
            companyId = "accenture",
            companyName = "Accenture",
            logoUrl = "ACN",
            location = "Dublin, Ireland",
            website = "https://www.accenture.com/careers",
            industry = "Consulting & Services",
            workCulture = "Inclusion, client excellence, intense learning, technological agility.",
            salaryRange = "₹4.5 - ₹8.5 LPA",
            technologiesUsed = listOf("Java", "SQL", "Cloud basics", "Python", "JavaScript"),
            eligibilityCriteria = "Cognitive & Tech Assessment eligibility.",
            hiringProcess = listOf("Cognitive & Tech Assessment", "Coding Test", "Communication Assessment", "Tech/HR Interview"),
            interviewDifficulty = "Medium"
        ),
        Company(
            companyId = "wipro",
            companyName = "Wipro",
            logoUrl = "WIP",
            location = "Bengaluru, Karnataka, India",
            website = "https://careers.wipro.com",
            industry = "IT Services",
            workCulture = "Inclusive, Azim Premji Foundation values, Elite/Turbo programs.",
            salaryRange = "₹3.5 - ₹6.5 LPA",
            technologiesUsed = listOf("Java", "Python", "SQL", "HTML", "CSS", "JS"),
            eligibilityCriteria = "3-year service agreement check.",
            hiringProcess = listOf("Elite OA Assessment", "Technical Interview", "HR Interview"),
            interviewDifficulty = "Easy"
        ),
        Company(
            companyId = "capgemini",
            companyName = "Capgemini",
            logoUrl = "CAP",
            location = "Paris, France",
            website = "https://www.capgemini.com/careers",
            industry = "IT Services & Consulting",
            workCulture = "Collaborative, freedom, trust, corporate ethics, global teams.",
            salaryRange = "₹4.0 - ₹7.5 LPA",
            technologiesUsed = listOf("Java", "C++", "SQL", "JavaScript", "Spring Boot"),
            eligibilityCriteria = "Up to 1-year academic gap allowed.",
            hiringProcess = listOf("Pseudo Code & MCQ Test", "English Communication Test", "Game Aptitude Test", "Technical/HR Interview"),
            interviewDifficulty = "Medium"
        ),
        Company(
            companyId = "zoho",
            companyName = "Zoho",
            logoUrl = "Z",
            location = "Chennai, Tamil Nadu, India",
            website = "https://www.zoho.com/careers",
            industry = "SaaS & Product",
            workCulture = "Flat hierarchy, hands-on skills over CGPA, high product ownership.",
            salaryRange = "₹6.5 - ₹15.0 LPA",
            technologiesUsed = listOf("Java", "C", "C++", "JavaScript", "HTML", "CSS", "DSA"),
            eligibilityCriteria = "No degree/CGPA required.",
            hiringProcess = listOf("Written Programming Test", "Advanced CLI Coding", "LLD/Design Round", "HR Round"),
            interviewDifficulty = "Hard"
        )
    )

    fun getOfflineJobs(): List<Job> = listOf(
        Job(
            jobId = "job_g1",
            companyId = "google",
            companyName = "Google",
            jobTitle = "AI Engineer",
            jobRole = "AI Engineer",
            requiredSkills = listOf("Python", "TensorFlow", "Deep Learning", "NLP", "Statistics"),
            preferredSkills = listOf("PyTorch", "GCP", "MLOps"),
            experienceRequired = "0-2 Years",
            salaryRange = "₹25 - ₹40 LPA",
            jobDescription = "Design and optimize deep learning algorithms and deploy scalable models at scale.",
            applicationLink = "https://careers.google.com/jobs",
            location = "Mountain View, CA, USA",
            hiringProcess = listOf("OA (Math/Logic)", "Coding 1 (DSA)", "Coding 2 (ML)", "Googleyness"),
            interviewDifficulty = "Hard"
        ),
        Job(
            jobId = "job_g2",
            companyId = "google",
            companyName = "Google",
            jobTitle = "Android Developer",
            jobRole = "Android Developer",
            requiredSkills = listOf("Kotlin", "Jetpack Compose", "MVVM", "Retrofit", "Room", "Coroutines", "Git"),
            preferredSkills = listOf("Dagger Hilt", "Clean Architecture", "Unit Testing"),
            experienceRequired = "1-3 Years",
            salaryRange = "₹20 - ₹35 LPA",
            jobDescription = "Develop next-gen features for Android platforms using modern declarative UI frameworks.",
            applicationLink = "https://careers.google.com/jobs",
            location = "Mountain View, CA, USA",
            hiringProcess = listOf("OA (Coding)", "Technical Compose", "Architecture MVVM", "Googleyness"),
            interviewDifficulty = "Hard"
        ),
        Job(
            jobId = "job_ms1",
            companyId = "microsoft",
            companyName = "Microsoft",
            jobTitle = "Software Developer",
            jobRole = "Software Developer",
            requiredSkills = listOf("C++", "C#", "SQL Server", "DSA", "OOPs", "Git"),
            preferredSkills = listOf("Azure", "System Design", "Multithreading"),
            experienceRequired = "0-2 Years",
            salaryRange = "₹20 - ₹32 LPA",
            jobDescription = "Work on Windows Core or Office Cloud integrations writing high-performance C++ and C#.",
            applicationLink = "https://careers.microsoft.com/jobs",
            location = "Redmond, WA, USA",
            hiringProcess = listOf("OA (Codility)", "DSA Whiteboard", "LLD/SOLID design", "AA Round"),
            interviewDifficulty = "Hard"
        ),
        Job(
            jobId = "job_amz1",
            companyId = "amazon",
            companyName = "Amazon",
            jobTitle = "Java Full Stack Developer",
            jobRole = "Java Full Stack Developer",
            requiredSkills = listOf("Java", "Spring Boot", "React", "SQL", "Microservices", "Git", "AWS"),
            preferredSkills = listOf("Docker", "Kubernetes", "NoSQL"),
            experienceRequired = "1-3 Years",
            salaryRange = "₹18 - ₹30 LPA",
            jobDescription = "Build transactional microservices and user interfaces for high-scale merchant systems.",
            applicationLink = "https://careers.amazon.com/jobs",
            location = "Seattle, WA, USA",
            hiringProcess = listOf("OA (DSA/LP)", "Tech DSA (HashMap/Trees)", "System LLD", "Bar Raiser"),
            interviewDifficulty = "Hard"
        ),
        Job(
            jobId = "job_tcs1",
            companyId = "tcs",
            companyName = "TCS",
            jobTitle = "Software Developer",
            jobRole = "Software Developer",
            requiredSkills = listOf("Java", "SQL", "HTML", "CSS", "JavaScript", "Git"),
            preferredSkills = listOf("Bootstrap", "DBMS", "Software Engineering"),
            experienceRequired = "0-1 Years",
            salaryRange = "₹3.6 - Gold/Ninja Scale",
            jobDescription = "Develop frontend and backend modules under corporate digital transformation projects.",
            applicationLink = "https://www.tcs.com/careers",
            location = "Mumbai, India",
            hiringProcess = listOf("TCS NQT Test", "Technical OOPS", "HR Verification"),
            interviewDifficulty = "Easy"
        ),
        Job(
            jobId = "job_inf1",
            companyId = "infosys",
            companyName = "Infosys",
            jobTitle = "Java Full Stack Developer",
            jobRole = "Java Full Stack Developer",
            requiredSkills = listOf("Java", "Spring Boot", "MySQL", "HTML", "CSS", "JavaScript", "Git"),
            preferredSkills = listOf("Hibernate", "Angular", "Web Services"),
            experienceRequired = "0-2 Years",
            salaryRange = "₹4.5 - ₹8.5 LPA",
            jobDescription = "Design database-driven web APIs and connect responsive HTML/CSS frontends.",
            applicationLink = "https://www.infosys.com/careers",
            location = "Mysore, India",
            hiringProcess = listOf("InfyTQ Coding", "JDBC & SQL joints", "HR Interview"),
            interviewDifficulty = "Easy"
        ),
        Job(
            jobId = "job_z1",
            companyId = "zoho",
            companyName = "Zoho",
            jobTitle = "Software Developer",
            jobRole = "Software Developer",
            requiredSkills = listOf("Java", "C", "C++", "OOPs", "DSA", "Git"),
            preferredSkills = listOf("DBMS", "CLI scripting", "Multithreading"),
            experienceRequired = "0-2 Years",
            salaryRange = "₹6.5 - ₹12.0 LPA",
            jobDescription = "Build high-integrity business applications, write local database systems, and optimize core modules.",
            applicationLink = "https://www.zoho.com/careers",
            location = "Chennai, India",
            hiringProcess = listOf("C/Java Written Test", "CLI Booking System (3hr)", "LLD Code optimization", "HR Interview"),
            interviewDifficulty = "Hard"
        )
    )

    fun getOfflineQuestions(): List<InterviewQuestion> {
        val list = mutableListOf<InterviewQuestion>()
        var count = 1
        getOfflineCompanies().forEach { comp ->
            comp.name.let { cName ->
                when (cName) {
                    "Google" -> {
                        list.add(
                            InterviewQuestion(
                                questionId = "q_${count++}",
                                companyId = "google",
                                companyName = cName,
                                role = "AI Engineer",
                                questionType = "Technical",
                                question = "How do you find the longest path in a Directed Acyclic Graph (DAG)?",
                                difficulty = "Hard",
                                answerHint = "Perform topological sort, initialize distances as negative infinity (source = 0), and update adjacent distances in topological order. Complexity is O(V + E)."
                            )
                        )
                        list.add(
                            InterviewQuestion(
                                questionId = "q_${count++}",
                                companyId = "google",
                                companyName = cName,
                                role = "Software Developer",
                                questionType = "Coding",
                                question = "Design a globally distributed rate limiter.",
                                difficulty = "Hard",
                                answerHint = "Use Redis Cluster for in-memory token counts, Sliding Window Log algorithm to prevent spikes, and local caching with Gossip protocol sync."
                            )
                        )
                        list.add(
                            InterviewQuestion(
                                questionId = "q_${count++}",
                                companyId = "google",
                                companyName = cName,
                                role = "Software Developer",
                                questionType = "HR",
                                question = "Describe a situation where you had to make a critical technical decision with incomplete information.",
                                difficulty = "Medium",
                                answerHint = "Explain how you assessed the risks, gathered validation data, formulated a fallback plan, and iteratively monitored outcomes to minimize downtime."
                            )
                        )
                        list.add(
                            InterviewQuestion(
                                questionId = "q_${count++}",
                                companyId = "google",
                                companyName = cName,
                                role = "AI Engineer",
                                questionType = "Technical",
                                question = "How would you handle a conflict between consistency and performance in a real-time analytics system?",
                                difficulty = "Hard",
                                answerHint = "Assess requirements using CAP theorem. For analytics, trade off immediate consistency for eventual consistency by introducing a message queue (Kafka) and batch processing."
                            )
                        )
                    }
                    "Microsoft" -> {
                        list.add(
                            InterviewQuestion(
                                questionId = "q_${count++}",
                                companyId = "microsoft",
                                companyName = cName,
                                role = "Software Developer",
                                questionType = "Coding",
                                question = "Print binary tree boundary nodes in anti-clockwise direction.",
                                difficulty = "Medium",
                                answerHint = "Traverse left boundary, print all leaves, and traverse right boundary in reverse. Time complexity is O(N)."
                            )
                        )
                        list.add(
                            InterviewQuestion(
                                questionId = "q_${count++}",
                                companyId = "microsoft",
                                companyName = cName,
                                role = "Software Developer",
                                questionType = "Technical",
                                question = "Difference between final, finally, and finalize in Java.",
                                difficulty = "Easy",
                                answerHint = "final is a modifier (immutable var, non-overridable method/class). finally is an exception handling block. finalize is a deprecated cleanup method."
                            )
                        )
                        list.add(
                            InterviewQuestion(
                                questionId = "q_${count++}",
                                companyId = "microsoft",
                                companyName = cName,
                                role = "Software Developer",
                                questionType = "Technical",
                                question = "Explain the difference between a Process and a Thread, and how they share memory.",
                                difficulty = "Medium",
                                answerHint = "A process is an independent executing program with its own address space. A thread is a lightweight sub-unit sharing the parent process's heap and memory, but has its own stack."
                            )
                        )
                        list.add(
                            InterviewQuestion(
                                questionId = "q_${count++}",
                                companyId = "microsoft",
                                companyName = cName,
                                role = "Software Developer",
                                questionType = "HR",
                                question = "Tell me about a project where you had to quickly learn and adopt a new technology.",
                                difficulty = "Easy",
                                answerHint = "Use the STAR method. Describe why the tech was needed, your learning strategy (docs, prototypes), the execution, and the successful outcome."
                            )
                        )
                    }
                    "Amazon" -> {
                        list.add(
                            InterviewQuestion(
                                questionId = "q_${count++}",
                                companyId = "amazon",
                                companyName = cName,
                                role = "Software Developer",
                                questionType = "Technical",
                                question = "Design a URL shortener like Bit.ly.",
                                difficulty = "Medium",
                                answerHint = "Use Base62 encoding on a unique auto-incrementing ID. Set up Redis cache in front of an indexed SQL database for redirection. Handle collisions/scaling."
                            )
                        )
                        list.add(
                            InterviewQuestion(
                                questionId = "q_${count++}",
                                companyId = "amazon",
                                companyName = cName,
                                role = "Software Developer",
                                questionType = "HR",
                                question = "Tell me about a time you took ownership of a task that was not explicitly assigned to you.",
                                difficulty = "Medium",
                                answerHint = "Amazon LP: Ownership. Highlight how you identified a system gap or bug, took charge of fixing it without being asked, and documented it for the team."
                            )
                        )
                        list.add(
                            InterviewQuestion(
                                questionId = "q_${count++}",
                                companyId = "amazon",
                                companyName = cName,
                                role = "Software Developer",
                                questionType = "HR",
                                question = "Describe a situation where you had to deliver results under tight deadline pressure.",
                                difficulty = "Medium",
                                answerHint = "Amazon LP: Deliver Results. Emphasize task prioritization, dividing work, managing developer tradeoffs, and executing the critical path to hit the launch date."
                            )
                        )
                    }
                    "Zoho" -> {
                        list.add(
                            InterviewQuestion(
                                questionId = "q_${count++}",
                                companyId = "zoho",
                                companyName = cName,
                                role = "Software Developer",
                                questionType = "Coding",
                                question = "Design a CLI-based Taxi Booking Application.",
                                difficulty = "Hard",
                                answerHint = "Model entities (Taxi, Booking, Customer). Use a greedy allocation algorithm based on proximity and lowest earnings. Maintain transaction histories in memory."
                            )
                        )
                        list.add(
                            InterviewQuestion(
                                questionId = "q_${count++}",
                                companyId = "zoho",
                                companyName = cName,
                                role = "Software Developer",
                                questionType = "Technical",
                                question = "What is database normalization? Explain 1NF, 2NF, and 3NF.",
                                difficulty = "Medium",
                                answerHint = "Structuring databases to reduce redundancy. 1NF: Atomic values. 2NF: 1NF + no partial dependency. 3NF: 2NF + no transitive dependency."
                            )
                        )
                        list.add(
                            InterviewQuestion(
                                questionId = "q_${count++}",
                                companyId = "zoho",
                                companyName = cName,
                                role = "Software Developer",
                                questionType = "HR",
                                question = "How do you handle constructive criticism from seniors or teammates?",
                                difficulty = "Easy",
                                answerHint = "Focus on active listening, keeping ego aside, seeking clarification to improve, and applying the suggestions to future commits."
                            )
                        )
                    }
                    else -> {
                        list.add(
                            InterviewQuestion(
                                questionId = "q_${count++}",
                                companyId = comp.companyId,
                                companyName = cName,
                                role = "Software Developer",
                                questionType = "Technical",
                                question = "What are the four pillars of OOPS?",
                                difficulty = "Easy",
                                answerHint = "Encapsulation (data hiding), Inheritance (code reuse), Polymorphism (overloading/overriding), and Abstraction (hiding details)."
                            )
                        )
                        list.add(
                            InterviewQuestion(
                                questionId = "q_${count++}",
                                companyId = comp.companyId,
                                companyName = cName,
                                role = "Software Developer",
                                questionType = "Technical",
                                question = "Write a query to find the second highest salary from Employee table.",
                                difficulty = "Easy",
                                answerHint = "SELECT MAX(Salary) FROM Employee WHERE Salary < (SELECT MAX(Salary) FROM Employee);"
                            )
                        )
                        list.add(
                            InterviewQuestion(
                                questionId = "q_${count++}",
                                companyId = comp.companyId,
                                companyName = cName,
                                role = "Software Developer",
                                questionType = "Technical",
                                question = "Difference between Method Overloading and Method Overriding in Java.",
                                difficulty = "Easy",
                                answerHint = "Overloading is compile-time (same method name, different signatures). Overriding is runtime (subclass redefines parent method with same signature)."
                            )
                        )
                        list.add(
                            InterviewQuestion(
                                questionId = "q_${count++}",
                                companyId = comp.companyId,
                                companyName = cName,
                                role = "Software Developer",
                                questionType = "HR",
                                question = "Why should we hire you over other candidates?",
                                difficulty = "Easy",
                                answerHint = "Align your tech strengths, quick learning abilities, and cultural compatibility with the company's core services and business requirements."
                            )
                        )
                        list.add(
                            InterviewQuestion(
                                questionId = "q_${count++}",
                                companyId = comp.companyId,
                                companyName = cName,
                                role = "Software Developer",
                                questionType = "HR",
                                question = "Where do you see yourself in five years?",
                                difficulty = "Easy",
                                answerHint = "Express a desire to deepen technical expertise, transition into design/architectural responsibilities, and lead project delivery."
                            )
                        )
                    }
                }
            }
        }
        return list
    }

    fun getOfflineRoles(): List<JobRoleModel> = listOf(
        JobRoleModel(
            roleId = "sw_dev",
            roleName = "Software Developer",
            requiredSkills = listOf("Java", "C++", "Python", "DSA", "OOPs", "SQL", "Git"),
            certifications = listOf("AWS Certified Cloud Practitioner", "Java Professional SE Certificate"),
            projects = listOf("Personal Task Manager CLI", "E-commerce Payment Integration API"),
            roadmap = listOf("Master a Core Language (Java/Python)", "Study DSA and Time Complexity", "Learn SQL Databases", "Understand Git & OOPs"),
            salaryRange = "₹8 - ₹18 LPA",
            futureScope = "Continuous growth due to enterprise digitization and cloud scaling."
        ),
        JobRoleModel(
            roleId = "java_fs",
            roleName = "Java Full Stack Developer",
            requiredSkills = listOf("Java", "Spring Boot", "React", "SQL", "Microservices", "HTML", "CSS", "JavaScript", "Git"),
            certifications = listOf("Oracle Spring Boot Professional", "Frontend Stack Meta Certification"),
            projects = listOf("Full Stack E-Shop Platform", "Social Networking Microservices API"),
            roadmap = listOf("Java & Spring Boot APIs", "React Components & State Management", "Relational Database Indexing", "Cloud Container Deployment"),
            salaryRange = "₹10 - ₹22 LPA",
            futureScope = "High demand for building transactional microservices and scalable SaaS portals."
        ),
        JobRoleModel(
            roleId = "data_analyst",
            roleName = "Data Analyst",
            requiredSkills = listOf("SQL", "Excel", "Tableau", "Power BI", "Python", "Statistics"),
            certifications = listOf("Google Data Analytics Professional", "Microsoft Power BI Data Analyst"),
            projects = listOf("Retail Sales Performance Dashboard", "SQL Cohort Retention Study"),
            roadmap = listOf("Excel Charts & Pivot Tables", "SQL Window Functions & Aggregations", "Tableau Interactive Visualizations", "Basic Statistics & Pandas"),
            salaryRange = "₹6 - ₹12 LPA",
            futureScope = "Data-driven business auditing makes this a highly stable and crucial function."
        ),
        JobRoleModel(
            roleId = "ai_eng",
            roleName = "AI Engineer",
            requiredSkills = listOf("Python", "TensorFlow", "PyTorch", "Deep Learning", "NLP", "Computer Vision", "Mathematics"),
            certifications = listOf("DeepLearning.AI TensorFlow Developer", "AWS Certified Machine Learning"),
            projects = listOf("Custom Text Generator using Transformers", "Object Detection Pipeline for Retail"),
            roadmap = listOf("Python & Statistics Foundation", "Machine Learning Classifiers", "Neural Network Implementations", "RAG & Large Language Models"),
            salaryRange = "₹15 - ₹35 LPA",
            futureScope = "Exponential demand driven by generative AI agents and deep optimization workloads."
        ),
        JobRoleModel(
            roleId = "android_dev",
            roleName = "Android Developer",
            requiredSkills = listOf("Kotlin", "Jetpack Compose", "MVVM", "Retrofit", "Room", "Coroutines", "Git"),
            certifications = listOf("Google Associate Android Developer"),
            projects = listOf("Offline-First News Client", "Habit Tracker App with Declarative Animation"),
            roadmap = listOf("Kotlin Fundamentals", "Declarative Jetpack Compose UI", "Architecture MVVM & Dependency Injection", "Coroutines Async Threading"),
            salaryRange = "₹8 - ₹18 LPA",
            futureScope = "Steady demand as businesses require modern native mobile interactions."
        )
    )

    fun seedFirestore(onComplete: (Boolean, String?) -> Unit) {
        val firestore = FirebaseFirestore.getInstance()

        firestore.runTransaction { transaction ->
            // Seed companies
            getOfflineCompanies().forEach { comp ->
                val ref = firestore.collection("companies").document(comp.companyId)
                transaction.set(ref, comp, SetOptions.merge())
            }

            // Seed jobs
            getOfflineJobs().forEach { job ->
                val ref = firestore.collection("jobs").document(job.jobId)
                transaction.set(ref, job, SetOptions.merge())
            }

            // Seed interview questions
            getOfflineQuestions().forEach { qn ->
                val ref = firestore.collection("interview_questions").document(qn.questionId)
                transaction.set(ref, qn, SetOptions.merge())
            }

            // Seed job roles
            getOfflineRoles().forEach { role ->
                val ref = firestore.collection("job_roles").document(role.roleId)
                transaction.set(ref, role, SetOptions.merge())
            }
            null
        }.addOnSuccessListener {
            onComplete(true, null)
        }.addOnFailureListener { e ->
            onComplete(false, e.localizedMessage)
        }
    }
}
