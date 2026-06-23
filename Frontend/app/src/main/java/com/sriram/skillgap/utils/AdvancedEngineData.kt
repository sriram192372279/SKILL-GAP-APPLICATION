package com.sriram.skillgap.utils

import com.sriram.skillgap.data.model.User
import java.util.UUID

// ==========================================
// 1. Placement Probability Models
// ==========================================
data class CompanyReadiness(
    val companyName: String,
    val readinessPercentage: Int,
    val technicalScore: Int,
    val interviewScore: Int,
    val codingScore: Int,
    val communicationScore: Int,
    val missingRequirements: List<String>,
    val improvementRecommendations: List<String>
)

// ==========================================
// 2. Career Twin Models
// ==========================================
data class CareerTwinProjection(
    val timeline: String, // "3 Months", "6 Months", "1 Year", "2 Years"
    val position: String,
    val predictedSalary: String,
    val skillGrowth: List<String>,
    val employabilityScore: Int
)

data class CareerTwinProfile(
    val currentSkills: List<String>,
    val futureSkills: List<String>,
    val growthForecast: String,
    val progressTimeline: List<String>,
    val projections: List<CareerTwinProjection>
)

// ==========================================
// 3. AI Mock Interview Models
// ==========================================
data class MockInterviewQuestion(
    val id: String,
    val question: String,
    val type: String, // "Technical", "HR", "Coding", "Company-Specific"
    val standardAnswer: String,
    val difficulty: String
)

data class MockInterviewEvaluation(
    val overallScore: Int,
    val confidenceScore: Int,
    val accuracyScore: Int,
    val generatedFeedback: String,
    val improvementAreas: List<String>
)

// ==========================================
// 4. Career Success Simulator Models
// ==========================================
data class SuccessSimulationResult(
    val timeToReadyWeeks: Int,
    val successProbability: Int,
    val learningPath: List<String>,
    val missingSkills: List<String>,
    val recommendedProjects: List<RecommendedProject>,
    val timelineProgress: List<Pair<String, Int>> // Month -> Readiness %
)

// ==========================================
// 5. Recruiter View Models
// ==========================================
data class RecruiterInsights(
    val resumeStrength: Int,
    val resumeWeakness: List<String>,
    val atsScore: Int,
    val hiringRecommendation: String, // "Strong Hire", "Hire", "Consider", "Needs Improvement"
    val employabilityRating: String, // "★ ★ ★ ★ ★", etc.
    val recruiterInsightsText: String,
    val missingSkills: List<String>,
    val interviewReadiness: Int
)

// ==========================================
// 6. Advanced Project Recommendation Models
// ==========================================
data class RecommendedProject(
    val id: String,
    val title: String,
    val difficulty: String, // "Beginner", "Intermediate", "Advanced"
    val duration: String,
    val technologies: List<String>,
    val learningOutcome: String,
    val category: String // "Java Developer", "Data Analyst", "AI Engineer"
)

// ==========================================
// 8. Personalized Coding Preparation Center Models
// ==========================================
data class CodingPrepQuestion(
    val id: String,
    val category: String, // "Java", "SQL", "DSA", "OOPS", "Aptitude"
    val question: String,
    val options: List<String>,
    val correctAnswer: Int, // Index of correct option
    val explanation: String,
    val difficulty: String
)

// ==========================================
// 10. Patent Innovation Center Models
// ==========================================
data class PatentMetrics(
    val careerRiskScore: Int,
    val futureSkillDemandScore: Int,
    val adaptiveLearningScore: Int,
    val placementProbabilityScore: Int,
    val recruiterAcceptanceScore: Int
)


// ==========================================
// ADVANCED ENGINE DATA REPOSITORY & ALGORITHMS
// ==========================================
object AdvancedEngineData {

    val companiesList = listOf("TCS", "Infosys", "Cognizant", "Accenture", "Capgemini", "Wipro", "Zoho", "Amazon", "Microsoft", "Google")

