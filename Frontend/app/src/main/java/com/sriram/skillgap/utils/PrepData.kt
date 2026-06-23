package com.sriram.skillgap.utils

data class PrepQuestion(
    val question: String,
    val answer: String,
    val companies: List<String>
)

data class PrepRoleDetails(
    val title: String,
    val selectionProcess: List<String>,
    val techSkills: List<String>,
    val toolsSkills: List<String>,
    val softSkills: List<String>,
    val roadmap30Days: List<String>,
    val roadmap3Months: List<String>,
    val prepSteps: List<String>,
    val interviewQuestions: List<PrepQuestion>
)

object PrepData {
    fun getRoleDetails(title: String): PrepRoleDetails {
        return when (title) {
            "Android Developer" -> getAndroidDeveloperDetails()
            "Full Stack Developer" -> getFullStackDeveloperDetails()
            "Data Scientist" -> getDataScientistDetails()
            "DevOps Engineer" -> getDevOpsDetails()
            "UI/UX Designer" -> getUIUXDetails()
            "Cybersecurity Analyst" -> getCybersecurityDetails()
            "Cloud Engineer" -> getCloudDetails()
            "AI/ML Engineer" -> getAIMLDetails()
            "Data Analyst" -> getDataAnalystDetails()
            "Product Manager" -> getProductManagerDetails()
            "Software QA/Testing Engineer" -> getQADetails()
            else -> getAndroidDeveloperDetails() // Default fallback
        }
    }

    private fun getAndroidDeveloperDetails() = PrepRoleDetails(
        title = "Android Developer",
        selectionProcess = listOf(
            "Round 1: Resume Shortlisting & ATS Screening",
            "Round 2: Online Coding Assessment (Data Structures & Algorithms in Kotlin/Java)",
            "Round 3: Android Technical Interview (Coroutines, Compose, Jetpack, LiveData, Room)",
            "Round 4: System Design & Architecture (MVVM/MVI, Clean Architecture, Dependency Injection)",
            "Round 5: Managerial & Culture Fit / HR Round"
        ),
        techSkills = listOf("Kotlin", "Java", "Android Jetpack", "Coroutines & Flow", "MVVM/MVI Architecture", "Room DB & Retrofit", "Dagger Hilt DI", "Unit Testing (JUnit/Espresso)"),
        toolsSkills = listOf("Android Studio", "Git & GitHub", "Figma (UI Specs)", "Gradle Build System", "Firebase Crashlytics", "Postman (API Testing)"),
        softSkills = listOf("Collaborative Problem Solving", "Effective Communication", "Attention to UX Details", "Agile Adaptability"),
        roadmap30Days = listOf(
            "Day 1 to 10: Master Kotlin Fundamentals, OOPS, Null Safety, Collections, and basic Android views or Composables.",
            "Day 11 to 20: Build UI layouts using Jetpack Compose, understand state management, and integrate Room Database.",
            "Day 21 to 30: Connect networking with Retrofit, handle background threads with Coroutines, and implement MVVM pattern."
        ),
        roadmap3Months = listOf(
            "Month 1: Deep dive into advanced Jetpack Compose, state flow, database handling, and REST API integration.",
            "Month 2: Master Dependency Injection (Dagger Hilt), Unit Testing, and modularizing your Android application.",
            "Month 3: Learn clean architecture, CI/CD with GitHub Actions, publish an app on Play Store, and practice mock interviews."
        ),
        prepSteps = listOf(
            "Step 1: Solidify Kotlin skills. Focus on functional programming, scope functions (let, run, apply), and Coroutines.",
            "Step 2: Practice UI development. Jetpack Compose is standard now. Learn state hoisting and side effects.",
            "Step 3: Build 2 premium portfolio projects. One offline-first app (Room) and one network-connected app (Retrofit).",
            "Step 4: Master Architecture. Be prepared to explain MVVM, MVI, and Clean Architecture on a whiteboard.",
            "Step 5: Solve core Android interview questions and brush up on basic DSA."
        ),
        interviewQuestions = (1..30).map { i ->
            when (i) {
                1 -> PrepQuestion("What is the difference between val and var?", "val creates a read-only (immutable) variable whose value cannot be reassigned once initialized, whereas var creates a mutable variable that can be reassigned.", listOf("Google", "TCS", "Infosys"))
                2 -> PrepQuestion("Explain the Android Activity Lifecycle.", "It consists of onCreate(), onStart(), onResume(), onPause(), onStop(), onDestroy(), and onRestart() which manage the states of an activity.", listOf("Google", "Microsoft", "Cognizant"))
                3 -> PrepQuestion("What is Jetpack Compose?", "It is Android's modern toolkit for building native UI using a declarative approach, which simplifies UI development compared to XML layouts.", listOf("Google", "Amazon", "Wipro"))
                4 -> PrepQuestion("How do Coroutines differ from threads?", "Coroutines are lightweight, cooperative threads of execution that run on top of actual OS threads and consume far fewer resources.", listOf("Netflix", "Google", "Accenture"))
                5 -> PrepQuestion("Explain MVVM architecture in Android.", "Model-View-ViewModel separates UI logic (View) from business logic (Model) using a ViewModel helper that exposes observable states to the View.", listOf("Meta", "Amazon", "TCS"))
                6 -> PrepQuestion("What is Dagger Hilt?", "Hilt is a dependency injection library built on top of Dagger to simplify dependency injection in Android apps by managing container lifecycles.", listOf("Google", "Uber", "Capgemini"))
                7 -> PrepQuestion("What is LiveData vs Flow?", "LiveData is lifecycle-aware and designed for UI data holding within Android, whereas Kotlin Flow is a reactive stream API that is not tied to Android lifecycles natively.", listOf("Samsung", "Infosys", "Mindtree"))
                8 -> PrepQuestion("What is the purpose of Room Database?", "Room is an abstraction layer over SQLite that provides compile-time verification of SQL queries and integrates seamlessly with LiveData/Flow.", listOf("Adobe", "Flipkart", "TCS"))
                9 -> PrepQuestion("How does Retrofit work?", "Retrofit is a type-safe HTTP client for Android and Java that translates your Java/Kotlin interface into an actual REST API caller using annotations.", listOf("Google", "Microsoft", "Wipro"))
                10 -> PrepQuestion("What is the difference between Launch and Async in Coroutines?", "launch is a fire-and-forget builder that returns a Job and does not return a result, whereas async returns a Deferred object that can be awaited.", listOf("Google", "Amazon", "Cognizant"))
                11 -> PrepQuestion("What is State Hoisting in Compose?", "State hoisting is a pattern of moving state to a composable's caller to make the composable stateless and highly reusable.", listOf("Google", "Facebook", "Infosys"))
                12 -> PrepQuestion("Explain the differences between Serializable and Parcelable.", "Serializable is standard Java using reflection (slower), while Parcelable is Android-specific and optimized for IPC using explicit serialization logic (much faster).", listOf("Sony", "TCS", "Cognizant"))
                13 -> PrepQuestion("What is a Service in Android?", "A Service is an application component that can perform long-running operations in the background without providing a user interface.", listOf("Google", "Microsoft", "L&T"))
                14 -> PrepQuestion("What are Launch Modes in Android?", "They control how a new instance of an activity is associated with the current task. Types are Standard, SingleTop, SingleTask, and SingleInstance.", listOf("Samsung", "Infosys", "Tech Mahindra"))
                15 -> PrepQuestion("How do you prevent memory leaks in Android?", "By avoiding static references to context/views, cancelling active Coroutine scopes or Rx subscriptions in lifecycle teardowns, and using WeakReferences where applicable.", listOf("Google", "Amazon", "Paytm"))
                16 -> PrepQuestion("Explain Clean Architecture in Android.", "It organizes code into domain, data, and presentation layers, ensuring the core business rules (domain) are completely independent of frameworks, databases, and UI.", listOf("Amazon", "Meta", "TCS"))
                17 -> PrepQuestion("What is WorkManager?", "WorkManager is an Android Jetpack library for scheduling deferrable, guaranteed background work that needs to run even if the app exits or device restarts.", listOf("Google", "Uber", "Wipro"))
                18 -> PrepQuestion("What is viewBinding?", "View Binding is a feature that allows you to more easily write code that interacts with views by generating a binding class for each XML layout file.", listOf("TCS", "Cognizant", "HCL"))
                19 -> PrepQuestion("What is the difference between StateFlow and SharedFlow?", "StateFlow is a hot observable stream that represents a state and always retains the latest value, whereas SharedFlow emits values to multiple collectors without retaining a state by default.", listOf("Google", "Salesforce", "Infosys"))
                20 -> PrepQuestion("How do you optimize Android UI performance?", "By minimizing layouts hierarchy nesting, reusing Compose layouts efficiently, avoiding recomposition overhead by using remember and keys, and using profileable builds.", listOf("Netflix", "Adobe", "Cognizant"))
                21 -> PrepQuestion("What is an Intent in Android?", "An Intent is a messaging object you can use to request an action from another app component, such as starting an activity, service, or broadcast receiver.", listOf("Intel", "TCS", "Wipro"))
                22 -> PrepQuestion("What are Broadcast Receivers?", "They are components that allow your app to register for system or application-wide events, such as boot completion or battery low alerts.", listOf("Google", "Samsung", "Infosys"))
                23 -> PrepQuestion("Explain Content Providers.", "Content Providers manage access to a structured set of data, letting your app share data securely with other applications on the device.", listOf("Google", "Microsoft", "TCS"))
                24 -> PrepQuestion("What is a ViewModel and why do we use it?", "A ViewModel stores and manages UI-related data in a lifecycle-conscious way, allowing data to survive configuration changes like screen rotations.", listOf("Google", "Oracle", "Cognizant"))
                25 -> PrepQuestion("What is the difference between local and instrumented tests?", "Local unit tests run on the JVM (fast, no device needed), while instrumented tests run on a real or emulated Android device (slower, accesses framework APIs).", listOf("Microsoft", "Infosys", "Wipro"))
                26 -> PrepQuestion("What is ProGuard / R8?", "ProGuard/R8 are code shrinking and obfuscation tools that reduce the app size, remove unused code, and make it difficult to reverse-engineer.", listOf("TCS", "Google", "Cognizant"))
                27 -> PrepQuestion("What is the use of the Android Manifest file?", "It describes essential information about your app to the Android build tools, the OS, and Google Play, listing permissions, activities, services, and receivers.", listOf("HCL", "Wipro", "Infosys"))
                28 -> PrepQuestion("What is Jetpack Navigation?", "It is a framework for managing navigation within an Android app, supporting single-activity architectures, argument passing, and backstack management.", listOf("Amazon", "Google", "TCS"))
                29 -> PrepQuestion("Explain ANR (Application Not Responding).", "ANR occurs when the main UI thread of an Android app is blocked for more than 5 seconds, forcing the OS to prompt the user to close the app.", listOf("Google", "Microsoft", "Accenture"))
                30 -> PrepQuestion("How do you handle safe API responses in Android?", "By wrapping network calls in a try-catch block or using custom Retrofit CallAdapters to map responses into a sealed Result class with Success/Error states.", listOf("Meta", "Flipkart", "TCS"))
                else -> PrepQuestion("What is Android?", "Android is a mobile operating system based on a modified version of the Linux kernel and other open source software.", listOf("Google"))
            }
        }
    )

