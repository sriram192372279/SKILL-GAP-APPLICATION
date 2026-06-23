package com.sriram.skillgap.utils

data class CompanyRoleInfo(
    val roleTitle: String,
    val requiredSkills: List<String>,
    val expectedPackage: String,
    val roadmapSteps: List<String>
)

data class HiringRound(
    val roundName: String,
    val difficulty: String, // Easy, Medium, Hard
    val duration: String,
    val commonTopics: List<String>,
    val prepTips: String
)

data class CompanyQuestion(
    val id: String,
    val category: String, // Aptitude, Java, DSA, OOPS, HR, System Design
    val question: String,
    val answer: String,
    val difficulty: String,
    val codeSnippet: String? = null
)

data class CompanyInfo(
    val name: String,
    val logoText: String,
    val overview: String,
    val headquarters: String,
    val type: String, // Product, Service, Consulting, SaaS
    val employeeCount: String,
    val workCulture: String,
    val salaryRange: String,
    val requiredTech: List<String>,
    val eligibilityCriteria: String,
    val hiringRounds: List<HiringRound>,
    val roles: List<CompanyRoleInfo>,
    val questions: List<CompanyQuestion>
)

object CompanyData {
    val companies = listOf(
        CompanyInfo(
            name = "Google",
            logoText = "G",
            overview = "Google LLC is a multinational technology company focusing on artificial intelligence, search engine technology, online advertising, cloud computing, computer software, and quantum computing.",
            headquarters = "Mountain View, California, USA",
            type = "Product / Tech Giant",
            employeeCount = "180,000+",
            workCulture = "High autonomy, focus on innovation (20% project time), flat hierarchy, excellent benefits, and data-driven decision making. High emphasis on psychological safety.",
            salaryRange = "₹18 LPA - ₹55+ LPA (For Freshers)",
            requiredTech = listOf("Java/C++", "Python", "Go", "Kotlin", "TensorFlow", "Kubernetes", "Linux"),
            eligibilityCriteria = "CGPA 7.5+ or equivalent coding excellence, B.Tech/M.Tech in CS or related fields. No active backlogs.",
            hiringRounds = listOf(
                HiringRound("Round 1: Aptitude & Online Assessment", "Hard", "90 Mins", listOf("Advanced DSA", "Graph Algorithms", "Math & Logic puzzles"), "Focus on time management. Google's online assessment is notoriously difficult, with 2 challenging DSA problems."),
                HiringRound("Round 2: Technical Coding Round 1", "Hard", "45 Mins", listOf("Dynamic Programming", "Trees & Graphs", "Complexity Analysis"), "Always communicate your thought process out loud. Do not start coding immediately; discuss the brute force first, then optimize."),
                HiringRound("Round 3: Technical Coding Round 2", "Hard", "45 Mins", listOf("Design Data Structures", "Recursion", "Systematic Edge Cases"), "Think about thread safety and memory footprint. Google interviewers look for clean, modular, bug-free production-grade code."),
                HiringRound("Round 4: Googleyness & Leadership", "Medium", "45 Mins", listOf("Conflict Resolution", "Diversity & Inclusion", "Ambiguous Situations"), "Use the STAR method (Situation, Task, Action, Result). Show how you deal with failure, work in teams, and lead initiatives.")
            ),
            roles = listOf(
                CompanyRoleInfo("Software Engineer", listOf("DSA", "System Design", "C++", "Java"), "₹22 - ₹35 LPA", listOf("Phase 1: Advanced DSA (Leetcode Medium/Hard)", "Phase 2: Master System Design & OS/Networking basics", "Phase 3: Mock interviews with Google engineers")),
                CompanyRoleInfo("AI Engineer", listOf("Python", "Deep Learning", "TensorFlow", "Calculus"), "₹25 - ₹40 LPA", listOf("Phase 1: Statistics & Advanced Calculus", "Phase 2: Deep Learning Models & Fine-Tuning", "Phase 3: Deploy ML models at scale on Cloud")),
                CompanyRoleInfo("Cloud Engineer", listOf("Go", "Kubernetes", "Docker", "GCP"), "₹18 - ₹30 LPA", listOf("Phase 1: Network & Virtualization fundamentals", "Phase 2: GCP Architect Certification curriculum", "Phase 3: Kubernetes cluster management projects")),
                CompanyRoleInfo("Data Analyst", listOf("SQL", "Python", "Tableau", "Statistics"), "₹14 - ₹22 LPA", listOf("Phase 1: Advanced SQL queries & Pandas", "Phase 2: Business metrics & Guesstimates", "Phase 3: Data visualization & executive dashboarding"))
            ),
            questions = listOf(
                CompanyQuestion("G-1", "DSA", "How do you find the longest path in a Directed Acyclic Graph (DAG)?", "To find the longest path in a DAG: 1. Perform a Topological Sort of the graph. 2. Initialize distances to all vertices as negative infinity, and source distance as 0. 3. Process all vertices in topological order. For each vertex, update the distances of all its adjacent vertices if distance[v] < distance[u] + weight(u, v). The complexity will be O(V + E).", "Hard"),
                CompanyQuestion("G-2", "System Design", "Design a globally distributed rate limiter like the one used at Google.", "A globally distributed rate limiter can be designed using: 1. Redis Cluster to maintain token counts in-memory. 2. Sliding Window Log or Sliding Window Counter algorithm to prevent traffic bursts at window boundaries. 3. Leaky Bucket / Token Bucket to smooth out spikes. 4. Local caching at edge locations with synchronization protocols (Gossip protocol) to minimize API latency.", "Hard"),
                CompanyQuestion("G-3", "Aptitude", "You have 8 identical-looking balls, but 1 is slightly heavier. How do you find the heavier ball in exactly 2 weighings using a balance scale?", "1. Divide the 8 balls into groups: 3, 3, and 2.\n2. Weigh the two groups of 3 against each other. Case A: If they balance, the heavier ball is in the group of 2. Weigh the remaining 2 balls against each other to find the heavy one (Weighing #2). Case B: If they don't balance, take the heavier group of 3. Pick any 2 balls and weigh them against each other. If they balance, the unweighed 3rd ball is the heavy one. If they don't balance, the heavier one is revealed.", "Medium"),
                CompanyQuestion("G-4", "Java", "How does ConcurrentHashMap achieve concurrency in Java?", "Prior to Java 8, ConcurrentHashMap used Segment Locking (reentrant lock on 16 buckets). From Java 8 onwards, it uses a node-level lock (using synchronized blocks) on the first node of each bucket list/tree, combined with CAS (Compare-And-Swap) operations for empty buckets. This enables concurrent writes on different buckets without locking the entire map.", "Hard"),
                CompanyQuestion("G-5", "OOPS", "What is the difference between composition and inheritance? Why does Google prefer composition?", "Inheritance is an 'is-a' relationship (tightly coupled, breaks encapsulation if subclass relies on superclass details), while Composition is a 'has-a' relationship (loosely coupled, modular, dynamic at runtime). Google prefers composition because it prevents the fragile base class problem and allows changing component behavior dynamically by injecting different implementations.", "Medium"),
                CompanyQuestion("G-6", "HR", "Tell me about a time you had a technical disagreement with a team member. How did you resolve it?", "Use the STAR method. Answer structure: 'In my college project, my peer wanted to use MongoDB for transaction-heavy data while I advocated for PostgreSQL due to ACID guarantees. I scheduled a call, drew a comparison table focusing on data integrity vs speed, created a small prototype of both, and demonstrated that Postgres prevented double-write anomalies. We agreed on Postgres and finished on time. I learned to keep disagreements objective and data-centric.'", "Medium")
            )
        ),
        CompanyInfo(
            name = "Microsoft",
            logoText = "MS",
            overview = "Microsoft Corporation is an American multinational technology corporation producing computer software, consumer electronics, personal computers, and cloud services.",
            headquarters = "Redmond, Washington, USA",
            type = "Product / Tech Giant",
            employeeCount = "220,000+",
            workCulture = "Growth Mindset (championed by Satya Nadella), highly collaborative, emphasis on learning, work-life balance, and continuous integration of cutting-edge technology.",
            salaryRange = "₹16 LPA - ₹48+ LPA (For Freshers)",
            requiredTech = listOf("C#/.NET", "C++", "TypeScript", "Python", "Azure Cloud", "SQL Server"),
            eligibilityCriteria = "65% or 6.5 CGPA throughout graduation. B.E/B.Tech/M.Tech/MCA/MS. No active backlogs.",
            hiringRounds = listOf(
                HiringRound("Round 1: Codility Online Assessment", "Hard", "90 Mins", listOf("Array Manipulation", "String processing", "Time Complexity optimizations"), "Write clean and highly optimized code. Microsoft's test uses Codility, where performance tests on large datasets carry heavy weight."),
                HiringRound("Round 2: Technical Round 1 (DSA)", "Medium", "45 Mins", listOf("Linked Lists", "Binary Trees", "Sorting & Searching"), "Microsoft interviewers love tree traversal and pointer manipulation. Focus heavily on pointer corner cases (null pointers, single node trees)."),
                HiringRound("Round 3: Technical Round 2 (System/OOPS)", "Hard", "45 Mins", listOf("LLD (Low Level Design)", "Concurrency & Threading", "API design"), "Be ready to write actual class definitions on a whiteboard. Use clean OOPS patterns (SOLID principles)."),
                HiringRound("Round 4: AA (Asappropriate / Director) Round", "Medium", "45 Mins", listOf("System Design", "Cultural Fitment", "Design Thinking"), "This is a bar-raiser round. The director tests your core engineering values, design mindset, and long-term career alignment.")
            ),
            roles = listOf(
                CompanyRoleInfo("Software Engineer", listOf("DSA", "C#", "C++", "System Design"), "₹20 - ₹32 LPA", listOf("Phase 1: Master trees, graphs, and system programming", "Phase 2: Deep dive into SOLID principles & LLD design patterns", "Phase 3: Practice mock system design of products like Teams")),
                CompanyRoleInfo("Cloud Engineer", listOf("Azure", "PowerShell", "Docker", "Linux"), "₹16 - ₹28 LPA", listOf("Phase 1: Cloud administration & virtualization", "Phase 2: Azure cloud architecture certifications", "Phase 3: Infrastructure automation with Terraform")),
                CompanyRoleInfo("Java Developer", listOf("Java", "Spring Boot", "SQL", "Microservices"), "₹15 - ₹26 LPA", listOf("Phase 1: Advanced Java & concurrency frameworks", "Phase 2: Spring Boot API microservices design", "Phase 3: Distributed database indexing")),
                CompanyRoleInfo("Data Analyst", listOf("Excel", "SQL", "Power BI", "DAX"), "₹12 - ₹18 LPA", listOf("Phase 1: Master SQL queries & data cleaning", "Phase 2: Learn Power BI dashboarding & DAX formulas", "Phase 3: Business case studies & presentation skills"))
            ),
            questions = listOf(
                CompanyQuestion("MS-1", "DSA", "Given a binary tree, print its boundary nodes in anti-clockwise direction.", "Boundary traversal consists of: 1. Print the left boundary (excluding leaf nodes). 2. Print all leaf nodes (left to right). 3. Print the right boundary in reverse order (excluding leaf nodes). We use recursive functions to traverse left, leaves, and right. Time complexity is O(N) where N is number of nodes.", "Medium"),
                CompanyQuestion("MS-2", "Java", "What is the difference between final, finally, and finalize in Java?", "1. 'final' is a keyword used as a modifier: a final class cannot be inherited, a final method cannot be overridden, and a final variable's value cannot be reassigned.\n2. 'finally' is a block used in exception handling (try-catch-finally) that executes guaranteed code (e.g., closing resource connections) whether an exception is thrown or not.\n3. 'finalize' is a protected method in the Object class that was historically called by the Garbage Collector before an object is destroyed (now deprecated in modern Java).", "Easy"),
                CompanyQuestion("MS-3", "OOPS", "Explain the SOLID principles with real-world scenarios.", "S: Single Responsibility (A class has only one reason to change, e.g., Invoice generator only generates invoices, not database saves).\nO: Open/Closed (Open for extension, closed for modification, e.g., using Interfaces for payment methods instead of switch-cases).\nL: Liskov Substitution (Derived types must be completely substitutable for base types, e.g., a Square class should not inherit from Rectangle if it violates width/height behaviors).\nI: Interface Segregation (Client should not be forced to implement methods it doesn't use, e.g., split large interfaces into smaller specialized ones).\nD: Dependency Inversion (Depend on abstractions, not concretions, e.g., inject interfaces using Dagger/Hilt or Spring @Autowired).", "Medium"),
                CompanyQuestion("MS-4", "System Design", "Design a collaborative document editing tool like Microsoft Word Online.", "Requires: 1. Operational Transformation (OT) or Conflict-Free Replicated Data Types (CRDTs) to merge concurrent edits from multiple users. 2. WebSockets for low-latency bidirectional real-time communication. 3. Document Broker servers to coordinate synchronization. 4. Redis cache for active documents, and NoSQL document store (like CosmosDB) for persistent saves.", "Hard"),
                CompanyQuestion("MS-5", "HR", "What is your biggest failure and what did you learn from it?", "Structure: State a genuine professional/academic setback, avoid fatal mistakes, and highlight the learning path. 'In my pre-final year, I led a hackathon project. I tried to implement too many complex features instead of focusing on a stable MVP. Due to this, our app crashed during the final demo. We didn't place. I learned the vital importance of prioritization, building robust core MVPs first, and leaving ample time for testing. Since then, I plan my sprints using strict MoSCoW prioritization.'", "Easy")
            )
        ),
        CompanyInfo(
            name = "Amazon",
            logoText = "AMZ",
            overview = "Amazon.com, Inc. is an American multinational technology company focusing on e-commerce, cloud computing (AWS), online advertising, digital streaming, and artificial intelligence.",
            headquarters = "Seattle, Washington, USA",
            type = "Product / Tech Giant",
            employeeCount = "1,500,000+",
            workCulture = "Customer obsession, high frugality, ownership mindset, and rapid delivery loops. Driven strongly by the '16 Leadership Principles' (LP). Known for the 'Two-Pizza Team' rule.",
            salaryRange = "₹15 LPA - ₹45+ LPA (For Freshers)",
            requiredTech = listOf("Java", "C++", "Python", "AWS (EC2, S3, DynamoDB)", "SQL", "NoSQL"),
            eligibilityCriteria = "CGPA 6.5+ or 65% with B.Tech/B.E/MCA/M.Tech degree. Active backlogs are not allowed.",
            hiringRounds = listOf(
                HiringRound("Round 1: Online Assessment (OA)", "Hard", "120 Mins", listOf("DSA (2 Questions)", "Work Style Assessment", "System Logic"), "The Work Style Assessment evaluates you directly on Amazon's 16 Leadership Principles. Study them closely before sitting for the test."),
                HiringRound("Round 2: Technical Interview 1 (DSA)", "Hard", "60 Mins", listOf("LRU Cache", "Graphs (Dijkstra/BFS)", "Heaps & Priority Queues"), "First 15 minutes will be situational questions based on Amazon Leadership Principles. Make sure you have projects prepared that align with these."),
                HiringRound("Round 3: Technical Interview 2 (System Design)", "Hard", "60 Mins", listOf("System Design", "Scalability", "Load Balancing", "Databases"), "Focus on scale. Amazon deals with billions of requests. Understand database sharding, asynchronous queues (SQS), and caching."),
                HiringRound("Round 4: Bar Raiser Round", "Hard", "60 Mins", listOf("Leadership Principles Deep Dive", "Complex coding challenges", "Failure analysis"), "The Bar Raiser is an independent interviewer who ensures you raise the hiring standard. Focus heavily on 'Customer Obsession' and 'Deliver Results'.")
            ),
            roles = listOf(
                CompanyRoleInfo("Software Engineer", listOf("DSA", "Java", "System Design", "AWS"), "₹18 - ₹30 LPA", listOf("Phase 1: Master Graphs, Dynamic Programming, and Heap algorithms", "Phase 2: Study microservices architecture, SQS, DynamoDB at scale", "Phase 3: Prepare 15 structured STAR stories based on 16 LPs")),
                CompanyRoleInfo("Cloud Engineer", listOf("AWS", "Linux", "Terraform", "Python"), "₹15 - ₹25 LPA", listOf("Phase 1: Deep dive into AWS Core Services (EC2, VPC, IAM, S3)", "Phase 2: Master infrastructure automation with Terraform and CloudFormation", "Phase 3: Study disaster recovery and multi-region scaling")),
                CompanyRoleInfo("Full Stack Developer", listOf("React", "Node.js", "DynamoDB", "AWS"), "₹16 - ₹27 LPA", listOf("Phase 1: React & Next.js frontend state optimization", "Phase 2: Node.js serverless REST APIs on AWS Lambda", "Phase 3: Distributed sessions and caching")),
                CompanyRoleInfo("AI Engineer", listOf("Python", "SageMaker", "NLP", "Statistics"), "₹20 - ₹34 LPA", listOf("Phase 1: Machine learning statistical modeling & evaluation", "Phase 2: Deploy ML pipelines using AWS SageMaker", "Phase 3: Integrate LLMs and vector search"))
            ),
            questions = listOf(
                CompanyQuestion("AMZ-1", "DSA", "Design and implement a Least Recently Used (LRU) Cache.", "An LRU Cache can be implemented using: 1. A Doubly Linked List to keep track of the access order (most recently used at head, least recently used at tail). 2. A Hash Map storing keys mapped to the corresponding node in the Doubly Linked List, ensuring O(1) lookups. During a GET operation, we move the node to the head. During a PUT operation, if the key exists, update value and move to head. If cache is full, delete tail node, remove it from map, insert new node at head.", "Hard"),
                CompanyQuestion("AMZ-2", "System Design", "Design a package shipping and tracking dashboard like Amazon logistics system.", "Components: 1. Package Ingestion API backed by Kafka to queue tracking updates. 2. Write-heavy tracking service saving status to DynamoDB (key-value schema optimized for fast updates by package ID). 3. Read-heavy Customer API served via Redis Cache. 4. Location Tracking Service that aggregates coordinates using Geohashes to track delivery agents in real-time.", "Hard"),
                CompanyQuestion("AMZ-3", "HR", "Explain Amazon's 'Customer Obsession' with a personal example.", "Example: 'In my college project, we designed a library management app. Our initial user testing showed that senior students found the search interface confusing, as it required entering book ISBNs. Even though the database schema was designed for ISBN indexing, I realized the customer pain. I rewrote the search service to query using regex on title, author, and subjects, and added fuzzy matching. This increased student database queries by 60%. I learned that customer convenience always supercedes database convenience.'", "Medium"),
                CompanyQuestion("AMZ-4", "Java", "What is the difference between ArrayList and LinkedList in Java? Which is better for what operations?", "ArrayList is backed by a dynamic resizing array, providing O(1) search by index but O(N) inserts/deletes in the middle due to element shifting. LinkedList is backed by a doubly linked list, providing O(N) search but O(1) inserts/deletes once the node is located. For modern JVMs, ArrayList is almost always preferred due to Cache Locality (elements are adjacent in memory, minimizing CPU cache misses).", "Easy")
            )
        ),
        CompanyInfo(
            name = "TCS",
            logoText = "TCS",
            overview = "Tata Consultancy Services is an Indian multinational information technology services and consulting company. It is a subsidiary of the Tata Group and operates in 150 locations across 46 countries.",
            headquarters = "Mumbai, Maharashtra, India",
            type = "Service / IT Consulting",
            employeeCount = "600,000+",
            workCulture = "Stable career paths, strong ethical values, focus on long-term relationship building, structured training programs (TCS Initial Learning Program - ILP), and high job security.",
            salaryRange = "₹3.36 LPA (Ninja) - ₹7.0+ LPA (Digital/Prime)",
            requiredTech = listOf("Java/Python", "SQL", "C/C++", "HTML/CSS/JS", "SDLC Fundamentals"),
            eligibilityCriteria = "CGPA 6.0+ or 60% throughout 10th, 12th, and B.Tech. Maximum of 1 active backlog allowed at the time of exam.",
            hiringRounds = listOf(
                HiringRound("Round 1: TCS NQT (National Qualifier Test)", "Medium", "180 Mins", listOf("Quantitative Aptitude", "Verbal Ability", "Logical Reasoning", "Basic Coding (2 questions)"), "TCS NQT has strict section-wise timing. You cannot go back to previous questions. Speed and accuracy in aptitude are key."),
                HiringRound("Round 2: Technical Interview", "Easy", "30 Mins", listOf("Java/Python syntax", "OOPS Concepts", "SQL Joins", "Project details"), "Be prepared to write basic code on paper (Fibonacci, Palindrome, Reverse String) and explain your final year project."),
                HiringRound("Round 3: Managerial Interview", "Easy", "20 Mins", listOf("Real-world Scenarios", "Teamwork experiences", "Tech adaptability"), "This round evaluates your flexibility. Be ready to discuss how you would learn new technologies (like Cloud or AI) if assigned."),
                HiringRound("Round 4: HR Round", "Easy", "15 Mins", listOf("Tata Values", "Relocation agreement", "Shift flexibility"), "HR checks communication skills, willingness to relocate across India, and confirmation of night shifts.")
            ),
            roles = listOf(
                CompanyRoleInfo("Software Engineer", listOf("Java", "SQL", "HTML/CSS", "SDLC"), "₹3.6 - ₹7.0 LPA", listOf("Phase 1: Master Java/Python basic programming and OOPs", "Phase 2: Master SQL database joins and writing simple scripts", "Phase 3: Study TCS NQT previous aptitude papers")),
                CompanyRoleInfo("Java Developer", listOf("Java", "OOPs", "JDBC", "Spring Boot"), "₹4.0 - ₹8.0 LPA", listOf("Phase 1: Strong Core Java concepts (Multi-threading, Collections)", "Phase 2: JDBC database connections & Servlet basics", "Phase 3: Basic REST APIs using Spring Boot")),
                CompanyRoleInfo("Data Analyst", listOf("Excel", "SQL", "Power BI"), "₹3.5 - ₹6.5 LPA", listOf("Phase 1: Intermediate Excel (Pivot tables, formulas)", "Phase 2: Structured SQL queries & aggregate functions", "Phase 3: Basic data reporting layouts"))
            ),
            questions = listOf(
                CompanyQuestion("TCS-1", "Aptitude", "A train 120m long passes a platform 180m long in 15 seconds. What is the speed of the train in km/h?", "1. Total distance = Train length + Platform length = 120m + 180m = 300m.\n2. Speed in m/s = Distance / Time = 300m / 15s = 20 m/s.\n3. Convert m/s to km/h by multiplying with 18/5: 20 * (18 / 5) = 4 * 18 = 72 km/h.", "Easy"),
                CompanyQuestion("TCS-2", "Java", "What are the four pillars of OOPS? Explain with simple examples.", "1. Encapsulation: Wrapping data (variables) and code (methods) together into a single unit (e.g., a Class with private fields and public getter/setter methods).\n2. Inheritance: Acquiring properties of a parent class (e.g., a 'Car' class inherits from 'Vehicle' class using the 'extends' keyword).\n3. Polymorphism: Performing a single task in different ways. Overloading (static: same method name, different arguments) and Overriding (dynamic: subclass replaces superclass method).\n4. Abstraction: Hiding internal details and showing only functionality (e.g., using Abstract classes or Interfaces).", "Easy"),
                CompanyQuestion("TCS-3", "DSA", "Write a program to reverse a string without using built-in reverse functions.", "We can reverse a string in Java using a character array and a two-pointer approach:\n```java\npublic String reverse(String str) {\n    char[] arr = str.toCharArray();\n    int left = 0, right = arr.length - 1;\n    while(left < right) {\n        char temp = arr[left];\n        arr[left] = arr[right];\n        arr[right] = temp;\n        left++; right--;\n    }\n    return new String(arr);\n}\n```\nTime complexity is O(N) and space complexity is O(N) to store the character array.", "Easy"),
                CompanyQuestion("TCS-4", "HR", "Why do you want to join TCS? What do you know about our company?", "Answer: 'I want to join TCS because it is a global pioneer in IT services and is renowned for its highly ethical work culture and investment in fresher development. I've read about TCS's Initial Learning Program (ILP), which provides an exceptional launchpad for graduates. TCS's commitment to CSR and social initiatives is inspiring. I am excited to contribute my coding skills in an ecosystem that scales technology globally while holding strong values.'", "Easy")
            )
        ),
        CompanyInfo(
            name = "Infosys",
            logoText = "INF",
            overview = "Infosys Limited is an Indian multinational information technology company that provides business consulting, information technology and outsourcing services.",
            headquarters = "Bengaluru, Karnataka, India",
            type = "Service / IT Consulting",
            employeeCount = "330,000+",
            workCulture = "Heavy emphasis on continuous learning (Infosys Lex platform), iconic training campus at Mysore, structural training programs, and highly professional delivery processes.",
            salaryRange = "₹3.6 LPA (System Engineer) - ₹9.5 LPA (Specialist Programmer)",
            requiredTech = listOf("Java/C++", "Python", "SQL", "JavaScript", "HTML/CSS", "Cloud basics"),
            eligibilityCriteria = "60% or CGPA 6.0 throughout 10th, 12th, and Graduation. No active backlogs are allowed.",
            hiringRounds = listOf(
                HiringRound("Round 1: Infosys Online Test (InfyTQ / HackWithInfy)", "Medium", "150 Mins", listOf("Data Structures", "Python/Java Programming", "DBMS & SQL"), "InfyTQ uses hands-on coding tests. Prepare thoroughly in OOPs and writing complex database SQL queries."),
                HiringRound("Round 2: Technical Interview", "Medium", "45 Mins", listOf("Core Java / OOPs", "SQL Joins & Indexing", "Project Deep Dive"), "Be ready to explain the database schema of your project. Write standard SQL queries on the screen or paper."),
                HiringRound("Round 3: HR & Communication Round", "Easy", "20 Mins", listOf("Communication evaluation", "Relocation", "Infosys core values"), "Infosys evaluates soft skills heavily. Speak clearly, logically, and confirm flexibility to relocate to any center (especially Mysore/Bangalore).")
            ),
            roles = listOf(
                CompanyRoleInfo("Software Engineer", listOf("Java", "SQL", "OOPs", "HTML/CSS"), "₹3.6 - ₹5.5 LPA", listOf("Phase 1: Complete OOPs concepts in Java/Python", "Phase 2: Master SQL database constraints and basic queries", "Phase 3: Build a simple web application using JDBC")),
                CompanyRoleInfo("Java Developer", listOf("Java", "Spring Boot", "MySQL", "Rest API"), "₹4.5 - ₹8.5 LPA", listOf("Phase 1: Core Java collections framework and threading", "Phase 2: Spring Boot MVC and Hibernate", "Phase 3: Setup database relationships and index optimizations")),
                CompanyRoleInfo("Data Analyst", listOf("SQL", "Excel", "Tableau"), "₹3.8 - ₹6.8 LPA", listOf("Phase 1: Master SQL window functions and aggregations", "Phase 2: Build Tableau metrics dashboards", "Phase 3: Clean messy data profiles using Python"))
            ),
            questions = listOf(
                CompanyQuestion("INF-1", "Java", "What is the difference between JVM, JRE, and JDK in Java?", "1. JVM (Java Virtual Machine) is an abstract machine that runs compiled bytecodes (.class files). It is platform-dependent.\n2. JRE (Java Runtime Environment) consists of the JVM + Core Libraries needed to execute Java programs. It provides only runtime features.\n3. JDK (Java Development Kit) is the full development package containing JRE + Development tools (javac compiler, debugger, etc.). Every Java developer needs the JDK.", "Easy"),
                CompanyQuestion("INF-2", "OOPS", "Explain the difference between Method Overloading and Method Overriding in Java.", "Method Overloading (Compile-time Polymorphism) occurs in the same class when multiple methods have the same name but different signatures (parameters count/types). Method Overriding (Run-time Polymorphism) occurs when a subclass provides a specific implementation of a method already declared in its superclass, having the same name, return type, and parameters, using @Override annotation.", "Easy"),
                CompanyQuestion("INF-3", "Aptitude", "If 5 workers can build a wall in 8 days, how many workers are needed to build the same wall in 4 days?", "1. Calculate total work in worker-days: 5 workers * 8 days = 40 worker-days.\n2. To complete 40 worker-days of work in 4 days, divide total work by target days: 40 / 4 = 10 workers.\n3. So, 10 workers are needed to build the wall in 4 days.", "Easy"),
                CompanyQuestion("INF-4", "HR", "What are your strengths and weaknesses?", "Formula: State a positive professional strength with an example, and a minor, non-critical weakness that you are actively working to improve. 'My major strength is my fast learning agility; I mastered Spring Boot in 2 weeks for our final project. My weakness is that I sometimes hesitate to delegate tasks in team projects because I want them done perfectly. I'm actively working on this by using Jira boards to distribute responsibilities clearly and trusting my peers.'", "Easy")
            )
        ),
        CompanyInfo(
            name = "Cognizant",
            logoText = "CTS",
            overview = "Cognizant Technology Solutions is an American multinational information technology services and consulting company providing IT, consulting, and business process outsourcing.",
            headquarters = "Teaneck, New Jersey, USA",
            type = "Service / IT Consulting",
            employeeCount = "350,000+",
            workCulture = "Collaborative, client-centric, focus on skill development, vast domain exposure, structured rewards, and progressive tech training schemes.",
            salaryRange = "₹4.01 LPA (GenC) - ₹12.0+ LPA (GenC Pro/Next)",
            requiredTech = listOf("Java/C#", "Python", "SQL", "JavaScript", "Cloud Foundations"),
            eligibilityCriteria = "Minimum 60% in 10th, 12th, and B.Tech. No active backlogs. 1-year gap allowed.",
            hiringRounds = listOf(
                HiringRound("Round 1: AMCAT Aptitude & Coding", "Medium", "120 Mins", listOf("Quantitative Math", "Automata Fix Coding", "Logical Reasoning"), "Prepare with AMCAT previous test formats. Cognizant uses AMCAT, which contains debugging coding rounds (Automata Fix) where you must correct buggy code."),
                HiringRound("Round 2: Technical Interview", "Medium", "40 Mins", listOf("Java/C# OOPS", "SQL queries (Subqueries)", "Web basics"), "Focus on HTML/CSS/JS and database keys. Be ready to explain your coding solutions from the online assessment."),
                HiringRound("Round 3: HR Round", "Easy", "15 Mins", listOf("Verification of documents", "Relocation", "Shift allowance"), "HR verifies academic eligibility certificates and explains the salary packages, training streams, and relocation policies.")
            ),
            roles = listOf(
                CompanyRoleInfo("Software Engineer", listOf("Java", "SQL", "JavaScript", "OOPS"), "₹4.0 - ₹6.5 LPA", listOf("Phase 1: Master OOPS & SQL database keys", "Phase 2: Master standard arrays & strings coding questions", "Phase 3: Study debugging & troubleshooting techniques")),
                CompanyRoleInfo("Full Stack Developer", listOf("React", "Node.js", "MongoDB", "CSS"), "₹5.0 - ₹9.0 LPA", listOf("Phase 1: Build robust frontend layouts in HTML/CSS/JS", "Phase 2: Connect servers with Node & Express", "Phase 3: Database design constraints")),
                CompanyRoleInfo("Cybersecurity Analyst", listOf("Security+", "Wireshark", "Linux"), "₹4.5 - ₹8.0 LPA", listOf("Phase 1: Study TCP/IP networking protocols", "Phase 2: Learn Wireshark packet capture analysis", "Phase 3: Basic vulnerability scanning"))
            ),
            questions = listOf(
                CompanyQuestion("CTS-1", "Java", "What is the difference between String, StringBuilder, and StringBuffer in Java?", "1. String is immutable (once created, its value cannot be modified in the String Constant Pool). Modifying creates a new String object.\n2. StringBuilder is mutable, allowing modification of string content without creating new objects. It is non-thread-safe (faster, used for single-threaded tasks).\n3. StringBuffer is mutable and thread-safe (methods are synchronized, slower than StringBuilder, used in multi-threaded environments).", "Medium"),
                CompanyQuestion("CTS-2", "OOPS", "What is an abstract class vs interface in Java?", "An abstract class can have instance variables, constructors, concrete methods, and abstract methods, allowing partial implementation. A subclass inherits using 'extends'. An interface (prior to Java 8) could only have constant variables and abstract methods (from Java 8, it can have default/static methods). A class implements using 'implements' and supports multiple inheritance, which abstract classes do not.", "Easy"),
                CompanyQuestion("CTS-3", "DSA", "Write a query to find the second highest salary of an employee from an Employee table.", "Using SQL: \n```sql\nSELECT MAX(Salary) \nFROM Employee \nWHERE Salary < (SELECT MAX(Salary) FROM Employee);\n```\nAlternatively, using LIMIT and OFFSET:\n```sql\nSELECT Salary \nFROM Employee \nORDER BY Salary DESC \nLIMIT 1 OFFSET 1;\n```", "Easy"),
                CompanyQuestion("CTS-4", "HR", "Are you comfortable with night shifts and relocating?", "Answer: 'Yes, I am completely comfortable with relocating and working in night shifts. As a fresher, my primary goal is to learn and contribute to projects under the guidance of experienced professionals. Relocating to a new city is a great opportunity to adapt to new environments and build independence, and working in night shifts will help me support global clients directly.'", "Easy")
            )
        ),
        CompanyInfo(
            name = "Accenture",
            logoText = "ACN",
            overview = "Accenture plc is a Dublin-based multinational professional services company specializing in information technology services and consulting.",
            headquarters = "Dublin, Ireland",
            type = "Consulting / Service Giant",
            employeeCount = "730,000+",
            workCulture = "Inclusion, client excellence, intense learning, technological agility, corporate professionalism, and structured team coordination setups.",
            salaryRange = "₹4.5 LPA (ASE) - ₹6.5 LPA (FSE / SE)",
            requiredTech = listOf("Java/C#", "SQL", "Cloud Basics", "JavaScript", "Critical Logic"),
            eligibilityCriteria = "CGPA 6.0+ or 60% with no active backlogs. Up to 1-year academic gap allowed.",
            hiringRounds = listOf(
                HiringRound("Round 1: Cognitive & Technical Assessment", "Medium", "90 Mins", listOf("Cognitive ability", "Pseudo-code analysis", "MS Office & Cloud basics"), "Cognitive section tests logic, verbal, and math. The technical section contains pseudo-code questions where you must predict outputs."),
                HiringRound("Round 2: Online Coding Test", "Medium", "45 Mins", listOf("Arrays", "String manipulation", "Loops & conditions"), "Two coding problems of easy-to-medium difficulty. Clearing this round unlocks SE / Advanced ASE eligibility."),
                HiringRound("Round 3: Communication Assessment", "Easy", "20 Mins", listOf("Reading sentence aloud", "Listening and repeating", "Story retelling"), "Fully automated AI voice round. Tests English pronunciation, sentence syntax, fluency, and active listening capabilities."),
                HiringRound("Round 4: Technical & HR HR Interview", "Easy", "25 Mins", listOf("Project discussion", "Situational case questions", "Behavioral logic"), "A conversational interview focusing on your teamwork, leadership roles in college, and technical challenges faced in your project.")
            ),
            roles = listOf(
                CompanyRoleInfo("Software Engineer", listOf("Java", "SQL", "Cloud basics", "Python"), "₹4.5 - ₹7.0 LPA", listOf("Phase 1: Command Java fundamentals & data types", "Phase 2: Master SQL aggregations & data query parameters", "Phase 3: Learn basic cloud deployment concepts")),
                CompanyRoleInfo("Full Stack Developer", listOf("React", "Node.js", "Express", "Bootstrap"), "₹5.5 - ₹9.0 LPA", listOf("Phase 1: Dynamic frontend development using React", "Phase 2: Rest API endpoint development with Node & Express", "Phase 3: Project deployment steps")),
                CompanyRoleInfo("Data Analyst", listOf("SQL", "Excel", "Power BI"), "₹4.2 - ₹6.8 LPA", listOf("Phase 1: Command Excel pivot tables & lookups", "Phase 2: Connect databases to Power BI dashboards", "Phase 3: Clean data structures with SQL"))
            ),
            questions = listOf(
                CompanyQuestion("ACN-1", "Java", "What is garbage collection in Java? How do you trigger it manually?", "Garbage Collection (GC) is Java's automatic memory management process that identifies and destroys unreferenced objects in the Heap area, freeing up memory. In Java, GC is managed by the JVM. You can request GC manually using `System.gc()` or `Runtime.getRuntime().gc()`, but there is no guarantee that the JVM will run it immediately, as it is controlled by the JVM's scheduling thread.", "Easy"),
                CompanyQuestion("ACN-2", "Aptitude", "If a man sells an item for ₹240 making a profit of 20%, what was the cost price of the item?", "1. Selling Price (SP) = Cost Price (CP) * (1 + Profit%)\n2. 240 = CP * (1 + 0.20)\n3. 240 = CP * 1.2\n4. CP = 240 / 1.2 = ₹200.\n5. Therefore, the Cost Price was ₹200.", "Easy"),
                CompanyQuestion("ACN-3", "OOPS", "Explain method overriding vs method overloading with a single code sample.", "```java\nclass Calculator {\n    // Method Overloading (Static Polymorphism)\n    int add(int a, int b) { return a + b; }\n    double add(double a, double b) { return a + b; }\n}\nclass AdvancedCalculator extends Calculator {\n    // Method Overriding (Dynamic Polymorphism)\n    @Override\n    int add(int a, int b) { return a + b + 10; } // modified behavior\n}\n```", "Medium"),
                CompanyQuestion("ACN-4", "HR", "Describe a situation where you had to work with a difficult team member.", "Answer: 'In our pre-final semester project, one member was repeatedly missing deadlines for his module, which delayed our integration. Instead of complaining to the professor, I had a private call with him. I learned he was struggling with writing SQL databases and felt overwhelmed. I helped him map out the schema, shared standard tutorials, and paired with him for 3 days. We completed the project on time. I learned that what looks like resistance is often just confusion, and empathy is vital in teams.'", "Medium")
            )
        ),
        CompanyInfo(
            name = "Wipro",
            logoText = "WIP",
            overview = "Wipro Limited is an Indian multinational corporation that provides information technology, consultant and business process services.",
            headquarters = "Bengaluru, Karnataka, India",
            type = "Service / IT Consulting",
            employeeCount = "250,000+",
            workCulture = "Inclusive, focus on customer satisfaction, structural career programs (Wipro Elite, Wipro Turbo), community development (Azim Premji Foundation values), and work life integrity.",
            salaryRange = "₹3.5 LPA (Elite) - ₹6.5 LPA (Turbo)",
            requiredTech = listOf("Java/Python", "SQL", "C/C++", "HTML/CSS/JS", "Logical Reason"),
            eligibilityCriteria = "CGPA 6.0 or 60% in 10th, 12th, and Graduation. Up to 1 active backlog allowed at time of test.",
            hiringRounds = listOf(
                HiringRound("Round 1: Wipro Elite Assessment", "Medium", "120 Mins", listOf("Quantitative Math", "Essay Writing", "Basic Coding"), "The essay writing section is evaluated by an automated AI checker. Ensure correct spelling, punctuation, and use standard corporate words."),
                HiringRound("Round 2: Technical Interview", "Easy", "30 Mins", listOf("Core programming", "OOPs principles", "Database keys"), "Be ready to write basic string manipulation programs like palindrome, anagram, and explain primary vs foreign keys in databases."),
                HiringRound("Round 3: HR Interview", "Easy", "15 Mins", listOf("Relocation", "3-year service agreement details", "Communication"), "Verification of certificates, discussion of Wipro service agreements, shift timings, and communication readiness checks.")
            ),
            roles = listOf(
                CompanyRoleInfo("Software Engineer", listOf("Java", "SQL", "HTML/CSS"), "₹3.5 - ₹5.5 LPA", listOf("Phase 1: Basic programming syntax & OOPs in Java/Python", "Phase 2: Master SQL database constraints and queries", "Phase 3: Study Wipro Elite previous test papers")),
                CompanyRoleInfo("Java Developer", listOf("Java", "JDBC", "Spring Boot"), "₹4.0 - ₹7.0 LPA", listOf("Phase 1: Java collections, standard arrays & strings", "Phase 2: JDBC setup & Servlets basics", "Phase 3: Spring Boot REST endpoint integrations")),
                CompanyRoleInfo("Data Analyst", listOf("Excel", "SQL", "Tableau"), "₹3.6 - ₹6.0 LPA", listOf("Phase 1: Master Excel formulas and formatting", "Phase 2: Write database queries with aggregate functions", "Phase 3: Basic reporting dashboard designs"))
            ),
            questions = listOf(
                CompanyQuestion("WIP-1", "Java", "Explain final vs finally in Java.", "final is a keyword used as a modifier: a final class cannot be inherited, a final method cannot be overridden, and a final variable cannot be reassigned. finally is a block used in exception handling (try-catch-finally) that executes guaranteed code (like closing resource connections) whether an exception is thrown or not.", "Easy"),
                CompanyQuestion("WIP-2", "Aptitude", "Find the odd one out of the series: 3, 5, 7, 12, 17, 19.", "All numbers in the series (3, 5, 7, 17, 19) are prime numbers, except 12, which is a composite number. Thus, 12 is the odd one out.", "Easy"),
                CompanyQuestion("WIP-3", "DSA", "Write a Java program to check if a string is a palindrome.", "```java\npublic boolean isPalindrome(String str) {\n    int left = 0, right = str.length() - 1;\n    while(left < right) {\n        if(str.charAt(left) != str.charAt(right)) {\n            return false;\n        }\n        left++; right--;\n    }\n    return true;\n}\n```", "Easy"),
                CompanyQuestion("WIP-4", "HR", "How do you handle work-related stress?", "Answer: 'I handle work-related stress by prioritizing my tasks using a structured checklist and focusing on one item at a time. I break down large, overwhelming tasks into smaller, manageable chunks. I also take short 5-minute walks to clear my mind and practice active breathing. In college, when we had overlapping exams and project submissions, keeping a strict Google Calendar schedule helped me manage everything without burning out.'", "Easy")
            )
        ),
        CompanyInfo(
            name = "Capgemini",
            logoText = "CAP",
            overview = "Capgemini SE is a French multinational information technology services and consulting company, headquartered in Paris, France.",
            headquarters = "Paris, France",
            type = "Service / IT Consulting",
            employeeCount = "360,000+",
            workCulture = "Collaborative, freedom, trust, corporate ethics, multigeographic team environments, and professional career scaling schemes.",
            salaryRange = "₹4.0 LPA (A4) - ₹7.5 LPA (Analyst / Senior)",
            requiredTech = listOf("Java/C++", "SQL", "JavaScript", "Pseudo-coding", "Logics"),
            eligibilityCriteria = "CGPA 6.0 or 60% in B.Tech/M.Tech/MCA. Max 1 active backlog. Up to 1-year academic gap allowed.",
            hiringRounds = listOf(
                HiringRound("Round 1: Pseudo Code & MCQ Test", "Medium", "90 Mins", listOf("Pseudo code logic", "Data Structures MCQs", "Networking basics"), "Pseudo code questions are highly logic-based, tracing loops, bitwise operations, and array boundaries. Speed is critical."),
                HiringRound("Round 2: English Communication Round", "Easy", "30 Mins", listOf("Grammar MCQs", "Sentence corrections", "Active speech"), "Automated English assessment testing standard grammar rules, spelling corrections, and vocabulary skills."),
                HiringRound("Round 3: Game-based Aptitude Test", "Medium", "30 Mins", listOf("Symmetrical puzzles", "Grid challenges", "Digit challenges"), "Includes rapid cognitive puzzles testing logic, response speed, memory, and cognitive load management."),
                HiringRound("Round 4: Technical & HR Combined Interview", "Easy", "30 Mins", listOf("Project schema", "Core OOPS questions", "General behavioral"), "A conversational review of your CV details, project frameworks, basic coding syntax, and standard HR questions.")
            ),
            roles = listOf(
                CompanyRoleInfo("Software Engineer", listOf("Java", "SQL", "JavaScript", "HTML/CSS"), "₹4.0 - ₹6.5 LPA", listOf("Phase 1: Master programming syntax & OOPS principles", "Phase 2: Master SQL joins & primary/foreign keys", "Phase 3: Basic web development principles")),
                CompanyRoleInfo("Full Stack Developer", listOf("React", "Node.js", "SQL"), "₹5.0 - ₹8.5 LPA", listOf("Phase 1: Dynamic web UI development with React", "Phase 2: Backend integration with database connections", "Phase 3: Dynamic state management")),
                CompanyRoleInfo("Java Developer", listOf("Java", "JDBC", "Spring Boot"), "₹4.2 - ₹7.5 LPA", listOf("Phase 1: Advanced core Java classes, structures, and exceptions", "Phase 2: JDBC setup & servlet APIs", "Phase 3: Basic REST APIs using Spring Boot"))
            ),
            questions = listOf(
                CompanyQuestion("CAP-1", "Java", "What is the difference between dynamic binding and static binding in Java?", "Static binding occurs at compile time and is resolved by the compiler. It is used for private, final, static methods and variables. Dynamic binding occurs at runtime and is resolved by the JVM, which decides the method to call based on the actual object type during inheritance.", "Medium"),
                CompanyQuestion("CAP-2", "OOPS", "Explain interfaces vs abstract classes in Java. When to use what?", "Use interfaces to define a contract or common behavior across completely unrelated classes (e.g. Runnable, Serializable). Use abstract classes when you want to share common code, state, and partial implementations among highly related classes (e.g., base Class 'Vehicle' for Car, Truck).", "Easy"),
                CompanyQuestion("CAP-3", "Aptitude", "Find the sum of all natural numbers from 1 to 50.", "1. Formula for the sum of first N natural numbers = N * (N + 1) / 2.\n2. Here, N = 50.\n3. Sum = 50 * (50 + 1) / 2 = 25 * 51 = 1275.\n4. So, the sum of numbers from 1 to 50 is 1275.", "Easy"),
                CompanyQuestion("CAP-4", "HR", "How do you handle criticism from senior team members?", "Answer: 'I handle criticism with an open, constructive mindset. I separate personal feelings from professional growth. In college, during our final year project review, our professor criticized our UI design, pointing out it was hard to navigate. Instead of getting defensive, I took notes, asked for specific improvements, redesigning the navigation menu with my team. The revised prototype was highly praised. I view feedback as a shortcut to learning.'", "Easy")
            )
        ),
        CompanyInfo(
            name = "Zoho",
            logoText = "Z",
            overview = "Zoho Corporation is an Indian multinational technology company that makes web-based business tools and SaaS software suites.",
            headquarters = "Chennai, Tamil Nadu, India",
            type = "Product / SaaS Pioneer",
            employeeCount = "15,000+",
            workCulture = "Non-corporate, flat, startup-like, high focus on practical skill over academic CGPA, self-reliance, and direct product development exposure.",
            salaryRange = "₹6.5 LPA - ₹15+ LPA (For Freshers)",
            requiredTech = listOf("Java/C", "JavaScript", "HTML/CSS", "OOPs", "Data Structures"),
            eligibilityCriteria = "No minimum CGPA required. Open to all degrees. Purely evaluated based on programming test rounds.",
            hiringRounds = listOf(
                HiringRound("Round 1: Written Programming Test", "Medium", "180 Mins", listOf("C / Java outputs", "Basic math puzzles", "Pointer tracing"), "Features heavy dry-running of complex loops, nested pointers, and writing basic algorithms on paper in C or Java."),
                HiringRound("Round 2: Advanced Programming Test", "Hard", "180 Mins", listOf("Design dynamic tables", "Text Editors", "Matrix operations"), "You must build a complete mini-utility (e.g. Call Taxi Booking, Railroad booking system) from scratch with all logic in Java/C++ in 3 hours."),
                HiringRound("Round 3: Technical LLD / Design Round", "Hard", "60 Mins", listOf("LLD Design patterns", "Recursion", "In-memory database setups"), "Reviewing your code from Round 2. You will be asked to add new complex features and optimize the classes on the screen."),
                HiringRound("Round 4: General Technical / HR Round", "Medium", "30 Mins", listOf("Technical alignment", "Work style values", "Product ideas"), "Zoho tests your real passion for building software, direct product interest, and adaptability in self-reliant team cultures.")
            ),
            roles = listOf(
                CompanyRoleInfo("Software Engineer", listOf("DSA", "Java", "C", "OOPS"), "₹6.5 - ₹12.0 LPA", listOf("Phase 1: Master pointer tracing, recursion, and core array manipulation", "Phase 2: Master building fully functional CLI applications", "Phase 3: Study low-level design & SOLID principles")),
                CompanyRoleInfo("Java Developer", listOf("Java", "Multithreading", "OOPS"), "₹7.0 - ₹13.0 LPA", listOf("Phase 1: Core Java memory footprint & multi-threading loops", "Phase 2: Create local data persistence schemas", "Phase 3: OOPs class refactor exercises")),
                CompanyRoleInfo("Full Stack Developer", listOf("JavaScript", "CSS", "Java", "HTML"), "₹6.8 - ₹12.5 LPA", listOf("Phase 1: Vanilla JS DOM manipulation & event loop", "Phase 2: Connect servers with local APIs", "Phase 3: Build interactive, responsive layouts"))
            ),
            questions = listOf(
                CompanyQuestion("Z-1", "Java", "Why is Java a platform-independent language, but the JVM is platform-dependent?", "Java is platform-independent because source code (.java) is compiled into bytecode (.class) which is an intermediate language independent of host machine architecture. The JVM (Java Virtual Machine) translates this bytecode into machine-specific instructions for the host OS. Since different operating systems (Windows, Mac, Linux) have different processors and assembly structures, the JVM must be customized for each platform. Hence, the JVM itself is platform-dependent.", "Easy"),
                CompanyQuestion("Z-2", "DSA", "Write a program in Java to print a matrix in spiral order.", "Spiral traversal uses boundaries (top, bottom, left, right) and shifts them as we print rows/columns. Realized in O(M*N) time where matrix size is M x N.\n```java\npublic void printSpiral(int[][] mat) {\n    int top = 0, bottom = mat.length - 1;\n    int left = 0, right = mat[0].length - 1;\n    while(top <= bottom && left <= right) {\n        for(int i = left; i <= right; i++) System.out.print(mat[top][i] + \" \");\n        top++;\n        for(int i = top; i <= bottom; i++) System.out.print(mat[i][right] + \" \");\n        right--;\n        if(top <= bottom) {\n            for(int i = right; i >= left; i--) System.out.print(mat[bottom][i] + \" \");\n            bottom--;\n        }\n        if(left <= right) {\n            for(int i = bottom; i >= top; i--) System.out.print(mat[i][left] + \" \");\n            left++;\n        }\n    }\n}\n```", "Medium"),
                CompanyQuestion("Z-3", "OOPS", "Explain the difference between interface, abstract class, and concrete class with a Zoho context.", "In a Zoho Creator app: 1. Interface: Defines common action definitions without states (e.g. `Clickable` with `onClick()`). 2. Abstract Class: Defines base properties & common code (e.g. `UIWidget` containing coordinates, background color, and abstract `render()`). 3. Concrete Class: Fully implements all features (e.g. `SubmitButton` extending `UIWidget` and implementing `Clickable`, rendering a button and clicking it).", "Medium"),
                CompanyQuestion("Z-4", "HR", "Why Zoho? We don't care about CGPA, so why should we hire you?", "Answer: 'Zoho's philosophy of valuing practical skill and hands-on coding capability over rote memorization is exactly where I thrive. I love building real projects from scratch. I built a dynamic task management CLI in Java and a personal web dashboard, learning databases and multi-threading through actual coding failures rather than just textbooks. I am self-reliant, passionate about software craftsmanship, and eager to write code that Zoho users interact with daily.'", "Medium")
            )
        ),
        CompanyInfo(
            name = "IBM",
            logoText = "IBM",
            overview = "International Business Machines Corporation is an American multinational technology corporation providing hybrid cloud, AI (Watson), consulting, and infrastructure solutions.",
            headquarters = "Armonk, New York, USA",
            type = "Product / Tech Pioneer",
            employeeCount = "280,000+",
            workCulture = "Ethical, research-centric, structured, valuing global teamwork, technical depth, patent creation, and enterprise-level system integration.",
            salaryRange = "₹5 LPA - ₹18+ LPA (For Freshers)",
            requiredTech = listOf("Java/Python", "SQL", "Cloud basics", "Docker", "Node.js", "AI foundations"),
            eligibilityCriteria = "CGPA 6.0 or 60% with B.Tech/M.Tech/MCA degree. Maximum of 1 active backlog.",
            hiringRounds = listOf(
                HiringRound("Round 1: IBM IPAT / Cognitive Assessment", "Medium", "60 Mins", listOf("Logical grids", "Numerical series", "Cognitive memory"), "A highly analytical, pattern-matching test designed to measure reasoning and cognitive load handling under speed."),
                HiringRound("Round 2: Online Coding Test", "Medium", "60 Mins", listOf("Data structures", "SQL queries", "Python/Java scripts"), "A coding test containing 1 coding problem (Medium) and 5-10 technical MCQs on OS, DBMS, and Networking."),
                HiringRound("Round 3: Technical Interview", "Medium", "45 Mins", listOf("Core Java", "Database architecture", "Cloud basics"), "Focus on cloud terms (SaaS/IaaS/PaaS) and OOPS. Be ready to explain databases normalization and write clean loops."),
                HiringRound("Round 4: HR Round", "Easy", "15 Mins", listOf("General fitment", "Relocation", "Infosys core values"), "Standard fresher HR checking communications, document clearance, and adaptability to team shifts.")
            ),
            roles = listOf(
                CompanyRoleInfo("Software Engineer", listOf("Java", "SQL", "Docker", "Python"), "₹5.5 - ₹10.0 LPA", listOf("Phase 1: Master Java OOPs & database normalization principles", "Phase 2: Master dynamic array structures and files", "Phase 3: Basic Docker containerization setups")),
                CompanyRoleInfo("AI Engineer", listOf("Python", "Watson API", "NLP", "Pandas"), "₹7.0 - ₹14.0 LPA", listOf("Phase 1: Study statistics and data preprocessing in Python", "Phase 2: Integrate pre-trained IBM Watson APIs", "Phase 3: Standard classification models")),
                CompanyRoleInfo("Cloud Engineer", listOf("Kubernetes", "Linux", "AWS", "Bash"), "₹6.0 - ₹11.5 LPA", listOf("Phase 1: Basic virtual machines and network topologies", "Phase 2: Containerize apps using Docker", "Phase 3: Study IBM Hybrid Cloud / AWS topologies"))
            ),
            questions = listOf(
                CompanyQuestion("IBM-1", "Java", "What is the difference between abstract classes and interfaces in Java 8?", "In Java 8, interfaces were upgraded to support default and static methods with concrete code, making them closer to abstract classes. However: 1. Abstract classes can have instance states (variables) and constructors, whereas interfaces cannot maintain instance states (variables are implicitly public static final). 2. A class can inherit only one abstract class but can implement multiple interfaces. 3. Abstract classes represent an identity ('is-a'), while interfaces represent a capability ('can-do').", "Medium"),
                CompanyQuestion("IBM-2", "OOPS", "What is runtime polymorphism and how is it implemented in Java?", "Runtime polymorphism (Dynamic Method Dispatch) is a process in which a call to an overridden method is resolved at runtime rather than compile time. It is implemented using inheritance and method overriding. A parent reference variable points to a child object, and when an overridden method is called, the JVM determines which implementation to run based on the actual object type at runtime, e.g., `Animal a = new Dog(); a.makeSound();` runs Dog's version of makeSound.", "Easy"),
                CompanyQuestion("IBM-3", "HR", "What do you know about IBM's Watson AI?", "Answer: 'IBM Watson is a pioneer in cognitive computing and artificial intelligence. It uses advanced natural language processing, machine learning, and data analytics to extract insights from large volumes of unstructured data. Watson became famous in 2011 by winning Jeopardy against human champions. Today, Watson APIs are integrated into hybrid cloud architectures to help enterprises automate customer support, analyze logs, and optimize operations across healthcare, finance, and logistics.'", "Medium")
            )
        ),
        CompanyInfo(
            name = "Oracle",
            logoText = "ORC",
            overview = "Oracle Corporation is an American multinational computer technology corporation famous for its relational database software, enterprise cloud solutions, and Java ownership.",
            headquarters = "Austin, Texas, USA",
            type = "Product / Tech Giant",
            employeeCount = "140,000+",
            workCulture = "Rigorous technical standards, focus on security, robustness, deep database engineering values, and structured product lifecycle management.",
            salaryRange = "₹12 LPA - ₹38+ LPA (For Freshers)",
            requiredTech = listOf("Java", "C++", "SQL (PL/SQL)", "Data Structures", "Oracle Cloud Infrastructure (OCI)", "Operating Systems"),
            eligibilityCriteria = "CGPA 7.0 or 70% with CS/IT related degrees. No active backlogs allowed.",
            hiringRounds = listOf(
                HiringRound("Round 1: Technical OA", "Hard", "120 Mins", listOf("DSA Coding", "SQL queries", "Core CS Fundamentals (OS/DBMS/Networks)"), "Oracle's OA is comprehensive, testing core OS thread schedules, complex database index lookups, and tough DSA coding."),
                HiringRound("Round 2: Technical Interview 1", "Hard", "50 Mins", listOf("Advanced DSA", "Memory Management", "C++/Java pointers"), "Oracle interviewers love to drill down on the exact memory layouts of classes, thread cycles, and binary search trees."),
                HiringRound("Round 3: Technical Interview 2 (SQL/DBMS)", "Hard", "50 Mins", listOf("PL/SQL", "Normalization", "Database indexing internals (B-Trees)"), "Write highly optimized SQL queries with subqueries, self-joins, and explain database locks (pessimistic vs optimistic)."),
                HiringRound("Round 4: Director / HR Round", "Medium", "30 Mins", listOf("Enterprise problem solving", "Career alignment", "Culture fit"), "Tests your long-term dedication, architectural values, and communication clarity under technical stress.")
            ),
            roles = listOf(
                CompanyRoleInfo("Software Engineer", listOf("DSA", "Java", "C++", "OS/DBMS"), "₹15 - ₹28 LPA", listOf("Phase 1: Master advanced data structures, pointers, and memory blocks", "Phase 2: Master multi-threading and OS scheduling mechanisms", "Phase 3: Study low-level system designs")),
                CompanyRoleInfo("Java Developer", listOf("Java", "JVM internals", "SQL", "Spring"), "₹12 - ₹24 LPA", listOf("Phase 1: Deep dive into JVM memory pools, GC, and memory leaks", "Phase 2: Master concurrent collections and locks", "Phase 3: Write enterprise Java APIs")),
                CompanyRoleInfo("Data Analyst", listOf("SQL", "PL/SQL", "Oracle DB", "Excel"), "₹10 - ₹18 LPA", listOf("Phase 1: Write PL/SQL stored procedures & triggers", "Phase 2: Optimize query execution paths using explain plan", "Phase 3: Business metrics analysis"))
            ),
            questions = listOf(
                CompanyQuestion("ORC-1", "DSA", "Explain the difference between a B-Tree and a B+ Tree. Why are they preferred for databases?", "In a B-Tree, both keys and actual record pointers are stored in both internal and leaf nodes. In a B+ Tree, internal nodes store only search keys (allowing more keys per node and higher branching factor), while all data records and values are stored exclusively in leaf nodes. Additionally, the leaf nodes in a B+ Tree are linked together sequentially in a linked list. B+ Trees are preferred for database indexes because: 1. They enable fast range scans (simply traverse leaf list). 2. They ensure consistent lookup times (every record is at the same leaf level). 3. Their higher branching factor reduces disk I/O operations drastically.", "Hard"),
                CompanyQuestion("ORC-2", "Java", "What is JVM garbage collection? Explain the G1 Garbage Collector.", "JVM Garbage Collection automates memory recovery by destroying unreferenced objects in Heap. The G1 (Garbage First) collector is designed for multi-processor machines with large memory. It partitions the Java Heap into equal-sized virtual regions. It estimates which regions contain the most garbage ('garbage-first') and collects them first, prioritizing recovery speed. This minimizes GC pause durations compared to CMS or Parallel GC.", "Hard"),
                CompanyQuestion("ORC-3", "OOPS", "Explain the concept of encapsulation. How is it violated, and how do we prevent violations?", "Encapsulation is hiding internal object data and forcing access via public APIs (getters/setters). It is violated by returning direct references to mutable objects (e.g. returning a private Date or List reference). The caller can modify the private object state directly. It is prevented by returning deep copies or immutable views of the private objects, preserving strict control.", "Medium")
            )
        ),
        CompanyInfo(
            name = "Deloitte",
            logoText = "DEL",
            overview = "Deloitte Touche Tohmatsu Limited is a multinational professional services network specializing in audit, consulting, financial advisory, and technology integration.",
            headquarters = "London, United Kingdom",
            type = "Consulting / Service Giant",
            employeeCount = "415,000+",
            workCulture = "Highly corporate, focus on client communication, business analytical thinking, structured slides presentations, and systematic risk compliance frameworks.",
            salaryRange = "₹4.5 LPA - ₹10 LPA (For Freshers)",
            requiredTech = listOf("SQL", "Python/Java", "Excel", "Data Visualization", "Logical reasoning"),
            eligibilityCriteria = "CGPA 6.0 or 60% in B.Tech/B.E/MCA. No active backlogs are allowed.",
            hiringRounds = listOf(
                HiringRound("Round 1: AMCAT Cognitive Assessment", "Medium", "90 Mins", listOf("Quantitative reasoning", "Verbal logic", "Data Interpretation"), "Standard AMCAT testing math formulas, reading comprehension, and business data trends analysis."),
                HiringRound("Round 2: Technical Interview", "Easy", "40 Mins", listOf("SQL query writing", "Excel parameters", "Basic programming syntax"), "Focus heavily on SQL commands, data analysis techniques, and explain final year project's business impact."),
                HiringRound("Round 3: Case Study & Managerial Round", "Medium", "30 Mins", listOf("Client problem scenarios", "Business estimates", "Teamwork"), "Evaluates your analytical thinking. You will be asked how to help a retail client transition their store data to digital databases."),
                HiringRound("Round 4: Partner / HR Round", "Easy", "15 Mins", listOf("Career motivations", "Professional values", "Communication"), "Confirms professional standards, relocation readiness, communication clarity, and corporate values alignment.")
            ),
            roles = listOf(
                CompanyRoleInfo("Software Engineer", listOf("Java", "SQL", "HTML/CSS"), "₹4.5 - ₹7.0 LPA", listOf("Phase 1: Strong basic Java OOPs classes", "Phase 2: Master SQL joins & aggregations", "Phase 3: Basic web development principles")),
                CompanyRoleInfo("Data Analyst", listOf("Excel", "SQL", "Tableau", "Power BI"), "₹4.5 - ₹8.0 LPA", listOf("Phase 1: Master Excel tables & lookups", "Phase 2: Write queries using group by & subqueries", "Phase 3: Build client metrics dashboards")),
                CompanyRoleInfo("Cloud Engineer", listOf("AWS", "Azure", "Security Basics"), "₹5.0 - ₹9.0 LPA", listOf("Phase 1: Basic networking & cloud infrastructure terms", "Phase 2: Study IAM policies and server configs", "Phase 3: Infrastructure templates basics"))
            ),
            questions = listOf(
                CompanyQuestion("DEL-1", "Java", "What is the difference between interface default methods and private methods introduced in Java 9?", "In Java 8, default methods let us add new public functionalities to interfaces without breaking existing implementing classes. In Java 9, private methods were introduced to let us write common helper code shared among multiple default methods within the interface. This prevents code duplication and keeps default methods clean, without exposing private helper methods to external classes.", "Medium"),
                CompanyQuestion("DEL-2", "OOPS", "What is dynamic polymorphism vs static polymorphism?", "Static polymorphism (Compile-time) is method overloading, resolved by compilers based on signatures. Dynamic polymorphism (Runtime) is method overriding, resolved by JVMs during execution based on the actual object type. This allows writing highly extensible, modular codebase interfaces.", "Easy"),
                CompanyQuestion("DEL-3", "HR", "How do you align business values with technical implementations?", "Answer: 'I believe technology exists to solve business problems and deliver value to clients. When writing a system, I don't just focus on code loops, but on outcomes: does this speed up invoice generations for the client? Does it reduce user drop-offs? In college, we built a food pre-order app. We optimized the database queries because reducing the page load speed by 2 seconds directly reduced student checkout drop-offs by 15%. I always analyze the client's business goals first.'", "Medium")
            )
        ),
        CompanyInfo(
            name = "HCL",
            logoText = "HCL",
            overview = "HCL Technologies Limited is an Indian multinational information technology services and consulting company, specializing in digital, engineering, cloud, and software solutions.",
            headquarters = "Noida, Uttar Pradesh, India",
            type = "Service / IT Consulting",
            employeeCount = "220,000+",
            workCulture = "Employee-first philosophy, continuous upskilling, steady growth, flexible work streams, and systematic project delivery setups.",
            salaryRange = "₹3.6 LPA - ₹7.0+ LPA (For Freshers)",
            requiredTech = listOf("Java/C++", "SQL", "JavaScript", "HTML/CSS", "Logical Reasoning"),
            eligibilityCriteria = "CGPA 6.0 or 60% with B.Tech/MCA degrees. Maximum 1 active backlog.",
            hiringRounds = listOf(
                HiringRound("Round 1: HCL CoCubes Assessment", "Medium", "90 Mins", listOf("Quantitative reasoning", "Verbal grammar", "Coding basics"), "Standard CoCubes aptitude section with simple arrays & logic coding exercises. Practice time-bound math quizzes."),
                HiringRound("Round 2: Technical Interview", "Easy", "30 Mins", listOf("Core programming", "OOPs definitions", "SQL table joints"), "Be ready to trace simple recursion loops, explain class modifiers, and write basic loops (factorials, primes)."),
                HiringRound("Round 3: HR Interview", "Easy", "15 Mins", listOf("General checks", "Service agreements", "Shift agreements"), "Standard fresher HR checking certificate validity, communications, relocation, and shift preferences.")
            ),
            roles = listOf(
                CompanyRoleInfo("Software Engineer", listOf("Java", "SQL", "HTML/CSS"), "₹3.6 - ₹5.5 LPA", listOf("Phase 1: Basic programming syntax & OOPs in Java/Python", "Phase 2: Master SQL database constraints and queries", "Phase 3: Basic web development layouts")),
                CompanyRoleInfo("Java Developer", listOf("Java", "JDBC", "Spring Boot"), "₹4.0 - ₹7.0 LPA", listOf("Phase 1: Core Java structures & Collections", "Phase 2: JDBC setup & Servlet basics", "Phase 3: Basic REST APIs")),
                CompanyRoleInfo("Data Analyst", listOf("Excel", "SQL", "Power BI"), "₹3.5 - ₹6.0 LPA", listOf("Phase 1: Master Excel formulas & formatting", "Phase 2: Write queries using aggregate functions", "Phase 3: Basic reporting layouts"))
            ),
            questions = listOf(
                CompanyQuestion("HCL-1", "Java", "What is the difference between public, private, protected, and default modifiers in Java?", "1. private: Accessible only within the same class.\n2. default (no modifier): Accessible only within the same package.\n3. protected: Accessible within the same package and by subclasses in other packages.\n4. public: Accessible from any class in any package.", "Easy"),
                CompanyQuestion("HCL-2", "Aptitude", "The average of 5 consecutive odd numbers is 25. What is the smallest number?", "1. Let the 5 consecutive odd numbers be: x, x+2, x+4, x+6, x+8.\n2. Average = Sum / 5 = (5x + 20) / 5 = x + 4.\n3. Given Average = 25, so x + 4 = 25.\n4. x = 21.\n5. Therefore, the smallest number is 21.", "Easy"),
                CompanyQuestion("HCL-3", "HR", "Tell me about yourself.", "Answer structure: Briefly cover current education/degree, highlight 1 core technical project, mention your coding skills, and align with the company. 'I am a final year B.Tech student in Computer Science. My core strengths are Java and SQL. For my final year project, I built a Web-based Inventory Management App using JDBC and MySQL, which automated stocking alerts. I maintained a CGPA of 8.1. I'm passionate about writing clean code and I'm excited to start my professional career at HCL.'", "Easy")
            )
        ),
        CompanyInfo(
            name = "Tech Mahindra",
            logoText = "TM",
            overview = "Tech Mahindra is an Indian multinational information technology services and consulting company, part of the Mahindra Group, specialized in telecom and digital engineering services.",
            headquarters = "Pune, Maharashtra, India",
            type = "Service / IT Consulting",
            employeeCount = "150,000+",
            workCulture = "Collaborative, steady career progression, strong community values, tech versatility, and global client interaction scopes.",
            salaryRange = "₹3.25 LPA - ₹6.5 LPA (For Freshers)",
            requiredTech = listOf("Java/C++", "SQL", "HTML/CSS", "Networking basics", "Logical aptitude"),
            eligibilityCriteria = "Minimum 60% throughout B.Tech/MCA. Max 1 backlog allowed at the time of hiring.",
            hiringRounds = listOf(
                HiringRound("Round 1: Tech Mahindra Online Test", "Medium", "90 Mins", listOf("Quantitative reasoning", "Verbal logic", "Non-verbal puzzles"), "Speed-based online test. Focus heavily on practice aptitude papers and basic grammar sections."),
                HiringRound("Round 2: Technical Interview", "Easy", "30 Mins", listOf("Core OOPs", "SQL commands", "Project architecture"), "Write basic programs (reversing string, sum of digits) and explain the flow diagram of your project."),
                HiringRound("Round 3: HR Round", "Easy", "15 Mins", listOf("General details", "Relocation agreement", "Communication checks"), "Verifying personal files, describing training tracks, shift arrangements, and explaining company values.")
            ),
            roles = listOf(
                CompanyRoleInfo("Software Engineer", listOf("Java", "SQL", "HTML/CSS"), "₹3.25 - ₹5.5 LPA", listOf("Phase 1: Basic programming syntax & OOPs in Java/Python", "Phase 2: Master SQL database constraints and queries", "Phase 3: Basic web development layouts")),
                CompanyRoleInfo("Java Developer", listOf("Java", "JDBC", "Spring Boot"), "₹3.8 - ₹6.8 LPA", listOf("Phase 1: Core Java structures & Collections", "Phase 2: JDBC setup & Servlet basics", "Phase 3: Basic REST APIs")),
                CompanyRoleInfo("Data Analyst", listOf("Excel", "SQL", "Power BI"), "₹3.3 - ₹5.8 LPA", listOf("Phase 1: Master Excel formulas & formatting", "Phase 2: Write queries using aggregate functions", "Phase 3: Basic reporting layouts"))
            ),
            questions = listOf(
                CompanyQuestion("TM-1", "Java", "What is the difference between abstract class and interface in Java?", "An abstract class can contain instance variables, constructors, concrete methods, and abstract methods. A subclass extends it using the `extends` keyword. An interface can only contain public static final variables and abstract methods (default/static methods in Java 8). A class implements it using `implements` and can implement multiple interfaces, enabling multiple inheritance.", "Easy"),
                CompanyQuestion("TM-2", "Aptitude", "If a work can be completed by 8 workers in 15 days, how many workers can complete the same work in 10 days?", "1. Total work in worker-days = 8 * 15 = 120 worker-days.\n2. To complete 120 worker-days in 10 days, divide total work by days: 120 / 10 = 12 workers.\n3. Therefore, 12 workers are needed.", "Easy"),
                CompanyQuestion("TM-3", "HR", "Why Tech Mahindra?", "Answer: 'Tech Mahindra is a global giant and a pioneer in telecom and digital engineering services, presenting freshers with massive learning domains. Being a part of the esteemed Mahindra Group, its core ethics and employee-friendly culture are outstanding. I've read about Tech Mahindra's focus on upskilling through advanced academies. I am eager to start my career in an environment that drives technology transformation across diverse industries.'", "Easy")
            )
        ),
        CompanyInfo(
            name = "Flipkart",
            logoText = "FK",
            overview = "Flipkart Private Limited is an Indian e-commerce company, headquartered in Bengaluru, Karnataka, India, and registered in Singapore. It is a subsidiary of Walmart.",
            headquarters = "Bengaluru, Karnataka, India",
            type = "Product / E-commerce Giant",
            employeeCount = "35,000+",
            workCulture = "Fast-paced, high ownership, data-driven, customer-first, focus on scale, modular code architectures, and fast iteration cycles.",
            salaryRange = "₹14 LPA - ₹32+ LPA (For Freshers)",
            requiredTech = listOf("Java", "C++", "Python", "SQL", "NoSQL", "System Design", "Microservices"),
            eligibilityCriteria = "CGPA 7.0 or equivalent CS/IT related B.Tech/M.Tech degree. Outstanding coding portfolio.",
            hiringRounds = listOf(
                HiringRound("Round 1: Machine Coding Round", "Hard", "120 Mins", listOf("SOLID principles LLD", "Clean Code design", "Runnable CLI utility"), "You must design and implement a fully working LLD problem (e.g. Booking system, Ride sharing) on your local laptop, with clean, modular OOPs classes, handling concurrency, and write unit tests in 2 hours."),
                HiringRound("Round 2: Technical Interview 1 (DSA)", "Hard", "50 Mins", listOf("Graph algorithms", "Dynamic Programming", "Queue manipulations"), "Drills down on complex DSA problem solving. Communicate your optimizations clearly and cover all edge cases (null inputs, cycles)."),
                HiringRound("Round 3: Technical Interview 2 (System Design)", "Hard", "60 Mins", listOf("Microservices", "Caching (Redis)", "Database partitioning"), "Focus on scale. Flipkart handles massive traffic spikes during Big Billion Days. Learn distributed architectures, read-heavy optimizations, and load balances."),
                HiringRound("Round 4: Engineering Manager / HR", "Medium", "45 Mins", listOf("Behavioral STAR questions", "Failure analysis", "Ambiguity handling"), "Tests ownership mindset, ability to work under high delivery pressure, and collaboration values.")
            ),
            roles = listOf(
                CompanyRoleInfo("Software Engineer", listOf("DSA", "Java", "System Design", "SOLID LLD"), "₹18 - ₹28 LPA", listOf("Phase 1: Master advanced graphs, dynamic programming, and heaps", "Phase 2: Master writing clean code LLD in 90 minutes", "Phase 3: Distributed database partitioning & caching")),
                CompanyRoleInfo("Full Stack Developer", listOf("React", "Node.js", "Redis", "NoSQL"), "₹15 - ₹24 LPA", listOf("Phase 1: Optimized frontend rendering & image lazy loads", "Phase 2: Asynchronous queue servers with Node.js & Redis", "Phase 3: Secure distributed sessions")),
                CompanyRoleInfo("Data Analyst", listOf("SQL", "Python", "Tableau", "Metrics"), "₹12 - ₹18 LPA", listOf("Phase 1: Write intermediate SQL self-joins and subqueries", "Phase 2: Track business KPIs & perform A/B test analysis", "Phase 3: Visual dashboards"))
            ),
            questions = listOf(
                CompanyQuestion("FK-1", "DSA", "Explain the Machine Coding round at Flipkart. What do interviewers look for?", "The Machine Coding round evaluates your ability to write clean, modular, extensible, and fully-functional object-oriented code. You are given a problem statement (e.g. splitwise system) and must write fully working CLI classes in 2 hours. Interviewers look for: 1. Application of SOLID principles. 2. Proper Separation of Concerns (Model, View, Service, Repository). 3. Extensibility (adding a new feature should not break existing classes). 4. Concurrency handling (thread safety). 5. Proper exception handling and input validations.", "Hard"),
                CompanyQuestion("FK-2", "System Design", "Design a Flash Sale System (e.g. Redmi phone sale) that handles millions of hits in a few seconds.", "Requires: 1. Rate Limiting at API gateway to drop excess hits. 2. CDN caching for product detail pages. 3. Distributed In-memory locks (Redis) to validate inventory decrement. 4. An asynchronous message queue (Kafka) to process successful checkouts, avoiding direct database lock congestion. 5. Write-heavy NoSQL DB for order processing.", "Hard"),
                CompanyQuestion("FK-3", "Java", "What is Java Volatile keyword? How is it different from synchronized block?", "The `volatile` keyword in Java is used to mark a variable as 'being stored in main memory'. This guarantees visibility of changes to other threads immediately, preventing CPU cache synchronization issues. However, `volatile` does not provide atomicity (it doesn't lock or prevent race conditions on compound actions like `i++`). A `synchronized` block provides both thread mutual exclusion (only one thread can execute it at a time) and memory visibility, but has higher performance overhead.", "Medium")
            )
        ),
        CompanyInfo(
            name = "PayPal",
            logoText = "PP",
            overview = "PayPal Holdings, Inc. is an American multinational financial technology corporation operating an online payments system in the majority of countries that support online money transfers.",
            headquarters = "San Jose, California, USA",
            type = "Product / Fintech Leader",
            employeeCount = "30,000+",
            workCulture = "Rigorous security compliance, focus on financial stability, collaborative, high tech scale, and professional engineering practices.",
            salaryRange = "₹12 LPA - ₹34+ LPA (For Freshers)",
            requiredTech = listOf("Java", "C++", "Python", "SQL", "REST Security", "Cloud Architectures", "Concurrency"),
            eligibilityCriteria = "CGPA 7.0 or equivalent CS/IT B.Tech/M.Tech degree. Strong knowledge of secure systems.",
            hiringRounds = listOf(
                HiringRound("Round 1: Online Coding Test", "Hard", "90 Mins", listOf("Advanced DSA", "Concurrency problems", "Math logic"), "Tests robust programming capabilities in Java/C++ with complex string algorithms and array manipulations."),
                HiringRound("Round 2: Technical Interview 1", "Hard", "50 Mins", listOf("Data Structures", "Thread safety", "Memory leakage"), "Interviews focus heavily on thread safety, concurrency locks, and writing optimized pointer logic."),
                HiringRound("Round 3: Technical Interview 2 (System Design)", "Hard", "50 Mins", listOf("Secure API design", "Distributed databases", "Event-driven systems"), "Designing secure payment flow paths, tokenizations, caching, and handling database consistency (ACID vs BASE)."),
                HiringRound("Round 4: Managerial / HR Round", "Medium", "30 Mins", listOf("STAR behavioral scenarios", "Ethical reasoning", "Culture alignment"), "Checks long-term motivation, ethical values, logical reasoning under stress, and teamwork.")
            ),
            roles = listOf(
                CompanyRoleInfo("Software Engineer", listOf("DSA", "Java", "System Design", "Thread safety"), "₹15 - ₹25 LPA", listOf("Phase 1: Master advanced graphs and trees algorithms", "Phase 2: Master Java concurrency and distributed locks", "Phase 3: Study REST API security protocols")),
                CompanyRoleInfo("Cybersecurity Analyst", listOf("Cryptography", "API Security", "Wireshark"), "₹14 - ₹24 LPA", listOf("Phase 1: Deep dive into TLS handshake and JWT tokens", "Phase 2: Master SQL Injection and XSS threat audits", "Phase 3: Threat intelligence tools")),
                CompanyRoleInfo("Cloud Engineer", listOf("AWS", "Kubernetes", "Linux"), "₹13 - ₹22 LPA", listOf("Phase 1: Cloud private VPC and load balancing topologies", "Phase 2: Master Kubernetes security configurations", "Phase 3: Deploy secure microservices"))
            ),
            questions = listOf(
                CompanyQuestion("PP-1", "DSA", "Given a set of non-overlapping intervals, insert a new interval into the intervals.", "To insert a new interval [s, e]: 1. Iterate through sorted intervals. 2. Add all intervals ending before the new interval starts. 3. Merge all overlapping intervals by updating s = min(s, start) and e = max(e, end). 4. Add the merged new interval. 5. Add all remaining intervals. Time complexity is O(N) where N is number of intervals.", "Medium"),
                CompanyQuestion("PP-2", "System Design", "Design a highly secure, consistent payment transaction API.", "Requires: 1. Idempotency Keys (guarantees that double-clicks on submit do not charge the user twice). 2. A distributed transaction orchestrator (Saga Pattern) to manage multiple services. 3. Secure TLS encryption for transit, and Tokenization for stored card details. 4. ACID-compliant relational databases (like PostgreSQL) for ledger entries.", "Hard"),
                CompanyQuestion("PP-3", "Java", "What is a dead lock in Java? How do you detect and prevent it?", "A deadlock occurs when two or more threads are blocked forever, each waiting for a lock held by the other thread. Deadlock detection: Use JVM tools like `jstack` or ThreadMXBean API to scan active threads. Prevention: 1. Lock Ordering (always acquire locks in a consistent, defined order). 2. Lock Timeouts (use `ReentrantLock.tryLock(timeout)` instead of synchronized). 3. Minimize locks holding scopes.", "Hard")
            )
        ),
        CompanyInfo(
            name = "Adobe",
            logoText = "ADB",
            overview = "Adobe Inc. is an American multinational computer software company famous for its creative and multimedia software products, including Photoshop and PDF.",
            headquarters = "San Jose, California, USA",
            type = "Product / Multimedia Giant",
            employeeCount = "28,000+",
            workCulture = "Creativity, excellence, work-life balance, rich benefits, focus on user interface and experience (UX), and high technical execution standards.",
            salaryRange = "₹15 LPA - ₹40+ LPA (For Freshers)",
            requiredTech = listOf("C++", "Java", "Python", "DSA", "OOPS", "Operating Systems", "Computer Graphics basics"),
            eligibilityCriteria = "CGPA 7.0 or equivalent with B.Tech/M.Tech degrees. Exceptional problem-solving skills.",
            hiringRounds = listOf(
                HiringRound("Round 1: Written Coding OA", "Hard", "90 Mins", listOf("Advanced DSA Coding", "OOPS MCQs", "Quant puzzles"), "Adobe's OA contains 2-3 hard DSA problems, with a heavy focus on binary search, trees, and dynamic programming."),
                HiringRound("Round 2: Technical Interview 1", "Hard", "50 Mins", listOf("Core DSA", "C++ Pointers / Java memory", "Complex matrices"), "Interviews focus on core data structures, memory allocations, pointer arithmetic, and writing clean logic."),
                HiringRound("Round 3: Technical Interview 2", "Hard", "50 Mins", listOf("SOLID principles", "OS Memory & Process Threading", "LLD of multimedia app"), "Focus on Low-Level Design, designing extensible classes for file systems, processing pipelines, and thread pools."),
                HiringRound("Round 4: Managerial / HR", "Medium", "30 Mins", listOf("Creativity review", "Failure discussions", "Cultural fitment"), "Checks your passion for software, visual details interest, collaboration history, and standard HR parameters.")
            ),
            roles = listOf(
                CompanyRoleInfo("Software Engineer", listOf("DSA", "C++", "OOPS", "OS Memory"), "₹18 - ₹30 LPA", listOf("Phase 1: Master memory allocation, custom pointers, and multi-threading", "Phase 2: Master low level design and SOLID patterns", "Phase 3: Practice building local desktop utilities")),
                CompanyRoleInfo("Full Stack Developer", listOf("React", "Node.js", "CSS", "TypeScript"), "₹15 - ₹25 LPA", listOf("Phase 1: Master complex UI animations and responsive web styling", "Phase 2: Build Node.js backend servers with file upload streams", "Phase 3: Web performance optimization")),
                CompanyRoleInfo("UI/UX Designer", listOf("Figma", "Creative Suite", "Typography"), "₹14 - ₹24 LPA", listOf("Phase 1: Master layout grid systems, typography, and color schemes", "Phase 2: Build complex wireframe user flows", "Phase 3: Interactive mock case studies"))
            ),
            questions = listOf(
                CompanyQuestion("ADB-1", "DSA", "Given an array of integers, find the maximum sum of a contiguous subarray (Kadane's Algorithm).", "Kadane's Algorithm maintains a running sum of elements. 1. Initialize `max_so_far = Integer.MIN_VALUE` and `max_ending_here = 0`. 2. Loop through the array. For each element, add it to `max_ending_here`. If `max_so_far < max_ending_here`, update `max_so_far = max_ending_here`. If `max_ending_here < 0`, reset `max_ending_here = 0`. Time complexity is O(N) and space complexity is O(1).", "Medium"),
                CompanyQuestion("ADB-2", "Java", "What is Java garbage collection tuning? How do you optimize Heap memory?", "Garbage Collection tuning involves adjusting JVM parameters to minimize GC pauses and optimize memory allocation. 1. Set initial and max heap size to be equal (`-Xms` and `-Xmx`) to prevent dynamic resizing overhead. 2. Adjust nursery/young generation size (`-XX:NewRatio`). 3. Choose the G1 Garbage Collector (`-XX:+UseG1GC`) for large heaps. 4. Use profiling tools (VisualVM, JProfiler) to locate memory leak spots and optimize reference closures.", "Hard"),
                CompanyQuestion("ADB-3", "OOPS", "Explain the concept of abstraction. How does it differ from encapsulation?", "Abstraction is the process of hiding internal implementation details and showing only the essential features of an object (implemented using abstract classes and interfaces). Encapsulation is the process of binding data (variables) and code (methods) together into a single unit and restricting direct access (implemented using private access modifiers and public getters/setters). Abstraction is about *What* an object does; Encapsulation is about *How* it does it securely.", "Easy")
            )
        ),
        CompanyInfo(
            name = "SAP",
            logoText = "SAP",
            overview = "SAP SE is a German multinational software corporation based in Walldorf, Baden-Württemberg. It develops enterprise software to manage business operations and customer relations.",
            headquarters = "Walldorf, Germany",
            type = "Product / Enterprise ERP Giant",
            employeeCount = "110,000+",
            workCulture = "Steady work-life balance, rich benefits, focus on long-term enterprise value, high ethical values, structured engineering pathways.",
            salaryRange = "₹8 LPA - ₹24+ LPA (For Freshers)",
            requiredTech = listOf("Java", "C++", "SQL", "OOPs", "ERP basics", "Data Structures"),
            eligibilityCriteria = "CGPA 7.0 or equivalent with B.Tech/MCA/M.Tech degree. No active backlogs allowed.",
            hiringRounds = listOf(
                HiringRound("Round 1: SAP Written Coding Test", "Medium", "90 Mins", listOf("Aptitude logic", "OOPs concepts MCQs", "Coding (2 problems)"), "Tests quantitative math logic, basic arrays & string manipulation coding, and database key concepts."),
                HiringRound("Round 2: Technical Interview 1", "Medium", "45 Mins", listOf("Core Java", "SQL database joins", "Project detailed schemas"), "Interviews focus on core OOPs principles, Java collections framework, and writing clean SQL joints queries."),
                HiringRound("Round 3: Technical Interview 2", "Medium", "45 Mins", listOf("System interactions", "Advanced OOPS class designs", "Logic puzzles"), "Design clean class structures for an inventory system, solve logic puzzles, and explain indexing."),
                HiringRound("Round 4: Managerial & HR", "Easy", "30 Mins", listOf("Teamwork scenarios", "Flexibility", "Infosys values"), "Standard corporate HR checking relocation, communications, and interest in enterprise software.")
            ),
            roles = listOf(
                CompanyRoleInfo("Software Engineer", listOf("Java", "SQL", "OOPs", "DSA"), "₹9 - ₹16 LPA", listOf("Phase 1: Master programming syntax & Java OOPs classes", "Phase 2: Master SQL subqueries & indices", "Phase 3: Study low level design patterns")),
                CompanyRoleInfo("Java Developer", listOf("Java", "Spring Boot", "MySQL", "Rest APIs"), "₹8 - ₹15 LPA", listOf("Phase 1: Advanced core Java classes & multithreading", "Phase 2: Spring Boot microservices API design", "Phase 3: Database scaling")),
                CompanyRoleInfo("Data Analyst", listOf("SQL", "Excel", "Power BI"), "₹7 - ₹12 LPA", listOf("Phase 1: Master Excel tables & pivot charts", "Phase 2: Write queries using joins & aggregations", "Phase 3: Build enterprise metrics layouts"))
            ),
            questions = listOf(
                CompanyQuestion("SAP-1", "Java", "What is the difference between HashMap and Hashtable in Java?", "1. HashMap is non-synchronized (not thread-safe, faster), while Hashtable is synchronized (thread-safe, slower).\n2. HashMap allows one null key and multiple null values, whereas Hashtable does not allow any null keys or null values.\n3. HashMap is preferred for modern single-threaded JVM programs, using `ConcurrentHashMap` if thread safety is required.", "Easy"),
                CompanyQuestion("SAP-2", "OOPS", "Explain the concept of method overloading. How does the compiler resolve overloaded methods?", "Method Overloading is declaring multiple methods in the same class with the same name but different parameters (signature). The compiler resolves overloaded methods at compile time (Static Polymorphism) using overload resolution: it matches the call argument types, count, and order with the method signatures. If an exact match is not found, the compiler performs automatic type promotion (e.g. char to int, float to double) to locate the best match.", "Easy"),
                CompanyQuestion("SAP-3", "HR", "Why are ERP systems critical for large enterprises?", "Answer: 'Enterprise Resource Planning (ERP) systems are the digital backbone of large companies. They integrate all core business processes—including finance, HR, manufacturing, supply chain, services, and procurement—into a single, centralized database system. This eliminates data silos, ensures real-time visibility of company metrics, automates routine workflows, and enforces consistent regulatory compliance. SAP is a pioneer in ERP, and writing robust code for these systems directly impact how global enterprises operate.'", "Medium")
            )
        ),
        CompanyInfo(
            name = "ServiceNow",
            logoText = "SN",
            overview = "ServiceNow, Inc. is an American software company based in Santa Clara, California that provides a cloud computing platform to help companies manage digital workflows for enterprise operations.",
            headquarters = "Santa Clara, California, USA",
            type = "Product / SaaS Leader",
            employeeCount = "22,000+",
            workCulture = "High energy, innovative, focus on cloud workflows, collaborative team spaces, robust tech stacks, and fast-paced SaaS execution loops.",
            salaryRange = "₹15 LPA - ₹38+ LPA (For Freshers)",
            requiredTech = listOf("Java/JavaScript", "React", "SQL", "Cloud workflows", "DSA", "SaaS platforms"),
            eligibilityCriteria = "CGPA 7.0 or 70% with B.Tech/M.Tech in CS/IT related fields. Strong coding skills.",
            hiringRounds = listOf(
                HiringRound("Round 1: Online Technical Assessment", "Hard", "90 Mins", listOf("DSA (2-3 coding problems)", "SaaS logic", "JS loops"), "ServiceNow OA features challenging DSA questions, with heavy emphasis on queues, graphs, and string parsing."),
                HiringRound("Round 2: Technical Interview 1", "Hard", "50 Mins", listOf("DSA coding", "OOPs designs", "JS scopes"), "Interviews focus on core DSA problems solving, dynamic programming, JavaScript DOM/event loop, and writing clean OOPs classes."),
                HiringRound("Round 3: Technical Interview 2 (System Design)", "Hard", "50 Mins", listOf("SaaS multi-tenancy", "VPC routing", "WebSockets"), "Design dynamic workflows systems, real-time message sync, database scale, and microservices caching."),
                HiringRound("Round 4: Managerial / HR Round", "Medium", "30 Mins", listOf("STAR behavioral questions", "Culture fit", "Ambiguity checks"), "Checks team collaboration values, work schedules, career drive, and communication clarity.")
            ),
            roles = listOf(
                CompanyRoleInfo("Software Engineer", listOf("DSA", "Java", "System Design", "SaaS"), "₹18 - ₹28 LPA", listOf("Phase 1: Master dynamic arrays, graphs, and trees", "Phase 2: Master JavaScript scopes and JVM concurrency", "Phase 3: System Design and SaaS multi-tenancy")),
                CompanyRoleInfo("Full Stack Developer", listOf("React", "Node.js", "SQL", "JavaScript"), "₹16 - ₹26 LPA", listOf("Phase 1: Advanced React custom hooks and state management", "Phase 2: REST APIs and event queue connections", "Phase 3: Responsive layouts")),
                CompanyRoleInfo("Cloud Engineer", listOf("AWS", "Kubernetes", "Linux"), "₹15 - ₹24 LPA", listOf("Phase 1: Virtual network setups and load balancer paths", "Phase 2: Docker containers and Kubernetes clusters", "Phase 3: CI/CD automation templates"))
            ),
            questions = listOf(
                CompanyQuestion("SN-1", "DSA", "Given a set of intervals, merge all overlapping intervals.", "To merge overlapping intervals: 1. Sort intervals by start time. 2. Create a list of merged intervals and insert the first interval. 3. Traverse sorted intervals. For each interval, if it overlaps with the last interval in the merged list (i.e. start <= last.end), update `last.end = max(last.end, end)`. Otherwise, add the interval to the merged list. Complexity is O(N log N) due to sorting.", "Medium"),
                CompanyQuestion("SN-2", "System Design", "Design a multi-tenant SaaS workflow system like ServiceNow.", "Requires: 1. Logical Database Isolation (each tenant has a tenant_id column in database tables) or Schema Isolation to segregate tenant data. 2. Metadata-driven architecture to allow tenants to customize fields dynamically without changing database tables schemas. 3. A distributed execution queue (RabbitMQ) to handle background workflow tasks. 4. Redis cache for fast metadata lookup.", "Hard"),
                CompanyQuestion("SN-3", "Java", "What is the difference between checked and unchecked exceptions in Java?", "Checked exceptions (e.g. IOException, SQLException) are exceptions that are checked at compile time by the compiler, which forces you to handle them using try-catch blocks or declare them in the method signature using `throws`. Unchecked exceptions (e.g. NullPointerException, ArithmeticException) inherit from RuntimeException and are not checked at compile time; they occur at runtime and are usually caused by programming errors.", "Easy")
            )
        )
    )

    fun getCompanyByName(name: String): CompanyInfo? {
        return companies.find { it.name.equals(name, ignoreCase = true) }
    }
}