    // Project Recommendations Database
    val projectsDatabase = listOf(
        RecommendedProject(
            id = "proj-1",
            title = "Secure Core Banking System",
            difficulty = "Advanced",
            duration = "4 Weeks",
            technologies = listOf("Java", "Spring Boot", "PostgreSQL", "Spring Security", "JUnit"),
            learningOutcome = "Master transaction isolation levels, dynamic secure authentication, multi-threaded ledger balancing, and robust unit testing cycles.",
            category = "Java Developer"
        ),
        RecommendedProject(
            id = "proj-2",
            title = "Real-Time Inventory Management Control",
            difficulty = "Intermediate",
            duration = "3 Weeks",
            technologies = listOf("Java", "Hibernate", "MySQL", "JavaFX", "Log4j"),
            learningOutcome = "Implement reliable concurrency control, relational database triggers, local caching, and design patterns like DAO and Singleton.",
            category = "Java Developer"
        ),
        RecommendedProject(
            id = "proj-3",
            title = "Corporate Employee Operations Portal",
            difficulty = "Beginner",
            duration = "2 Weeks",
            technologies = listOf("Java Servlet", "JSP", "JDBC", "Bootstrap"),
            learningOutcome = "Understand MVC structure, session management, dynamic user routing, and writing direct JDBC execution pools.",
            category = "Java Developer"
        ),
        RecommendedProject(
            id = "proj-4",
            title = "Omnichannel E-commerce Sales Performance Dashboard",
            difficulty = "Intermediate",
            duration = "3 Weeks",
            technologies = listOf("Python", "Pandas", "Power BI", "SQL", "Streamlit"),
            learningOutcome = "Create multi-dimensional data models, write complex aggregate window queries, clean transactional profiles, and build active metrics widgets.",
            category = "Data Analyst"
        ),
        RecommendedProject(
            id = "proj-5",
            title = "Predictive Customer Analytics Engine",
            difficulty = "Advanced",
            duration = "4 Weeks",
            technologies = listOf("Python", "Scikit-Learn", "Tableau", "SQL Server"),
            learningOutcome = "Perform feature engineering, design customer churn classifier models, cluster profiles, and design strategic executive dashboards.",
            category = "Data Analyst"
        ),
        RecommendedProject(
            id = "proj-6",
            title = "Business Intelligence Real-Time Monitor",
            difficulty = "Beginner",
            duration = "2 Weeks",
            technologies = listOf("Excel", "SQL", "Tableau Public"),
            learningOutcome = "Master pivot charts, writing multi-table database joins, parsing dirty CSV files, and laying out intuitive visual stories.",
            category = "Data Analyst"
        ),
        RecommendedProject(
            id = "proj-7",
            title = "AI-Powered Smart Resume Analyzer",
            difficulty = "Advanced",
            duration = "4 Weeks",
            technologies = listOf("Python", "NLP", "Spacy", "Vite", "FastAPI"),
            learningOutcome = "Train custom NER pipelines for skill extraction, calculate TF-IDF keyword cosine similarities, and deploy scalable local web interfaces.",
            category = "AI Engineer"
        ),
        RecommendedProject(
            id = "proj-8",
            title = "Self-Learning Intelligent Support Chatbot",
            difficulty = "Intermediate",
            duration = "3 Weeks",
            technologies = listOf("Python", "PyTorch", "RAG Vector DB", "LangChain"),
            learningOutcome = "Implement local retrieval-augmented architectures, chunk vector knowledgebases, design clean API prompts, and handle dynamic sessions.",
            category = "AI Engineer"
        ),
        RecommendedProject(
            id = "proj-9",
            title = "Contextual Content Recommendation Engine",
            difficulty = "Advanced",
            duration = "4 Weeks",
            technologies = listOf("Python", "Collaborative Filtering", "FastAPI", "MongoDB"),
            learningOutcome = "Design collaborative and content-based recommendation systems, handle cold-start anomalies, and manage high-throughput matrix updates.",
            category = "AI Engineer"
        )
    )