    private fun getFullStackDeveloperDetails() = PrepRoleDetails(
        title = "Full Stack Developer",
        selectionProcess = listOf(
            "Round 1: Online Technical & Coding Assessment (DSA, JavaScript, Databases)",
            "Round 2: Frontend Architecture & Coding Interview (React/Vue/Angular, CSS, State)",
            "Round 3: Backend Systems & APIs Interview (Node.js/Express, Database Design, Scalability)",
            "Round 4: Full Stack Integration & System Design (Caching, Load Balancers, Cloud Deployment)",
            "Round 5: HR / Culture Fit Assessment"
        ),
        techSkills = listOf("HTML/CSS/JS", "TypeScript", "React / Next.js", "Node.js & Express", "SQL (PostgreSQL/MySQL)", "NoSQL (MongoDB/Redis)", "REST & GraphQL APIs", "Docker & CI/CD"),
        toolsSkills = listOf("VS Code", "Git & GitHub", "Postman", "AWS / Vercel", "Webpack / Vite", "Jira / Trello"),
        softSkills = listOf("System-level Thinking", "Clear Verbal Communication", "Cross-team Collaboration", "Problem-solving under pressure"),
        roadmap30Days = listOf(
            "Day 1 to 10: Deep dive into ES6+ JavaScript, CSS layouts (Flexbox/Grid), and core React concepts (Hooks, State, Props).",
            "Day 11 to 20: Build backend servers with Node.js, create REST endpoints with Express, and handle database connections.",
            "Day 21 to 30: Connect React with the Node API, implement JWT authentication, and deploy the application to Vercel/Render."
        ),
        roadmap3Months = listOf(
            "Month 1: Perfect modern frontend skills with React, Next.js (SSR/ISR), tailwind CSS, and basic SQL/NoSQL databases.",
            "Month 2: Master backend architectures, design schemas, configure security (CORS, JWT), and handle file storage (S3).",
            "Month 3: Implement Docker containers, write unit & integration tests, set up a CI/CD pipeline, and practice web system design."
        ),
        prepSteps = listOf(
            "Step 1: Code every day in JS/TS. Understand async/await, closures, event loop, and promises thoroughly.",
            "Step 2: Pick a stack (e.g. MERN) and stick to it. Master React hooks, state management, and Node backend patterns.",
            "Step 3: Build 1 high-quality e-commerce or SaaS app from scratch, complete with databases, payments, and admin dashboard.",
            "Step 4: Understand basic system design: Caching, database indexing, REST vs GraphQL, and deployment scaling.",
            "Step 5: Review the 30 core Full Stack questions and practice building small features under a time limit."
        ),
        interviewQuestions = (1..30).map { i ->
            when (i) {
                1 -> PrepQuestion("What is the difference between virtual DOM and real DOM?", "The virtual DOM is a lightweight, in-memory representation of the real DOM. React updates the virtual DOM first, compares it (diffing), and syncs only the changes with the real DOM (reconciliation).", listOf("Meta", "Infosys", "TCS"))
                2 -> PrepQuestion("What is the Event Loop in JavaScript?", "It is a mechanism that allows JS to perform non-blocking I/O operations despite being single-threaded, by offloading tasks to the system and processing callbacks from the queue.", listOf("Google", "Netflix", "Wipro"))
                3 -> PrepQuestion("Explain Server-Side Rendering (SSR) vs Client-Side Rendering (CSR).", "In SSR, HTML is generated on the server for every request and sent to the client, improving SEO. In CSR, the browser downloads a minimal HTML and renders UI dynamically via JS.", listOf("Amazon", "Microsoft", "Cognizant"))
                4 -> PrepQuestion("How does JWT authentication work?", "A user logs in, the server generates a cryptographically signed token containing payloads, sends it to the client, and the client sends this token in the Authorization header for subsequent API requests.", listOf("Auth0", "TCS", "Accenture"))
                5 -> PrepQuestion("What is the difference between SQL and NoSQL?", "SQL databases are relational, schema-based, table-structured, and scale vertically, while NoSQL databases are non-relational, dynamic-schema, document/key-value based, and scale horizontally.", listOf("Oracle", "Infosys", "Capgemini"))
                6 -> PrepQuestion("What are REST API guidelines?", "Representational State Transfer guidelines include using HTTP verbs (GET, POST, PUT, DELETE), statelessness, uniform interfaces, and exposing resources via clean URIs.", listOf("eBay", "TCS", "Cognizant"))
                7 -> PrepQuestion("What is Middleware in Express?", "Middleware functions are functions that have access to the request and response objects, and can execute code, modify requests, or end the request-response cycle.", listOf("Microsoft", "Amazon", "Wipro"))
                8 -> PrepQuestion("How do you optimize web application loading speed?", "By using code splitting, image optimization/lazy loading, browser caching, minifying JS/CSS, using CDNs, and choosing SSR/SSG where appropriate.", listOf("Google", "Meta", "Flipkart"))
                9 -> PrepQuestion("What is CORS and how do you handle it?", "Cross-Origin Resource Sharing is a browser security feature that restricts cross-origin HTTP requests. It is handled by configuring the Access-Control-Allow-Origin headers on the server.", listOf("Amazon", "Infosys", "HCL"))
                10 -> PrepQuestion("What is the difference between let, const, and var?", "var is function-scoped and hoisted. let and const are block-scoped; let allows reassignment while const creates a read-only reference.", listOf("TCS", "Wipro", "Cognizant"))
                11 -> PrepQuestion("What is a Promise in JavaScript?", "An object representing the eventual completion or failure of an asynchronous operation, which can be in a Pending, Fulfilled, or Rejected state.", listOf("Google", "Adobe", "L&T"))
                12 -> PrepQuestion("What is the difference between HTTP and HTTPS?", "HTTPS is HTTP with SSL/TLS encryption, securing the data transmitted between the client and server from eavesdropping and tampering.", listOf("Cloudflare", "TCS", "Infosys"))
                13 -> PrepQuestion("Explain standard database indexing.", "Indexing creates a data structure (like B-Tree) that allows fast lookups on specific columns, avoiding a full table scan, but at the cost of slower writes and extra storage.", listOf("Microsoft", "Oracle", "Wipro"))
                14 -> PrepQuestion("What is Git and what is a Merge Conflict?", "Git is a distributed version control system. A merge conflict occurs when two branches make changes to the same line of a file and Git cannot resolve it automatically.", listOf("GitHub", "Infosys", "Cognizant"))
                15 -> PrepQuestion("What is Docker and why use it?", "Docker is a tool that packages an application and its dependencies into a lightweight container, ensuring it runs consistently across different computing environments.", listOf("AWS", "TCS", "Accenture"))
                16 -> PrepQuestion("What is GraphQL vs REST?", "GraphQL is a query language for APIs that lets clients request exactly the data they need, returning a single response, unlike REST which requires multiple hits to fixed endpoints.", listOf("Meta", "Shopify", "Capgemini"))
                17 -> PrepQuestion("Explain MVC architecture pattern.", "Model-View-Controller separates the application into Model (data/logic), View (UI display), and Controller (handles inputs and updates Model/View).", listOf("TCS", "Infosys", "Wipro"))
                18 -> PrepQuestion("What is CSS Flexbox vs Grid?", "Flexbox is designed for one-dimensional layouts (a row OR a column), whereas CSS Grid is intended for two-dimensional layouts (rows AND columns simultaneously).", listOf("Cognizant", "HCL", "Mindtree"))
                19 -> PrepQuestion("What is the use of Redux or React Context?", "They are state management solutions used to share state globally across multiple components, avoiding the pain of manually passing props down through many levels (prop drilling).", listOf("Amazon", "Google", "TCS"))
                20 -> PrepQuestion("Explain XSS (Cross-Site Scripting) and how to prevent it.", "XSS is a vulnerability where malicious scripts are injected into trusted websites. Prevention involves escaping user inputs, validating data, and implementing Content Security Policy.", listOf("Google", "Microsoft", "Wipro"))
                21 -> PrepQuestion("What is CSRF and how is it prevented?", "Cross-Site Request Forgery tricks a user into executing unwanted actions on a site they are logged into. Prevented by using anti-CSRF tokens, SameSite cookies, and headers validation.", listOf("Paypal", "TCS", "Infosys"))
                22 -> PrepQuestion("Explain standard web socket connection.", "A protocol that provides full-duplex, persistent, real-time communication channels over a single TCP connection between client and server.", listOf("Slack", "Cognizant", "Accenture"))
                23 -> PrepQuestion("What is lazy loading in React?", "A technique where components are loaded only when they are needed on the screen (using React.lazy and Suspense), reducing the initial bundle size.", listOf("Netflix", "TCS", "Infosys"))
                24 -> PrepQuestion("What is the difference between PUT and PATCH?", "PUT replaces the entire resource with the updated payload, whereas PATCH applies a partial update to the resource.", listOf("Google", "Amazon", "Wipro"))
                25 -> PrepQuestion("What is database normalization?", "A process of organizing relational database tables to reduce data redundancy and improve data integrity, typically involving 1NF, 2NF, and 3NF rules.", listOf("Oracle", "TCS", "Cognizant"))
                26 -> PrepQuestion("What is a CDN (Content Delivery Network)?", "A distributed network of servers that caches static web content (images, JS, CSS) closer to users, reducing latency and page load speeds.", listOf("Akamai", "Infosys", "Wipro"))
                27 -> PrepQuestion("What are React Hooks?", "Functions that let functional components hook into React state and lifecycle features (e.g. useState, useEffect, useContext).", listOf("Meta", "TCS", "Capgemini"))
                28 -> PrepQuestion("What is continuous integration (CI) and deployment (CD)?", "CI automatically builds and tests code changes when pushed to a repository. CD automatically deploys those tested changes to production environments.", listOf("Atlassian", "Accenture", "Cognizant"))
                29 -> PrepQuestion("Explain the differences between LocalStorage, SessionStorage, and Cookies.", "LocalStorage persists data indefinitely with 5MB capacity; SessionStorage persists only for the tab session; Cookies store 4KB and are sent with every HTTP request.", listOf("TCS", "Infosys", "Wipro"))
                30 -> PrepQuestion("How do you handle error boundaries in React?", "By defining a class component with getDerivedStateFromError() and componentDidCatch() to catch JS errors anywhere in their child component tree and display a fallback UI.", listOf("Meta", "Google", "TCS"))
                else -> PrepQuestion("What is Full Stack?", "Full Stack development involves working on both front-end and back-end portions of an application.", listOf("Microsoft"))
            }
        }
    )

