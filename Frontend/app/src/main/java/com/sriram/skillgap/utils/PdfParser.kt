package com.sriram.skillgap.utils

import android.content.Context
import android.net.Uri
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper

object PdfParser {
    fun extractText(context: Context, uri: Uri): String {
        PDFBoxResourceLoader.init(context)
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val document = PDDocument.load(inputStream)
            val stripper = PDFTextStripper()
            val text = stripper.getText(document)
            document.close()
            text
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun parseSkills(text: String): List<String> {
        val allPossibleSkills = JobData.roles.flatMap { it.requiredSkills + it.atsKeywords }.distinct()
        
        // Lowercase and normalize whitespace
        val textLower = text.lowercase().replace(Regex("\\s+"), " ")
        
        // Comprehensive mapping of all skills to their potential synonyms & variations
        val synonyms = mapOf(
            // Android / Mobile
            "kotlin" to listOf("kotlin", "kmp", "kotlin-multiplatform"),
            "jetpack compose" to listOf("jetpack compose", "compose ui", "compose"),
            "mvvm" to listOf("mvvm", "model-view-viewmodel", "model view viewmodel"),
            "retrofit" to listOf("retrofit", "retrofit2"),
            "room" to listOf("room database", "room persistence", "room ORM", "room"),
            "dagger hilt" to listOf("hilt", "dagger", "dagger-hilt", "dagger hilt"),
            "unit testing" to listOf("unit testing", "junit", "mockito", "espresso", "instrumentation testing", "ui testing"),
            "coroutines" to listOf("coroutines", "kotlin coroutines", "kotlin flows", "kotlin flow", "flow", "flows", "asynchronous"),
            "git" to listOf("git", "github", "gitlab", "bitbucket", "version control"),
            "native mobile" to listOf("native mobile", "android", "ios", "swift", "kotlin mobile", "swiftui", "cocoapods"),
            "architecture components" to listOf("architecture components", "livedata", "viewmodel", "workmanager", "navigation component"),
            "lifecycle" to listOf("lifecycle", "activity lifecycle", "fragment lifecycle", "android lifecycle"),
            "clean architecture" to listOf("clean architecture", "onion architecture", "solid principles", "dependency inversion"),
            "rest api" to listOf("rest api", "restful api", "restful apis", "rest apis", "http api", "web api"),

            // Data Science / Machine Learning
            "python" to listOf("python", "py", "ipython", "cpython"),
            "r" to listOf("r", "r-programming", "r programming", "r lang"),
            "sql" to listOf("sql", "mysql", "postgresql", "sqlite", "pl/sql", "tsql", "oracle sql", "ms-sql", "mariadb", "nosql"),
            "machine learning" to listOf("machine learning", "ml", "supervised learning", "unsupervised learning", "semi-supervised learning", "algorithms"),
            "pandas" to listOf("pandas", "numpy", "matplotlib", "seaborn", "scipy"),
            "scikit-learn" to listOf("scikit-learn", "scikit learn", "sklearn"),
            "deep learning" to listOf("deep learning", "neural network", "neural networks", "keras", "tensorflow", "pytorch", "cnn", "rnn", "lstm"),
            "statistics" to listOf("statistics", "statistical modeling", "probability", "anova", "hypothesis testing", "regression"),
            "predictive modeling" to listOf("predictive modeling", "predictive model", "regression", "classification", "scikit-learn", "forecast"),
            "nlp" to listOf("nlp", "natural language processing", "spacy", "nltk", "transformers", "bert", "gpt"),
            "big data" to listOf("big data", "hadoop", "spark", "hive", "mapreduce", "pyspark"),
            "data visualization" to listOf("data visualization", "charts", "plots", "seaborn", "matplotlib", "powerbi", "tableau", "visualization"),
            "feature engineering" to listOf("feature engineering", "feature selection", "dimensionality reduction", "pca"),

            // Full Stack / Web
            "react" to listOf("react", "react.js", "reactjs", "react js"),
            "node.js" to listOf("node.js", "nodejs", "node js", "node"),
            "express" to listOf("express", "express.js", "expressjs", "express js"),
            "mongodb" to listOf("mongodb", "mongo", "mongoose"),
            "javascript" to listOf("javascript", "js", "es6", "ecmascript"),
            "typescript" to listOf("typescript", "ts"),
            "docker" to listOf("docker", "container", "containers", "dockerfile"),
            "mern stack" to listOf("mern stack", "mern", "mongodb", "express", "react", "node"),
            "restful api" to listOf("restful api", "rest api", "restful apis", "rest apis", "rest-api", "web api", "apis"),
            "frontend/backend integration" to listOf("frontend/backend", "fullstack", "full stack", "api integration", "client-server"),
            "state management" to listOf("state management", "redux", "recoil", "mobx", "context api", "zustand"),

            // DevOps
            "aws" to listOf("aws", "amazon web services", "ec2", "s3", "rds", "lambda", "iam", "cloudformation"),
            "kubernetes" to listOf("kubernetes", "k8s", "helm", "minikube"),
            "jenkins" to listOf("jenkins", "ci/cd", "ci-cd", "continuous integration"),
            "terraform" to listOf("terraform", "iac", "infrastructure as code"),
            "linux" to listOf("linux", "ubuntu", "debian", "redhat", "centos", "bash", "unix"),
            "ci/cd" to listOf("ci/cd", "ci-cd", "github actions", "gitlab ci", "travis", "circleci", "continuous integration"),
            "shell scripting" to listOf("shell scripting", "bash scripting", "shell script", "bash script", "powershell"),
            "infrastructure as code" to listOf("infrastructure as code", "iac", "terraform", "ansible", "pulumi"),
            "containerization" to listOf("containerization", "docker", "podman", "kubernetes", "containers"),
            "automation" to listOf("automation", "scripting", "automated", "ansible", "chef", "puppet"),
            "cloud architecture" to listOf("cloud architecture", "cloud computing", "cloud setup", "cloud migration"),

            // UI/UX Design
            "figma" to listOf("figma", "figma design"),
            "adobe xd" to listOf("adobe xd", "xd"),
            "user research" to listOf("user research", "user interviews", "surveys", "persona", "usability studies"),
            "prototyping" to listOf("prototyping", "prototypes", "interactive prototypes", "wireframe"),
            "wireframing" to listOf("wireframing", "wireframes", "lo-fi design"),
            "color theory" to listOf("color theory", "palette", "colors", "contrast"),
            "typography" to listOf("typography", "fonts", "readability", "hierarchy"),
            "usability testing" to listOf("usability testing", "user testing", "heuristic evaluation"),
            "interaction design" to listOf("interaction design", "ixd", "ui/ux", "ui ux", "user interface"),
            "visual design" to listOf("visual design", "graphic design", "figma", "sketch", "adobe xd"),
            "design thinking" to listOf("design thinking", "user-centered design", "wireframing", "prototyping"),
            "user persona" to listOf("user persona", "user personas", "user research", "customer journey"),

            // Cybersecurity
            "network security" to listOf("network security", "firewalls", "vpn", "routing", "ips", "ids", "wireshark"),
            "penetration testing" to listOf("penetration testing", "pen testing", "pentest", "ethical hacking", "metasploit"),
            "ethical hacking" to listOf("ethical hacking", "hacking", "kali linux", "wireshark"),
            "siem" to listOf("siem", "splunk", "arcsight", "log analysis", "security onion"),
            "firewalls" to listOf("firewall", "firewalls", "palo alto", "fortinet", "cisco asa"),
            "cryptography" to listOf("cryptography", "encryption", "decryption", "ssl", "tls", "aes", "rsa"),
            "risk assessment" to listOf("risk assessment", "threat modeling", "security audit", "vulnerability assessment"),
            "information security" to listOf("information security", "infosec", "cybersecurity", "cyber security"),
            "threat intelligence" to listOf("threat intelligence", "threat scanning", "malware analysis"),
            "incident response" to listOf("incident response", "disaster recovery", "incident management"),
            "vulnerability management" to listOf("vulnerability management", "vulnerability scanning", "penetration testing", "pentesting"),
            "compliance" to listOf("compliance", "gdpr", "hipaa", "pci-dss", "iso 27001", "soc2"),

            // Cloud Engineering
            "azure" to listOf("azure", "microsoft azure", "active directory"),
            "gcp" to listOf("gcp", "google cloud", "google cloud platform"),
            "cloud security" to listOf("cloud security", "iam", "kms", "cloud trail", "shield"),
            "networking" to listOf("networking", "vpc", "subnet", "dns", "load balancer", "tcp/ip"),
            "storage" to listOf("storage", "s3", "blob storage", "cloud storage", "ebs"),
            "compute" to listOf("compute", "ec2", "virtual machines", "app engine", "cloud run"),
            "serverless" to listOf("serverless", "lambda", "cloud functions", "azure functions"),
            "cloud computing" to listOf("cloud computing", "cloud platform", "cloud migration"),
            "virtualization" to listOf("virtualization", "vmware", "hyper-v", "virtual machines", "vbox"),
            "scalability" to listOf("scalability", "autoscaling", "high availability", "load balancing"),
            "reliability" to listOf("reliability", "fault tolerance", "disaster recovery", "backup"),
            "cost optimization" to listOf("cost optimization", "finops", "budget planning", "cost savings"),

            // AI / ML
            "tensorflow" to listOf("tensorflow", "tf"),
            "pytorch" to listOf("pytorch", "torch"),
            "deep learning models" to listOf("deep learning", "neural network", "neural networks", "keras", "tensorflow", "pytorch"),
            "neural networks" to listOf("neural network", "neural networks", "cnn", "rnn", "transformer"),
            "nlp processing" to listOf("nlp", "natural language processing", "spacy", "nltk", "transformers"),
            "computer vision" to listOf("computer vision", "opencv", "image processing", "yolo"),
            "model deployment" to listOf("model deployment", "mlops", "flask", "fastapi", "docker", "triton"),
            "mathematics" to listOf("mathematics", "linear algebra", "calculus", "probability", "statistics"),

            // Data Analysis
            "excel" to listOf("excel", "ms excel", "spreadsheets", "vlookup"),
            "tableau" to listOf("tableau", "tableau desktop", "tableau public"),
            "power bi" to listOf("power bi", "powerbi", "pbi"),
            "business intelligence" to listOf("business intelligence", "bi", "tableau", "power bi", "looker"),
            "data cleaning" to listOf("data cleaning", "data preprocessing", "data wrangling", "pandas"),
            "dashboard creation" to listOf("dashboard", "dashboards", "data visualization"),
            "statistical analysis" to listOf("statistical analysis", "hypothesis testing", "regression"),
            "reporting" to listOf("reporting", "reports", "pdf report", "automated reports"),

            // Product Management
            "product roadmap" to listOf("product roadmap", "roadmap", "prioritization", "jira"),
            "agile/scrum" to listOf("agile", "scrum", "kanban", "sprints"),
            "a/b testing" to listOf("a/b testing", "ab testing", "split testing"),
            "market analysis" to listOf("market analysis", "competitor analysis", "market research"),
            "product metrics" to listOf("product metrics", "kpi", "okr", "analytics", "amplitude", "mixpanel"),
            "product lifecycle" to listOf("product lifecycle", "product roadmap", "product launch"),
            "user personas" to listOf("user personas", "user persona", "customer journey"),
            "agile methodologies" to listOf("agile", "scrum", "kanban", "sprints"),
            "feature prioritization" to listOf("feature prioritization", "prioritization", "backlog grooming"),
            "stakeholder management" to listOf("stakeholder management", "stakeholder relations", "communication"),

            // QA / Testing
            "selenium" to listOf("selenium", "selenium webdriver"),
            "junit" to listOf("junit", "junit5"),
            "testng" to listOf("testng"),
            "manual testing" to listOf("manual testing", "exploratory testing", "black box testing"),
            "automation testing" to listOf("automation testing", "test automation", "selenium", "cypress", "playwright"),
            "java" to listOf("java", "jdk", "oops"),
            "api testing (postman)" to listOf("api testing", "postman", "rest assured"),
            "jira" to listOf("jira", "atlassian", "confluence"),
            "test automation" to listOf("test automation", "selenium", "appium", "automated testing", "junit"),
            "bug tracking" to listOf("bug tracking", "jira", "bugzilla", "issue tracking"),
            "regression testing" to listOf("regression", "integration testing", "system testing"),
            "ci/cd integration" to listOf("ci/cd", "continuous integration", "jenkins", "github actions"),
            "test case design" to listOf("test case", "test cases", "test design", "test planning")
        )

        return allPossibleSkills.filter { skill ->
            val skillLower = skill.lowercase()
            val listToCheck = synonyms[skillLower] ?: listOf(skillLower)
            
            listToCheck.any { term ->
                val escapedTerm = Regex.escape(term)
                val isPureAlpha = term.all { it.isLetter() || it.isWhitespace() }
                
                if (isPureAlpha) {
                    // Alphabetic terms need word boundaries to avoid subset matches (e.g., "digital" matching "git")
                    val pattern = "(?i)\\b$escapedTerm\\b"
                    textLower.contains(Regex(pattern))
                } else {
                    // Terms with symbols (like C++, C#, .NET, CI/CD, UI/UX) need non-alphanumeric boundary wrapping
                    val pattern = "(?i)(?:^|[^a-zA-Z0-9])($escapedTerm)(?:$|[^a-zA-Z0-9])"
                    textLower.contains(Regex(pattern))
                }
            }
        }
    }
}