    // Personalized Practice Questions Database
    val codingPrepQuestions = listOf(
        // Java
        CodingPrepQuestion(
            id = "cp-java-1",
            category = "Java",
            question = "Which of the following is true about string memory allocation in Java?",
            options = listOf(
                "Strings are always created on the stack area for rapid access.",
                "Using the 'new' keyword bypasses the String Constant Pool and creates a new object in the Heap.",
                "String objects in the String Constant Pool are garbage collected immediately when a method returns.",
                "Strings are mutable if defined using final."
            ),
            correctAnswer = 1,
            explanation = "Using 'new String(\"...\")' explicitly instructs the JVM to construct a fresh object on the heap, ignoring the String Constant Pool. Normal literals are stored in the pool.",
            difficulty = "Intermediate"
        ),
        CodingPrepQuestion(
            id = "cp-java-2",
            category = "Java",
            question = "How does the 'transient' keyword affect Java serialization?",
            options = listOf(
                "It causes a variable to be serialized in encrypted format.",
                "It marks a class as non-serializable.",
                "It specifies that a variable should not be serialized during object persistence.",
                "It forces the compiler to ignore thread modifications."
            ),
            correctAnswer = 2,
            explanation = "Variables declared with the 'transient' modifier are skipped by the default Java serialization mechanism and their default values are assigned upon deserialization.",
            difficulty = "Hard"
        ),
        // SQL
        CodingPrepQuestion(
            id = "cp-sql-1",
            category = "SQL",
            question = "Which SQL clause is used to filter records after aggregate functions have been applied?",
            options = listOf("WHERE", "HAVING", "GROUP BY", "FILTER BY"),
            correctAnswer = 1,
            explanation = "The 'HAVING' clause is evaluated after groups have been computed by the 'GROUP BY' clause, whereas 'WHERE' filters rows before aggregation occurs.",
            difficulty = "Beginner"
        ),
        CodingPrepQuestion(
            id = "cp-sql-2",
            category = "SQL",
            question = "What is the difference between a CLUSTERED INDEX and a NON-CLUSTERED INDEX?",
            options = listOf(
                "A clustered index stores data physically in order, while a non-clustered index is a separate structure pointing to the rows.",
                "A clustered index is faster for inserts, whereas a non-clustered index is faster for range scans.",
                "A table can have multiple clustered indexes but only one non-clustered index.",
                "Clustered indexes are stored entirely in RAM."
            ),
            correctAnswer = 0,
            explanation = "A clustered index defines the physical order of data storage in a table (hence maximum of 1 per table). A non-clustered index creates a separate B-Tree structure pointing back to data pages.",
            difficulty = "Hard"
        ),
        // DSA
        CodingPrepQuestion(
            id = "cp-dsa-1",
            category = "DSA",
            question = "What is the worst-case time complexity of searching in a Balanced Binary Search Tree (like AVL tree)?",
            options = listOf("O(1)", "O(N)", "O(log N)", "O(N log N)"),
            correctAnswer = 2,
            explanation = "Balanced BSTs strictly maintain a height of O(log N) where N is the number of nodes, guaranteeing O(log N) search, insert, and delete cycles.",
            difficulty = "Intermediate"
        ),
        CodingPrepQuestion(
            id = "cp-dsa-2",
            category = "DSA",
            question = "Which data structure is fundamentally utilized to implement Breadth-First Search (BFS) in graphs?",
            options = listOf("Stack", "Queue", "Priority Queue", "Linked List"),
            correctAnswer = 1,
            explanation = "BFS traverses adjacent level nodes sequentially. A Queue (FIFO) is used to enqueue neighboring unvisited nodes for processing, preserving order.",
            difficulty = "Beginner"
        ),
        // OOPS
        CodingPrepQuestion(
            id = "cp-oops-1",
            category = "OOPS",
            question = "What is the fragility of base class problem in inheritance?",
            options = listOf(
                "When subclasses exceed memory bounds of parent classes.",
                "When changes to a base class unintentionally break the functionality of subclasses inheriting it.",
                "When base classes cannot compile without subclasses.",
                "When the base class is declared final."
            ),
            correctAnswer = 1,
            explanation = "The fragile base class problem is an architectural flaw in tight inheritance designs, where modifications in the base class alter subclass assertions in unforeseen ways.",
            difficulty = "Intermediate"
        ),
        // Aptitude
        CodingPrepQuestion(
            id = "cp-apt-1",
            category = "Aptitude",
            question = "A car travels at 60 km/h for 2 hours and then at 80 km/h for 3 hours. What is its average speed for the entire journey?",
            options = listOf("70 km/h", "72 km/h", "68 km/h", "75 km/h"),
            correctAnswer = 1,
            explanation = "Total distance = (60 * 2) + (80 * 3) = 120 + 240 = 360 km. Total time = 2 + 3 = 5 hours. Average Speed = 360 / 5 = 72 km/h.",
            difficulty = "Beginner"
        )
    )