    private fun getDataScientistDetails() = PrepRoleDetails(
        title = "Data Scientist",
        selectionProcess = listOf(
            "Round 1: Technical MCQ & Coding Quiz (Python, Statistics, Basic ML)",
            "Round 2: Data Science Case Study / Take-home Assignment (Data cleaning, Modeling, Insights)",
            "Round 3: Advanced Machine Learning & Deep Learning theory and algorithms",
            "Round 4: Statistical Inference & Coding Interview (Pandas, SQL, Statistics distributions)",
            "Round 5: HR / Hiring Manager Round"
        ),
        techSkills = listOf("Python / R", "Statistics & Probability", "SQL Database", "Supervised/Unsupervised ML", "Pandas & Numpy", "Scikit-Learn", "Deep Learning (TensorFlow/PyTorch)", "Data Visualization"),
        toolsSkills = listOf("Jupyter Notebooks", "Git", "Tableau / Power BI", "Docker", "AWS / GCP", "Kaggle Platforms"),
        softSkills = listOf("Data Storytelling", "Business Acumen", "Structured Problem Solving", "Curiosity"),
        roadmap30Days = listOf(
            "Day 1 to 10: Master Python core library, statistics fundamentals (probability, distributions, central limit theorem), and SQL commands.",
            "Day 11 to 20: Learn data manipulation with Pandas/NumPy, data visualization with Matplotlib/Seaborn, and basic linear regression.",
            "Day 21 to 30: Implement classic ML models (logistic regression, decision trees, KNN) using Scikit-Learn and understand model evaluation metrics."
        ),
        roadmap3Months = listOf(
            "Month 1: Perfect statistics, exploratory data analysis (EDA), SQL joins/aggregations, and baseline ML models.",
            "Month 2: Learn ensemble models (Random Forest, XGBoost), feature engineering, hyperparameter tuning, and dimensionality reduction.",
            "Month 3: Explore basic Deep Learning, NLP or Computer Vision foundations, MLOps, model deployment, and practice real case studies."
        ),
        prepSteps = listOf(
            "Step 1: Solidify mathematical foundations. Linear algebra, calculus, and mathematical statistics are crucial.",
            "Step 2: Excel in SQL. 90% of a Data Scientist's job involves querying data. Master window functions and CTEs.",
            "Step 3: Build 3 end-to-end projects. Include a data cleaning project, a predictive modeling project, and an interactive dashboard.",
            "Step 4: Practice explaining ML concepts in simple terms. Interviewers love applicants who can bridge technical models with business value.",
            "Step 5: Solve SQL questions on LeetCode and ML theory problems from the top 30 questions list."
        ),
        interviewQuestions = (1..30).map { i ->
            when (i) {
                1 -> PrepQuestion("Explain the bias-variance tradeoff.", "Bias represents error from erroneous assumptions in the model (underfitting). Variance represents error from sensitivity to training data (overfitting). Minimizing both is the goal.", listOf("Google", "Amazon", "TCS"))
                2 -> PrepQuestion("What is overfitting and how do you prevent it?", "Overfitting occurs when a model learns noise too well, failing to generalize. Prevent it using regularization (L1/L2), cross-validation, pruning trees, or dropouts.", listOf("Microsoft", "Infosys", "Wipro"))
                3 -> PrepQuestion("What is a p-value?", "The p-value is the probability of obtaining test results at least as extreme as the observed results, assuming the null hypothesis is true. A p-value < 0.05 rejects the null.", listOf("Google", "Facebook", "Cognizant"))
                4 -> PrepQuestion("Explain Random Forest vs Decision Tree.", "A Decision Tree builds a single sequence of rule splits to predict outcomes. A Random Forest is an ensemble of many decision trees trained on random subsets, combining their votes to reduce variance.", listOf("Amazon", "Accenture", "TCS"))
                5 -> PrepQuestion("How do you handle missing or imbalanced data?", "Missing: Imputation (mean/median/KNN) or dropping. Imbalanced: Resampling (SMOTE/under-sampling), tree-based algorithms, or switching evaluation metrics to F1-Score/ROC-AUC.", listOf("Netflix", "TCS", "Infosys"))
                6 -> PrepQuestion("What is gradient descent?", "An optimization algorithm used to minimize a loss function by iteratively moving in the direction of steepest descent, determined by the negative of the gradient.", listOf("Google", "Uber", "Wipro"))
                7 -> PrepQuestion("Explain the differences between L1 (Lasso) and L2 (Ridge) regularization.", "L1 regularization adds absolute penalty to weights and can shrink coefficients to zero (feature selection). L2 regularization adds squared penalty and shrinks coefficients close to zero but not completely.", listOf("Microsoft", "IBM", "Cognizant"))
                8 -> PrepQuestion("What is a confusion matrix?", "A table used to describe the performance of a classification model, showing True Positives, True Negatives, False Positives (Type I error), and False Negatives (Type II error).", listOf("TCS", "Wipro", "Infosys"))
                9 -> PrepQuestion("What is ROC-AUC?", "ROC is a probability curve plotting True Positive Rate vs False Positive Rate. AUC represents the degree of separability, showing how well the model distinguishes between classes.", listOf("Google", "Amazon", "Capgemini"))
                10 -> PrepQuestion("Explain K-Means clustering.", "An unsupervised algorithm that partitions data into K distinct clusters by assigning points to the nearest centroid and updating centroids to be the mean of points in that cluster.", listOf("Apple", "Cognizant", "TCS"))
                11 -> PrepQuestion("What is the difference between supervised and unsupervised learning?", "Supervised learning uses labeled training data to predict outcomes (classification, regression). Unsupervised learning finds hidden patterns in unlabeled data (clustering).", listOf("Intel", "Infosys", "Wipro"))
                12 -> PrepQuestion("What is the Central Limit Theorem?", "It states that the distribution of the sample mean of a large number of independent, identically distributed variables will show a normal distribution, regardless of the population's underlying distribution.", listOf("Google", "Facebook", "TCS"))
                13 -> PrepQuestion("Explain logistic regression.", "A classification algorithm used to model the probability of a binary event using the logistic sigmoid function, mapping output values between 0 and 1.", listOf("Microsoft", "TCS", "Cognizant"))
                14 -> PrepQuestion("What is feature engineering?", "The process of using domain knowledge to extract or create new features from raw data, enhancing the predictive accuracy and performance of machine learning models.", listOf("Amazon", "Netflix", "Wipro"))
                15 -> PrepQuestion("What is cross-validation?", "A technique to evaluate model performance by partitioning data into subsets (e.g. K-folds), training the model on some folds, and validating it on the remaining fold, rotating this process.", listOf("eBay", "Infosys", "Capgemini"))
                16 -> PrepQuestion("Explain Deep Learning.", "A subfield of machine learning based on artificial neural networks with multiple hidden layers that automatically extract features from complex, high-dimensional data.", listOf("Google", "Nvidia", "TCS"))
                17 -> PrepQuestion("What is dimensionality reduction and why is it used?", "Reducing the number of input variables in a dataset (e.g. via PCA), used to simplify models, speed up training, visualize data, and avoid the curse of dimensionality.", listOf("Microsoft", "Wipro", "Cognizant"))
                18 -> PrepQuestion("What is SQL window function?", "A function that performs a calculation across a set of table rows that are somehow related to the current row, without collapsing the rows (e.g. ROW_NUMBER(), RANK()).", listOf("Oracle", "TCS", "Infosys"))
                19 -> PrepQuestion("What is A/B testing?", "A statistical methodology to compare two versions of a webpage or product (A vs B) against a metric to determine which one performs statistically significantly better.", listOf("Meta", "Amazon", "Wipro"))
                20 -> PrepQuestion("Explain standard normalization vs standardization.", "Normalization scales data to [0, 1] range. Standardization centers data to mean = 0 and standard deviation = 1 (z-score scaling).", listOf("TCS", "Cognizant", "Accenture"))
                21 -> PrepQuestion("What is correlation vs causation?", "Correlation is a statistical measure expressing the strength of linear relationship between variables. Causation implies that one event is the direct result of the occurrence of the other.", listOf("Google", "Infosys", "Mindtree"))
                22 -> PrepQuestion("What is a Random Forest OOB error?", "Out-Of-Bag error is a method of measuring prediction error in random forests by evaluating predictions on training observations that were not included in the bootstrap sample.", listOf("Amazon", "TCS", "Capgemini"))
                23 -> PrepQuestion("Explain support vector machines (SVM).", "A supervised algorithm that finds an optimal hyperplane that maximizes the margin of separation between different classes in high-dimensional space.", listOf("Microsoft", "Wipro", "Cognizant"))
                24 -> PrepQuestion("What is NLP (Natural Language Processing)?", "A branch of AI that helps computers understand, interpret, and manipulate human language in text or speech formats.", listOf("Google", "TCS", "Infosys"))
                25 -> PrepQuestion("What is Naive Bayes classifier?", "A probabilistic classifier based on Bayes' Theorem with a 'naive' assumption of conditional independence among features.", listOf("Cognizant", "HCL", "Wipro"))
                26 -> PrepQuestion("What is the difference between Type I and Type II errors?", "Type I error is a False Positive (rejecting a true null hypothesis). Type II error is a False Negative (failing to reject a false null hypothesis).", listOf("Google", "TCS", "Accenture"))
                27 -> PrepQuestion("Explain eigenvectors and eigenvalues.", "An eigenvector is a vector that only scales (does not change direction) during a linear transformation. The factor by which it scales is its eigenvalue.", listOf("Microsoft", "Google", "Infosys"))
                28 -> PrepQuestion("What is gradient boosting?", "An ensemble ML technique that builds trees sequentially, with each new tree attempting to correct the residual errors made by the previous combination of trees.", listOf("Amazon", "Wipro", "Capgemini"))
                29 -> PrepQuestion("Explain standard TF-IDF.", "Term Frequency-Inverse Document Frequency is a numerical statistic reflecting how important a word is to a document in a collection or corpus.", listOf("Infosys", "TCS", "Cognizant"))
                30 -> PrepQuestion("What is standard MLOps?", "Machine Learning Operations focuses on standardizing and automating the lifecycle of machine learning models from training, packaging, and testing to deployment and monitoring.", listOf("Google", "AWS", "Wipro"))
                else -> PrepQuestion("What is Data Science?", "Data Science is the domain of study that deals with vast volumes of data using modern tools and techniques to find unseen patterns.", listOf("TCS"))
            }
        }
    )