    // Mock Interview Pools
    val interviewQuestionsDatabase = listOf(
        MockInterviewQuestion("q-1", "Explain the concept of thread lifecycle states in Java. What is thread deadlock?", "Technical", "A thread in Java has multiple lifecycle states: New, Runnable, Blocked, Waiting, Timed Waiting, and Terminated. A deadlock occurs when two or more threads are permanently blocked, each holding a lock that the other needs.", "Hard"),
        MockInterviewQuestion("q-2", "Why do you want to work at our company specifically? Describe our values.", "HR", "Align your answer with the target company's mission. For example, for Google, emphasize innovation and Googleyness; for Amazon, emphasize customer obsession, operational excellence, and ownership.", "Easy"),
        MockInterviewQuestion("q-3", "Write a pseudo-code or outline an algorithm to detect a cycle in a singly linked list.", "Coding", "Use Floyd's Cycle-Finding Algorithm (Two Pointers: Slow and Fast). Advance slow by 1 node and fast by 2 nodes. If they meet, a cycle exists. If fast reaches null, no cycle exists.", "Medium"),
        MockInterviewQuestion("q-4", "Explain the ACID properties of transactional databases. How does a database manage transaction isolation?", "Technical", "ACID stands for Atomicity, Consistency, Isolation, Durability. Isolation is managed through locking mechanisms (Shared, Exclusive) and isolation levels (Read Uncommitted, Read Committed, Repeatable Read, Serializable).", "Hard"),
        MockInterviewQuestion("q-5", "Where do you see yourself in five years? How does this role align with that?", "HR", "Outline a career path focused on technical mastery and team leadership. State that this role provides the optimal foundation to build core engineering principles.", "Easy")
    )

    // Company Profiles database for Company Intelligence Hub
    val companyHubData = mapOf(
        "Google" to mapOf(
            "Overview" to "Google LLC is an American multinational technology giant focusing on artificial intelligence, search engines, cloud computing, and computer hardware.",
            "HiringProcess" to "OA (2 DSA problems) -> 3 Technical Coding Rounds (Graph, DP, structures) -> Googleyness & Leadership Interview.",
            "Difficulty" to "Hard (9.5/10)",
            "Salary" to "₹18 LPA - ₹55+ LPA (For fresh graduates)",
            "Culture" to "Autonomy, Innovation focus, psychologically safe team dynamics, 20% projects time.",
            "Tech" to "Java, C++, Python, Go, GCP, TensorFlow, Kubernetes, Linux",
            "Eligibility" to "B.Tech/M.Tech with CGPA 7.5+ or top-tier competitive coding scores.",
            "Trends" to "High hiring focus on Generative AI systems, RAG database architectures, and distributed systems optimization.",
            "Questions" to listOf(
                "Find the longest path in a Directed Acyclic Graph (DAG).",
                "Design a highly scalable global rate limiter like the one used at Google.",
                "How does ConcurrentHashMap achieve high concurrent throughput in Java?"
            ),
            "Checklist" to listOf("Master LeetCode Hard Trees & Graphs", "Study System Design (caching, sharding)", "Prepare STAR behavioral questions")
        ),
        "Microsoft" to mapOf(
            "Overview" to "Microsoft Corporation is a global leader in software, operating systems, cloud services (Azure), and enterprise applications.",
            "HiringProcess" to "Codility OA (Arrays, Strings) -> 2 Technical Rounds (Trees, lists, pointers) -> LLD SOLID Round -> Director (Asappropriate) Round.",
            "Difficulty" to "Hard (8.8/10)",
            "Salary" to "₹16 LPA - ₹48+ LPA (For freshers)",
            "Culture" to "Satya Nadella's Growth Mindset, highly collaborative, corporate balance, and azure cloud learning.",
            "Tech" to "C#/.NET, C++, TypeScript, Python, Azure, SQL Server",
            "Eligibility" to "65% or 6.5 CGPA throughout graduation. B.E/B.Tech/MCA.",
            "Trends" to "Integration of Copilot products across all systems, massive scale hybrid cloud deployments.",
            "Questions" to listOf(
                "Print boundary nodes of a binary tree in anti-clockwise direction.",
                "What is the difference between final, finally, and finalize in Java?",
                "Design a collaborative real-time document editing tool like Word Online."
            ),
            "Checklist" to listOf("Excel in pointer manipulation & tree operations", "Master SOLID & Low-Level Design (LLD)", "Azure Core Fundamentals")
        ),
        "Amazon" to mapOf(
            "Overview" to "Amazon is a global giant dominating E-commerce, Cloud Infrastructure (AWS), Streaming, and AI algorithms.",
            "HiringProcess" to "OA (Coding + Work Style leadership questions) -> 2 Technical Interviews (Graphs, Heaps, Cache) -> System Design -> Bar Raiser Round.",
            "Difficulty" to "Hard (9.2/10)",
            "Salary" to "₹15 LPA - ₹45+ LPA",
            "Culture" to "Customer Obsession, Ownership, Frugality, 16 Leadership Principles directly evaluated.",
            "Tech" to "Java, C++, Python, AWS, SQL, NoSQL structures",
            "Eligibility" to "B.Tech/M.Tech/MCA with 65%+ and zero active backlogs.",
            "Trends" to "Massive supply chain automation, Serverless computing configurations, and NoSQL ledger scaling.",
            "Questions" to listOf(
                "Design and implement a Least Recently Used (LRU) Cache in O(1).",
                "Design a scalable real-time shipping tracking backend logistics network.",
                "Provide a personal example showcasing customer obsession under constraints."
            ),
            "Checklist" to listOf("Memorize & outline scenarios for 16 Leadership Principles", "LeetCode Medium/Hard Graphs, Trees & Heaps", "Master AWS System Design patterns")
        ),
        "TCS" to mapOf(
            "Overview" to "Tata Consultancy Services is India's largest global IT service, consulting, and digital solutions provider.",
            "HiringProcess" to "TCS NQT (Aptitude, Verbal, Basic Coding) -> Technical Interview -> Managerial Adaptability Round -> HR Relocation Round.",
            "Difficulty" to "Medium (6.0/10)",
            "Salary" to "₹3.36 LPA (Ninja) - ₹7.0+ LPA (Digital/Prime)",
            "Culture" to "Stable careers, high corporate values, training platforms (ILP), and high job stability.",
            "Tech" to "Java, Python, SQL, C++, HTML/CSS, SDLC Core",
            "Eligibility" to "60% or 6.0 CGPA throughout academic records. Max 1 active backlog.",
            "Trends" to "Large cloud migrations projects, massive AI-literacy program for employees.",
            "Questions" to listOf(
                "A train passes a platform in 15 seconds. Calculate the train speed.",
                "Explain the four pillars of OOPS with clean real-world analogies.",
                "Write code to reverse a string without using built-in library functions."
            ),
            "Checklist" to listOf("Speed-drill TCS NQT quantitative aptitude questions", "Study basic Java OOPS and syntax structures", "Prepare database primary/foreign keys & basic SQL joins")
        )
    )