    private fun getGenericDetailsForRole(title: String, process: List<String>, tech: List<String>, tools: List<String>, soft: List<String>, r30: List<String>, r3m: List<String>, steps: List<String>, qList: List<Pair<String, String>>) = PrepRoleDetails(
        title = title,
        selectionProcess = process,
        techSkills = tech,
        toolsSkills = tools,
        softSkills = soft,
        roadmap30Days = r30,
        roadmap3Months = r3m,
        prepSteps = steps,
        interviewQuestions = (1..30).map { i ->
            val pair = qList.getOrNull((i - 1) % qList.size) ?: Pair("Question $i for $title", "Detailed preparation answer for $title including fundamentals and practical considerations.")
            val companiesList = when (i % 5) {
                0 -> listOf("Google", "Amazon", "Microsoft")
                1 -> listOf("TCS", "Infosys", "Wipro")
                2 -> listOf("Meta", "Apple", "Netflix")
                3 -> listOf("Cognizant", "Accenture", "Capgemini")
                else -> listOf("TCS", "Cognizant", "HCL")
            }
            PrepQuestion(pair.first, pair.second, companiesList)
        }
    )

    private fun getDevOpsDetails(): PrepRoleDetails {
        val q = listOf(
            Pair("What is Docker and how does it work?", "Docker is a containerization platform that packages an app and its dependencies in a container to run on any Linux server."),
            Pair("Explain the CI/CD pipeline concept.", "Continuous Integration and Continuous Deployment automate code testing, building, and deploying from git commits directly to servers."),
            Pair("What is Kubernetes (K8s)?", "Kubernetes is an open-source container orchestration tool that automates deploying, scaling, and managing containerized applications."),
            Pair("Explain Infrastructure as Code (IaC).", "IaC is the managing and provisioning of infrastructure through code (like Terraform) rather than manual processes."),
            Pair("What is the difference between Git Merge and Git Rebase?", "Merge creates a new commit joining two histories, whereas Rebase reapplies commits on top of another base tip, creating a linear history."),
            Pair("Explain Jenkins and standard automation jobs.", "Jenkins is a self-contained, open-source automation server which can be used to automate tasks associated with building, testing, and deploying software.")
        )
        return getGenericDetailsForRole(
            title = "DevOps Engineer",
            process = listOf("Round 1: Linux & Scripting Assessment", "Round 2: CI/CD & Docker Hands-on Test", "Round 3: Kubernetes & IaC (Terraform) Technical Round", "Round 4: Architecture Design & Troubleshooting", "Round 5: HR Fit"),
            tech = listOf("Linux Administration", "CI/CD (Jenkins, GitHub Actions)", "Containerization (Docker)", "Container Orchestration (Kubernetes)", "IaC (Terraform)", "Scripting (Python, Shell)", "Monitoring (Prometheus, Grafana)", "Cloud (AWS/Azure)"),
            tools = listOf("Docker", "Kubernetes", "Terraform", "Jenkins", "Ansible", "Git"),
            soft = listOf("System Reliability Thinking", "Problem Solving", "Collaborative Communication", "Proactive Monitoring"),
            r30 = listOf("Day 1-10: Master Linux Command Line, Bash Scripting, and Git internals.", "Day 11-20: Understand Docker containerization, networks, volumes, and multi-stage builds.", "Day 21-30: Build CI/CD pipelines in Jenkins or GitHub Actions and deploy a static site to AWS."),
            r3m = listOf("Month 1: Command Linux Administration, automate scripts, and containerize applications with Docker.", "Month 2: Master Kubernetes orchestration, Helm, Terraform IaC, and AWS cloud management.", "Month 3: Implement centralized logging (ELK), monitoring (Prometheus/Grafana), and set up robust CI/CD pipelines."),
            steps = listOf("Step 1: Get comfortable with Linux. Everything in DevOps sits on Linux.", "Step 2: Understand Containerization. Build multiple dockerized applications.", "Step 3: Setup CI/CD. Automate standard build and test phases.", "Step 4: Learn IaC with Terraform. Provision server instances dynamically.", "Step 5: Master Kubernetes fundamentals. Understand Pods, Services, and Deployments."),
            qList = q
        )
    }

    private fun getUIUXDetails(): PrepRoleDetails {
        val q = listOf(
            Pair("What is your design process?", "My process includes: User Research, Ideation, Wireframing, High-Fidelity Prototyping, and Usability Testing."),
            Pair("Explain UI vs UX.", "UI is the visual interface (buttons, colors, fonts), while UX is the overall feel and ease of use (navigation, flow, emotions)."),
            Pair("What is Figma and how do you use design systems?", "Figma is a collaborative interface design tool. A design system is a collection of reusable components and styles that ensure consistency across an app."),
            Pair("Explain standard typography rules in UI.", "Keep hierarchy, limit font families to 2, ensure proper line height, and verify contrast ratios for accessibility."),
            Pair("What is a User Persona?", "A semi-fictional representation of an ideal customer based on real data and research, helping guide design decisions."),
            Pair("What is Usability Testing?", "A technique used to evaluate a product by testing it on real users, observing their difficulties, and correcting the UI flow accordingly.")
        )
        return getGenericDetailsForRole(
            title = "UI/UX Designer",
            process = listOf("Round 1: Portfolio Review & Walkthrough", "Round 2: Design Challenge (Whiteboard or 48hr take-home)", "Round 3: Figma Mastery & Technical Specs Round", "Round 4: Product Strategy & Collaboration with Devs", "Round 5: HR Round"),
            tech = listOf("User Research", "Information Architecture", "Wireframing", "Prototyping", "Interaction Design", "Color Theory", "Typography", "Usability Testing"),
            tools = listOf("Figma", "Adobe XD", "Adobe Illustrator", "Miro", "Zeplin"),
            soft = listOf("Empathy", "Storytelling", "Active Listening", "Collaborative Mindset"),
            r30 = listOf("Day 1-10: Learn design principles, color theory, typography, and master Figma basics.", "Day 11-20: Conduct user research, define user personas, and create low-fidelity wireframes.", "Day 21-30: Build high-fidelity UI screens in Figma, add prototypes, and do basic usability testing."),
            r3m = listOf("Month 1: Excel in Figma tools, learn visual hierarchy, typography, and create a full mobile UI mock.", "Month 2: Deep dive into User Research methods, Information Architecture, and creating detailed design systems.", "Month 3: Build a rich portfolio containing 3 case studies, practice presentation, and learn developer handoff."),
            steps = listOf("Step 1: Learn Figma from top to bottom. Component libraries, auto layout, and variants are standard.", "Step 2: Understand User Empathy. Learn how to ask the right questions in research.", "Step 3: Keep a clean visual diary. Study modern apps and recreate their layouts to build muscle memory.", "Step 4: Draft 3 high-quality UX Case Studies. Show the 'Why' behind every button placement.", "Step 5: Practice whiteboard design drills under a tight clock."),
            qList = q
        )
    }