    // ==========================================
    // PLACEMENT PROBABILITY CALCULATOR
    // ==========================================
    fun calculatePlacementProbability(user: User, matchingSkills: List<String>, missingSkills: List<String>, solvedAptitude: Set<String>, solvedCoding: Set<String>, interviewConfidence: Int): List<CompanyReadiness> {
        val userSkills = matchingSkills.map { it.lowercase() }.toSet()
        val totalSolved = solvedAptitude.size + solvedCoding.size
        
        return companiesList.map { company ->
            // Algorithmic weights depending on company tier
            val isProductTier = company in listOf("Google", "Amazon", "Microsoft", "Zoho")
            
            val techWeight = if (isProductTier) 0.35f else 0.45f
            val codingWeight = if (isProductTier) 0.35f else 0.20f
            val interviewWeight = 0.20f
            val commWeight = if (isProductTier) 0.10f else 0.15f
            
            // 1. Calculate Technical Readiness (based on skills)
            val requiredSkills = when (company) {
                "Google" -> listOf("Java", "C++", "Python", "Go", "Kubernetes", "Linux")
                "Microsoft" -> listOf("C#", "C++", "TypeScript", "Python", "Azure", "SQL")
                "Amazon" -> listOf("Java", "C++", "Python", "AWS", "SQL", "NoSQL")
                "Zoho" -> listOf("Java", "C", "JavaScript", "HTML", "CSS", "DSA")
                "TCS" -> listOf("Java", "Python", "SQL", "HTML", "CSS", "SDLC")
                "Infosys" -> listOf("Java", "C++", "Python", "SQL", "JavaScript", "HTML")
                "Cognizant" -> listOf("Java", "SQL", "JavaScript", "Cloud")
                "Accenture" -> listOf("Java", "SQL", "Cloud", "JavaScript")
                "Capgemini" -> listOf("Java", "C++", "SQL", "JavaScript")
                "Wipro" -> listOf("Java", "Python", "SQL", "HTML", "CSS")
                else -> listOf("Java", "SQL", "DSA", "OOPS")
            }
            
            val matchedRequiredCount = requiredSkills.count { it.lowercase() in userSkills }
            val techScore = ((matchedRequiredCount.toFloat() / requiredSkills.size) * 100).toInt().coerceIn(30, 100)
            
            // 2. Coding Readiness
            val baseCoding = (solvedCoding.size * 10).coerceAtMost(100)
            val codingScore = if (isProductTier) {
                (baseCoding * 0.8f + user.technicalScore * 0.2f).toInt().coerceIn(20, 100)
            } else {
                (baseCoding * 0.6f + user.technicalScore * 0.4f).toInt().coerceIn(40, 100)
            }
            
            // 3. Interview Readiness
            val interviewScore = ((interviewConfidence * 0.7f) + (user.employabilityScore * 0.3f)).toInt().coerceIn(30, 100)
            
            // 4. Communication Readiness
            val communicationScore = ((interviewConfidence * 0.6f) + 40f).toInt().coerceIn(50, 95)
            
            // Overall Selection Probability %
            val selectionProbability = (
                (techScore * techWeight) + 
                (codingScore * codingWeight) + 
                (interviewScore * interviewWeight) + 
                (communicationScore * commWeight)
            ).toInt().coerceIn(10, 98)
            
            // Missing Requirements
            val missingReqs = requiredSkills.filter { it.lowercase() !in userSkills }
            
            // Improvement Recommendations
            val recommendations = mutableListOf<String>()
            if (techScore < 70) recommendations.add("Acquire missing core technologies: ${missingReqs.take(2).joinToString(", ")}.")
            if (codingScore < 60) recommendations.add("Solve at least 15 more DSA / dynamic programming challenges in coding preparation center.")
            if (interviewScore < 65) recommendations.add("Simulate mock technical drills and increase interview confidence using the simulator.")
            if (communicationScore < 70) recommendations.add("Practice articulating design patterns aloud using the STAR method for behavioral rounds.")
            if (recommendations.isEmpty()) recommendations.add("Perform advanced mock reviews for this company. You are in excellent readiness shape!")
            
            CompanyReadiness(
                companyName = company,
                readinessPercentage = selectionProbability,
                technicalScore = techScore,
                interviewScore = interviewScore,
                codingScore = codingScore,
                communicationScore = communicationScore,
                missingRequirements = missingReqs,
                improvementRecommendations = recommendations
            )
        }
    }

    // ==========================================
    // CAREER TWIN ENGINE FORECAST
    // ==========================================
    fun generateCareerTwin(user: User, matchingSkills: List<String>, missingSkills: List<String>): CareerTwinProfile {
        val currentJob = user.dreamJob ?: "Software Engineer"
        val userSkills = matchingSkills.take(6)
        
        // Dynamic Future Roles
        val futureRole = when {
            currentJob.contains("Android", true) -> "Lead Mobile Architect"
            currentJob.contains("Data Sci", true) -> "Principal ML Operations Architect"
            currentJob.contains("Full Stack", true) -> "Principal Systems Architect"
            currentJob.contains("AI/ML", true) -> "Director of AI Systems"
            currentJob.contains("Cloud", true) -> "Director of Cloud Operations"
            currentJob.contains("DevOps", true) -> "Lead Platform Architect"
            currentJob.contains("Analyst", true) -> "Director of Business Analytics"
            else -> "Principal Enterprise Architect"
        }
        
        // Skill expansions for futures
        val futureSkills = when {
            currentJob.contains("Android", true) -> listOf("Kotlin Multiplatform", "Gemini Mobile SDK", "Compose Multiplatform", "KMM Cloud integrations")
            currentJob.contains("Data Sci", true) -> listOf("LLM Orchestrator Frameworks", "Vector Databases", "Distributed MLOps", "Agentic AI")
            currentJob.contains("Full Stack", true) -> listOf("Serverless Edge Runtimes", "tRPC GraphQL", "System Scalability Design", "Distributed Locks")
            else -> listOf("Advanced System Architectures", "Multi-Cloud Security", "Enterprise scale microservices", "AI-assisted developer flows")
        }
        
        // Base packages
        val startSal = when {
            currentJob.contains("Android", true) -> 6
            currentJob.contains("Data Sci", true) -> 8
            currentJob.contains("Full Stack", true) -> 7
            currentJob.contains("AI/ML", true) -> 9
            else -> 5
        }
        
        val proj3M = CareerTwinProjection(
            timeline = "3 Months",
            position = "Associate $currentJob",
            predictedSalary = "₹${startSal} LPA",
            skillGrowth = userSkills + futureSkills.take(1),
            employabilityScore = (user.employabilityScore + 8).coerceAtMost(98)
        )
        
        val proj6M = CareerTwinProjection(
            timeline = "6 Months",
            position = "Junior $currentJob",
            predictedSalary = "₹${(startSal * 1.3).toInt()} LPA",
            skillGrowth = userSkills + futureSkills.take(2),
            employabilityScore = (user.employabilityScore + 15).coerceAtMost(98)
        )
        
        val proj1Y = CareerTwinProjection(
            timeline = "1 Year",
            position = "Senior $currentJob",
            predictedSalary = "₹${(startSal * 1.8).toInt()} LPA",
            skillGrowth = userSkills + futureSkills.take(3),
            employabilityScore = (user.employabilityScore + 22).coerceAtMost(98)
        )
        
        val proj2Y = CareerTwinProjection(
            timeline = "2 Years",
            position = futureRole,
            predictedSalary = "₹${(startSal * 2.6).toInt()} LPA",
            skillGrowth = userSkills + futureSkills,
            employabilityScore = 96
        )
        
        val timelineProgress = listOf(
            "Month 0: Current active skills validated locally by NLP parser.",
            "Month 3: Bridged 50% technical skill gaps, created 2 comprehensive projects.",
            "Month 6: Mastered advanced modern frameworks, cleared mock technical rounds.",
            "Year 1: Promoted to Senior Engineering roles with high system architectural values.",
            "Year 2: Expanded into $futureRole managing enterprise scale vector systems."
        )
        
        return CareerTwinProfile(
            currentSkills = matchingSkills.ifEmpty { listOf("Java", "SQL basics", "HTML/CSS") },
            futureSkills = futureSkills,
            growthForecast = "Based on your active learning pace, your placement probability will grow by **45%** in 6 months, scaling your salary potential to **₹${(startSal * 1.8).toInt()} LPA** in 1 year.",
            progressTimeline = timelineProgress,
            projections = listOf(proj3M, proj6M, proj1Y, proj2Y)
        )
    }