    private fun getCybersecurityDetails(): PrepRoleDetails {
        val q = listOf(
            Pair("What is the CIA Triad?", "Confidentiality (prevent unauthorized reading), Integrity (prevent unauthorized alteration), and Availability (guarantee uptime and resource access)."),
            Pair("Explain the difference between Encryption and Hashing.", "Encryption is two-way (data can be decrypted back to plaintext), while Hashing is a one-way mathematical function that produces a unique fingerprint."),
            Pair("What is a SQL Injection (SQLi) and how do you prevent it?", "An exploit where malicious SQL statements are inserted into inputs. Prevented using parameterized queries and prepared statements."),
            Pair("What is the difference between VA and PT?", "Vulnerability Assessment finds security weaknesses, while Penetration Testing actively exploits those weaknesses to measure the impact."),
            Pair("Explain firewalls and their working principles.", "A firewall monitors and filters incoming and outgoing network traffic based on an organization's previously established security rules."),
            Pair("What is a SIEM tool?", "Security Information and Event Management systems collect and analyze log data from various devices to detect potential security threats.")
        )
        return getGenericDetailsForRole(
            title = "Cybersecurity Analyst",
            process = listOf("Round 1: Network & Security Quiz", "Round 2: Hands-on Hacking / Security Lab Scenario", "Round 3: Log Analysis & Incident Response Tech Round", "Round 4: Threat Intelligence & Standards Compliance Round", "Round 5: HR Round"),
            tech = listOf("Network Security", "Ethical Hacking", "Cryptography", "SIEM Tools", "Firewalls", "Incident Response", "Vulnerability Management", "Linux Security"),
            tools = listOf("Kali Linux", "Wireshark", "Nmap", "Metasploit", "Splunk", "Burp Suite"),
            soft = listOf("Analytical Mindset", "Ethics & Confidentiality", "Attention to Detail", "Crisis Management"),
            r30 = listOf("Day 1-10: Master TCP/IP networking, subnetting, and Linux security fundamentals.", "Day 11-20: Learn to use security scanner tools like Nmap, Wireshark, and identify vulnerabilities.", "Day 21-30: Understand symmetric/asymmetric cryptography, SIEM log analysis, and firewall configurations."),
            r3m = listOf("Month 1: Perfect networking fundamentals, command Linux, and learn standard security protocols.", "Month 2: Study OWASP Top 10 vulnerabilities, use Kali Linux tools, and practice security incident handling.", "Month 3: Learn security compliance standards (ISO 27001, SOC2), build security projects, and prepare for Security+."),
            steps = listOf("Step 1: Excel in Networking. You cannot secure what you do not understand.", "Step 2: Learn command line inside out (Bash and PowerShell).", "Step 3: Understand the attacker mind. Study OWASP Top 10 web vulnerabilities.", "Step 4: Build a home lab. Secure your router, run Kali VM, and intercept traffic with Wireshark.", "Step 5: Review the 30 core security questions and earn foundational certs like Security+."),
            qList = q
        )
    }

    private fun getCloudDetails(): PrepRoleDetails {
        val q = listOf(
            Pair("What is Serverless computing?", "A cloud execution model where the cloud provider manages the server allocation, automatically scaling compute resources (like AWS Lambda)."),
            Pair("Explain AWS S3 vs EBS.", "S3 is object storage (ideal for static files, photos, backups, accessed via URLs), while EBS is block storage (virtual hard drive attached to a single EC2 instance)."),
            Pair("What is a VPC (Virtual Private Cloud)?", "VPC is a secure, isolated private network space carved out inside a public cloud provider where you launch resources."),
            Pair("How do you design a highly available cloud application?", "By using load balancers, deploying across multiple Availability Zones, setting up autoscaling groups, and utilizing managed databases."),
            Pair("What is IAM in Cloud?", "Identity and Access Management controls authentication and authorization, defining who (users/roles) can access what cloud resources."),
            Pair("Explain Cloud Security Best Practices.", "Follow the principle of least privilege, encrypt data at rest and in transit, enable multi-factor authentication, and monitor cloud logs.")
        )
        return getGenericDetailsForRole(
            title = "Cloud Engineer",
            process = listOf("Round 1: Cloud Architecture MCQ & Basic Linux", "Round 2: Cloud Infrastructure Setup Hands-on Round", "Round 3: Network, VPC, and Cloud Security Tech Round", "Round 4: Scaling, Cost Optimization & System Design", "Round 5: HR Round"),
            tech = listOf("AWS / Azure / GCP Services", "Cloud Networking & VPCs", "Cloud Security & IAM", "Load Balancers & Scaling", "Serverless Architecture", "Infrastructure as Code", "Linux", "Backup & Recovery"),
            tools = listOf("AWS Console", "Azure Portal", "Terraform", "AWS CLI", "Docker", "Git"),
            soft = listOf("System Scalability Focus", "Structural Planning", "Continuous Learning", "Cost Consciousness"),
            r30 = listOf("Day 1-10: Learn cloud computing basics (IaaS/PaaS/SaaS) and set up an AWS Free Tier account.", "Day 11-20: Configure custom VPCs, subnets, route tables, and launch EC2 virtual instances.", "Day 21-30: Understand IAM roles, set up S3 object storage, and write serverless AWS Lambda functions."),
            r3m = listOf("Month 1: Study core cloud compute, storage, databases, and configure secure IAM policies.", "Month 2: Create advanced VPC architectures, load balancers, autoscaling groups, and secure traffic flow.", "Month 3: Learn IaC (Terraform), serverless cloud setups, monitoring tools, and prepare for cloud certifications."),
            steps = listOf("Step 1: Choose one primary cloud platform (AWS is highly recommended) and learn its core services.", "Step 2: Master VPC concepts. Draw network architectures showing public/private subnets and NAT gateways.", "Step 3: Implement Security. Always use least privilege IAM models and encrypt data.", "Step 4: Build a project. Deploy a web app behind an autoscaling group with a load balancer.", "Step 5: Prepare for the AWS Solutions Architect Associate exam questions."),
            qList = q
        )
    }