    // ==========================================
    // CAREER SUCCESS SIMULATOR ENGINE
    // ==========================================
    fun runSuccessSimulation(targetCompany: String, targetRole: String, studyHoursPerDay: Float, skillLevel: String, missingSkills: List<String>): SuccessSimulationResult {
        // Calculate weeks to ready
        val baseHoursNeeded = when (skillLevel) {
            "Beginner" -> 350
            "Intermediate" -> 200
            else -> 90
        }
        
        val hourlyOffsetForProduct = if (targetCompany in listOf("Google", "Amazon", "Microsoft")) 1.4f else 0.9f
        val hoursNeeded = (baseHoursNeeded * hourlyOffsetForProduct).toInt()
        
        val weeklyHours = studyHoursPerDay * 7
        val weeksToReady = (hoursNeeded / weeklyHours.coerceAtLeast(1f)).toInt().coerceIn(4, 52)
        
        // Probability of Success
        val baseProb = when (skillLevel) {
            "Beginner" -> 45
            "Intermediate" -> 70
            else -> 88
        }
        val hourBonus = ((studyHoursPerDay - 2) * 5).toInt().coerceIn(-15, 12)
        val companyTierPenalty = if (targetCompany in listOf("Google", "Amazon", "Microsoft")) -15 else 5
        val successProb = (baseProb + hourBonus + companyTierPenalty).coerceIn(15, 96)
        
        // Generate required learning path
        val path = listOf(
            "Phase 1 (Weeks 1-${(weeksToReady * 0.3).toInt().coerceAtLeast(1)}): Establish strong foundational syntax in required skills.",
            "Phase 2 (Weeks ${(weeksToReady * 0.3).toInt() + 1}-${(weeksToReady * 0.7).toInt().coerceAtLeast(2)}): Build 2 projects focusing on the target company stack.",
            "Phase 3 (Weeks ${(weeksToReady * 0.7).toInt() + 1}-$weeksToReady): Solve company question banks and perform mock simulator drills."
        )
        
        // Missing Skills
        val simulatedMissing = if (missingSkills.isNotEmpty()) missingSkills else listOf("Advanced System design", "Thread Concurrency API")
        
        // Recommended Projects
        val recommendedProjs = projectsDatabase.filter { 
            it.category.lowercase().contains(targetRole.lowercase().take(4)) || 
            targetRole.lowercase().contains(it.category.lowercase().take(4))
        }.ifEmpty { projectsDatabase.take(3) }
        
        // Progress Forecast timeline
        val timeline = (1..6).map { month ->
            val readiness = ((month / 6f) * successProb + (1f - (month / 6f)) * 30f).toInt().coerceIn(10, 100)
            "Month $month" to readiness
        }
        
        return SuccessSimulationResult(
            timeToReadyWeeks = weeksToReady,
            successProbability = successProb,
            learningPath = path,
            missingSkills = simulatedMissing,
            recommendedProjects = recommendedProjs,
            timelineProgress = timeline
        )
    }

    // ==========================================
    // RECRUITER VIEW MODE ENGINE
    // ==========================================
    fun generateRecruiterDashboard(user: User, matchingSkills: List<String>, missingSkills: List<String>): RecruiterInsights {
        val totalSkills = matchingSkills.size + missingSkills.size
        val compatibility = if (totalSkills > 0) (matchingSkills.size * 100) / totalSkills else 55
        
        val ats = user.atsScore.coerceAtLeast(60)
        
        val resumeStrength = (compatibility * 0.6f + ats * 0.4f).toInt().coerceIn(30, 98)
        
        val weaknesses = mutableListOf<String>()
        if (missingSkills.isNotEmpty()) {
            weaknesses.add("Lacks verified projects in: ${missingSkills.take(2).joinToString(", ")}.")
        } else {
            weaknesses.add("Resume needs more quantifiable business metrics to emphasize impact.")
        }
        if (ats < 75) weaknesses.add("Resume formatting density is high, making it harder for ATS parsing bots.")
        
        val recLevel = when {
            resumeStrength >= 85 -> "Strong Hire"
            resumeStrength in 70..84 -> "Hire"
            resumeStrength in 50..69 -> "Consider"
            else -> "Needs Improvement"
        }
        
        val rating = when {
            resumeStrength >= 85 -> "★ ★ ★ ★ ★"
            resumeStrength in 70..84 -> "★ ★ ★ ★ ☆"
            resumeStrength in 50..69 -> "★ ★ ★ ☆ ☆"
            else -> "★ ★ ☆ ☆ ☆"
        }
        
        val insights = if (resumeStrength >= 80) {
            "Applicant demonstrates outstanding technical matching. High-performing candidate with strong core skills. Recommended for immediate fast-track screening."
        } else {
            "Candidate possesses solid foundations, but has notable technical gaps. Recommend screening after completing targeted training in missing stack areas."
        }
        
        return RecruiterInsights(
            resumeStrength = resumeStrength,
            resumeWeakness = weaknesses,
            atsScore = ats,
            hiringRecommendation = recLevel,
            employabilityRating = rating,
            recruiterInsightsText = insights,
            missingSkills = missingSkills.ifEmpty { listOf("Advanced caching", "Distributed architectures") },
            interviewReadiness = ((user.employabilityScore * 0.6f) + 30).toInt().coerceIn(10, 98)
        )
    }

    // ==========================================
    // PATENT INNOVATION CENTER METRICS
    // ==========================================
    fun calculatePatentMetrics(user: User, matchingSkills: List<String>, missingSkills: List<String>, solvedAptitude: Set<String>, solvedCoding: Set<String>): PatentMetrics {
        val matchingCount = matchingSkills.size
        val totalCount = (matchingSkills.size + missingSkills.size).coerceAtLeast(1)
        val matchPercent = (matchingCount * 100) / totalCount
        
        // 1. Career Risk Score: lower matches = higher risk
        val careerRisk = (100 - matchPercent).coerceIn(10, 92)
        
        // 2. Future Skill Demand Score
        val futureDemand = (matchPercent * 0.7f + (solvedCoding.size * 8) + 20).toInt().coerceIn(30, 98)
        
        // 3. Adaptive Learning Score
        val adaptiveScore = ((solvedAptitude.size + solvedCoding.size) * 12 + 40).coerceIn(40, 96)
        
        // 4. Placement Probability Score
        val placementScore = ((matchPercent * 0.6f) + (user.atsScore * 0.3f) + 10).toInt().coerceIn(15, 96)
        
        // 5. Recruiter Acceptance Score
        val recruiterScore = ((user.atsScore * 0.5f) + (matchPercent * 0.4f) + 10).toInt().coerceIn(20, 98)
        
        return PatentMetrics(
            careerRiskScore = careerRisk,
            futureSkillDemandScore = futureDemand,
            adaptiveLearningScore = adaptiveScore,
            placementProbabilityScore = placementScore,
            recruiterAcceptanceScore = recruiterScore
        )
    }
}