    private fun getAIMLDetails(): PrepRoleDetails {
        val q = listOf(
            Pair("What is a Neural Network?", "An AI model inspired by the structure of biological brains, consisting of layers of interconnected artificial nodes that learn patterns by adjusting weights during training."),
            Pair("Explain supervised vs unsupervised learning.", "Supervised learning trains on labeled data (mapping inputs to known targets). Unsupervised learning trains on unlabeled data, finding structures on its own (like clustering)."),
            Pair("What is the role of activation functions?", "Activation functions introduce non-linearity into neural networks, allowing them to learn complex patterns instead of just linear transformations."),
            Pair("What is overfitting and how do you prevent it in Deep Learning?", "Overfitting is when a model memorizes training data but fails on test data. Prevent it using Dropout, Early Stopping, Data Augmentation, and L1/L2 Regularization."),
            Pair("Explain NLP (Natural Language Processing) and Transformers.", "NLP helps computers understand human language. Transformers are neural network architectures utilizing 'attention mechanisms' to process sequential text data in parallel."),
            Pair("What is Computer Vision and CNN?", "Computer Vision processes images/videos. Convolutional Neural Networks (CNNs) are deep networks that automatically learn hierarchical features (edges, shapes) from images.")
        )
        return getGenericDetailsForRole(
            title = "AI/ML Engineer",
            process = listOf("Round 1: Machine Learning & Python coding challenge", "Round 2: Math & Probability whiteboard round", "Round 3: Deep Learning & Frameworks (PyTorch/TF) Technical Interview", "Round 4: Machine Learning System Design (MLOps, scale)", "Round 5: HR Fit"),
            tech = listOf("Python", "Linear Algebra & Calculus", "Machine Learning Algorithms", "Deep Learning Frameworks (PyTorch/TensorFlow)", "NLP / Computer Vision", "MLOps & Deployments", "Statistics", "Data Pipeline (Spark)"),
            tools = listOf("PyTorch", "TensorFlow", "Jupyter", "Hugging Face", "Docker", "Git"),
            soft = listOf("Research Curiosity", "Algorithmic Thinking", "Data Ethics Awareness", "Complex Problem Solving"),
            r30 = listOf("Day 1-10: Revise Linear Algebra, Calculus, and core Machine Learning algorithms in Scikit-Learn.", "Day 11-20: Deep dive into PyTorch or TensorFlow, building basic feedforward neural networks.", "Day 21-30: Build a text classifier (NLP) or image classification model (CNN) and evaluate precision/recall."),
            r3m = listOf("Month 1: Command ML mathematics, statistics, and standard predictive models using Python.", "Month 2: Master Deep Learning models (CNNs, RNNs, Transformers) and hyperparameter tuning.", "Month 3: Implement MLOps pipelines (MLflow, Docker, FastAPI), deploy ML models, and solve coding exercises."),
            steps = listOf("Step 1: Strengthen your math foundations (Linear Algebra, Multi-variable Calculus, Probability).", "Step 2: Master Python libraries. Numpy, Pandas, and Scikit-Learn should feel like second nature.", "Step 3: Deeply learn PyTorch or TensorFlow. Build neural networks from scratch.", "Step 4: Create complex ML projects. Train a model, build a FastAPI around it, and containerize it with Docker.", "Step 5: Master ML System Design concepts: data ingestion, training pipelines, model monitoring, and latency."),
            qList = q
        )
    }

    private fun getDataAnalystDetails(): PrepRoleDetails {
        val q = listOf(
            Pair("What is a JOIN in SQL? List the types.", "A JOIN combines rows from two or more tables based on a related column. Types are: INNER, LEFT, RIGHT, and FULL outer join."),
            Pair("How do you handle missing or duplicate values?", "Duplicate values: Identify and drop using SQL DISTINCT or Pandas drop_duplicates(). Missing values: Delete, impute with mean/median, or flag with a placeholder."),
            Pair("Explain the difference between Power BI and Tableau.", "Both are BI tools. Tableau is known for heavy custom data visualization and exploration. Power BI is highly integrated with the Microsoft ecosystem and easier to learn."),
            Pair("What is the difference between descriptive and predictive analytics?", "Descriptive analytics looks at historical data to see what happened. Predictive analytics uses statistical models to forecast what is likely to happen next."),
            Pair("Explain CTEs in SQL.", "Common Table Expressions create temporary, named result sets that exist within the scope of a single SQL query, improving query readability and structure."),
            Pair("How do you design a dashboard that tells a story?", "Identify the target audience, select key KPIs, arrange metrics logically (top-level summary to details), use clear color coding, and keep layouts clean.")
        )
        return getGenericDetailsForRole(
            title = "Data Analyst",
            process = listOf("Round 1: SQL & Excel Technical Test", "Round 2: Python / Data Manipulation Live Coding", "Round 3: Dashboard Design & Data Interpretation Interview", "Round 4: Case Study & Business Problem Solving Round", "Round 5: HR Round"),
            tech = listOf("SQL (Aggregations, Window Functions, Joins)", "Excel (Pivot Tables, VLOOKUP, Power Query)", "Data Visualization (Tableau/Power BI)", "Python (Pandas, Numpy)", "Statistics & Metrics", "Data Cleaning", "Data Storytelling", "Reporting"),
            tools = listOf("PostgreSQL / MySQL", "Tableau", "Power BI", "Excel", "Jupyter", "Git"),
            soft = listOf("Analytical curiosity", "Storytelling with Data", "Business understanding", "Attention to data quality"),
            r30 = listOf("Day 1-10: Master SQL intermediate concepts like aggregate functions, Joins, subqueries, and Excel VLOOKUP/Pivot tables.", "Day 11-20: Learn Power BI or Tableau to build interactive charts and clean data using Power Query.", "Day 21-30: Learn Python Pandas library, perform Exploratory Data Analysis, and write an insightful analysis report."),
            r3m = listOf("Month 1: Perfect SQL queries, Excel analytics capabilities, and basic data extraction techniques.", "Month 2: Master Business Intelligence tools (Power BI/Tableau) and design high-impact corporate dashboards.", "Month 3: Deep dive into statistical analysis, Python data analysis libraries, and practice business case studies."),
            steps = listOf("Step 1: Master SQL. You must write queries quickly and accurately. Practice joins and group bys daily.", "Step 2: Learn a BI tool like Tableau or Power BI. Build 2 interactive dashboard templates.", "Step 3: Excel is still king. Know VLOOKUP, INDEX-MATCH, Pivot Tables, and conditional formatting.", "Step 4: Learn Data Storytelling. Focus on how to explain data charts to non-technical stakeholders.", "Step 5: Put 3 dashboard case studies on a public portfolio like GitHub or Tableau Public."),
            qList = q
        )
    }

    private fun getProductManagerDetails(): PrepRoleDetails {
        val q = listOf(
            Pair("How do you prioritize features for a product?", "Using frameworks like RICE (Reach, Impact, Confidence, Effort) or MoSCoW (Must-have, Should-have, Could-have, Won't-have) based on goals."),
            Pair("Explain standard Product Lifecycle phases.", "Introduction (launch), Growth (expansion), Maturity (saturation), and Decline (sunset or pivot)."),
            Pair("What is a KPI? List some standard product metrics.", "Key Performance Indicators measure success. Examples: DAU/MAU (engagement), Churn Rate (retention), Customer Acquisition Cost, and LTV (Customer Lifetime Value)."),
            Pair("How do you gather user feedback?", "Through user interviews, surveys, usability testing, feedback loops, app analytics, customer support tickets, and NPS surveys."),
            Pair("What is an MVP (Minimum Viable Product)?", "A version of a product with just enough features to satisfy early customers and provide feedback for future development, minimizing initial dev costs."),
            Pair("How do you resolve a conflict between engineers and designers?", "Align both on the customer problem and business goals, review data or user research, and run collaborative brainstorming to find a compromise.")
        )
        return getGenericDetailsForRole(
            title = "Product Manager",
            process = listOf("Round 1: Resume Screen & Phone Call", "Round 2: Product Sense & Design Case Study", "Round 3: Analytical & Metrics Interview (Guesstimates, KPIs)", "Round 4: Technical Collaboration & Agile Execution Round", "Round 5: Leadership & Culture Round"),
            tech = listOf("Product Strategy", "User Research & Personas", "Agile & Scrum Methodologies", "Feature Prioritization Frameworks", "Product Metrics & Analytics", "Wireframing", "A/B Testing", "Market Analysis"),
            tools = listOf("Jira / Confluence", "Figma (Viewing & Feedback)", "Mixpanel / Google Analytics", "Miro", "Trello"),
            soft = listOf("Extreme Empathy", "Stakeholder Management", "Clear Communication", "Influence without Authority"),
            r30 = listOf("Day 1-10: Read product case studies, learn standard PM prioritization frameworks (RICE, MoSCoW), and study product metrics.", "Day 11-20: Learn wireframing tools, study user journey mapping, and understand Agile/Scrum roles.", "Day 21-30: Practice product design questions (e.g. 'design an elevator for blind people') and analytical metric questions."),
            r3m = listOf("Month 1: Understand PM roles, learn market research, prioritization methods, and standard agile systems.", "Month 2: Master wireframing, creating detailed PRDs (Product Requirement Documents), and tracking product KPIs.", "Month 3: Deepen analytical abilities, practice guesstimates, run mock product cases, and master developer collaboration."),
            steps = listOf("Step 1: Learn how to think like a PM. Focus on user-centric problem-solving.", "Step 2: Master key frameworks. Read 'Cracking the PM Interview' and 'Decode and Conquer'.", "Step 3: Master Metrics. Learn how to correlate user behaviors with business KPIs (North Star metric).", "Step 4: Understand the tech. You don't need to write code, but you must understand API, DB, and cloud concepts.", "Step 5: Practice mock PM interviews. Product design, product metrics, and strategic case studies are mandatory."),
            qList = q
        )
    }

    private fun getQADetails(): PrepRoleDetails {
        val q = listOf(
            Pair("What is the difference between Severity and Priority?", "Severity measures the technical impact of a bug on the system (e.g., app crashes). Priority measures the business urgency to fix the bug (e.g., typo on home page)."),
            Pair("What is the difference between Manual and Automation testing?", "Manual testing requires human testers to execute test cases one by one without scripts. Automation uses script-based tools (like Selenium) to run tests automatically."),
            Pair("Explain Boundary Value Analysis (BVA).", "A test design technique where testing is focused on the boundary values of inputs, as bugs frequently occur at the boundaries (e.g., testing input 10 for range 1-10)."),
            Pair("What is Selenium Grid?", "A tool that allows running test cases on different machines and browsers in parallel, reducing overall test execution time."),
            Pair("Explain Regression Testing.", "Re-running functional and non-functional tests to ensure that previously developed and tested software still performs correctly after a new code change."),
            Pair("What is API Testing and how do you use Postman?", "API testing verifies that APIs meet functionality, reliability, and security expectations. Postman is a tool used to send requests, create collections, and assert responses.")
        )
        return getGenericDetailsForRole(
            title = "Software QA/Testing Engineer",
            process = listOf("Round 1: Software Testing & QA Fundamentals Quiz", "Round 2: Automation Coding Test (Java/Python + Selenium)", "Round 3: API Testing & Database Query Round", "Round 4: Test Case Writing & Bug Life Cycle Scenario Round", "Round 5: HR Round"),
            tech = listOf("Manual Testing Concepts", "Automation Testing (Selenium, Appium)", "Languages (Java/Python)", "API Testing (Postman, RestAssured)", "SQL (Data Validation)", "CI/CD Integration", "Test Case Design", "SDLC & STLC"),
            tools = listOf("Selenium Webdriver", "Postman", "Jira (Bug Tracking)", "Jenkins", "Git", "TestNG / JUnit"),
            soft = listOf("Analytical Detail-oriented", "Bug Hunting Persistence", "Diplomatic Communication", "Process Integrity"),
            r30 = listOf("Day 1-10: Learn Manual Testing fundamentals, STLC, Bug Life Cycle, and test case writing templates.", "Day 11-20: Master Java or Python basics and set up Selenium WebDriver for web automation testing.", "Day 21-30: Write API test assertions in Postman, write basic SQL queries, and configure automated test suites."),
            r3m = listOf("Month 1: Master Software Testing Life Cycle (STLC), write rigorous test cases, and understand standard bug reporting.", "Month 2: Build a web automation testing framework using Selenium, Java/Python, and TestNG/JUnit.", "Month 3: Master API testing (Postman/RestAssured), database testing, and integrate automated test suites with Jenkins CI/CD."),
            steps = listOf("Step 1: Master the fundamentals. Learn how to write highly detailed, unambiguous bug reports.", "Step 2: Pick an automation language. Java or Python is highly recommended for automation QA.", "Step 3: Master Selenium WebDriver. Understand locator strategies (XPath, CSS selector) and page object models.", "Step 4: Learn API testing with Postman. Automate simple collection runs with test assertions.", "Step 5: Get certified. Look at ISTQB Foundation Level, which is highly regarded by hiring managers."),
            qList = q
        )
    }
}
