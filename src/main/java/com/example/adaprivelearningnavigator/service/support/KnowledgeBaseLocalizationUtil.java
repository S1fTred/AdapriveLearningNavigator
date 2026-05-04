package com.example.adaprivelearningnavigator.service.support;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public final class KnowledgeBaseLocalizationUtil {

    public static final String ROLE_BASED_CATEGORY = "ROLE_BASED";
    public static final String SKILL_BASED_CATEGORY = "SKILL_BASED";

    private static final Set<String> ROLE_BASED_ROADMAP_CODES = Set.of(
            "frontend",
            "backend",
            "full-stack",
            "devops",
            "devsecops",
            "data-analyst",
            "ai-engineer",
            "ai-data-scientist",
            "data-engineer",
            "android",
            "machine-learning",
            "postgresql-dba",
            "ios",
            "blockchain",
            "qa",
            "software-architect",
            "cyber-security",
            "ux-design",
            "technical-writer",
            "game-developer",
            "server-side-game-developer",
            "mlops",
            "product-manager",
            "engineering-manager",
            "devrel",
            "bi-analyst",
            "backend-beginner",
            "frontend-beginner",
            "devops-beginner",
            "ai-product-builder",
            "java-backend",
            "java-mobile"
    );

    private static final Set<String> MVP_ROLE_BASED_ROADMAP_CODES = Set.of(
            "frontend",
            "backend",
            "full-stack",
            "devops",
            "devsecops",
            "data-analyst",
            "ai-engineer",
            "ai-data-scientist",
            "data-engineer",
            "android",
            "machine-learning",
            "postgresql-dba",
            "ios",
            "blockchain",
            "qa",
            "software-architect",
            "cyber-security",
            "ux-design",
            "technical-writer",
            "game-developer",
            "server-side-game-developer",
            "mlops",
            "product-manager",
            "engineering-manager",
            "devrel",
            "bi-analyst"
    );

    private static final Set<String> MVP_SKILL_BASED_ROADMAP_CODES = Set.of(
            "sql",
            "computer-science",
            "react",
            "vue",
            "angular",
            "javascript",
            "typescript",
            "nodejs",
            "python",
            "system-design",
            "java",
            "aspnet-core",
            "api-design",
            "spring-boot",
            "flutter",
            "cpp",
            "rust",
            "golang",
            "software-design-architecture",
            "graphql",
            "react-native",
            "design-system",
            "prompt-engineering",
            "mongodb",
            "linux",
            "kubernetes",
            "docker",
            "aws",
            "terraform",
            "datastructures-and-algorithms",
            "redis",
            "git-github",
            "php",
            "cloudflare",
            "ai-red-teaming",
            "ai-agents",
            "nextjs",
            "code-review",
            "kotlin",
            "html",
            "css",
            "swift-ui",
            "shell-bash",
            "laravel",
            "elasticsearch",
            "wordpress",
            "django",
            "ruby",
            "ruby-on-rails",
            "claude-code",
            "vibe-coding"
    );

    private static final Map<String, String> ROLE_CODE_OVERRIDES = Map.ofEntries(
            Map.entry("android", "Android-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("ai-agents", "AI-\u0430\u0433\u0435\u043d\u0442\u044b"),
            Map.entry("ai-data-scientist", "AI-\u0441\u043f\u0435\u0446\u0438\u0430\u043b\u0438\u0441\u0442 \u043f\u043e Data Science"),
            Map.entry("ai-engineer", "AI-\u0438\u043d\u0436\u0435\u043d\u0435\u0440"),
            Map.entry("ai-product-builder", "\u0420\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a AI-\u043f\u0440\u043e\u0434\u0443\u043a\u0442\u043e\u0432"),
            Map.entry("ai-red-teaming", "AI Red Teaming"),
            Map.entry("angular", "Angular-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("api-design", "\u041f\u0440\u043e\u0435\u043a\u0442\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u0435 API"),
            Map.entry("aspnet-core", "ASP.NET Core-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("aws", "AWS"),
            Map.entry("backend", "Backend-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("backend-beginner", "Backend-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a \u0434\u043b\u044f \u043d\u0430\u0447\u0438\u043d\u0430\u044e\u0449\u0438\u0445"),
            Map.entry("bi-analyst", "BI-\u0430\u043d\u0430\u043b\u0438\u0442\u0438\u043a"),
            Map.entry("blockchain", "Blockchain-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("computer-science", "\u0418\u043d\u0444\u043e\u0440\u043c\u0430\u0442\u0438\u043a\u0430"),
            Map.entry("code-review", "\u041f\u0438\u0440\u0430\u043c\u0438\u0434\u0430 Code Review"),
            Map.entry("cpp", "C++-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("css", "CSS"),
            Map.entry("cyber-security", "\u0421\u043f\u0435\u0446\u0438\u0430\u043b\u0438\u0441\u0442 \u043f\u043e \u043a\u0438\u0431\u0435\u0440\u0431\u0435\u0437\u043e\u043f\u0430\u0441\u043d\u043e\u0441\u0442\u0438"),
            Map.entry("data-analyst", "\u0410\u043d\u0430\u043b\u0438\u0442\u0438\u043a \u0434\u0430\u043d\u043d\u044b\u0445"),
            Map.entry("data-engineer", "\u0418\u043d\u0436\u0435\u043d\u0435\u0440 \u0434\u0430\u043d\u043d\u044b\u0445"),
            Map.entry("datastructures-and-algorithms", "\u0421\u0442\u0440\u0443\u043a\u0442\u0443\u0440\u044b \u0434\u0430\u043d\u043d\u044b\u0445 \u0438 \u0430\u043b\u0433\u043e\u0440\u0438\u0442\u043c\u044b"),
            Map.entry("design-system", "\u0414\u0438\u0437\u0430\u0439\u043d-\u0441\u0438\u0441\u0442\u0435\u043c\u044b"),
            Map.entry("devops", "DevOps-\u0438\u043d\u0436\u0435\u043d\u0435\u0440"),
            Map.entry("devops-beginner", "DevOps-\u0438\u043d\u0436\u0435\u043d\u0435\u0440 \u0434\u043b\u044f \u043d\u0430\u0447\u0438\u043d\u0430\u044e\u0449\u0438\u0445"),
            Map.entry("devrel", "DevRel-\u0441\u043f\u0435\u0446\u0438\u0430\u043b\u0438\u0441\u0442"),
            Map.entry("devsecops", "DevSecOps-\u0438\u043d\u0436\u0435\u043d\u0435\u0440"),
            Map.entry("django", "Django"),
            Map.entry("docker", "Docker"),
            Map.entry("elasticsearch", "Elasticsearch"),
            Map.entry("engineering-manager", "\u0418\u043d\u0436\u0435\u043d\u0435\u0440\u043d\u044b\u0439 \u043c\u0435\u043d\u0435\u0434\u0436\u0435\u0440"),
            Map.entry("flutter", "Flutter-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("frontend", "Frontend-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("frontend-beginner", "Frontend-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a \u0434\u043b\u044f \u043d\u0430\u0447\u0438\u043d\u0430\u044e\u0449\u0438\u0445"),
            Map.entry("full-stack", "Full Stack-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("game-developer", "\u0420\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a \u0438\u0433\u0440"),
            Map.entry("git-github", "Git \u0438 GitHub"),
            Map.entry("git-github-beginner", "Git \u0438 GitHub \u0434\u043b\u044f \u043d\u0430\u0447\u0438\u043d\u0430\u044e\u0449\u0438\u0445"),
            Map.entry("golang", "Go-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("graphql", "GraphQL"),
            Map.entry("html", "HTML"),
            Map.entry("ios", "iOS-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("java", "Java-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("java-backend", "Java Backend-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("java-mobile", "Java Mobile-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("javascript", "JavaScript"),
            Map.entry("kotlin", "Kotlin"),
            Map.entry("kubernetes", "Kubernetes"),
            Map.entry("laravel", "Laravel"),
            Map.entry("leetcode", "LeetCode"),
            Map.entry("linux", "Linux"),
            Map.entry("machine-learning", "ML-\u0438\u043d\u0436\u0435\u043d\u0435\u0440"),
            Map.entry("mlops", "MLOps-\u0438\u043d\u0436\u0435\u043d\u0435\u0440"),
            Map.entry("mongodb", "MongoDB"),
            Map.entry("nextjs", "Next.js"),
            Map.entry("nodejs", "Node.js-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("php", "PHP-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("postgresql-dba", "\u0410\u0434\u043c\u0438\u043d\u0438\u0441\u0442\u0440\u0430\u0442\u043e\u0440 PostgreSQL"),
            Map.entry("product-manager", "\u041f\u0440\u043e\u0434\u0443\u043a\u0442\u043e\u0432\u044b\u0439 \u043c\u0435\u043d\u0435\u0434\u0436\u0435\u0440"),
            Map.entry("prompt-engineering", "\u041f\u0440\u043e\u043c\u043f\u0442-\u0438\u043d\u0436\u0438\u043d\u0438\u0440\u0438\u043d\u0433"),
            Map.entry("python", "Python-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("qa", "QA-\u0438\u043d\u0436\u0435\u043d\u0435\u0440"),
            Map.entry("react", "React-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("react-native", "React Native-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("ruby-on-rails", "Ruby on Rails-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("rust", "Rust-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("scala", "Scala"),
            Map.entry("server-side-game-developer", "\u0421\u0435\u0440\u0432\u0435\u0440\u043d\u044b\u0439 \u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a \u0438\u0433\u0440"),
            Map.entry("shell-bash", "Bash"),
            Map.entry("software-architect", "\u0410\u0440\u0445\u0438\u0442\u0435\u043a\u0442\u043e\u0440 \u041f\u041e"),
            Map.entry("software-design-architecture", "\u041f\u0440\u043e\u0435\u043a\u0442\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u0435 \u0438 \u0430\u0440\u0445\u0438\u0442\u0435\u043a\u0442\u0443\u0440\u0430 \u041f\u041e"),
            Map.entry("spring-boot", "Spring Boot-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("swift-ui", "SwiftUI"),
            Map.entry("system-design", "System Design"),
            Map.entry("sql", "SQL"),
            Map.entry("terraform", "Terraform"),
            Map.entry("typescript", "TypeScript"),
            Map.entry("technical-writer", "\u0422\u0435\u0445\u043d\u0438\u0447\u0435\u0441\u043a\u0438\u0439 \u043f\u0438\u0441\u0430\u0442\u0435\u043b\u044c"),
            Map.entry("ux-design", "UX-\u0434\u0438\u0437\u0430\u0439\u043d"),
            Map.entry("vibe-coding", "Vibe Coding"),
            Map.entry("wordpress", "WordPress"),
            Map.entry("vue", "Vue-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a")
    );

    private static final Map<String, String> EXACT_TOPIC_OVERRIDES = Map.ofEntries(
            Map.entry("Pick a Language", "\u0412\u044b\u0431\u043e\u0440 \u044f\u0437\u044b\u043a\u0430"),
            Map.entry("Basics of OOP", "\u041e\u0441\u043d\u043e\u0432\u044b \u041e\u041e\u041f"),
            Map.entry("The Fundamentals", "\u041e\u0441\u043d\u043e\u0432\u044b"),
            Map.entry("Introduction", "\u0412\u0432\u0435\u0434\u0435\u043d\u0438\u0435"),
            Map.entry("Learn the Basics", "\u0418\u0437\u0443\u0447\u0438\u0442\u0435 \u043e\u0441\u043d\u043e\u0432\u044b"),
            Map.entry("More Exercises", "\u0414\u043e\u043f\u043e\u043b\u043d\u0438\u0442\u0435\u043b\u044c\u043d\u044b\u0435 \u0443\u043f\u0440\u0430\u0436\u043d\u0435\u043d\u0438\u044f"),
            Map.entry("More Commands", "\u0414\u043e\u043f\u043e\u043b\u043d\u0438\u0442\u0435\u043b\u044c\u043d\u044b\u0435 \u043a\u043e\u043c\u0430\u043d\u0434\u044b"),
            Map.entry("Usecases", "\u0421\u0446\u0435\u043d\u0430\u0440\u0438\u0438 \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u044f"),
            Map.entry("Transactions", "\u0422\u0440\u0430\u043d\u0437\u0430\u043a\u0446\u0438\u0438"),
            Map.entry("Loops", "\u0426\u0438\u043a\u043b\u044b"),
            Map.entry("Data Types", "\u0422\u0438\u043f\u044b \u0434\u0430\u043d\u043d\u044b\u0445"),
            Map.entry("Conditionals", "\u0423\u0441\u043b\u043e\u0432\u043d\u044b\u0435 \u043a\u043e\u043d\u0441\u0442\u0440\u0443\u043a\u0446\u0438\u0438"),
            Map.entry("Lists", "\u0421\u043f\u0438\u0441\u043a\u0438"),
            Map.entry("Array", "\u041c\u0430\u0441\u0441\u0438\u0432"),
            Map.entry("Arrays", "\u041c\u0430\u0441\u0441\u0438\u0432\u044b"),
            Map.entry("Strings", "\u0421\u0442\u0440\u043e\u043a\u0438"),
            Map.entry("Boolean", "\u041b\u043e\u0433\u0438\u0447\u0435\u0441\u043a\u0438\u0439 \u0442\u0438\u043f"),
            Map.entry("Variables", "\u041f\u0435\u0440\u0435\u043c\u0435\u043d\u043d\u044b\u0435"),
            Map.entry("Operators", "\u041e\u043f\u0435\u0440\u0430\u0442\u043e\u0440\u044b"),
            Map.entry("Switch", "\u041e\u043f\u0435\u0440\u0430\u0442\u043e\u0440 switch"),
            Map.entry("Functions", "\u0424\u0443\u043d\u043a\u0446\u0438\u0438"),
            Map.entry("Closures", "\u0417\u0430\u043c\u044b\u043a\u0430\u043d\u0438\u044f"),
            Map.entry("Recursion", "\u0420\u0435\u043a\u0443\u0440\u0441\u0438\u044f"),
            Map.entry("Type Casting", "\u041f\u0440\u0438\u0432\u0435\u0434\u0435\u043d\u0438\u0435 \u0442\u0438\u043f\u043e\u0432"),
            Map.entry("Inheritance", "\u041d\u0430\u0441\u043b\u0435\u0434\u043e\u0432\u0430\u043d\u0438\u0435"),
            Map.entry("Interfaces", "\u0418\u043d\u0442\u0435\u0440\u0444\u0435\u0439\u0441\u044b"),
            Map.entry("Object Oriented Programming", "\u041e\u0431\u044a\u0435\u043a\u0442\u043d\u043e-\u043e\u0440\u0438\u0435\u043d\u0442\u0438\u0440\u043e\u0432\u0430\u043d\u043d\u043e\u0435 \u043f\u0440\u043e\u0433\u0440\u0430\u043c\u043c\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u0435"),
            Map.entry("Version Control", "\u041a\u043e\u043d\u0442\u0440\u043e\u043b\u044c \u0432\u0435\u0440\u0441\u0438\u0439"),
            Map.entry("Version Control Systems", "\u0421\u0438\u0441\u0442\u0435\u043c\u044b \u043a\u043e\u043d\u0442\u0440\u043e\u043b\u044f \u0432\u0435\u0440\u0441\u0438\u0439"),
            Map.entry("Development IDE", "\u0421\u0440\u0435\u0434\u0430 \u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u043a\u0438"),
            Map.entry("Basics of Kotlin", "\u041e\u0441\u043d\u043e\u0432\u044b Kotlin"),
            Map.entry("Data Structures and Algorithms", "\u0421\u0442\u0440\u0443\u043a\u0442\u0443\u0440\u044b \u0434\u0430\u043d\u043d\u044b\u0445 \u0438 \u0430\u043b\u0433\u043e\u0440\u0438\u0442\u043c\u044b"),
            Map.entry("What is and how to use Gradle?", "\u0427\u0442\u043e \u0442\u0430\u043a\u043e\u0435 Gradle \u0438 \u043a\u0430\u043a \u0435\u0433\u043e \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442\u044c?"),
            Map.entry("Create a Basic Hello World App", "\u0421\u043e\u0437\u0434\u0430\u043d\u0438\u0435 \u043f\u0440\u043e\u0441\u0442\u043e\u0433\u043e \u043f\u0440\u0438\u043b\u043e\u0436\u0435\u043d\u0438\u044f Hello World"),
            Map.entry("App Components", "\u041a\u043e\u043c\u043f\u043e\u043d\u0435\u043d\u0442\u044b \u043f\u0440\u0438\u043b\u043e\u0436\u0435\u043d\u0438\u044f"),
            Map.entry("Interface & Navigation", "\u0418\u043d\u0442\u0435\u0440\u0444\u0435\u0439\u0441 \u0438 \u043d\u0430\u0432\u0438\u0433\u0430\u0446\u0438\u044f"),
            Map.entry("Dependency Injection", "\u0412\u043d\u0435\u0434\u0440\u0435\u043d\u0438\u0435 \u0437\u0430\u0432\u0438\u0441\u0438\u043c\u043e\u0441\u0442\u0435\u0439"),
            Map.entry("Design Patterns", "\u041f\u0430\u0442\u0442\u0435\u0440\u043d\u044b \u043f\u0440\u043e\u0435\u043a\u0442\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u044f"),
            Map.entry("Build Tools", "\u0418\u043d\u0441\u0442\u0440\u0443\u043c\u0435\u043d\u0442\u044b \u0441\u0431\u043e\u0440\u043a\u0438"),
            Map.entry("Package Managers", "\u041c\u0435\u043d\u0435\u0434\u0436\u0435\u0440\u044b \u043f\u0430\u043a\u0435\u0442\u043e\u0432"),
            Map.entry("Repo Hosting Services", "\u0421\u0435\u0440\u0432\u0438\u0441\u044b \u0445\u043e\u0441\u0442\u0438\u043d\u0433\u0430 \u0440\u0435\u043f\u043e\u0437\u0438\u0442\u043e\u0440\u0438\u0435\u0432"),
            Map.entry("State Management", "\u0423\u043f\u0440\u0430\u0432\u043b\u0435\u043d\u0438\u0435 \u0441\u043e\u0441\u0442\u043e\u044f\u043d\u0438\u0435\u043c"),
            Map.entry("Linters and Formatters", "\u041b\u0438\u043d\u0442\u0435\u0440\u044b \u0438 \u0444\u043e\u0440\u043c\u0430\u0442\u0442\u0435\u0440\u044b"),
            Map.entry("Authentication", "\u0410\u0443\u0442\u0435\u043d\u0442\u0438\u0444\u0438\u043a\u0430\u0446\u0438\u044f"),
            Map.entry("Authorization", "\u0410\u0432\u0442\u043e\u0440\u0438\u0437\u0430\u0446\u0438\u044f"),
            Map.entry("Networking", "\u0421\u0435\u0442\u0435\u0432\u043e\u0435 \u0432\u0437\u0430\u0438\u043c\u043e\u0434\u0435\u0439\u0441\u0442\u0432\u0438\u0435"),
            Map.entry("Web Sockets", "WebSocket"),
            Map.entry("Storage", "\u0425\u0440\u0430\u043d\u0435\u043d\u0438\u0435 \u0434\u0430\u043d\u043d\u044b\u0445"),
            Map.entry("Caching", "\u041a\u044d\u0448\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u0435"),
            Map.entry("Testing", "\u0422\u0435\u0441\u0442\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u0435"),
            Map.entry("Unit Testing", "\u041c\u043e\u0434\u0443\u043b\u044c\u043d\u043e\u0435 \u0442\u0435\u0441\u0442\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u0435"),
            Map.entry("Integration Testing", "\u0418\u043d\u0442\u0435\u0433\u0440\u0430\u0446\u0438\u043e\u043d\u043d\u043e\u0435 \u0442\u0435\u0441\u0442\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u0435"),
            Map.entry("Functional Testing", "\u0424\u0443\u043d\u043a\u0446\u0438\u043e\u043d\u0430\u043b\u044c\u043d\u043e\u0435 \u0442\u0435\u0441\u0442\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u0435"),
            Map.entry("Debugging", "\u041e\u0442\u043b\u0430\u0434\u043a\u0430"),
            Map.entry("Deployment", "\u0420\u0430\u0437\u0432\u0435\u0440\u0442\u044b\u0432\u0430\u043d\u0438\u0435"),
            Map.entry("Documentation", "\u0414\u043e\u043a\u0443\u043c\u0435\u043d\u0442\u0430\u0446\u0438\u044f"),
            Map.entry("Components", "\u041a\u043e\u043c\u043f\u043e\u043d\u0435\u043d\u0442\u044b"),
            Map.entry("Views", "\u041f\u0440\u0435\u0434\u0441\u0442\u0430\u0432\u043b\u0435\u043d\u0438\u044f"),
            Map.entry("Pagination", "\u041f\u0430\u0433\u0438\u043d\u0430\u0446\u0438\u044f"),
            Map.entry("Distribution", "\u041f\u0443\u0431\u043b\u0438\u043a\u0430\u0446\u0438\u044f"),
            Map.entry("Performance", "\u041f\u0440\u043e\u0438\u0437\u0432\u043e\u0434\u0438\u0442\u0435\u043b\u044c\u043d\u043e\u0441\u0442\u044c"),
            Map.entry("Security", "\u0411\u0435\u0437\u043e\u043f\u0430\u0441\u043d\u043e\u0441\u0442\u044c"),
            Map.entry("Monitoring", "\u041c\u043e\u043d\u0438\u0442\u043e\u0440\u0438\u043d\u0433"),
            Map.entry("Logging", "\u041b\u043e\u0433\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u0435"),
            Map.entry("Rate Limiting", "\u041e\u0433\u0440\u0430\u043d\u0438\u0447\u0435\u043d\u0438\u0435 \u0447\u0430\u0441\u0442\u043e\u0442\u044b \u0437\u0430\u043f\u0440\u043e\u0441\u043e\u0432"),
            Map.entry("Query Optimization", "\u041e\u043f\u0442\u0438\u043c\u0438\u0437\u0430\u0446\u0438\u044f \u0437\u0430\u043f\u0440\u043e\u0441\u043e\u0432"),
            Map.entry("Migrations", "\u041c\u0438\u0433\u0440\u0430\u0446\u0438\u0438"),
            Map.entry("Containers", "\u041a\u043e\u043d\u0442\u0435\u0439\u043d\u0435\u0440\u044b"),
            Map.entry("Microservices", "\u041c\u0438\u043a\u0440\u043e\u0441\u0435\u0440\u0432\u0438\u0441\u044b"),
            Map.entry("Security Best Practices", "\u041b\u0443\u0447\u0448\u0438\u0435 \u043f\u0440\u0430\u043a\u0442\u0438\u043a\u0438 \u0431\u0435\u0437\u043e\u043f\u0430\u0441\u043d\u043e\u0441\u0442\u0438"),
            Map.entry("Concurrency", "\u041a\u043e\u043d\u043a\u0443\u0440\u0435\u043d\u0442\u043d\u043e\u0441\u0442\u044c"),
            Map.entry("Multithreading", "\u041c\u043d\u043e\u0433\u043e\u043f\u043e\u0442\u043e\u0447\u043d\u043e\u0441\u0442\u044c"),
            Map.entry("Threads", "\u041f\u043e\u0442\u043e\u043a\u0438"),
            Map.entry("Channels", "\u041a\u0430\u043d\u0430\u043b\u044b"),
            Map.entry("Memory Management", "\u0423\u043f\u0440\u0430\u0432\u043b\u0435\u043d\u0438\u0435 \u043f\u0430\u043c\u044f\u0442\u044c\u044e"),
            Map.entry("Functional Programming", "\u0424\u0443\u043d\u043a\u0446\u0438\u043e\u043d\u0430\u043b\u044c\u043d\u043e\u0435 \u043f\u0440\u043e\u0433\u0440\u0430\u043c\u043c\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u0435"),
            Map.entry("Architecture", "\u0410\u0440\u0445\u0438\u0442\u0435\u043a\u0442\u0443\u0440\u0430"),
            Map.entry("Architectural Patterns", "\u0410\u0440\u0445\u0438\u0442\u0435\u043a\u0442\u0443\u0440\u043d\u044b\u0435 \u043f\u0430\u0442\u0442\u0435\u0440\u043d\u044b"),
            Map.entry("Design principles", "\u041f\u0440\u0438\u043d\u0446\u0438\u043f\u044b \u043f\u0440\u043e\u0435\u043a\u0442\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u044f"),
            Map.entry("Templates", "\u0428\u0430\u0431\u043b\u043e\u043d\u044b"),
            Map.entry("Skills", "\u041d\u0430\u0432\u044b\u043a\u0438"),
            Map.entry("Tools", "\u0418\u043d\u0441\u0442\u0440\u0443\u043c\u0435\u043d\u0442\u044b"),
            Map.entry("APIs", "API"),
            Map.entry("Routing", "\u041c\u0430\u0440\u0448\u0440\u0443\u0442\u0438\u0437\u0430\u0446\u0438\u044f"),
            Map.entry("Images", "\u0418\u0437\u043e\u0431\u0440\u0430\u0436\u0435\u043d\u0438\u044f"),
            Map.entry("Plugins", "\u041f\u043b\u0430\u0433\u0438\u043d\u044b"),
            Map.entry("Mobile Apps", "\u041c\u043e\u0431\u0438\u043b\u044c\u043d\u044b\u0435 \u043f\u0440\u0438\u043b\u043e\u0436\u0435\u043d\u0438\u044f"),
            Map.entry("Programming Languages", "\u042f\u0437\u044b\u043a\u0438 \u043f\u0440\u043e\u0433\u0440\u0430\u043c\u043c\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u044f"),
            Map.entry("CAP Theorem", "\u0422\u0435\u043e\u0440\u0435\u043c\u0430 CAP"),
            Map.entry("Linear", "\u041b\u0438\u043d\u0435\u0439\u043d\u044b\u0435 \u0441\u0442\u0440\u0443\u043a\u0442\u0443\u0440\u044b"),
            Map.entry("Map", "\u0410\u0441\u0441\u043e\u0446\u0438\u0430\u0442\u0438\u0432\u043d\u044b\u0439 \u043c\u0430\u0441\u0441\u0438\u0432"),
            Map.entry("Set", "\u041c\u043d\u043e\u0436\u0435\u0441\u0442\u0432\u043e"),
            Map.entry("Fetch", "\u041f\u043e\u043b\u0443\u0447\u0435\u043d\u0438\u0435 \u0434\u0430\u043d\u043d\u044b\u0445"),
            Map.entry("Sorting Algorithms", "\u0410\u043b\u0433\u043e\u0440\u0438\u0442\u043c\u044b \u0441\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u043a\u0438"),
            Map.entry("Search Algorithms", "\u0410\u043b\u0433\u043e\u0440\u0438\u0442\u043c\u044b \u043f\u043e\u0438\u0441\u043a\u0430"),
            Map.entry("Basic Syntax", "\u0411\u0430\u0437\u043e\u0432\u044b\u0439 \u0441\u0438\u043d\u0442\u0430\u043a\u0441\u0438\u0441"),
            Map.entry("Forms", "\u0424\u043e\u0440\u043c\u044b"),
            Map.entry("Form Validation", "\u0412\u0430\u043b\u0438\u0434\u0430\u0446\u0438\u044f \u0444\u043e\u0440\u043c"),
            Map.entry("Environment Variables", "\u041f\u0435\u0440\u0435\u043c\u0435\u043d\u043d\u044b\u0435 \u043e\u043a\u0440\u0443\u0436\u0435\u043d\u0438\u044f"),
            Map.entry("Breadth First Search", "\u041f\u043e\u0438\u0441\u043a \u0432 \u0448\u0438\u0440\u0438\u043d\u0443"),
            Map.entry("Depth First Search", "\u041f\u043e\u0438\u0441\u043a \u0432 \u0433\u043b\u0443\u0431\u0438\u043d\u0443"),
            Map.entry("Cloud Design Patterns", "\u041e\u0431\u043b\u0430\u0447\u043d\u044b\u0435 \u043f\u0430\u0442\u0442\u0435\u0440\u043d\u044b \u043f\u0440\u043e\u0435\u043a\u0442\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u044f"),
            Map.entry("Replication", "\u0420\u0435\u043f\u043b\u0438\u043a\u0430\u0446\u0438\u044f"),
            Map.entry("Sharding", "\u0428\u0430\u0440\u0434\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u0435"),
            Map.entry("Callbacks", "\u041a\u043e\u043b\u0431\u044d\u043a\u0438"),
            Map.entry("Booleans", "\u041b\u043e\u0433\u0438\u0447\u0435\u0441\u043a\u0438\u0435 \u0437\u043d\u0430\u0447\u0435\u043d\u0438\u044f"),
            Map.entry("Integers", "\u0426\u0435\u043b\u044b\u0435 \u0447\u0438\u0441\u043b\u0430"),
            Map.entry("Floats", "\u0427\u0438\u0441\u043b\u0430 \u0441 \u043f\u043b\u0430\u0432\u0430\u044e\u0449\u0435\u0439 \u0442\u043e\u0447\u043a\u043e\u0439"),
            Map.entry("Enums", "\u041f\u0435\u0440\u0435\u0447\u0438\u0441\u043b\u0435\u043d\u0438\u044f"),
            Map.entry("Type Inference", "\u0412\u044b\u0432\u043e\u0434 \u0442\u0438\u043f\u043e\u0432"),
            Map.entry("Comments", "\u041a\u043e\u043c\u043c\u0435\u043d\u0442\u0430\u0440\u0438\u0438"),
            Map.entry("Namespaces", "\u041f\u0440\u043e\u0441\u0442\u0440\u0430\u043d\u0441\u0442\u0432\u0430 \u0438\u043c\u0435\u043d"),
            Map.entry("Exceptions", "\u0418\u0441\u043a\u043b\u044e\u0447\u0435\u043d\u0438\u044f"),
            Map.entry("Lambdas", "\u041b\u044f\u043c\u0431\u0434\u0430-\u0444\u0443\u043d\u043a\u0446\u0438\u0438"),
            Map.entry("Iterators", "\u0418\u0442\u0435\u0440\u0430\u0442\u043e\u0440\u044b"),
            Map.entry("Streams", "\u041f\u043e\u0442\u043e\u043a\u0438 \u0434\u0430\u043d\u043d\u044b\u0445"),
            Map.entry("Object", "\u041e\u0431\u044a\u0435\u043a\u0442"),
            Map.entry("List", "\u0421\u043f\u0438\u0441\u043e\u043a"),
            Map.entry("Sets", "\u041c\u043d\u043e\u0436\u0435\u0441\u0442\u0432\u0430"),
            Map.entry("Queues", "\u041e\u0447\u0435\u0440\u0435\u0434\u0438"),
            Map.entry("Generics", "\u0414\u0436\u0435\u043d\u0435\u0440\u0438\u043a\u0438"),
            Map.entry("Reactive Programming", "\u0420\u0435\u0430\u043a\u0442\u0438\u0432\u043d\u043e\u0435 \u043f\u0440\u043e\u0433\u0440\u0430\u043c\u043c\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u0435"),
            Map.entry("Events", "\u0421\u043e\u0431\u044b\u0442\u0438\u044f"),
            Map.entry("Architectural Styles", "\u0410\u0440\u0445\u0438\u0442\u0435\u043a\u0442\u0443\u0440\u043d\u044b\u0435 \u0441\u0442\u0438\u043b\u0438"),
            Map.entry("Background Jobs", "\u0424\u043e\u043d\u043e\u0432\u044b\u0435 \u0437\u0430\u0434\u0430\u0447\u0438"),
            Map.entry("Containerization", "\u041a\u043e\u043d\u0442\u0435\u0439\u043d\u0435\u0440\u0438\u0437\u0430\u0446\u0438\u044f"),
            Map.entry("Naming Conventions", "\u0421\u043e\u0433\u043b\u0430\u0448\u0435\u043d\u0438\u044f \u043e\u0431 \u0438\u043c\u0435\u043d\u043e\u0432\u0430\u043d\u0438\u0438"),
            Map.entry("Web Frameworks", "\u0412\u0435\u0431-\u0444\u0440\u0435\u0439\u043c\u0432\u043e\u0440\u043a\u0438"),
            Map.entry("Performance Monitoring", "\u041c\u043e\u043d\u0438\u0442\u043e\u0440\u0438\u043d\u0433 \u043f\u0440\u043e\u0438\u0437\u0432\u043e\u0434\u0438\u0442\u0435\u043b\u044c\u043d\u043e\u0441\u0442\u0438"),
            Map.entry("Data Sources", "\u0418\u0441\u0442\u043e\u0447\u043d\u0438\u043a\u0438 \u0434\u0430\u043d\u043d\u044b\u0445"),
            Map.entry("Predictive Analytics", "\u041f\u0440\u0435\u0434\u0438\u043a\u0442\u0438\u0432\u043d\u0430\u044f \u0430\u043d\u0430\u043b\u0438\u0442\u0438\u043a\u0430"),
            Map.entry("Supervised Learning", "\u041e\u0431\u0443\u0447\u0435\u043d\u0438\u0435 \u0441 \u0443\u0447\u0438\u0442\u0435\u043b\u0435\u043c"),
            Map.entry("Unsupervised Learning", "\u041e\u0431\u0443\u0447\u0435\u043d\u0438\u0435 \u0431\u0435\u0437 \u0443\u0447\u0438\u0442\u0435\u043b\u044f"),
            Map.entry("Reinforcement Learning", "\u041e\u0431\u0443\u0447\u0435\u043d\u0438\u0435 \u0441 \u043f\u043e\u0434\u043a\u0440\u0435\u043f\u043b\u0435\u043d\u0438\u0435\u043c"),
            Map.entry("Guidelines", "\u0420\u0435\u043a\u043e\u043c\u0435\u043d\u0434\u0430\u0446\u0438\u0438"),
            Map.entry("Frameworks", "\u0424\u0440\u0435\u0439\u043c\u0432\u043e\u0440\u043a\u0438"),
            Map.entry("Serverless", "Serverless"),
            Map.entry("Machine Learning", "\u041c\u0430\u0448\u0438\u043d\u043d\u043e\u0435 \u043e\u0431\u0443\u0447\u0435\u043d\u0438\u0435"),
            Map.entry("The Internet", "\u0418\u043d\u0442\u0435\u0440\u043d\u0435\u0442"),
            Map.entry("Databases", "\u0411\u0430\u0437\u044b \u0434\u0430\u043d\u043d\u044b\u0445"),
            Map.entry("Relational Databases", "\u0420\u0435\u043b\u044f\u0446\u0438\u043e\u043d\u043d\u044b\u0435 \u0431\u0430\u0437\u044b \u0434\u0430\u043d\u043d\u044b\u0445"),
            Map.entry("Non-Relational Databases", "\u041d\u0435\u0440\u0435\u043b\u044f\u0446\u0438\u043e\u043d\u043d\u044b\u0435 \u0431\u0430\u0437\u044b \u0434\u0430\u043d\u043d\u044b\u0445"),
            Map.entry("Error Handling", "\u041e\u0431\u0440\u0430\u0431\u043e\u0442\u043a\u0430 \u043e\u0448\u0438\u0431\u043e\u043a"),
            Map.entry("Accessibility", "\u0414\u043e\u0441\u0442\u0443\u043f\u043d\u043e\u0441\u0442\u044c"),
            Map.entry("Animations", "\u0410\u043d\u0438\u043c\u0430\u0446\u0438\u0438"),
            Map.entry("Layouts", "\u041c\u0430\u043a\u0435\u0442\u044b"),
            Map.entry("Internationalization", "\u0418\u043d\u0442\u0435\u0440\u043d\u0430\u0446\u0438\u043e\u043d\u0430\u043b\u0438\u0437\u0430\u0446\u0438\u044f"),
            Map.entry("Localization", "\u041b\u043e\u043a\u0430\u043b\u0438\u0437\u0430\u0446\u0438\u044f")
            ,
            Map.entry("Anonymous Functions / Lambdas", "Анонимные функции и лямбды"),
            Map.entry("The apply method", "Метод apply"),
            Map.entry("By-name parameters", "Параметры по имени"),
            Map.entry("Case classes", "Case-классы"),
            Map.entry("Case Objects", "Case-объекты"),
            Map.entry("Data Handling", "Обработка данных"),
            Map.entry("Data Structures: Class, Trait, and Object", "Структуры данных: class, trait и object"),
            Map.entry("Distributed computing", "Распределённые вычисления"),
            Map.entry("Know your ecosystem", "Понимание экосистемы"),
            Map.entry("Effect systems", "Системы эффектов"),
            Map.entry("Functions returning functions", "Функции, возвращающие функции"),
            Map.entry("Functions & Methods", "Функции и методы"),
            Map.entry("Implicit parameters", "Неявные параметры"),
            Map.entry("Laziness", "Ленивая вычислимость"),
            Map.entry("Lazy collections", "Ленивые коллекции"),
            Map.entry("Lazy vals", "Ленивые значения (lazy val)"),
            Map.entry("Books", "Книги"),
            Map.entry("Capabilities", "Возможности"),
            Map.entry("Capture checking", "Проверка захвата"),
            Map.entry("Category Theory", "Теория категорий"),
            Map.entry("Conversions", "Преобразования"),
            Map.entry("Courses", "Курсы"),
            Map.entry("Docs", "Документация"),
            Map.entry("Early returns", "Ранний возврат"),
            Map.entry("GUI Frameworks and Libraries", "GUI-фреймворки и библиотеки"),
            Map.entry("Method calls", "Вызовы методов"),
            Map.entry("Mutable collections", "Изменяемые коллекции"),
            Map.entry("Know your platform", "Понимание платформы"),
            Map.entry("Recursion basics", "Основы рекурсии"),
            Map.entry("Scope & Visibility", "Область видимости и уровни доступа"),
            Map.entry("Sealed traits", "Sealed-trait'ы"),
            Map.entry("Standard operators", "Стандартные операторы"),
            Map.entry("Setting Up Scala", "Настройка Scala"),
            Map.entry("Tail recursion", "Хвостовая рекурсия"),
            Map.entry("Total / partial functions", "Полные и частичные функции"),
            Map.entry("Type hierarchy", "Иерархия типов"),
            Map.entry("Type parameters", "Параметры типов"),
            Map.entry("Type system", "Система типов"),
            Map.entry("The unapply method", "Метод unapply"),
            Map.entry("Working with Strings", "Работа со строками"),
            Map.entry("Video game engines", "Игровые движки"),
            Map.entry("Animatable Protocol", "Протокол Animatable"),
            Map.entry("Automatic Reference Counting (ARC)", "Автоматический подсчёт ссылок (ARC)"),
            Map.entry("Asynchronous Functions", "Асинхронные функции"),
            Map.entry("Asynchronous Sequences", "Асинхронные последовательности"),
            Map.entry("Background View Modifier", "Модификатор background"),
            Map.entry("Basic Functions", "Базовые функции"),
            Map.entry("Button", "Кнопка"),
            Map.entry("Catching Errors in Swift", "Перехват ошибок в Swift"),
            Map.entry("Clean Architecture", "Чистая архитектура"),
            Map.entry("Comments in Swift", "Комментарии в Swift"),
            Map.entry("Comparison Operators", "Операторы сравнения"),
            Map.entry("Computed Properties", "Вычисляемые свойства"),
            Map.entry("Continue & Break in Swift Loops", "Continue и break в циклах Swift"),
            Map.entry("Data Flow", "Поток данных"),
            Map.entry("Data Persistence", "Сохранение данных"),
            Map.entry("Enumerations", "Перечисления"),
            Map.entry("Explicit Animations", "Явные анимации"),
            Map.entry("Extensions", "Расширения"),
            Map.entry("Floats and Doubles", "Float и Double"),
            Map.entry("Font Modifier", "Модификатор font"),
            Map.entry("For-in Loops in Swift", "Циклы for-in в Swift"),
            Map.entry("Function Types", "Типы функций"),
            Map.entry("If / Else Statements", "Условные конструкции if / else"),
            Map.entry("Initialization", "Инициализация"),
            Map.entry("Installing Swift", "Установка Swift"),
            Map.entry("Integers in Swift", "Целые числа в Swift"),
            Map.entry("Introduction to Swift & SwiftUI", "Введение в Swift и SwiftUI"),
            Map.entry("IDEs", "IDE"),
            Map.entry("Logging & Debugging", "Логирование и отладка"),
            Map.entry("Logical Operators", "Логические операторы"),
            Map.entry("Memory Safety", "Безопасность памяти"),
            Map.entry("Methods in Swift Structures and Classes", "Методы в структурах и классах Swift"),
            Map.entry("Nested Functions", "Вложенные функции"),
            Map.entry("Nil-Coalescing Operator", "Оператор nil-coalescing"),
            Map.entry("Optional Chaining", "Опциональная цепочка вызовов"),
            Map.entry("Optionals and nil", "Optionals и nil"),
            Map.entry("Parameters in Swift Functions and Closures", "Параметры в функциях и замыканиях Swift"),
            Map.entry("Print & String Interpolation", "Print и интерполяция строк"),
            Map.entry("Error Propagation", "Проброс ошибок"),
            Map.entry("Properties", "Свойства"),
            Map.entry("Property Observers", "Наблюдатели свойств"),
            Map.entry("Property Wrappers", "Property wrapper'ы"),
            Map.entry("Protocols", "Протоколы"),
            Map.entry("Repeat...While Loop", "Цикл repeat...while"),
            Map.entry("Result Builders", "Result builder'ы"),
            Map.entry("Return Types", "Возвращаемые типы"),
            Map.entry("Server Frameworks", "Серверные фреймворки"),
            Map.entry("Semicolons in Swift", "Точки с запятой в Swift"),
            Map.entry("Stored Properties", "Хранимые свойства"),
            Map.entry("Strings in Swift", "Строки в Swift"),
            Map.entry("Strict Concurrency Checking", "Строгая проверка конкурентности"),
            Map.entry("Structures & Classes", "Структуры и классы"),
            Map.entry("Subscripts", "Сабскрипты"),
            Map.entry("Switch/Case Statements", "Конструкции switch/case"),
            Map.entry("Tasks & Task Groups", "Задачи и группы задач"),
            Map.entry("Text", "Текст"),
            Map.entry("Throwing Errors in Swift", "Генерация ошибок в Swift"),
            Map.entry("Trailing Closures", "Trailing closures"),
            Map.entry("Transitions", "Переходы"),
            Map.entry("Tuples in Swift", "Кортежи в Swift"),
            Map.entry("Type Annotations", "Аннотации типов"),
            Map.entry("Type Safety in Swift", "Безопасность типов в Swift"),
            Map.entry("UI Controls", "Элементы управления UI"),
            Map.entry("Unstructured Concurrency", "Неструктурированная конкурентность"),
            Map.entry("User Interaction", "Взаимодействие с пользователем"),
            Map.entry("Using Packages", "Использование пакетов"),
            Map.entry("While Loops in Swift", "Циклы while в Swift"),
            Map.entry("Learn the Basics of C#", "Изучите основы C#"),
            Map.entry("General Development Skills", "Общие навыки разработки"),
            Map.entry("Git - Version Control", "Git и контроль версий"),
            Map.entry("HTTP / HTTPs Protocol", "Протокол HTTP / HTTPS"),
            Map.entry("Database Fundamentals", "Основы баз данных"),
            Map.entry("SQL Basics", "Основы SQL"),
            Map.entry("Database Design Basics", "Основы проектирования баз данных"),
            Map.entry("ASP.NET Core Basics", "Основы ASP.NET Core"),
            Map.entry("Framework Basics", "Основы фреймворка"),
            Map.entry("Middlewares", "Middleware"),
            Map.entry("Filters and Attributes", "Фильтры и атрибуты"),
            Map.entry("Lazy, Eager, Explicit Loading", "Lazy, eager и explicit loading"),
            Map.entry("App Settings and Configs", "Настройки приложения и конфиги"),
            Map.entry("Distributed Cache", "Распределённый кэш"),
            Map.entry("Life Cycles", "Жизненные циклы"),
            Map.entry("DI Containers", "DI-контейнеры"),
            Map.entry("Cloud", "Облако"),
            Map.entry("Search Engines", "Поисковые движки"),
            Map.entry("API Clients and Communication", "API-клиенты и взаимодействие"),
            Map.entry("Real-Time Communication", "Взаимодействие в реальном времени"),
            Map.entry("Object Mapping", "Object mapping"),
            Map.entry("Manual Mapping", "Ручное сопоставление"),
            Map.entry("Native Background Service", "Нативный background service"),
            Map.entry("Task Scheduling", "Планирование задач"),
            Map.entry("Test Containers", "Тестовые контейнеры"),
            Map.entry("Code First + Migrations", "Code First и миграции"),
            Map.entry("Log Frameworks", "Фреймворки логирования"),
            Map.entry("Distributed Lock", "Распределённая блокировка"),
            Map.entry("Stack", "Стек"),
            Map.entry("Queue", "Очередь"),
            Map.entry("Graph", "Граф"),
            Map.entry("Tree", "Дерево"),
            Map.entry("Constant", "Константная сложность"),
            Map.entry("Logarithmic", "Логарифмическая сложность"),
            Map.entry("Full Binary Tree", "Полное бинарное дерево"),
            Map.entry("Selector", "Селектор"),
            Map.entry("Template", "Шаблон"),
            Map.entry("Styles", "Стили"),
            Map.entry("Imports", "Импорты"),
            Map.entry("Standalone", "Standalone-подход"),
            Map.entry("Access Control", "Контроль доступа"),
            Map.entry("App Architecture", "Архитектура приложения"),
            Map.entry("App Lifecycle", "Жизненный цикл приложения"),
            Map.entry("Benchmarks", "Бенчмарки"),
            Map.entry("Build Tags", "Теги сборки"),
            Map.entry("Capacity and Growth", "Ёмкость и рост"),
            Map.entry("Abstract Class", "Абстрактный класс"),
            Map.entry("Aggregate Operations", "Агрегирующие операции"),
            Map.entry("Catching Exceptions", "Перехват исключений"),
            Map.entry("Object Relational Mapping", "Объектно-реляционное отображение"),
            Map.entry("Triggers", "Триггеры"),
            Map.entry("Need of Design System", "Зачем нужна дизайн-система"),
            Map.entry("Governance", "Управление"),
            Map.entry("Pilot", "Пилотный этап"),
            Map.entry("Token", "Токен"),
            Map.entry("Basic Queries", "Базовые запросы"),
            Map.entry("Basic Routes", "Базовые маршруты"),
            Map.entry("Basic Controllers", "Базовые контроллеры"),
            Map.entry("Authentication in Laravel", "Аутентификация в Laravel"),
            Map.entry("Configuration in Laravel", "Конфигурация в Laravel"),
            Map.entry("Database Configuration", "Конфигурация базы данных"),
            Map.entry("Deployment Configuration", "Конфигурация развёртывания"),
            Map.entry("Module Architecture", "Архитектура модулей"),
            Map.entry("Setting up a New Project", "Настройка нового проекта"),
            Map.entry("Introduction to Angular", "Введение в Angular"),
            Map.entry("Angular Architecture", "Архитектура Angular"),
            Map.entry("Creating Modules", "Создание модулей"),
            Map.entry("Component Library", "Библиотека компонентов"),
            Map.entry("Design Language", "Язык дизайна"),
            Map.entry("Pattern", "Паттерн"),
            Map.entry("Stored Procedures", "Хранимые процедуры"),
            Map.entry("Constraints", "Ограничения"),
            Map.entry("Adjacency List", "Список смежности"),
            Map.entry("Adjacency Matrix", "Матрица смежности"),
            Map.entry("Polynomial", "Полиномиальная сложность"),
            Map.entry("Exponential", "Экспоненциальная сложность"),
            Map.entry("Big O", "Нотация Big O"),
            Map.entry("Big Omega", "Нотация Big Omega"),
            Map.entry("Big-Theta", "Нотация Big Theta"),
            Map.entry("Common Algorithms", "Базовые алгоритмы"),
            Map.entry("Insertion Sort", "Сортировка вставками"),
            Map.entry("Coverage", "Покрытие"),
            Map.entry("Cross-compilation", "Кросс-компиляция"),
            Map.entry("Characters", "Символы"),
            Map.entry("Code Organization", "Организация кода"),
            Map.entry("Conditional Expressions", "Условные выражения"),
            Map.entry("Constructors", "Конструкторы"),
            Map.entry("String", "Строка"),
            Map.entry("Double", "Число с плавающей точкой двойной точности"),
            Map.entry("Binary Data", "Бинарные данные"),
            Map.entry("Regular Expression", "Регулярное выражение"),
            Map.entry("Embedded Objects & Arrays", "Вложенные объекты и массивы"),
            Map.entry("Actions", "Действия"),
            Map.entry("Activity Logging", "Логирование активности"),
            Map.entry("Admin Menu", "Административное меню"),
            Map.entry("Child Themes", "Дочерние темы"),
            Map.entry("Composer & Autoloading", "Composer и автозагрузка"),
            Map.entry("Contributing to WordPress", "Вклад в WordPress"),
            Map.entry("Embeddings and Vector Search", "Эмбеддинги и векторный поиск"),
            Map.entry("Tokenization", "Токенизация"),
            Map.entry("Token Based Pricing", "Тарификация по токенам"),
            Map.entry("Temperature", "Temperature"),
            Map.entry("Frequency Penalty", "Штраф за частоту"),
            Map.entry("Presence Penalty", "Штраф за присутствие"),
            Map.entry("Reason and Plan", "Рассуждение и план"),
            Map.entry("Stopping Criteria", "Критерии остановки"),
            Map.entry("Acting / Tool Invocation", "Действие / вызов инструмента"),
            Map.entry("Perception / User Input", "Восприятие / ввод пользователя"),
            Map.entry("Max Length", "Максимальная длина"),
            Map.entry("Observation & Reflection", "Наблюдение и рефлексия"),
            Map.entry("Stage 1", "Этап 1"),
            Map.entry("Stage 4", "Этап 4"),
            Map.entry("Stage 9", "Этап 9"),
            Map.entry("Stage 12", "Этап 12"),
            Map.entry("Stage 13", "Этап 13"),
            Map.entry("Stage 14", "Этап 14"),
            Map.entry("Stage 17", "Этап 17")
    );

    private static final Map<String, String> ROLE_TOPIC_TITLE_OVERRIDES = new LinkedHashMap<>();
    private static final LinkedHashMap<String, String> TOPIC_PHRASE_TRANSLATIONS = new LinkedHashMap<>();
    private static final LinkedHashMap<String, String> TITLE_SEGMENT_OVERRIDES = new LinkedHashMap<>();

    static {
        ROLE_TOPIC_TITLE_OVERRIDES.put("Learn the basics", "Изучение основ");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Learn the Basics", "Изучение основ");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Learn the Fundamentals", "Изучение фундаментальных основ");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Understand the Basics", "Понимание основ");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Learn a Language", "Выбор и изучение языка");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Learn a Programming Language", "Изучение языка программирования");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Learn a Programming Lang.", "Изучение языка программирования");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Learn about APIs", "Изучение API");
        ROLE_TOPIC_TITLE_OVERRIDES.put("How does the internet work?", "Как работает интернет?");
        ROLE_TOPIC_TITLE_OVERRIDES.put("DNS and how it works?", "DNS и принцип его работы");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Browsers and how they work?", "Браузеры и принцип их работы");
        ROLE_TOPIC_TITLE_OVERRIDES.put("What is HTTP?", "Что такое HTTP?");
        ROLE_TOPIC_TITLE_OVERRIDES.put("What is Domain Name?", "Что такое доменное имя?");
        ROLE_TOPIC_TITLE_OVERRIDES.put("What is hosting?", "Что такое хостинг?");
        ROLE_TOPIC_TITLE_OVERRIDES.put("What is and how to setup X ?", "Что это такое и как настроить X?");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Writing Semantic HTML", "Семантическая HTML-разметка");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Forms and Validations", "Формы и валидация");
        ROLE_TOPIC_TITLE_OVERRIDES.put("SEO Basics", "Основы SEO");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Making Layouts", "Создание макетов");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Responsive Design", "Адаптивный дизайн");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Learn DOM Manipulation", "Работа с DOM");
        ROLE_TOPIC_TITLE_OVERRIDES.put("VCS Hosting", "Хостинг систем контроля версий");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Fetch API / Ajax (XHR)", "Fetch API и Ajax (XHR)");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Pick a Framework", "Выбор фреймворка");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Writing CSS", "Написание CSS");
        ROLE_TOPIC_TITLE_OVERRIDES.put("CSS Architecture", "Архитектура CSS");
        ROLE_TOPIC_TITLE_OVERRIDES.put("CSS Preprocessors", "Препроцессоры CSS");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Content Security Policy", "Политика безопасности контента");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Module Bundlers", "Сборщики модулей");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Authentication Strategies", "Стратегии аутентификации");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Web Security Basics", "Основы веб-безопасности");
        ROLE_TOPIC_TITLE_OVERRIDES.put("OWASP Security Risks", "Риски безопасности OWASP");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Web Components", "Веб-компоненты");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Type Checkers", "Проверка типов");
        ROLE_TOPIC_TITLE_OVERRIDES.put("HTML Templates", "HTML-шаблоны");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Custom Elements", "Пользовательские элементы");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Performance Metrics", "Метрики производительности");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Using Lighthouse", "Использование Lighthouse");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Using DevTools", "Использование DevTools");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Static Site Generators", "Генераторы статических сайтов");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Server Sent Events", "Server-Sent Events");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Service Workers", "Service Workers");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Desktop Apps", "Десктопные приложения");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Device Orientation", "Ориентация устройства");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Checkpoint - Static Webpages", "Контрольная точка: статические веб-страницы");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Checkpoint - Interactivity", "Контрольная точка: интерактивность");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Checkpoint - Collaborative Work", "Контрольная точка: командная работа");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Checkpoint - External Packages", "Контрольная точка: внешние пакеты");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Checkpoint - Frontend Apps", "Контрольная точка: frontend-приложения");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Checkpoint — CLI Apps", "Контрольная точка: CLI-приложения");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Checkpoint — Simple CRUD Apps", "Контрольная точка: простые CRUD-приложения");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Checkpoint — Complete App", "Контрольная точка: полноценное приложение");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Checkpoint — Deployment", "Контрольная точка: развёртывание");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Checkpoint — Automation", "Контрольная точка: автоматизация");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Checkpoint — CI / CD", "Контрольная точка: CI/CD");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Checkpoint — Monitoring", "Контрольная точка: мониторинг");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Checkpoint — Infrastructure", "Контрольная точка: инфраструктура");
        ROLE_TOPIC_TITLE_OVERRIDES.put("RESTful APIs", "RESTful API");
        ROLE_TOPIC_TITLE_OVERRIDES.put("JWT Auth", "JWT-аутентификация");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Linux Basics", "Основы Linux");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Basic AWS Services", "Базовые сервисы AWS");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Open API Specs", "Спецификации OpenAPI");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Basic Authentication", "Базовая аутентификация");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Server Side", "Серверная сторона");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Token Authentication", "Токен-аутентификация");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Client Side", "Клиентская сторона");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Cookie Based Auth", "Аутентификация на основе cookie");
        ROLE_TOPIC_TITLE_OVERRIDES.put("JSON APIs", "JSON API");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Web Security", "Веб-безопасность");
        ROLE_TOPIC_TITLE_OVERRIDES.put("OWASP Risks", "Риски OWASP");
        ROLE_TOPIC_TITLE_OVERRIDES.put("More about Databases", "Больше о базах данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("N+1 Problem", "Проблема N+1");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Server Security", "Безопасность сервера");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Failure Modes", "Режимы отказа");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Profiling Perfor.", "Профилирование производительности");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Scaling Databases", "Масштабирование баз данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Database Indexes", "Индексы базы данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Data Replication", "Репликация данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Sharding Strategies", "Стратегии шардирования");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Monolithic Apps", "Монолитные приложения");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Design and Development Principles", "Принципы проектирования и разработки");
        ROLE_TOPIC_TITLE_OVERRIDES.put("GOF Design Patterns", "Паттерны проектирования GoF");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Domain Driven Design", "Domain-Driven Design");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Test Driven Development", "Test-Driven Development");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Event Sourcing", "Event Sourcing");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Message Brokers", "Брокеры сообщений");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Search Engines", "Поисковые движки");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Web Servers", "Веб-серверы");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Real-Time Data", "Данные в реальном времени");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Long Polling", "Long polling");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Short Polling", "Short polling");
        ROLE_TOPIC_TITLE_OVERRIDES.put("NoSQL Databases", "NoSQL-базы данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Graceful Degradation", "Graceful degradation");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Circuit Breaker", "Circuit Breaker");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Building For Scale", "Проектирование под масштабирование");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Migration Strategies", "Стратегии миграции");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Operating System", "Операционная система");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Operating system", "Операционная система");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Terminal Knowledge", "Работа с терминалом");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Process Monitoring", "Мониторинг процессов");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Networking Tools", "Сетевые инструменты");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Text Manipulation", "Обработка текста");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Forward Proxy", "Прямой прокси");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Reverse Proxy", "Обратный прокси");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Caching Server", "Сервер кэширования");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Firewall", "Межсетевой экран");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Load Balancer", "Балансировщик нагрузки");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Networking Protocols", "Сетевые протоколы");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Networking & Protocols", "Сети и протоколы");
        ROLE_TOPIC_TITLE_OVERRIDES.put("OSI Model", "Модель OSI");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Cloud Providers", "Облачные провайдеры");
        ROLE_TOPIC_TITLE_OVERRIDES.put("White / Grey Listing", "Белые и серые списки");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Google Cloud", "Google Cloud");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Alibaba Cloud", "Alibaba Cloud");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Domain Keys", "Доменные ключи");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Configuration Management", "Управление конфигурацией");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Secret Management", "Управление секретами");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Infrastructure Monitoring", "Мониторинг инфраструктуры");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Logs Management", "Управление логами");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Cloud Specific Tools", "Инструменты конкретного облака");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Container Orchestration", "Оркестрация контейнеров");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Application Monitoring", "Мониторинг приложений");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Artifact Management", "Управление артефактами");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Data Management", "Управление данными");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Design and Implementation", "Проектирование и реализация");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Management and Monitoring", "Управление и мониторинг");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Cloud Computing", "Облачные вычисления");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Cloud-native ML Services", "Cloud-native сервисы машинного обучения");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Data Engineering Fundamentals", "Основы инженерии данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Machine Learning Fundamentals", "Основы машинного обучения");
        ROLE_TOPIC_TITLE_OVERRIDES.put("MLOps Principles", "Принципы MLOps");
        ROLE_TOPIC_TITLE_OVERRIDES.put("MLOps Components", "Компоненты MLOps");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Experiment Tracking & Model Registry", "Отслеживание экспериментов и реестр моделей");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Data Lineage & Feature Stores", "Происхождение данных и feature store");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Model Training & Serving", "Обучение и serving моделей");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Monitoring & Observability", "Мониторинг и наблюдаемость");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Access Control Lists (ACLs)", "Списки контроля доступа (ACL)");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Alert Types", "Типы оповещений");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Asymmetric Encryption", "Асимметричное шифрование");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Attack Surface Mapping", "Картирование поверхности атаки");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Audit & Compliance Mapping", "Аудит и соответствие требованиям");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Automated Patching", "Автоматическое обновление исправлений");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Bash Scripting", "Скрипты на Bash");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Build Pipeline Hardening", "Усиление безопасности build pipeline");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Certificate Lifecycle", "Жизненный цикл сертификатов");
        ROLE_TOPIC_TITLE_OVERRIDES.put("CIA Triad", "Триада CIA");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Cloud Security", "Облачная безопасность");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Container Security", "Безопасность контейнеров");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Cryptographic Hashing", "Криптографическое хэширование");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Cloud Security Posture Management (CSPM)", "Управление состоянием облачной безопасности (CSPM)");
        ROLE_TOPIC_TITLE_OVERRIDES.put("DDoS Mitigation Strategy", "Стратегия защиты от DDoS");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Defense in Depth", "Многоуровневая защита");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Dependency Risk Management", "Управление рисками зависимостей");
        ROLE_TOPIC_TITLE_OVERRIDES.put("DevSecOps vs. DevOps", "DevSecOps и DevOps: сравнение");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Docker Security", "Безопасность Docker");
        ROLE_TOPIC_TITLE_OVERRIDES.put("EDR Strategy", "Стратегия EDR");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Endpoint Detection", "Обнаружение угроз на endpoint-устройствах");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Enterprise Operations", "Корпоративные операции");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Identity and Access Management (IAM)", "Управление идентификацией и доступом (IAM)");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Identity Basics", "Основы идентификации");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Intrusion Detection Systems", "Системы обнаружения вторжений");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Image Scanning", "Сканирование образов");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Incident Response", "Реагирование на инциденты");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Input Validation Patterns", "Паттерны валидации ввода");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Intrusion Prevention Systems", "Системы предотвращения вторжений");
        ROLE_TOPIC_TITLE_OVERRIDES.put("IR Lifecycle", "Жизненный цикл реагирования на инциденты");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Key Management Service", "Сервис управления ключами");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Kubernetes Security", "Безопасность Kubernetes");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Large Scale Identity Strategy", "Стратегия идентификации в крупном масштабе");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Least Privilege", "Принцип наименьших привилегий");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Log Analysis", "Анализ логов");
        ROLE_TOPIC_TITLE_OVERRIDES.put("What is Data Analytics", "Что такое аналитика данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Types of Data Analytics", "Виды аналитики данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Descriptive Analytics", "Описательная аналитика");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Diagnostic Analytics", "Диагностическая аналитика");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Analysis / Reporting with Excel", "Анализ и отчётность в Excel");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Key Concepts of Data", "Ключевые понятия данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Collection", "Сбор данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Cleanup", "Очистка данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Exploration", "Исследование данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Visualisation", "Визуализация");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Prescriptive Analytics", "Предписательная аналитика");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Statistical Analysis", "Статистический анализ");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Data Manipulation Libraries", "Библиотеки обработки данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Data Visualisation Libraries", "Библиотеки визуализации данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Data Collection", "Сбор данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("CSV Files", "CSV-файлы");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Impact on Product Development", "Влияние на разработку продукта");
        ROLE_TOPIC_TITLE_OVERRIDES.put("What is an AI Engineer?", "Кто такой AI-инженер?");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Roles and Responsiblities", "Роли и обязанности");
        ROLE_TOPIC_TITLE_OVERRIDES.put("AI Engineer vs ML Engineer", "AI-инженер и ML-инженер: сравнение");
        ROLE_TOPIC_TITLE_OVERRIDES.put("AI vs AGI", "AI и AGI: различия");
        ROLE_TOPIC_TITLE_OVERRIDES.put("LLMs", "Большие языковые модели");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Inference", "Инференс");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Training", "Обучение моделей");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Embeddings", "Эмбеддинги");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Pre-trained Models", "Предобученные модели");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Benefits of Pre-trained Models", "Преимущества предобученных моделей");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Limitations and Considerations", "Ограничения и важные соображения");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Vector Databases", "Векторные базы данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("OpenAI Models", "Модели OpenAI");
        ROLE_TOPIC_TITLE_OVERRIDES.put("RAG", "Retrieval-Augmented Generation");
        ROLE_TOPIC_TITLE_OVERRIDES.put("AI Agents", "AI-агенты");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Open AI Models", "Модели OpenAI");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Prompt Engineering", "Промпт-инжиниринг");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Capabilities / Context Length", "Возможности и длина контекста");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Cut-off Dates / Knowledge", "Дата отсечения знаний");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Anthropic's Claude", "Claude от Anthropic");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Google's Gemini", "Gemini от Google");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Maximum Tokens", "Максимальное число токенов");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Azure AI", "Azure AI");
        ROLE_TOPIC_TITLE_OVERRIDES.put("AWS Sagemaker", "AWS SageMaker");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Token Counting", "Подсчёт токенов");
        ROLE_TOPIC_TITLE_OVERRIDES.put("OpenAI API", "OpenAI API");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Chat Completions API", "Chat Completions API");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Writing Prompts", "Написание промптов");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Hugging Face Models", "Модели Hugging Face");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Pricing Considerations", "Стоимость и ограничения тарификации");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Open AI Playground", "OpenAI Playground");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Fine-tuning", "Дообучение модели");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Prompt Injection Attacks", "Prompt injection-атаки");
        ROLE_TOPIC_TITLE_OVERRIDES.put("AI Safety and Ethics", "Безопасность и этика AI");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Security and Privacy Concerns", "Риски безопасности и приватности");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Bias and Fairness", "Смещения и справедливость");
        ROLE_TOPIC_TITLE_OVERRIDES.put("OpenSource AI", "Open-source AI");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Open vs Closed Source Models", "Открытые и закрытые модели: сравнение");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Popular Open Source Models", "Популярные open-source модели");
        ROLE_TOPIC_TITLE_OVERRIDES.put("OpenAI Moderation API", "OpenAI Moderation API");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Hugging Face", "Hugging Face");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Adding end-user IDs in prompts", "Добавление ID конечного пользователя в промпты");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Conducting adversarial testing", "Проведение adversarial-тестирования");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Hugging Face Tasks", "Задачи Hugging Face");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Robust prompt engineering", "Надёжный промпт-инжиниринг");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Hugging Face Hub", "Hugging Face Hub");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Know your Customers / Usecases", "Понимание пользователей и сценариев использования");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Constraining outputs and inputs", "Ограничение входных и выходных данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Inference SDK", "SDK для инференса");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Transformers.js", "Transformers.js");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Ollama", "Ollama");
        ROLE_TOPIC_TITLE_OVERRIDES.put("What are Embeddings", "Что такое эмбеддинги?");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Open AI Embedding Models", "Embedding-модели OpenAI");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Ollama Models", "Модели Ollama");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Semantic Search", "Семантический поиск");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Ollama SDK", "Ollama SDK");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Data Classification", "Классификация данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Open AI Embeddings API", "OpenAI Embeddings API");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Recommendation Systems", "Рекомендательные системы");
        ROLE_TOPIC_TITLE_OVERRIDES.put("RAG Usecases", "Сценарии использования RAG");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Sentence Transformers", "Sentence Transformers");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Anomaly Detection", "Обнаружение аномалий");
        ROLE_TOPIC_TITLE_OVERRIDES.put("RAG vs Fine-tuning", "RAG и дообучение: сравнение");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Models on Hugging Face", "Модели на Hugging Face");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Chunking", "Чанкинг");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Open-Source Embeddings", "Open-source эмбеддинги");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Embedding", "Эмбеддинг");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Vector Database", "Векторная база данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("RAG & Implementation", "RAG и реализация");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Retrieval Process", "Процесс извлечения данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Purpose and Functionality", "Назначение и функциональность");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Generation", "Генерация");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Open AI Assistant API", "OpenAI Assistant API");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Using SDKs Directly", "Прямое использование SDK");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Agents Usecases", "Сценарии использования AI-агентов");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Langchain", "LangChain");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Llama Index", "LlamaIndex");
        ROLE_TOPIC_TITLE_OVERRIDES.put("ReAct Prompting", "ReAct prompting");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Manual Implementation", "Ручная реализация");
        ROLE_TOPIC_TITLE_OVERRIDES.put("OpenAI Functions / Tools", "Функции и инструменты OpenAI");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Indexing Embeddings", "Индексация эмбеддингов");
        ROLE_TOPIC_TITLE_OVERRIDES.put("OpenAI Assistant API", "OpenAI Assistant API");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Performing Similarity Search", "Поиск по сходству");
        ROLE_TOPIC_TITLE_OVERRIDES.put("OpenAI Vision API", "OpenAI Vision API");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Multimodal AI", "Мультимодальный AI");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Multimodal AI Usecases", "Сценарии использования мультимодального AI");
        ROLE_TOPIC_TITLE_OVERRIDES.put("DALL-E API", "DALL-E API");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Image Understanding", "Понимание изображений");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Whisper API", "Whisper API");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Image Generation", "Генерация изображений");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Video Understanding", "Понимание видео");
        ROLE_TOPIC_TITLE_OVERRIDES.put("AI Code Editors", "AI-редакторы кода");
        ROLE_TOPIC_TITLE_OVERRIDES.put("LangChain for Multimodal Apps", "LangChain для мультимодальных приложений");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Audio Processing", "Обработка аудио");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Code Completion Tools", "Инструменты автодополнения кода");
        ROLE_TOPIC_TITLE_OVERRIDES.put("LlamaIndex for Multimodal Apps", "LlamaIndex для мультимодальных приложений");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Text-to-Speech", "Text-to-Speech");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Speech-to-Text", "Speech-to-Text");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Development Tools", "Инструменты разработки");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Mathematics", "Математика");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Statistics", "Статистика");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Econometrics", "Эконометрика");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Coding", "Программирование");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Exploratory Data Analysis", "Исследовательский анализ данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("What is Data Engineering?", "Что такое инженерия данных?");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Data Engineering vs Data Science", "Инженерия данных и Data Science: сравнение");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Data Engineering Lifecycle", "Жизненный цикл инженерии данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Skills and Responsibilities", "Навыки и обязанности");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Choosing the Right Technologies", "Выбор подходящих технологий");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Programming Skills", "Навыки программирования");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Data Generation", "Генерация данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Data Storage", "Хранение данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Data Ingestion", "Загрузка данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Networking Fundamentals", "Основы сетей");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Data Serving", "Предоставление данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Distributed Systems Basics", "Основы распределённых систем");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Data Normalization", "Нормализация данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Sources of Data", "Источники данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Database", "База данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Logs", "Логи");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Data Modelling Techniques", "Техники моделирования данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Data Collection Considerations", "Особенности сбора данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("OLTP vs OLAP", "OLTP и OLAP: сравнение");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Database Fundamentals", "Основы баз данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Slowly Changing Dimension - SCD", "Медленно изменяющиеся измерения (SCD)");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Horizontal vs Vertical Scaling", "Горизонтальное и вертикальное масштабирование");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Derivatives, Partial Derivatives", "Производные и частные производные");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Chain rule of derivation", "Правило цепочки для производных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("ML Engineer vs AI Engineer", "ML-инженер и AI-инженер: сравнение");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Matrix & Matrix Operations", "Матрицы и операции над матрицами");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Scalars, Vectors, Tensors", "Скаляры, векторы и тензоры");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Singular Value Decomposition", "Сингулярное разложение");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Gradient, Jacobian, Hessian", "Градиент, якобиан и гессиан");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Determinants, inverse of Matrix", "Определители и обратная матрица");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Eigenvalues, Diagonalization", "Собственные значения и диагонализация");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Basics of Probability", "Основы теории вероятностей");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Bayes Theorem", "Теорема Байеса");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Discrete Mathematics", "Дискретная математика");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Random Variances, PDFs", "Случайные величины и плотности распределения");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Types of Distribution", "Типы распределений");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Basic concepts", "Базовые понятия");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Graphs & Charts", "Графики и диаграммы");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Variables and Data Types", "Переменные и типы данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Essential libraries", "Основные библиотеки");
        ROLE_TOPIC_TITLE_OVERRIDES.put("What are Relational Databases?", "Что такое реляционные базы данных?");
        ROLE_TOPIC_TITLE_OVERRIDES.put("RDBMS Benefits and Limitations", "Преимущества и ограничения РСУБД");
        ROLE_TOPIC_TITLE_OVERRIDES.put("PostgreSQL vs NoSQL Databases", "PostgreSQL и NoSQL-базы: сравнение");
        ROLE_TOPIC_TITLE_OVERRIDES.put("PostgreSQL vs Other RDBMS", "PostgreSQL и другие РСУБД");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Basic RDBMS Concepts", "Базовые понятия РСУБД");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Object Model", "Объектная модель");
        ROLE_TOPIC_TITLE_OVERRIDES.put("High Level Database Concepts", "Высокоуровневые понятия баз данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Relational Model", "Реляционная модель");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Write-ahead Log", "Журнал предзаписи");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Query Processing", "Обработка запросов");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Installation and Setup", "Установка и настройка");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Querying Data", "Запрос данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Using Docker", "Использование Docker");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Filtering Data", "Фильтрация данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Using `systemd`", "Использование systemd");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Modifying Data", "Изменение данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Using `pg_ctl`", "Использование pg_ctl");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Connect using `psql`", "Подключение через psql");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Joining Tables", "Соединение таблиц");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Interoperability with Swift", "Совместимость со Swift");
        ROLE_TOPIC_TITLE_OVERRIDES.put("History and Why Swift?", "История и причины выбора Swift");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Benefits over Objective-C", "Преимущества перед Objective-C");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Core Programming Concepts", "Ключевые концепции программирования");
        ROLE_TOPIC_TITLE_OVERRIDES.put("ViewController Lifecycle", "Жизненный цикл ViewController");
        ROLE_TOPIC_TITLE_OVERRIDES.put("New Project", "Новый проект");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Interface overview", "Обзор интерфейса");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Basic Interfaces", "Базовые интерфейсы");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Declarative Syntax", "Декларативный синтаксис");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Basic Blockchain Knowledge", "Базовые знания о блокчейне");
        ROLE_TOPIC_TITLE_OVERRIDES.put("What is Blockchain", "Что такое блокчейн");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Blockchain Structure", "Структура блокчейна");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Basic Blockchain Operations", "Базовые операции блокчейна");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Why it matters?", "Почему это важно?");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Applications and Uses", "Применение и сценарии использования");
        ROLE_TOPIC_TITLE_OVERRIDES.put("General Blockchain Knowledge", "Общие знания о блокчейне");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Mining and Incentive Models", "Майнинг и модели стимулов");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Consensus Protocols", "Протоколы консенсуса");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Decentralization vs Trust", "Децентрализация и доверие: сравнение");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Blockchain Interoperability", "Интероперабельность блокчейнов");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Blockchain Forking", "Форки блокчейна");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Hybrid Smart Contracts", "Гибридные смарт-контракты");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Smart Contracts", "Смарт-контракты");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Learn the Fundamentals", "Изучение фундаментальных основ");
        ROLE_TOPIC_TITLE_OVERRIDES.put("What is Quality Assurance?", "Что такое обеспечение качества?");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Test Oracles", "Тестовые оракулы");
        ROLE_TOPIC_TITLE_OVERRIDES.put("QA Mindset", "Мышление QA-инженера");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Test Prioritization", "Приоритизация тестов");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Testing Approaches", "Подходы к тестированию");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Project Management", "Управление проектами");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Manage your Testing", "Управление тестированием");
        ROLE_TOPIC_TITLE_OVERRIDES.put("White Box Testing", "Тестирование белого ящика");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Gray Box Testing", "Тестирование серого ящика");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Black Box Testing", "Тестирование чёрного ящика");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Testing Techniques", "Техники тестирования");
        ROLE_TOPIC_TITLE_OVERRIDES.put("SDLC Delivery Model", "Модель поставки SDLC");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Agile Model", "Agile-модель");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Non-Functional Testing", "Нефункциональное тестирование");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Exploratory Testing", "Исследовательское тестирование");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Sanity Testing", "Sanity-тестирование");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Regression Testing", "Регрессионное тестирование");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Stress Testing", "Стресс-тестирование");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Smoke Testing", "Smoke-тестирование");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Security Testing", "Тестирование безопасности");
        ROLE_TOPIC_TITLE_OVERRIDES.put("What is Software Architecture", "Что такое архитектура ПО");
        ROLE_TOPIC_TITLE_OVERRIDES.put("What is a Software Architect", "Кто такой архитектор ПО");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Levels of Architecture", "Уровни архитектуры");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Application Architecture", "Архитектура приложения");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Solution Architecture", "Архитектура решения");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Enterprise Architecture", "Корпоративная архитектура");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Decision Making", "Принятие решений");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Simplifying Things", "Упрощение сложного");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Important Skills to Learn", "Важные навыки для изучения");
        ROLE_TOPIC_TITLE_OVERRIDES.put("How to Code", "Навык программирования");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Technical Skills", "Технические навыки");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Estimate and Evaluate", "Оценка и анализ");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Patterns & Design Principles", "Паттерны и принципы проектирования");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Consult & Coach", "Консультирование и наставничество");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Marketing Skills", "Маркетинговые навыки");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Human Decision Making", "Принятие решений человеком");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Persuasive Technology", "Убеждающие технологии");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Behavioral Science", "Поведенческая наука");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Behavioral Economics", "Поведенческая экономика");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Behavior Design", "Поведенческий дизайн");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Behavior Change Strategies", "Стратегии изменения поведения");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Conceptual Design", "Концептуальный дизайн");
        ROLE_TOPIC_TITLE_OVERRIDES.put("User Stories", "Пользовательские истории");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Business Model Canvas", "Канва бизнес-модели");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Lean Canvas", "Lean Canvas");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Create User Personas", "Создание пользовательских персон");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Understanding the Product", "Понимание продукта");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Technology Expertise", "Технологическая экспертиза");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Language Proficiency", "Языковая грамотность");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Who is a Technical Writer?", "Кто такой технический писатель?");
        ROLE_TOPIC_TITLE_OVERRIDES.put("What is Technical Writing?", "Что такое техническое письмо?");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Role of Technical Writers in Organizations", "Роль технических писателей в организациях");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Forms of Technical Writing", "Форматы технического письма");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Required Skills", "Необходимые навыки");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Written Communication Proficiency", "Навык письменной коммуникации");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Growth as a Technical Writer", "Развитие технического писателя");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Blogging Platforms", "Платформы для блогинга");
        ROLE_TOPIC_TITLE_OVERRIDES.put("SEO Tools", "SEO-инструменты");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Research Tools", "Инструменты исследования");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Publishing Tools", "Инструменты публикации");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Plagiarism Checker", "Проверка плагиата");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Editing Tools", "Инструменты редактирования");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Git / Version Control", "Git и контроль версий");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Story Telling", "Сторителлинг");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Subtle Selling", "Мягкие продажи");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Content Structure", "Структура контента");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Call to Actions", "Призывы к действию");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Content Research", "Исследование контента");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Crafting Great Titles", "Создание сильных заголовков");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Product Content", "Продуктовый контент");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Content Objectives & Intent", "Цели и намерение контента");
        ROLE_TOPIC_TITLE_OVERRIDES.put("General Product Prose", "Общая продуктовая проза");
        ROLE_TOPIC_TITLE_OVERRIDES.put("User Persona", "Пользовательская персона");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Writing Style Guides", "Гайды по стилю письма");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Search Trends", "Поисковые тренды");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Types of Technical Content", "Типы технического контента");
        ROLE_TOPIC_TITLE_OVERRIDES.put("How-to Guides", "Практические руководства");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Support Request Evaluation", "Оценка запросов поддержки");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Server-Side Development", "Серверная разработка");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Segment Structure", "Структура сегмента");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Congestion Control", "Управление перегрузкой");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Max Segment Size", "Максимальный размер сегмента");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Packet Structure", "Структура пакета");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Window Scaling", "Масштабирование окна");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Denial of Service", "Отказ в обслуживании");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Connection Hijacking", "Перехват соединения");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Resource Usage", "Использование ресурсов");
        ROLE_TOPIC_TITLE_OVERRIDES.put("TCP vs UDP", "TCP и UDP: сравнение");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Ordered vs Unordered", "Упорядоченная и неупорядоченная передача");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Heavy vs Lightweight", "Тяжёлые и легковесные подходы");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Packet vs Datagram", "Пакет и датаграмма: сравнение");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Reliable vs Unreliable", "Надёжная и ненадёжная передача");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Data Transfer", "Передача данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Reliable Transmission", "Надёжная передача");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Streaming vs Broadcast", "Потоковая и широковещательная передача");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Out-of-Band Data", "Внеполосные данные");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Flow Control", "Управление потоком");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Selective Ack.", "Выборочное подтверждение");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Error Detection", "Обнаружение ошибок");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Address Conversion", "Преобразование адресов");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Byte Manipulation", "Работа с байтами");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Product Development Lifecycle", "Жизненный цикл разработки продукта");
        ROLE_TOPIC_TITLE_OVERRIDES.put("What is Product Management?", "Что такое управление продуктом?");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Product vs Project Management", "Управление продуктом и проектом: сравнение");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Roles and Responsibilities", "Роли и обязанности");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Key Skills", "Ключевые навыки");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Mind Mapping", "Ментальные карты");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Product Identification", "Определение продукта");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Brainstorming Techniques", "Техники брейншторма");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Iterative Process", "Итеративный процесс");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Blue Ocean Strategy", "Стратегия голубого океана");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Problem Framing", "Формулирование проблемы");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Market Analysis", "Анализ рынка");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Identifying Market Needs", "Выявление потребностей рынка");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Competitive Analysis", "Конкурентный анализ");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Emerging Market Trends", "Новые рыночные тренды");
        ROLE_TOPIC_TITLE_OVERRIDES.put("User Research", "Пользовательские исследования");
        ROLE_TOPIC_TITLE_OVERRIDES.put("User Personas", "Пользовательские персоны");
        ROLE_TOPIC_TITLE_OVERRIDES.put("User Interviews", "Интервью с пользователями");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Surveys and Questionnaires", "Опросы и анкеты");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Ethnographic Research", "Этнографическое исследование");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Market Segmentation", "Сегментация рынка");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Case Studies", "Кейсы");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Feature Creep", "Разрастание функциональности");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Vision & Mission", "Видение и миссия");
        ROLE_TOPIC_TITLE_OVERRIDES.put("What is Engineering Management?", "Что такое инженерный менеджмент?");
        ROLE_TOPIC_TITLE_OVERRIDES.put("EM vs Tech Lead vs IC", "Engineering Manager, Tech Lead и IC: сравнение");
        ROLE_TOPIC_TITLE_OVERRIDES.put("System Monitoring & Performance", "Мониторинг системы и производительность");
        ROLE_TOPIC_TITLE_OVERRIDES.put("CI/CD Implementation", "Внедрение CI/CD");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Technical Roadmapping", "Техническое планирование roadmap");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Development / Release Workflow", "Процесс разработки и релизов");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Software Engineering Background", "Бэкграунд в software engineering");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Architectural Decision-Making", "Принятие архитектурных решений");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Testing Strategies", "Стратегии тестирования");
        ROLE_TOPIC_TITLE_OVERRIDES.put("System Design and Architecture", "System Design и архитектура");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Build vs Buy Evaluation", "Оценка build vs buy");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Technical Standards Setting", "Установка технических стандартов");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Technical Debt and Management", "Технический долг и управление им");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Technical Risk Assessment", "Оценка технических рисков");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Security  Best Practices", "Лучшие практики безопасности");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Code Review Best Practices", "Лучшие практики code review");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Scaling Infrastructure", "Масштабирование инфраструктуры");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Incident Management", "Управление инцидентами");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Technical Documentation", "Техническая документация");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Legacy System Retirement", "Вывод legacy-систем из эксплуатации");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Conflict Resolution", "Разрешение конфликтов");
        ROLE_TOPIC_TITLE_OVERRIDES.put("One-on-One Meetings", "Встречи one-on-one");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Hiring and Recruitment", "Найм и рекрутинг");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Feedback Delivery", "Предоставление обратной связи");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Team Meetings", "Командные встречи");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Team Structure and Design", "Структура и дизайн команды");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Team Motivation", "Мотивация команды");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Status Reporting", "Статусная отчётность");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Performance Evaluations", "Оценка эффективности");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Trust / Influence Building", "Построение доверия и влияния");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Stakeholder Management", "Управление стейкхолдерами");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Career Development Planning", "Планирование карьерного развития");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Emotional Intelligence", "Эмоциональный интеллект");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Cross-functional Collaboration", "Кросс-функциональное взаимодействие");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Mentoring and Coaching", "Менторинг и коучинг");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Agile methodologies", "Agile-методологии");
        ROLE_TOPIC_TITLE_OVERRIDES.put("What is DevRel?", "Что такое DevRel?");
        ROLE_TOPIC_TITLE_OVERRIDES.put("History and Evolution", "История и развитие");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Importance of DevRel", "Значимость DevRel");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Community Support", "Поддержка сообщества");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Developer Experience", "Developer Experience");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Content Creation", "Создание контента");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Developer Journey", "Путь разработчика");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Feedback Loop", "Петля обратной связи");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Developer Marketing", "Маркетинг для разработчиков");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Public Speaking", "Публичные выступления");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Presentation Techniques", "Техники презентации");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Writing Skills", "Навыки письма");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Rules of Three", "Правило трёх");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Basic Programming Skills", "Базовые навыки программирования");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Social Media", "Социальные сети");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Community Engagement", "Вовлечение сообщества");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Engaging Audience", "Вовлечение аудитории");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Online Communities", "Онлайн-сообщества");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Event Participation", "Участие в мероприятиях");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Managing Discussions", "Управление обсуждениями");
        ROLE_TOPIC_TITLE_OVERRIDES.put("APIs & SDKs", "API и SDK");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Repetition & Reinforcement", "Повторение и закрепление");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Issues & Pull Requests", "Issues и запросы на слияние (Pull Request)");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Handling Q&A", "Работа с вопросами и ответами");
        ROLE_TOPIC_TITLE_OVERRIDES.put("What is BI?", "Что такое BI?");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Why BI Matters?", "Почему BI важен?");
        ROLE_TOPIC_TITLE_OVERRIDES.put("BI Analyst vs Other Roles", "BI-аналитик и другие роли: сравнение");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Metrics and KPIs", "Метрики и KPI");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Stakeholder Identification", "Определение стейкхолдеров");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Types of BI Operations", "Типы BI-операций");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Key Business Functions", "Ключевые бизнес-функции");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Operational BI", "Операционный BI");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Types of Data Analysis", "Типы анализа данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Tactical BI", "Тактический BI");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Strategic BI", "Стратегический BI");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Categorical vs Numerical", "Категориальные и числовые данные");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Correlation vs Causation", "Корреляция и причинность");
        ROLE_TOPIC_TITLE_OVERRIDES.put("N plus one problem", "Проблема N+1");
        ROLE_TOPIC_TITLE_OVERRIDES.put("AI Product Creation Cycle", "Цикл создания AI-продукта");
        ROLE_TOPIC_TITLE_OVERRIDES.put("App Anatomy", "Анатомия приложения");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Browsers / DevTools", "Браузеры и DevTools");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Choose a Prototype Tool", "Выбор инструмента прототипирования");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Definition & Scope", "Определение и рамки");
        ROLE_TOPIC_TITLE_OVERRIDES.put("E2E Testing", "E2E-тестирование");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Feature Scoping", "Формирование границ функциональности");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Feedback & Validation", "Обратная связь и валидация");
        ROLE_TOPIC_TITLE_OVERRIDES.put("HTML / CSS / JavaScript", "HTML, CSS и JavaScript");
        ROLE_TOPIC_TITLE_OVERRIDES.put("New Feature / Structural Change", "Новая функция или структурное изменение");
        ROLE_TOPIC_TITLE_OVERRIDES.put("PostgreSQL / MySQL", "PostgreSQL и MySQL");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Problem Definition", "Определение проблемы");
        ROLE_TOPIC_TITLE_OVERRIDES.put("C# Basics", "Основы C#");
        ROLE_TOPIC_TITLE_OVERRIDES.put("OOP in C#", "ООП в C#");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Unity Editor Fundamentals", "Основы редактора Unity");
        ROLE_TOPIC_TITLE_OVERRIDES.put("MonoBehaviour and Game Loop", "MonoBehaviour и игровой цикл");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Input and Physics in Unity", "Ввод и физика в Unity");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Scriptable Objects and Project Architecture", "Scriptable Objects и архитектура проекта");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Unity UI Toolkit Basics", "Основы Unity UI Toolkit");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Java Basics", "Основы Java");
        ROLE_TOPIC_TITLE_OVERRIDES.put("OOP in Java", "ООП в Java");
        ROLE_TOPIC_TITLE_OVERRIDES.put("HTTP Basics", "Основы HTTP");
        ROLE_TOPIC_TITLE_OVERRIDES.put("SQL Basics", "Основы SQL");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Pull Request", "Pull Request");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Pull Requests", "Запросы на слияние (Pull Request)");
        ROLE_TOPIC_TITLE_OVERRIDES.put("!DOCTYPE Declaration", "Декларация !DOCTYPE");
        ROLE_TOPIC_TITLE_OVERRIDES.put("$0 in Shell Scripting", "Аргумент $0 в shell-скриптах");
        ROLE_TOPIC_TITLE_OVERRIDES.put("--hard", "Жёсткий reset через --hard");
        ROLE_TOPIC_TITLE_OVERRIDES.put("--mixed", "Смешанный reset через --mixed");
        ROLE_TOPIC_TITLE_OVERRIDES.put("-replace option in apply", "Опция -replace при terraform apply");
        ROLE_TOPIC_TITLE_OVERRIDES.put("2 3 4 Trees", "2-3-4 деревья");
        ROLE_TOPIC_TITLE_OVERRIDES.put("2 3 Search Trees", "2-3 деревья поиска");
        ROLE_TOPIC_TITLE_OVERRIDES.put("2-3 Trees", "2-3 деревья");
        ROLE_TOPIC_TITLE_OVERRIDES.put("A* Algorithm", "Алгоритм A*");
        ROLE_TOPIC_TITLE_OVERRIDES.put("AVL Trees", "AVL-деревья");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Binary Trees", "Бинарные деревья");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Bellman Ford's Algorithm", "Алгоритм Беллмана-Форда");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Bellman-Ford Algoritm", "Алгоритм Беллмана-Форда");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Algorithmic Complexity", "Алгоритмическая сложность");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Big-O Notation", "Нотация Big O");
        ROLE_TOPIC_TITLE_OVERRIDES.put("BigInt Operators", "Операторы BigInt");
        ROLE_TOPIC_TITLE_OVERRIDES.put("@Input \\u0026 @Output", "Входные и выходные параметры компонентов");
        ROLE_TOPIC_TITLE_OVERRIDES.put("@MockBean Annotation", "Аннотация @MockBean");
        ROLE_TOPIC_TITLE_OVERRIDES.put("@SpringBootTest Annotation", "Аннотация @SpringBootTest");
        ROLE_TOPIC_TITLE_OVERRIDES.put("AOF rewrite & compaction", "Перезапись и сжатие AOF");
        ROLE_TOPIC_TITLE_OVERRIDES.put("AOF rewrite \\u0026 compaction", "Перезапись и сжатие AOF");
        ROLE_TOPIC_TITLE_OVERRIDES.put("API Definitions", "Определения API");
        ROLE_TOPIC_TITLE_OVERRIDES.put("API Endpoints", "Endpoint'ы API");
        ROLE_TOPIC_TITLE_OVERRIDES.put("API Gateways", "API Gateway");
        ROLE_TOPIC_TITLE_OVERRIDES.put("API Keys", "API-ключи");
        ROLE_TOPIC_TITLE_OVERRIDES.put("API Performance", "Производительность API");
        ROLE_TOPIC_TITLE_OVERRIDES.put("API Protection", "Защита API");
        ROLE_TOPIC_TITLE_OVERRIDES.put("API Reference", "Справочник API");
        ROLE_TOPIC_TITLE_OVERRIDES.put("API References", "Справочники API");
        ROLE_TOPIC_TITLE_OVERRIDES.put("API Requests", "API-запросы");
        ROLE_TOPIC_TITLE_OVERRIDES.put("API Semantics", "Семантика API");
        ROLE_TOPIC_TITLE_OVERRIDES.put("API Styles", "Стили API");
        ROLE_TOPIC_TITLE_OVERRIDES.put("API Usage", "Использование API");
        ROLE_TOPIC_TITLE_OVERRIDES.put("API strategy", "Стратегия API");
        ROLE_TOPIC_TITLE_OVERRIDES.put("APIs & Integrations", "API и интеграции");
        ROLE_TOPIC_TITLE_OVERRIDES.put("APIs \\u0026 Integrations", "API и интеграции");
        ROLE_TOPIC_TITLE_OVERRIDES.put("APIs & SDKs", "API и SDK");
        ROLE_TOPIC_TITLE_OVERRIDES.put("APIs \\u0026 SDKs", "API и SDK");
        ROLE_TOPIC_TITLE_OVERRIDES.put("AI Model Integration", "Интеграция AI-моделей");
        ROLE_TOPIC_TITLE_OVERRIDES.put("AJAX in WordPress", "AJAX в WordPress");
        ROLE_TOPIC_TITLE_OVERRIDES.put("AWS Global Infrastructure", "Глобальная инфраструктура AWS");
        ROLE_TOPIC_TITLE_OVERRIDES.put("AWS / Azure / GCP", "AWS, Azure и GCP");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Accessibilty", "Доступность");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Accesibility", "Доступность");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Accessibility Inspector", "Accessibility Inspector");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Accuracy", "Точность");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Action Views", "Представления Action View");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Active Record Conditions", "Условия в Active Record");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Active-Active geo Distribution", "Active-Active геораспределение");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Activity Diagrams", "Диаграммы активности");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Actors", "Акторы");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Actuators", "Actuator'ы");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Adding / Updating", "Добавление и обновление");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Adding Disks", "Добавление дисков");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Adding Extra Extensions", "Добавление расширений");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Abbreviation Element", "Элемент аббревиатуры");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Address Element", "Элемент адреса");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Article Element", "Элемент статьи");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Aside Element", "Боковой элемент aside");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Body Tag", "Тег body");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Admin Customization", "Настройка админ-панели");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Adversarial Examples", "Состязательные примеры");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Adversarial Training", "Состязательное обучение");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Advocacy", "Адвокация");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Agent Loop", "Цикл работы агента");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Agent Team", "Команда агентов");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Agentic Loop", "Агентный цикл");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Agile Methodology", "Agile-методология");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Algorithmic Bias", "Алгоритмическая предвзятость");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Aliases", "Алиасы");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Alignment & Buy-In", "Согласование и поддержка решения");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Alignment \\u0026 Buy-In", "Согласование и поддержка решения");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Alter Table", "Изменение таблицы ALTER TABLE");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Always use env variables", "Всегда использовать переменные окружения");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Animations & Graphics", "Анимации и графика");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Animations \\u0026 Graphics", "Анимации и графика");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Anti-Corruption Layer", "Антикоррупционный слой");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Anticipate Questions", "Предугадывание вопросов");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Any Frontend Framework", "Любой frontend-фреймворк");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Any Programming Language", "Любой язык программирования");
        ROLE_TOPIC_TITLE_OVERRIDES.put("App Configurations", "Конфигурация приложения");
        ROLE_TOPIC_TITLE_OVERRIDES.put("App Directory", "Структура директории приложения");
        ROLE_TOPIC_TITLE_OVERRIDES.put("App Router", "App Router");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Apple App store", "Apple App Store");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Applicability", "Применимость");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Application Caching", "Кэширование приложения");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Application Layer", "Прикладной уровень");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Array to Slice Conversion", "Преобразование массива в slice");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Ask for Structured Output e.g. JSON, XML, HTML etc.", "Запрос структурированного вывода: JSON, XML, HTML и др.");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Ask for one task at a time", "Запрашивать одну задачу за раз");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Asnc-await (C#)", "Async/await в C#");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Assets Optimization", "Оптимизация assets");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Assets Pipeline", "Assets pipeline");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Assuming Roles", "Принятие ролей");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Async / Await", "Async/await");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Async Components", "Асинхронные компоненты");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Async Programming", "Асинхронное программирование");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Async Request Reply", "Асинхронный request-reply");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Asynchronism", "Асинхронность");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Asynchronous", "Асинхронный режим");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Asynchronous Django", "Асинхронный Django");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Authentication with Laravel", "Аутентификация в Laravel");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Automated Workflows", "Автоматизированные workflows");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Be Concise", "Писать кратко");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Be consistent", "Соблюдать единообразие");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Be mindful of extensions", "Учитывать расширения");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Be specific about what you want", "Формулировать запрос конкретно");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Be specific in what you want", "Формулировать задачу конкретно");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Behavior Change Games", "Игры для изменения поведения");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Benchmark Datasets", "Benchmark-наборы данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Better Errors", "Улучшенная обработка ошибок");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Bias Recognition", "Распознавание bias");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Bias Recognition / Mitigation", "Распознавание и снижение bias");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Bias & Toxicity Guardrails", "Защита от bias и токсичности");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Bias \\u0026 Toxicity Guardrails", "Защита от bias и токсичности");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Big Endian", "Порядок байтов Big Endian");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Bind Mounts", "Bind mount'ы");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Blade Loops", "Циклы Blade");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Blade Templating", "Шаблоны Blade");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Blameless Post-mortems", "Post-mortem без поиска виноватых");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Block Display", "Блочное отображение");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Block Editor", "Блочный редактор");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Block Theme Structure", "Структура блочной темы");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Block Theme Styles", "Стили блочной темы");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Block Themes", "Блочные темы");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Blockchains", "Блокчейны");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Blocks", "Блоки");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Blog Posts", "Посты в блоге");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Blogging", "Ведение блога");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Blue-Green Deployments", "Blue-Green deployments");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Board presentations", "Презентации для руководства");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Boundaries", "Границы");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Box Shadows", "Тени блоков");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Break Statement in Ruby", "Оператор break в Ruby");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Breakpoints", "Breakpoint'ы");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Broadcast Receiver", "Broadcast Receiver");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Brown Bags", "Brown bag встречи");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Browser Rendering", "Рендеринг в браузере");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Browsers", "Браузеры");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Brute Force", "Полный перебор");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Buckets / Objects", "Buckets и объекты");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Budget requests", "Бюджетные запросы");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Buffer Overflow", "Переполнение буфера");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Buffered Streams", "Буферизованные потоки");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Building Container Images", "Сборка container images");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Building JSON / RESTful APIs", "Создание JSON и RESTful API");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Building SDKs", "Создание SDK");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Building Your Portfolio", "Создание портфолио");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Building & Consuming APIs", "Создание и использование API");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Building \\u0026 Consuming APIs", "Создание и использование API");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Building for Scale", "Проектирование под масштабирование");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Built-in Functions", "Встроенные функции");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Built-in Types", "Встроенные типы");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Builtin", "Встроенные возможности");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Caching API Endpoints in Next.js", "Кэширование API endpoint'ов в Next.js");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Caching Data", "Кэширование данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Calculus", "Математический анализ");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Calibrating LLMs", "Калибровка LLM");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Call to Action", "Призыв к действию");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Callback Functions", "Callback-функции");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Callback Hell", "Callback hell");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Canary Deployments", "Canary deployments");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Canonical Link", "Каноническая ссылка");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Capacity Settings", "Настройки capacity");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Capturing Values & Memory Mgmt.", "Захват значений и управление памятью");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Capturing Values \\u0026 Memory Mgmt.", "Захват значений и управление памятью");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Cardinality Aggregation", "Агрегация cardinality");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Cascading Order", "Порядок каскадирования");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Category", "Категории");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Certificates", "Сертификаты");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Certifications", "Сертификации");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Chain of Thought (CoT)", "Chain of Thought (CoT)");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Chain of Thought Prompting", "Chain-of-Thought prompting");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Change Tracker API", "Change Tracker API");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Change strategy", "Стратегия изменений");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Channel", "Каналы");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Character", "Символы");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Character Encodings", "Кодировки символов");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Chart Categories", "Категории графиков");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Charting", "Построение графиков");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Chat Completions API", "Chat Completions API");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Chatty I/O", "Избыточный ввод-вывод");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Cheating", "Обман в пользовательском поведении");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Checking Service Logs", "Проверка логов сервиса");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Checking Service Status", "Проверка статуса сервиса");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Checkout Tags", "Переключение на теги");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Checksum", "Контрольная сумма");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Child Combinator", "Дочерний комбинатор");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Choreography", "Хореография сервисов");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Cipher", "Шифр");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Circuit Breaker", "Circuit Breaker");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Citing Sources", "Цитирование источников");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Claim Check", "Claim Check");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Classic Editor", "Классический редактор");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Classic Themes", "Классические темы");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Classification", "Классификация");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Clean Code", "Чистый код");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Clean Up", "Очистка ресурсов");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Clear the Page of Distractions", "Убрать отвлекающие элементы со страницы");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Client-Server", "Клиент-серверная архитектура");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Client-Side Data Fetching", "Получение данных на клиенте");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Client-Side Field Level", "Шифрование полей на стороне клиента");
        ROLE_TOPIC_TITLE_OVERRIDES.put(".NET Framework Based", "На основе .NET Framework");
        ROLE_TOPIC_TITLE_OVERRIDES.put("ACID, CAP Theorem", "ACID и CAP-теорема");
        ROLE_TOPIC_TITLE_OVERRIDES.put("ACL", "Списки контроля доступа (ACL)");
        ROLE_TOPIC_TITLE_OVERRIDES.put("ACLs", "Списки контроля доступа (ACL)");
        ROLE_TOPIC_TITLE_OVERRIDES.put("AMIs", "AMI-образы");
        ROLE_TOPIC_TITLE_OVERRIDES.put("AVERAGE", "Функция AVERAGE");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Accessibility Inspector", "Инспектор доступности");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Activity", "Activity в Android");
        ROLE_TOPIC_TITLE_OVERRIDES.put("ActivityIndicator", "Индикатор загрузки ActivityIndicator");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Adapter", "Паттерн Adapter");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Algorithms", "Алгоритмы");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Amazon EC2 ( Compute)", "Amazon EC2: вычисления");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Amazon RDS (Database)", "Amazon RDS: управляемая база данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Animated Builder", "AnimatedBuilder");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Animated Widget", "AnimatedWidget");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Anthropic Tool Use", "Использование tools в Anthropic");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Anthropic\\u0027s Claude", "Claude от Anthropic");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Antimalware", "Защита от вредоносного ПО");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Antivirus", "Антивирусная защита");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Argument Dependent Lookup (ADL)", "Argument Dependent Lookup (ADL)");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Ask Al to create a list of possible causes", "Попросить AI составить список возможных причин");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Asynchronous Flow", "Асинхронный Flow");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Atomic Operations & Memory Barriers", "Атомарные операции и memory barriers");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Atomic Operations \\u0026 Memory Barriers", "Атомарные операции и memory barriers");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Atomicity in Redis", "Атомарность в Redis");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Attack Vectors", "Векторы атак");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Attribute Based Access Control (ABAC)", "Attribute-Based Access Control (ABAC)");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Attribute Inheritance", "Наследование атрибутов");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Attribute Selectors", "Селекторы атрибутов");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Attributes", "Атрибуты");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Attributes Accessors", "Accessor'ы атрибутов");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Audit & Compliance Mapping", "Аудит и карта соответствия требованиям");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Audit \\u0026 Compliance Mapping", "Аудит и карта соответствия требованиям");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Authentication / Authorization", "Аутентификация и авторизация");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Authentication with Starter Kits", "Аутентификация через starter kits");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Authorization Gates", "Authorization Gates");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Auto layout", "Auto Layout");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Auto-Scaling", "Автомасштабирование");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Auto-Scaling Groups", "Группы автомасштабирования");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Autoconfiguration", "Автоконфигурация");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Autoloading", "Автозагрузка классов");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Automate the Act of Repetition", "Автоматизация повторяющихся действий");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Automated Deployments", "Автоматизированные deployments");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Automations", "Автоматизации");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Availability", "Доступность сервиса");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Availability in Numbers", "Доступность в цифрах");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Avoid Choice Overload", "Избегать перегрузки выбором");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Avoid Cognitive Overhead", "Избегать когнитивной перегрузки");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Avoid Direct Payments", "Избегать прямых платежей");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Avoid passing nulls, booleans", "Избегать передачи null и boolean-флагов");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Awareness Blog Posts", "Awareness-посты в блоге");
        ROLE_TOPIC_TITLE_OVERRIDES.put("BI Communities", "BI-сообщества");
        ROLE_TOPIC_TITLE_OVERRIDES.put("BI Competitions", "BI-соревнования");
        ROLE_TOPIC_TITLE_OVERRIDES.put("BI Platforms", "BI-платформы");
        ROLE_TOPIC_TITLE_OVERRIDES.put("BM25 Algorithm", "Алгоритм BM25");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Back Pressure", "Back pressure");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Backend Automation", "Автоматизация backend-тестов");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Backends for Frontend", "Backend for Frontend");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Backlinking", "Обратные ссылки");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Backpressure", "Backpressure");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Backtracking", "Backtracking");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Backup / Restore", "Backup и восстановление");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Badge", "Badge-компонент");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Badges", "Badge-компоненты");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Balance", "Балансировка решений");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Balanced Search Trees", "Сбалансированные деревья поиска");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Banner", "Banner-компонент");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Banners", "Banner-компоненты");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Barplot", "Столбчатая диаграмма");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Barrier", "Барьер синхронизации");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Base", "Базовые понятия");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Baseline", "Базовая линия");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Bash Alias", "Алиасы Bash");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Bash Debug", "Отладка Bash");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Basic Auth", "Basic Auth");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Basic PHP Syntax", "Базовый синтаксис PHP");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Basic Prompting", "Базовый prompting");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Basic Queries in Active Record", "Базовые запросы в Active Record");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Basic Regex Syntax", "Базовый синтаксис регулярных выражений");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Batch", "Пакетная обработка");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Batch Processing", "Пакетная обработка");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Batching", "Batching");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Bias", "Предвзятость модели");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Big-? Notation", "Нотация Big O");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Boolean Data Type", "Логический тип данных");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Boost Your WordPress Career", "Развитие карьеры в WordPress");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Boosting Queries", "Boosting-запросы");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Boot Loaders", "Загрузчики ОС");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Bottom Sheet", "Bottom Sheet");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Box", "Box-модель");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Brainwriting", "Brainwriting");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Build Tool API", "API build tool");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Business", "Бизнес-контекст");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Busy Frontend", "Перегруженный frontend");
        ROLE_TOPIC_TITLE_OVERRIDES.put("Buttons", "Кнопки");
        ROLE_TOPIC_TITLE_OVERRIDES.put("C Interoperability", "Интероперабельность с C");

        TOPIC_PHRASE_TRANSLATIONS.put("Product Development", "разработка продукта");
        TOPIC_PHRASE_TRANSLATIONS.put("Product Management", "управление продуктом");
        TOPIC_PHRASE_TRANSLATIONS.put("Product Requirements", "требования к продукту");
        TOPIC_PHRASE_TRANSLATIONS.put("Product Roadmap", "roadmap продукта");
        TOPIC_PHRASE_TRANSLATIONS.put("Creating a Roadmap", "создание roadmap");
        TOPIC_PHRASE_TRANSLATIONS.put("Product", "продукт");
        TOPIC_PHRASE_TRANSLATIONS.put("Development", "разработка");
        TOPIC_PHRASE_TRANSLATIONS.put("Validation", "валидация");
        TOPIC_PHRASE_TRANSLATIONS.put("Refinement", "доработка");
        TOPIC_PHRASE_TRANSLATIONS.put("Collaboration", "совместная работа");
        TOPIC_PHRASE_TRANSLATIONS.put("Deployment", "развёртывание");
        TOPIC_PHRASE_TRANSLATIONS.put("Prototyping", "прототипирование");
        TOPIC_PHRASE_TRANSLATIONS.put("Accessibility", "доступность");
        TOPIC_PHRASE_TRANSLATIONS.put("Aggregation", "агрегация");
        TOPIC_PHRASE_TRANSLATIONS.put("Algorithmic", "алгоритмический");
        TOPIC_PHRASE_TRANSLATIONS.put("Application", "приложение");
        TOPIC_PHRASE_TRANSLATIONS.put("Applications", "приложения");
        TOPIC_PHRASE_TRANSLATIONS.put("Built-in", "встроенный");
        TOPIC_PHRASE_TRANSLATIONS.put("Building", "создание");
        TOPIC_PHRASE_TRANSLATIONS.put("Caching", "кэширование");
        TOPIC_PHRASE_TRANSLATIONS.put("Callback", "callback");
        TOPIC_PHRASE_TRANSLATIONS.put("Callbacks", "callbacks");
        TOPIC_PHRASE_TRANSLATIONS.put("Checking", "проверка");
        TOPIC_PHRASE_TRANSLATIONS.put("Containers", "контейнеры");
        TOPIC_PHRASE_TRANSLATIONS.put("Container", "контейнер");
        TOPIC_PHRASE_TRANSLATIONS.put("Conversion", "преобразование");
        TOPIC_PHRASE_TRANSLATIONS.put("Customization", "настройка");
        TOPIC_PHRASE_TRANSLATIONS.put("Directory", "директория");
        TOPIC_PHRASE_TRANSLATIONS.put("Elements", "элементы");
        TOPIC_PHRASE_TRANSLATIONS.put("Element", "элемент");
        TOPIC_PHRASE_TRANSLATIONS.put("Endpoints", "endpoint'ы");
        TOPIC_PHRASE_TRANSLATIONS.put("Endpoint", "endpoint");
        TOPIC_PHRASE_TRANSLATIONS.put("Examples", "примеры");
        TOPIC_PHRASE_TRANSLATIONS.put("Functions", "функции");
        TOPIC_PHRASE_TRANSLATIONS.put("Function", "функция");
        TOPIC_PHRASE_TRANSLATIONS.put("Gateway", "gateway");
        TOPIC_PHRASE_TRANSLATIONS.put("Integrations", "интеграции");
        TOPIC_PHRASE_TRANSLATIONS.put("Integration", "интеграция");
        TOPIC_PHRASE_TRANSLATIONS.put("Inspector", "инспектор");
        TOPIC_PHRASE_TRANSLATIONS.put("Optimization", "оптимизация");
        TOPIC_PHRASE_TRANSLATIONS.put("Pipeline", "pipeline");
        TOPIC_PHRASE_TRANSLATIONS.put("Pipelines", "pipelines");
        TOPIC_PHRASE_TRANSLATIONS.put("Rendering", "рендеринг");
        TOPIC_PHRASE_TRANSLATIONS.put("Requests", "запросы");
        TOPIC_PHRASE_TRANSLATIONS.put("Request", "запрос");
        TOPIC_PHRASE_TRANSLATIONS.put("References", "справочники");
        TOPIC_PHRASE_TRANSLATIONS.put("Reference", "справочник");
        TOPIC_PHRASE_TRANSLATIONS.put("Semantics", "семантика");
        TOPIC_PHRASE_TRANSLATIONS.put("Status", "статус");
        TOPIC_PHRASE_TRANSLATIONS.put("Strategy", "стратегия");
        TOPIC_PHRASE_TRANSLATIONS.put("Tags", "теги");
        TOPIC_PHRASE_TRANSLATIONS.put("Types", "типы");
        TOPIC_PHRASE_TRANSLATIONS.put("Usage", "использование");
        TOPIC_PHRASE_TRANSLATIONS.put("Variables", "переменные");
        TOPIC_PHRASE_TRANSLATIONS.put("Workflows", "workflows");
        TOPIC_PHRASE_TRANSLATIONS.put("Workflow", "workflow");
        TOPIC_PHRASE_TRANSLATIONS.put("Generation", "генерация");
        TOPIC_PHRASE_TRANSLATIONS.put("Definition", "определение");
        TOPIC_PHRASE_TRANSLATIONS.put("Scope", "границы");
        TOPIC_PHRASE_TRANSLATIONS.put("Selection", "выбор");
        TOPIC_PHRASE_TRANSLATIONS.put("Discovery", "исследование");
        TOPIC_PHRASE_TRANSLATIONS.put("Execution", "исполнение");
        TOPIC_PHRASE_TRANSLATIONS.put("Growth", "рост");
        TOPIC_PHRASE_TRANSLATIONS.put("Maturity", "зрелость");
        TOPIC_PHRASE_TRANSLATIONS.put("Decline", "спад");
        TOPIC_PHRASE_TRANSLATIONS.put("Process", "процесс");
        TOPIC_PHRASE_TRANSLATIONS.put("Processes", "процессы");
        TOPIC_PHRASE_TRANSLATIONS.put("Lifecycle", "жизненный цикл");
        TOPIC_PHRASE_TRANSLATIONS.put("Responsibilities", "обязанности");
        TOPIC_PHRASE_TRANSLATIONS.put("Skills", "навыки");
        TOPIC_PHRASE_TRANSLATIONS.put("Required Skills", "необходимые навыки");
        TOPIC_PHRASE_TRANSLATIONS.put("Technical Skills", "технические навыки");
        TOPIC_PHRASE_TRANSLATIONS.put("Programming Skills", "навыки программирования");
        TOPIC_PHRASE_TRANSLATIONS.put("Communication", "коммуникация");
        TOPIC_PHRASE_TRANSLATIONS.put("Written Communication", "письменная коммуникация");
        TOPIC_PHRASE_TRANSLATIONS.put("Documentation", "документация");
        TOPIC_PHRASE_TRANSLATIONS.put("Technical Documentation", "техническая документация");
        TOPIC_PHRASE_TRANSLATIONS.put("Technical Writing", "техническое письмо");
        TOPIC_PHRASE_TRANSLATIONS.put("Content", "контент");
        TOPIC_PHRASE_TRANSLATIONS.put("Content Marketing", "контент-маркетинг");
        TOPIC_PHRASE_TRANSLATIONS.put("Content SEO", "SEO контента");
        TOPIC_PHRASE_TRANSLATIONS.put("Content Optimization", "оптимизация контента");
        TOPIC_PHRASE_TRANSLATIONS.put("Content Analysis", "анализ контента");
        TOPIC_PHRASE_TRANSLATIONS.put("Content Distribution", "распространение контента");
        TOPIC_PHRASE_TRANSLATIONS.put("Content Funnel", "контент-воронка");
        TOPIC_PHRASE_TRANSLATIONS.put("Help Content", "справочный контент");
        TOPIC_PHRASE_TRANSLATIONS.put("Release Notes", "заметки к релизу");
        TOPIC_PHRASE_TRANSLATIONS.put("Product Announcements", "продуктовые анонсы");
        TOPIC_PHRASE_TRANSLATIONS.put("Research Reports", "исследовательские отчёты");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Engineering", "инженерия данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Science", "Data Science");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Analytics", "аналитика данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Analysis", "анализ данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Warehouse", "хранилище данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Warehousing", "построение хранилищ данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Mart", "витрина данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Mesh", "Data Mesh");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Fabric", "Data Fabric");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Hub", "Data Hub");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Lake", "озеро данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Pipelines", "пайплайны данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Pipeline", "пайплайн данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Ingestion", "загрузка данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Sources", "источники данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Quality", "качество данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Lineage", "происхождение данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Masking", "маскирование данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Obfuscation", "обфускация данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Interoperability", "интероперабельность данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Cleaning", "очистка данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Cleanup", "очистка данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Transformation", "трансформация данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Visualisation", "визуализация данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Visualization", "визуализация данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Formats", "форматы данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Structures", "структуры данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Loading", "загрузка данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Preparation", "подготовка данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Train - Test Data", "обучающая и тестовая выборки");
        TOPIC_PHRASE_TRANSLATIONS.put("Missing Data", "пропущенные данные");
        TOPIC_PHRASE_TRANSLATIONS.put("Removing Duplicates", "удаление дубликатов");
        TOPIC_PHRASE_TRANSLATIONS.put("Finding Outliers", "поиск выбросов");
        TOPIC_PHRASE_TRANSLATIONS.put("Big Data Technologies", "технологии Big Data");
        TOPIC_PHRASE_TRANSLATIONS.put("Big Data Concepts", "концепции Big Data");
        TOPIC_PHRASE_TRANSLATIONS.put("Big Data Tools", "инструменты Big Data");
        TOPIC_PHRASE_TRANSLATIONS.put("Business Intelligence", "Business Intelligence");
        TOPIC_PHRASE_TRANSLATIONS.put("Predictive Analysis", "предиктивный анализ");
        TOPIC_PHRASE_TRANSLATIONS.put("Descriptive Analysis", "описательный анализ");
        TOPIC_PHRASE_TRANSLATIONS.put("Diagnostic Analysis", "диагностический анализ");
        TOPIC_PHRASE_TRANSLATIONS.put("Prescriptive Analysis", "предписательный анализ");
        TOPIC_PHRASE_TRANSLATIONS.put("Correlation Analysis", "корреляционный анализ");
        TOPIC_PHRASE_TRANSLATIONS.put("Hypothesis Testing", "проверка гипотез");
        TOPIC_PHRASE_TRANSLATIONS.put("Model Evaluation Techniques", "техники оценки модели");
        TOPIC_PHRASE_TRANSLATIONS.put("Feature Engineering", "построение признаков");
        TOPIC_PHRASE_TRANSLATIONS.put("Feature Selection", "отбор признаков");
        TOPIC_PHRASE_TRANSLATIONS.put("Feature Scaling", "масштабирование признаков");
        TOPIC_PHRASE_TRANSLATIONS.put("Normalization", "нормализация");
        TOPIC_PHRASE_TRANSLATIONS.put("Dimensionality Reduction", "снижение размерности");
        TOPIC_PHRASE_TRANSLATIONS.put("Model Selection", "выбор модели");
        TOPIC_PHRASE_TRANSLATIONS.put("Principal Component Analysis", "метод главных компонент");
        TOPIC_PHRASE_TRANSLATIONS.put("Natural Language Processing", "обработка естественного языка");
        TOPIC_PHRASE_TRANSLATIONS.put("Image Recognition", "распознавание изображений");
        TOPIC_PHRASE_TRANSLATIONS.put("Decision Trees", "деревья решений");
        TOPIC_PHRASE_TRANSLATIONS.put("Neural Networks", "нейронные сети");
        TOPIC_PHRASE_TRANSLATIONS.put("Linear Regression", "линейная регрессия");
        TOPIC_PHRASE_TRANSLATIONS.put("Logistic Regression", "логистическая регрессия");
        TOPIC_PHRASE_TRANSLATIONS.put("Deep Learning", "глубокое обучение");
        TOPIC_PHRASE_TRANSLATIONS.put("Machine Learning", "машинное обучение");
        TOPIC_PHRASE_TRANSLATIONS.put("Supervised Learning", "обучение с учителем");
        TOPIC_PHRASE_TRANSLATIONS.put("Unsupervised Learning", "обучение без учителя");
        TOPIC_PHRASE_TRANSLATIONS.put("Reinforcement Learning", "обучение с подкреплением");
        TOPIC_PHRASE_TRANSLATIONS.put("Semi-supervised Learning", "полуобучение");
        TOPIC_PHRASE_TRANSLATIONS.put("Self-supervised Learning", "самообучение");
        TOPIC_PHRASE_TRANSLATIONS.put("Cloud Security", "облачная безопасность");
        TOPIC_PHRASE_TRANSLATIONS.put("Cloud Skills", "облачные навыки");
        TOPIC_PHRASE_TRANSLATIONS.put("Cloud Computing", "облачные вычисления");
        TOPIC_PHRASE_TRANSLATIONS.put("Cloud Architectures", "облачные архитектуры");
        TOPIC_PHRASE_TRANSLATIONS.put("Cloud Storage", "облачное хранилище");
        TOPIC_PHRASE_TRANSLATIONS.put("Cloud SQL", "Cloud SQL");
        TOPIC_PHRASE_TRANSLATIONS.put("Cloud ML", "облачное машинное обучение");
        TOPIC_PHRASE_TRANSLATIONS.put("Cloud Messaging", "облачные сообщения");
        TOPIC_PHRASE_TRANSLATIONS.put("Cluster Computing", "кластерные вычисления");
        TOPIC_PHRASE_TRANSLATIONS.put("Cluster Management", "управление кластером");
        TOPIC_PHRASE_TRANSLATIONS.put("Distributed Systems", "распределённые системы");
        TOPIC_PHRASE_TRANSLATIONS.put("Distributed File Systems", "распределённые файловые системы");
        TOPIC_PHRASE_TRANSLATIONS.put("Messaging Systems", "системы сообщений");
        TOPIC_PHRASE_TRANSLATIONS.put("Messages vs Streams", "сообщения и потоки: сравнение");
        TOPIC_PHRASE_TRANSLATIONS.put("Async vs Sync Communication", "асинхронная и синхронная коммуникация");
        TOPIC_PHRASE_TRANSLATIONS.put("End-to-End Testing", "end-to-end тестирование");
        TOPIC_PHRASE_TRANSLATIONS.put("A/B Testing", "A/B-тестирование");
        TOPIC_PHRASE_TRANSLATIONS.put("Load Testing", "нагрузочное тестирование");
        TOPIC_PHRASE_TRANSLATIONS.put("Manual Testing", "ручное тестирование");
        TOPIC_PHRASE_TRANSLATIONS.put("Accessibility Testing", "тестирование доступности");
        TOPIC_PHRASE_TRANSLATIONS.put("Performance Testing", "тестирование производительности");
        TOPIC_PHRASE_TRANSLATIONS.put("Headless Testing", "headless-тестирование");
        TOPIC_PHRASE_TRANSLATIONS.put("Email Testing", "тестирование email");
        TOPIC_PHRASE_TRANSLATIONS.put("Verification and Validation", "верификация и валидация");
        TOPIC_PHRASE_TRANSLATIONS.put("Test Planning", "планирование тестирования");
        TOPIC_PHRASE_TRANSLATIONS.put("Version Control System", "система контроля версий");
        TOPIC_PHRASE_TRANSLATIONS.put("Monitoring and Logs", "мониторинг и логи");
        TOPIC_PHRASE_TRANSLATIONS.put("Monitoring & Logs", "мониторинг и логи");
        TOPIC_PHRASE_TRANSLATIONS.put("Secrets Management", "управление секретами");
        TOPIC_PHRASE_TRANSLATIONS.put("Manual Implementation", "ручная реализация");
        TOPIC_PHRASE_TRANSLATIONS.put("Security Skills", "навыки безопасности");
        TOPIC_PHRASE_TRANSLATIONS.put("Security Knowledge", "знания по безопасности");
        TOPIC_PHRASE_TRANSLATIONS.put("Operating Systems", "операционные системы");
        TOPIC_PHRASE_TRANSLATIONS.put("Operating System Hardening", "усиление защиты операционной системы");
        TOPIC_PHRASE_TRANSLATIONS.put("Computer Hardware Components", "компоненты компьютерного железа");
        TOPIC_PHRASE_TRANSLATIONS.put("Connection Types", "типы подключений");
        TOPIC_PHRASE_TRANSLATIONS.put("Public vs Private IP Addresses", "публичные и приватные IP-адреса");
        TOPIC_PHRASE_TRANSLATIONS.put("Threat Hunting", "поиск угроз");
        TOPIC_PHRASE_TRANSLATIONS.put("Vulnerability Management", "управление уязвимостями");
        TOPIC_PHRASE_TRANSLATIONS.put("Reverse Engineering", "реверс-инжиниринг");
        TOPIC_PHRASE_TRANSLATIONS.put("Penetration Testing", "тестирование на проникновение");
        TOPIC_PHRASE_TRANSLATIONS.put("Social Engineering", "социальная инженерия");
        TOPIC_PHRASE_TRANSLATIONS.put("Event Logs", "журналы событий");
        TOPIC_PHRASE_TRANSLATIONS.put("Group Policy", "групповая политика");
        TOPIC_PHRASE_TRANSLATIONS.put("Jump Server", "jump-сервер");
        TOPIC_PHRASE_TRANSLATIONS.put("Endpoint Security", "безопасность endpoint-устройств");
        TOPIC_PHRASE_TRANSLATIONS.put("Rogue Access Point", "поддельная точка доступа");
        TOPIC_PHRASE_TRANSLATIONS.put("Zero Trust", "Zero Trust");
        TOPIC_PHRASE_TRANSLATIONS.put("Business Process", "бизнес-процесс");
        TOPIC_PHRASE_TRANSLATIONS.put("Business Model", "бизнес-модель");
        TOPIC_PHRASE_TRANSLATIONS.put("Decision-Making", "принятие решений");
        TOPIC_PHRASE_TRANSLATIONS.put("Decision Making", "принятие решений");
        TOPIC_PHRASE_TRANSLATIONS.put("User Goals", "цели пользователя");
        TOPIC_PHRASE_TRANSLATIONS.put("User Interactions", "взаимодействия пользователя");
        TOPIC_PHRASE_TRANSLATIONS.put("User Defaults", "User Defaults");
        TOPIC_PHRASE_TRANSLATIONS.put("Project Files", "файлы проекта");
        TOPIC_PHRASE_TRANSLATIONS.put("Data binding", "привязка данных");
        TOPIC_PHRASE_TRANSLATIONS.put("UI Design", "UI-дизайн");
        TOPIC_PHRASE_TRANSLATIONS.put("Basics / Creating Animations", "основы создания анимаций");
        TOPIC_PHRASE_TRANSLATIONS.put("Delegate Pattern", "паттерн Delegate");
        TOPIC_PHRASE_TRANSLATIONS.put("Understanding and using Closures", "понимание и использование замыканий");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Persistence", "персистентность данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Static Library", "статическая библиотека");
        TOPIC_PHRASE_TRANSLATIONS.put("Code Quality Tools", "инструменты качества кода");
        TOPIC_PHRASE_TRANSLATIONS.put("Debugging Techniques", "техники отладки");
        TOPIC_PHRASE_TRANSLATIONS.put("Services", "сервисы");
        TOPIC_PHRASE_TRANSLATIONS.put("Content Provider", "Content Provider");
        TOPIC_PHRASE_TRANSLATIONS.put("Navigation Components", "компоненты навигации");
        TOPIC_PHRASE_TRANSLATIONS.put("Room Database", "база данных Room");
        TOPIC_PHRASE_TRANSLATIONS.put("File System", "файловая система");
        TOPIC_PHRASE_TRANSLATIONS.put("Repository Pattern", "паттерн Repository");
        TOPIC_PHRASE_TRANSLATIONS.put("Builder Pattern", "паттерн Builder");
        TOPIC_PHRASE_TRANSLATIONS.put("Factory Pattern", "паттерн Factory");
        TOPIC_PHRASE_TRANSLATIONS.put("Observer Pattern", "паттерн Observer");
        TOPIC_PHRASE_TRANSLATIONS.put("Common Services", "общие сервисы");
        TOPIC_PHRASE_TRANSLATIONS.put("Practices", "практики");
        TOPIC_PHRASE_TRANSLATIONS.put("Fuzz Testing", "fuzz-тестирование");
        TOPIC_PHRASE_TRANSLATIONS.put("Static Analysis", "статический анализ");
        TOPIC_PHRASE_TRANSLATIONS.put("Management Platforms", "платформы управления");
        TOPIC_PHRASE_TRANSLATIONS.put("Decentralized Storage", "децентрализованное хранилище");
        TOPIC_PHRASE_TRANSLATIONS.put("Decentralized Applications", "децентрализованные приложения");
        TOPIC_PHRASE_TRANSLATIONS.put("Client Nodes", "клиентские узлы");
        TOPIC_PHRASE_TRANSLATIONS.put("Client Libraries", "клиентские библиотеки");
        TOPIC_PHRASE_TRANSLATIONS.put("Node as a Service", "Node as a Service");
        TOPIC_PHRASE_TRANSLATIONS.put("On-Chain Scaling", "on-chain масштабирование");
        TOPIC_PHRASE_TRANSLATIONS.put("Socket Programming", "сокетное программирование");
        TOPIC_PHRASE_TRANSLATIONS.put("Thread Local Storage", "thread-local хранилище");
        TOPIC_PHRASE_TRANSLATIONS.put("Programming Techniques", "техники программирования");
        TOPIC_PHRASE_TRANSLATIONS.put("Dump Analysis", "анализ дампов");
        TOPIC_PHRASE_TRANSLATIONS.put("Message Queues", "очереди сообщений");
        TOPIC_PHRASE_TRANSLATIONS.put("Update Process", "процесс обновления");
        TOPIC_PHRASE_TRANSLATIONS.put("Reactive Model", "реактивная модель");
        TOPIC_PHRASE_TRANSLATIONS.put("Actor Model", "модель акторов");
        TOPIC_PHRASE_TRANSLATIONS.put("Service Mesh", "сервисная mesh-сеть");
        TOPIC_PHRASE_TRANSLATIONS.put("Containerization vs Virtualization", "контейнеризация и виртуализация: сравнение");
        TOPIC_PHRASE_TRANSLATIONS.put("Types of Scaling", "типы масштабирования");
        TOPIC_PHRASE_TRANSLATIONS.put("Network Segmentation", "сегментация сети");
        TOPIC_PHRASE_TRANSLATIONS.put("NIST Cybersecurity Framework", "фреймворк кибербезопасности NIST");
        TOPIC_PHRASE_TRANSLATIONS.put("Response Strategy", "стратегия реагирования");
        TOPIC_PHRASE_TRANSLATIONS.put("Risk Quantification", "количественная оценка рисков");
        TOPIC_PHRASE_TRANSLATIONS.put("Role-Based Access Control", "ролевая модель доступа");
        TOPIC_PHRASE_TRANSLATIONS.put("Secure Coding", "безопасное программирование");
        TOPIC_PHRASE_TRANSLATIONS.put("Secure Network Zoning", "безопасное зонирование сети");
        TOPIC_PHRASE_TRANSLATIONS.put("SOAR Automation", "автоматизация SOAR");
        TOPIC_PHRASE_TRANSLATIONS.put("SQL Injection Prevention", "защита от SQL-инъекций");
        TOPIC_PHRASE_TRANSLATIONS.put("Supply Chain Security", "безопасность цепочки поставки");
        TOPIC_PHRASE_TRANSLATIONS.put("Symmetric Encryption", "симметричное шифрование");
        TOPIC_PHRASE_TRANSLATIONS.put("Asymmetric Encryption", "асимметричное шифрование");
        TOPIC_PHRASE_TRANSLATIONS.put("Threat Modeling Workflows", "workflow моделирования угроз");
        TOPIC_PHRASE_TRANSLATIONS.put("Threat Modeling", "моделирование угроз");
        TOPIC_PHRASE_TRANSLATIONS.put("Cross-Site Scripting (XSS) Prevention", "защита от Cross-Site Scripting (XSS)");
        TOPIC_PHRASE_TRANSLATIONS.put("Attack Surface Mapping", "карта поверхности атаки");
        TOPIC_PHRASE_TRANSLATIONS.put("Audit & Compliance Mapping", "аудит и соответствие требованиям");
        TOPIC_PHRASE_TRANSLATIONS.put("Automated Patching", "автоматическое обновление патчей");
        TOPIC_PHRASE_TRANSLATIONS.put("Build Pipeline Hardening", "усиление защиты build pipeline");
        TOPIC_PHRASE_TRANSLATIONS.put("Certificate Lifecycle", "жизненный цикл сертификатов");
        TOPIC_PHRASE_TRANSLATIONS.put("Container Security", "безопасность контейнеров");
        TOPIC_PHRASE_TRANSLATIONS.put("Cryptographic Hashing", "криптографическое хеширование");
        TOPIC_PHRASE_TRANSLATIONS.put("DDoS Mitigation Strategy", "стратегия защиты от DDoS");
        TOPIC_PHRASE_TRANSLATIONS.put("Defense in Depth", "эшелонированная защита");
        TOPIC_PHRASE_TRANSLATIONS.put("Dependency Risk Management", "управление рисками зависимостей");
        TOPIC_PHRASE_TRANSLATIONS.put("Docker Security", "безопасность Docker");
        TOPIC_PHRASE_TRANSLATIONS.put("Endpoint Detection", "обнаружение угроз на endpoint-устройствах");
        TOPIC_PHRASE_TRANSLATIONS.put("Enterprise Operations", "корпоративные операции");
        TOPIC_PHRASE_TRANSLATIONS.put("Identity and Access Management (IAM)", "управление идентификацией и доступом (IAM)");
        TOPIC_PHRASE_TRANSLATIONS.put("Input Validation Patterns", "паттерны валидации ввода");
        TOPIC_PHRASE_TRANSLATIONS.put("Intrusion Detection Systems", "системы обнаружения вторжений");
        TOPIC_PHRASE_TRANSLATIONS.put("Intrusion Prevention Systems", "системы предотвращения вторжений");
        TOPIC_PHRASE_TRANSLATIONS.put("Key Management Service", "сервис управления ключами");
        TOPIC_PHRASE_TRANSLATIONS.put("Kubernetes Security", "безопасность Kubernetes");
        TOPIC_PHRASE_TRANSLATIONS.put("Large Scale Identity Strategy", "стратегия идентификации в больших системах");
        TOPIC_PHRASE_TRANSLATIONS.put("Least Privilege", "принцип минимальных привилегий");
        TOPIC_PHRASE_TRANSLATIONS.put("Log Analysis", "анализ логов");
        TOPIC_PHRASE_TRANSLATIONS.put("Multi-Region Security Planning", "планирование безопасности в нескольких регионах");
        TOPIC_PHRASE_TRANSLATIONS.put("Networking Basics", "основы сетей");
        TOPIC_PHRASE_TRANSLATIONS.put("Root Cause Analysis", "анализ первопричин");
        TOPIC_PHRASE_TRANSLATIONS.put("Scripting Knowledge", "знание скриптинга");
        TOPIC_PHRASE_TRANSLATIONS.put("Secure API Design", "безопасное проектирование API");
        TOPIC_PHRASE_TRANSLATIONS.put("SOAR Concepts", "концепции SOAR");
        TOPIC_PHRASE_TRANSLATIONS.put("Zero Trust Concepts", "концепции Zero Trust");
        TOPIC_PHRASE_TRANSLATIONS.put("Text Editors: Vim, Nano, and Emacs", "текстовые редакторы: Vim, Nano и Emacs");
        TOPIC_PHRASE_TRANSLATIONS.put("Pivot Tables", "сводные таблицы");
        TOPIC_PHRASE_TRANSLATIONS.put("Standard Deviation", "стандартное отклонение");
        TOPIC_PHRASE_TRANSLATIONS.put("Central Tendency", "центральная тенденция");
        TOPIC_PHRASE_TRANSLATIONS.put("Distribution Space", "пространство распределений");
        TOPIC_PHRASE_TRANSLATIONS.put("Visualizing Distributions", "визуализация распределений");
        TOPIC_PHRASE_TRANSLATIONS.put("Bar Charts", "столбчатые диаграммы");
        TOPIC_PHRASE_TRANSLATIONS.put("Line Chart", "линейный график");
        TOPIC_PHRASE_TRANSLATIONS.put("Stacked Charts", "накопительные диаграммы");
        TOPIC_PHRASE_TRANSLATIONS.put("Scatter Plot", "диаграмма рассеяния");
        TOPIC_PHRASE_TRANSLATIONS.put("Funnel Charts", "воронкообразные диаграммы");
        TOPIC_PHRASE_TRANSLATIONS.put("Pie Charts", "круговые диаграммы");
        TOPIC_PHRASE_TRANSLATIONS.put("Naive Byes", "наивный Байес");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Storage Solutions", "решения для хранения данных");
        TOPIC_PHRASE_TRANSLATIONS.put("K-Means Clustering", "кластеризация K-Means");
        TOPIC_PHRASE_TRANSLATIONS.put("Parallel Processing", "параллельная обработка");
        TOPIC_PHRASE_TRANSLATIONS.put("Star vs Snowflake Schema", "схемы Star и Snowflake: сравнение");
        TOPIC_PHRASE_TRANSLATIONS.put("Serverless Options", "serverless-варианты");
        TOPIC_PHRASE_TRANSLATIONS.put("Extract Data", "извлечение данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Transform Data", "трансформация данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Load Data", "загрузка данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Job Scheduling", "планирование задач");
        TOPIC_PHRASE_TRANSLATIONS.put("Containers & Orchestration", "контейнеры и оркестрация");
        TOPIC_PHRASE_TRANSLATIONS.put("Infrastructure as Code - IaC", "инфраструктура как код - IaC");
        TOPIC_PHRASE_TRANSLATIONS.put("Declarative vs Imperative", "декларативный и императивный подходы: сравнение");
        TOPIC_PHRASE_TRANSLATIONS.put("Authentication vs Authorization", "аутентификация и авторизация: сравнение");
        TOPIC_PHRASE_TRANSLATIONS.put("Reverse ETL Usecases", "сценарии Reverse ETL");
        TOPIC_PHRASE_TRANSLATIONS.put("Reverse ETL", "Reverse ETL");
        TOPIC_PHRASE_TRANSLATIONS.put("Implicit Intents", "неявные Intent");
        TOPIC_PHRASE_TRANSLATIONS.put("Explicit Intents", "явные Intent");
        TOPIC_PHRASE_TRANSLATIONS.put("Broadcast Receiver", "Broadcast Receiver");
        TOPIC_PHRASE_TRANSLATIONS.put("State Changes", "изменения состояния");
        TOPIC_PHRASE_TRANSLATIONS.put("Intent Filters", "фильтры Intent");
        TOPIC_PHRASE_TRANSLATIONS.put("Tasks & Backstack", "задачи и back stack");
        TOPIC_PHRASE_TRANSLATIONS.put("App Shortcuts", "ярлыки приложения");
        TOPIC_PHRASE_TRANSLATIONS.put("Bottom Sheet", "Bottom Sheet");
        TOPIC_PHRASE_TRANSLATIONS.put("Signed APK", "подписанный APK");
        TOPIC_PHRASE_TRANSLATIONS.put("Linear Algebra", "линейная алгебра");
        TOPIC_PHRASE_TRANSLATIONS.put("Descriptive Statistics", "описательная статистика");
        TOPIC_PHRASE_TRANSLATIONS.put("Inferential Statistics", "статистика вывода");
        TOPIC_PHRASE_TRANSLATIONS.put("Matrix & Matrix Operations", "матрицы и операции с матрицами");
        TOPIC_PHRASE_TRANSLATIONS.put("Scalars, Vectors, Tensors", "скаляры, векторы и тензоры");
        TOPIC_PHRASE_TRANSLATIONS.put("Singular Value Decomposition", "сингулярное разложение");
        TOPIC_PHRASE_TRANSLATIONS.put("Gradient, Jacobian, Hessian", "градиент, якобиан и гессиан");
        TOPIC_PHRASE_TRANSLATIONS.put("Determinants, inverse of Matrix", "детерминанты и обратная матрица");
        TOPIC_PHRASE_TRANSLATIONS.put("Eigenvalues, Diagonalization", "собственные значения и диагонализация");
        TOPIC_PHRASE_TRANSLATIONS.put("Basics of Probability", "основы теории вероятностей");
        TOPIC_PHRASE_TRANSLATIONS.put("Bayes Theorem", "теорема Байеса");
        TOPIC_PHRASE_TRANSLATIONS.put("Discrete Mathematics", "дискретная математика");
        TOPIC_PHRASE_TRANSLATIONS.put("Random Variances, PDFs", "случайные величины и функции плотности");
        TOPIC_PHRASE_TRANSLATIONS.put("Object Oriented Programming", "объектно-ориентированное программирование");
        TOPIC_PHRASE_TRANSLATIONS.put("Basic Syntax", "базовый синтаксис");
        TOPIC_PHRASE_TRANSLATIONS.put("Graphs & Charts", "графики и диаграммы");
        TOPIC_PHRASE_TRANSLATIONS.put("Variables and Data Types", "переменные и типы данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Functions, Builtin Functions", "функции и встроенные функции");
        TOPIC_PHRASE_TRANSLATIONS.put("Preprocessing Techniques", "техники предобработки");
        TOPIC_PHRASE_TRANSLATIONS.put("Feature Scaling & Normalization", "масштабирование и нормализация признаков");
        TOPIC_PHRASE_TRANSLATIONS.put("K-Nearest Neighbors (KNN)", "K-ближайших соседей (KNN)");
        TOPIC_PHRASE_TRANSLATIONS.put("Polynomial Regression", "полиномиальная регрессия");
        TOPIC_PHRASE_TRANSLATIONS.put("Support Vector Machines", "метод опорных векторов");
        TOPIC_PHRASE_TRANSLATIONS.put("ElasticNet Regularization", "регуляризация ElasticNet");
        TOPIC_PHRASE_TRANSLATIONS.put("Gradient Boosting Machines", "градиентный бустинг");
        TOPIC_PHRASE_TRANSLATIONS.put("Deep-Q Networks", "Deep-Q Networks");
        TOPIC_PHRASE_TRANSLATIONS.put("Policy Gradient", "policy gradient");
        TOPIC_PHRASE_TRANSLATIONS.put("Log Loss", "логарифмическая функция потерь");
        TOPIC_PHRASE_TRANSLATIONS.put("Metrics to Evaluate", "метрики оценки");
        TOPIC_PHRASE_TRANSLATIONS.put("Confusion Matrix", "матрица ошибок");
        TOPIC_PHRASE_TRANSLATIONS.put("Perceptron, Multi-layer Perceptrons", "перцептрон и многослойные перцептроны");
        TOPIC_PHRASE_TRANSLATIONS.put("Forward propagation", "прямое распространение");
        TOPIC_PHRASE_TRANSLATIONS.put("Back Propagation", "обратное распространение ошибки");
        TOPIC_PHRASE_TRANSLATIONS.put("Convolutional Neural Network", "свёрточная нейронная сеть");
        TOPIC_PHRASE_TRANSLATIONS.put("Activation Functions", "функции активации");
        TOPIC_PHRASE_TRANSLATIONS.put("Loss Functions", "функции потерь");
        TOPIC_PHRASE_TRANSLATIONS.put("Image & Video Recognition", "распознавание изображений и видео");
        TOPIC_PHRASE_TRANSLATIONS.put("Image Classification", "классификация изображений");
        TOPIC_PHRASE_TRANSLATIONS.put("Attention Mechanisms", "механизмы внимания");
        TOPIC_PHRASE_TRANSLATIONS.put("Image Segmentation", "сегментация изображений");
        TOPIC_PHRASE_TRANSLATIONS.put("Generative Adversarial Networks", "генеративно-состязательные сети");
        TOPIC_PHRASE_TRANSLATIONS.put("Attention Models", "модели внимания");
        TOPIC_PHRASE_TRANSLATIONS.put("Import / Export Using `COPY`", "импорт и экспорт через `COPY`");
        TOPIC_PHRASE_TRANSLATIONS.put("Default Privileges", "привилегии по умолчанию");
        TOPIC_PHRASE_TRANSLATIONS.put("Set Operations", "операции над множествами");
        TOPIC_PHRASE_TRANSLATIONS.put("Object Privileges", "привилегии объектов");
        TOPIC_PHRASE_TRANSLATIONS.put("Query Planner", "планировщик запросов");
        TOPIC_PHRASE_TRANSLATIONS.put("Checkpoints / Background Writer", "checkpoints и background writer");
        TOPIC_PHRASE_TRANSLATIONS.put("Authentication Models", "модели аутентификации");
        TOPIC_PHRASE_TRANSLATIONS.put("Row-Level Security", "безопасность на уровне строк");
        TOPIC_PHRASE_TRANSLATIONS.put("Using Logical Replication", "использование логической репликации");
        TOPIC_PHRASE_TRANSLATIONS.put("Logical Replication", "логическая репликация");
        TOPIC_PHRASE_TRANSLATIONS.put("Streaming Replication", "потоковая репликация");
        TOPIC_PHRASE_TRANSLATIONS.put("Bulk Loading / Processing Data", "массовая загрузка и обработка данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Practical Patterns / Antipatterns", "практические паттерны и антипаттерны");
        TOPIC_PHRASE_TRANSLATIONS.put("Sharding Patterns", "паттерны шардирования");
        TOPIC_PHRASE_TRANSLATIONS.put("Patterns / Antipatterns", "паттерны и антипаттерны");
        TOPIC_PHRASE_TRANSLATIONS.put("Vacuum Processing", "обработка vacuum");
        TOPIC_PHRASE_TRANSLATIONS.put("Per-User, Per-Database Setting", "настройки на пользователя и базу данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Procedures and Functions", "процедуры и функции");
        TOPIC_PHRASE_TRANSLATIONS.put("Storage Parameters", "параметры хранения");
        TOPIC_PHRASE_TRANSLATIONS.put("Recursive CTE", "рекурсивные CTE");
        TOPIC_PHRASE_TRANSLATIONS.put("Physical Storage and File Layout", "физическое хранение и структура файлов");
        TOPIC_PHRASE_TRANSLATIONS.put("Aggregate and Window functions", "агрегатные и оконные функции");
        TOPIC_PHRASE_TRANSLATIONS.put("Indexes and their Usecases", "индексы и сценарии их применения");
        TOPIC_PHRASE_TRANSLATIONS.put("Interoperability with Swift", "совместимость со Swift");
        TOPIC_PHRASE_TRANSLATIONS.put("Objective-C Basics", "основы Objective-C");
        TOPIC_PHRASE_TRANSLATIONS.put("iOS Architecture", "архитектура iOS");
        TOPIC_PHRASE_TRANSLATIONS.put("History and Why Swift?", "история и причины выбора Swift");
        TOPIC_PHRASE_TRANSLATIONS.put("Benefits over Objective-C", "преимущества перед Objective-C");
        TOPIC_PHRASE_TRANSLATIONS.put("Core Programming Concepts", "ключевые концепции программирования");
        TOPIC_PHRASE_TRANSLATIONS.put("ViewController Lifecycle", "жизненный цикл ViewController");
        TOPIC_PHRASE_TRANSLATIONS.put("Error Handling", "обработка ошибок");
        TOPIC_PHRASE_TRANSLATIONS.put("New Project", "новый проект");
        TOPIC_PHRASE_TRANSLATIONS.put("Interface overview", "обзор интерфейса");
        TOPIC_PHRASE_TRANSLATIONS.put("Basic Interfaces", "базовые интерфейсы");
        TOPIC_PHRASE_TRANSLATIONS.put("Declarative Syntax", "декларативный синтаксис");
        TOPIC_PHRASE_TRANSLATIONS.put("State Management", "управление состоянием");
        TOPIC_PHRASE_TRANSLATIONS.put("View Transitions", "переходы между views");
        TOPIC_PHRASE_TRANSLATIONS.put("Building Interfaces", "создание интерфейсов");
        TOPIC_PHRASE_TRANSLATIONS.put("Views and Modifiers", "views и модификаторы");
        TOPIC_PHRASE_TRANSLATIONS.put("Navigation Controllers, Segues", "navigation controllers и segues");
        TOPIC_PHRASE_TRANSLATIONS.put("Modals and Navigation", "модальные экраны и навигация");
        TOPIC_PHRASE_TRANSLATIONS.put("Presenting / Dismissing views", "показ и закрытие views");
        TOPIC_PHRASE_TRANSLATIONS.put("Publishers / Subscribers", "publishers и subscribers");
        TOPIC_PHRASE_TRANSLATIONS.put("Operators & Pipelines", "операторы и pipeline");
        TOPIC_PHRASE_TRANSLATIONS.put("Observables & observers", "observables и observers");
        TOPIC_PHRASE_TRANSLATIONS.put("Implementing Delegates", "реализация delegates");
        TOPIC_PHRASE_TRANSLATIONS.put("Capturing Values & Memory Mgmt.", "захват значений и управление памятью");
        TOPIC_PHRASE_TRANSLATIONS.put("Operation Queues", "очереди операций");
        TOPIC_PHRASE_TRANSLATIONS.put("Concurrency and Multithreading", "конкурентность и многопоточность");
        TOPIC_PHRASE_TRANSLATIONS.put("Dependency Manager", "менеджер зависимостей");
        TOPIC_PHRASE_TRANSLATIONS.put("Frameworks & Library", "фреймворки и библиотеки");
        TOPIC_PHRASE_TRANSLATIONS.put("Dynamic Library", "динамическая библиотека");
        TOPIC_PHRASE_TRANSLATIONS.put("Profiling Instruments", "инструменты профилирования");
        TOPIC_PHRASE_TRANSLATIONS.put("Test Plan & Coverage", "план тестирования и покрытие");
        TOPIC_PHRASE_TRANSLATIONS.put("App Store Distribution", "дистрибуция в App Store");
        TOPIC_PHRASE_TRANSLATIONS.put("App Store Optimization (ASO)", "оптимизация в App Store (ASO)");
        TOPIC_PHRASE_TRANSLATIONS.put("Product Development Lifecycle", "жизненный цикл разработки продукта");
        TOPIC_PHRASE_TRANSLATIONS.put("Product vs Project Management", "product management и project management: сравнение");
        TOPIC_PHRASE_TRANSLATIONS.put("Roles and Responsibilities", "роли и обязанности");
        TOPIC_PHRASE_TRANSLATIONS.put("Key Skills", "ключевые навыки");
        TOPIC_PHRASE_TRANSLATIONS.put("Product Identification", "идентификация продукта");
        TOPIC_PHRASE_TRANSLATIONS.put("Brainstorming Techniques", "техники брейнсторминга");
        TOPIC_PHRASE_TRANSLATIONS.put("Iterative Process", "итеративный процесс");
        TOPIC_PHRASE_TRANSLATIONS.put("Blue Ocean Strategy", "стратегия голубого океана");
        TOPIC_PHRASE_TRANSLATIONS.put("Problem Framing", "формулирование проблемы");
        TOPIC_PHRASE_TRANSLATIONS.put("Market Analysis", "анализ рынка");
        TOPIC_PHRASE_TRANSLATIONS.put("Identifying Market Needs", "выявление потребностей рынка");
        TOPIC_PHRASE_TRANSLATIONS.put("Competitive Analysis", "конкурентный анализ");
        TOPIC_PHRASE_TRANSLATIONS.put("Emerging Market Trends", "новые рыночные тренды");
        TOPIC_PHRASE_TRANSLATIONS.put("User Research", "исследование пользователей");
        TOPIC_PHRASE_TRANSLATIONS.put("User Personas", "пользовательские персоны");
        TOPIC_PHRASE_TRANSLATIONS.put("User Interviews", "интервью с пользователями");
        TOPIC_PHRASE_TRANSLATIONS.put("Surveys and Questionnaires", "опросы и анкеты");
        TOPIC_PHRASE_TRANSLATIONS.put("Ethnographic Research", "этнографическое исследование");
        TOPIC_PHRASE_TRANSLATIONS.put("Market Segmentation", "сегментация рынка");
        TOPIC_PHRASE_TRANSLATIONS.put("Feature Creep", "расползание функциональности");
        TOPIC_PHRASE_TRANSLATIONS.put("Vision & Mission", "видение и миссия");
        TOPIC_PHRASE_TRANSLATIONS.put("Value Proposition", "ценностное предложение");
        TOPIC_PHRASE_TRANSLATIONS.put("Value vs Features", "ценность и функции: сравнение");
        TOPIC_PHRASE_TRANSLATIONS.put("Strategic Thinking", "стратегическое мышление");
        TOPIC_PHRASE_TRANSLATIONS.put("Prioritising Features", "приоритизация функций");
        TOPIC_PHRASE_TRANSLATIONS.put("Writing PRDs", "написание PRD");
        TOPIC_PHRASE_TRANSLATIONS.put("Continuous Roadmapping", "непрерывное roadmapping");
        TOPIC_PHRASE_TRANSLATIONS.put("Outcome-Based Roadmaps", "roadmap на основе результатов");
        TOPIC_PHRASE_TRANSLATIONS.put("Prioritization Techniques", "техники приоритизации");
        TOPIC_PHRASE_TRANSLATIONS.put("Backlog Management", "управление backlog");
        TOPIC_PHRASE_TRANSLATIONS.put("User Story Mapping", "карта user stories");
        TOPIC_PHRASE_TRANSLATIONS.put("Release Strategies", "стратегии релизов");
        TOPIC_PHRASE_TRANSLATIONS.put("Feature Toggles", "feature toggles");
        TOPIC_PHRASE_TRANSLATIONS.put("Design Thinking", "design thinking");
        TOPIC_PHRASE_TRANSLATIONS.put("Interaction Design", "interaction design");
        TOPIC_PHRASE_TRANSLATIONS.put("Go-to-Market Strategy", "go-to-market стратегия");
        TOPIC_PHRASE_TRANSLATIONS.put("Launch Planning", "планирование запуска");
        TOPIC_PHRASE_TRANSLATIONS.put("Usability Testing", "юзабилити-тестирование");
        TOPIC_PHRASE_TRANSLATIONS.put("Remote User Testing", "удалённое пользовательское тестирование");
        TOPIC_PHRASE_TRANSLATIONS.put("Marketing Strategies", "маркетинговые стратегии");
        TOPIC_PHRASE_TRANSLATIONS.put("Sprint Planning", "планирование спринта");
        TOPIC_PHRASE_TRANSLATIONS.put("Daily Standups", "daily standups");
        TOPIC_PHRASE_TRANSLATIONS.put("Minimum Viable Product (MVP)", "минимально жизнеспособный продукт (MVP)");
        TOPIC_PHRASE_TRANSLATIONS.put("Key Product Metrics", "ключевые продуктовые метрики");
        TOPIC_PHRASE_TRANSLATIONS.put("Conversion Rate", "коэффициент конверсии");
        TOPIC_PHRASE_TRANSLATIONS.put("Retention Rate", "коэффициент удержания");
        TOPIC_PHRASE_TRANSLATIONS.put("Churn Rate", "коэффициент оттока");
        TOPIC_PHRASE_TRANSLATIONS.put("Data-Driven Decision Making", "принятие решений на основе данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Cohort Analysis", "когортный анализ");
        TOPIC_PHRASE_TRANSLATIONS.put("Feedback Loops", "петли обратной связи");
        TOPIC_PHRASE_TRANSLATIONS.put("Communication Techniques", "техники коммуникации");
        TOPIC_PHRASE_TRANSLATIONS.put("Difficult Conversations", "сложные разговоры");
        TOPIC_PHRASE_TRANSLATIONS.put("Active Listening", "активное слушание");
        TOPIC_PHRASE_TRANSLATIONS.put("Conflict Resolution", "разрешение конфликтов");
        TOPIC_PHRASE_TRANSLATIONS.put("Managing Stakeholders", "управление стейкхолдерами");
        TOPIC_PHRASE_TRANSLATIONS.put("Identifying Stakeholders", "определение стейкхолдеров");
        TOPIC_PHRASE_TRANSLATIONS.put("Stakeholder Mapping", "карта стейкхолдеров");
        TOPIC_PHRASE_TRANSLATIONS.put("What is Engineering Management?", "что такое engineering management?");
        TOPIC_PHRASE_TRANSLATIONS.put("System Monitoring & Performance", "мониторинг системы и производительность");
        TOPIC_PHRASE_TRANSLATIONS.put("CI/CD Implementation", "реализация CI/CD");
        TOPIC_PHRASE_TRANSLATIONS.put("Technical Roadmapping", "техническое roadmapping");
        TOPIC_PHRASE_TRANSLATIONS.put("Development / Release Workflow", "workflow разработки и релизов");
        TOPIC_PHRASE_TRANSLATIONS.put("Software Engineering Background", "бэкграунд в software engineering");
        TOPIC_PHRASE_TRANSLATIONS.put("Architectural Decision-Making", "принятие архитектурных решений");
        TOPIC_PHRASE_TRANSLATIONS.put("Testing Strategies", "стратегии тестирования");
        TOPIC_PHRASE_TRANSLATIONS.put("System Design and Architecture", "system design и архитектура");
        TOPIC_PHRASE_TRANSLATIONS.put("Technical Debt and Management", "технический долг и управление им");
        TOPIC_PHRASE_TRANSLATIONS.put("Technical Risk Assessment", "оценка технических рисков");
        TOPIC_PHRASE_TRANSLATIONS.put("Code Review Best Practices", "лучшие практики code review");
        TOPIC_PHRASE_TRANSLATIONS.put("Scaling Infrastructure", "масштабирование инфраструктуры");
        TOPIC_PHRASE_TRANSLATIONS.put("Incident Management", "управление инцидентами");
        TOPIC_PHRASE_TRANSLATIONS.put("Legacy System Retirement", "вывод legacy-систем из эксплуатации");
        TOPIC_PHRASE_TRANSLATIONS.put("One-on-One Meetings", "one-on-one встречи");
        TOPIC_PHRASE_TRANSLATIONS.put("Hiring and Recruitment", "найм и рекрутинг");
        TOPIC_PHRASE_TRANSLATIONS.put("Feedback Delivery", "предоставление обратной связи");
        TOPIC_PHRASE_TRANSLATIONS.put("Team Structure and Design", "структура и дизайн команды");
        TOPIC_PHRASE_TRANSLATIONS.put("Team Motivation", "мотивация команды");
        TOPIC_PHRASE_TRANSLATIONS.put("Performance Evaluations", "оценка производительности");
        TOPIC_PHRASE_TRANSLATIONS.put("Career Development Planning", "планирование развития карьеры");
        TOPIC_PHRASE_TRANSLATIONS.put("Emotional Intelligence", "эмоциональный интеллект");
        TOPIC_PHRASE_TRANSLATIONS.put("Cross-functional Collaboration", "кросс-функциональная работа");
        TOPIC_PHRASE_TRANSLATIONS.put("Mentoring and Coaching", "менторинг и коучинг");
        TOPIC_PHRASE_TRANSLATIONS.put("Resource Allocation", "распределение ресурсов");
        TOPIC_PHRASE_TRANSLATIONS.put("Project Tracking", "отслеживание проекта");
        TOPIC_PHRASE_TRANSLATIONS.put("Milestone Management", "управление milestones");
        TOPIC_PHRASE_TRANSLATIONS.put("Release Management", "управление релизами");
        TOPIC_PHRASE_TRANSLATIONS.put("Timeline Estimation", "оценка сроков");
        TOPIC_PHRASE_TRANSLATIONS.put("Budget Planning", "планирование бюджета");
        TOPIC_PHRASE_TRANSLATIONS.put("Cost Optimization", "оптимизация затрат");
        TOPIC_PHRASE_TRANSLATIONS.put("Change management", "управление изменениями");
        TOPIC_PHRASE_TRANSLATIONS.put("Vendor Management", "управление поставщиками");
        TOPIC_PHRASE_TRANSLATIONS.put("Stakeholder Communication", "коммуникация со стейкхолдерами");
        TOPIC_PHRASE_TRANSLATIONS.put("Business continuity", "непрерывность бизнеса");
        TOPIC_PHRASE_TRANSLATIONS.put("Architecture documentation", "архитектурная документация");
        TOPIC_PHRASE_TRANSLATIONS.put("Feature prioritization", "приоритизация функций");
        TOPIC_PHRASE_TRANSLATIONS.put("Process documentation", "документация процессов");
        TOPIC_PHRASE_TRANSLATIONS.put("Lessons Learned", "извлечённые уроки");
        TOPIC_PHRASE_TRANSLATIONS.put("Mentoring Programs", "программы менторинга");
        TOPIC_PHRASE_TRANSLATIONS.put("Knowledge bases", "базы знаний");
        TOPIC_PHRASE_TRANSLATIONS.put("Migration planning", "планирование миграции");
        TOPIC_PHRASE_TRANSLATIONS.put("Technology adoption", "внедрение технологий");
        TOPIC_PHRASE_TRANSLATIONS.put("Impact assessment", "оценка влияния");
        TOPIC_PHRASE_TRANSLATIONS.put("Communication planning", "планирование коммуникации");
        TOPIC_PHRASE_TRANSLATIONS.put("Culture evolution", "развитие культуры");
        TOPIC_PHRASE_TRANSLATIONS.put("What is DevRel?", "что такое DevRel?");
        TOPIC_PHRASE_TRANSLATIONS.put("History and Evolution", "история и развитие");
        TOPIC_PHRASE_TRANSLATIONS.put("Importance of DevRel", "важность DevRel");
        TOPIC_PHRASE_TRANSLATIONS.put("Community Support", "поддержка сообщества");
        TOPIC_PHRASE_TRANSLATIONS.put("Developer Experience", "developer experience");
        TOPIC_PHRASE_TRANSLATIONS.put("Developer Journey", "путь разработчика");
        TOPIC_PHRASE_TRANSLATIONS.put("Feedback Loop", "петля обратной связи");
        TOPIC_PHRASE_TRANSLATIONS.put("Developer Marketing", "маркетинг для разработчиков");
        TOPIC_PHRASE_TRANSLATIONS.put("Public Speaking", "публичные выступления");
        TOPIC_PHRASE_TRANSLATIONS.put("Presentation Techniques", "техники презентации");
        TOPIC_PHRASE_TRANSLATIONS.put("Rules of Three", "правило трёх");
        TOPIC_PHRASE_TRANSLATIONS.put("Basic Programming Skills", "базовые навыки программирования");
        TOPIC_PHRASE_TRANSLATIONS.put("Social Media", "социальные сети");
        TOPIC_PHRASE_TRANSLATIONS.put("Community Engagement", "вовлечение сообщества");
        TOPIC_PHRASE_TRANSLATIONS.put("Engaging Audience", "вовлечение аудитории");
        TOPIC_PHRASE_TRANSLATIONS.put("Online Communities", "онлайн-сообщества");
        TOPIC_PHRASE_TRANSLATIONS.put("Event Participation", "участие в событиях");
        TOPIC_PHRASE_TRANSLATIONS.put("Managing Discussions", "ведение обсуждений");
        TOPIC_PHRASE_TRANSLATIONS.put("Issues & Pull Requests", "issues и запросы на слияние (Pull Request)");
        TOPIC_PHRASE_TRANSLATIONS.put("Handling Q&A", "работа с Q&A");
        TOPIC_PHRASE_TRANSLATIONS.put("Understanding APIs", "понимание API");
        TOPIC_PHRASE_TRANSLATIONS.put("Writing Documentation", "написание документации");
        TOPIC_PHRASE_TRANSLATIONS.put("Managing Difficult Questions", "работа со сложными вопросами");
        TOPIC_PHRASE_TRANSLATIONS.put("Recognition Programs", "программы признания");
        TOPIC_PHRASE_TRANSLATIONS.put("Building a Community", "создание сообщества");
        TOPIC_PHRASE_TRANSLATIONS.put("Identifying Audience", "определение аудитории");
        TOPIC_PHRASE_TRANSLATIONS.put("Platform Selection", "выбор платформы");
        TOPIC_PHRASE_TRANSLATIONS.put("Encouraging Participation", "стимулирование участия");
        TOPIC_PHRASE_TRANSLATIONS.put("Initial Outreach", "первичный outreach");
        TOPIC_PHRASE_TRANSLATIONS.put("Community Guidelines", "правила сообщества");
        TOPIC_PHRASE_TRANSLATIONS.put("Feedback Collection", "сбор обратной связи");
        TOPIC_PHRASE_TRANSLATIONS.put("Code of Conduct", "кодекс поведения");
        TOPIC_PHRASE_TRANSLATIONS.put("Rules and Policies", "правила и политики");
        TOPIC_PHRASE_TRANSLATIONS.put("Community Management", "управление сообществом");
        TOPIC_PHRASE_TRANSLATIONS.put("Event Management", "управление событиями");
        TOPIC_PHRASE_TRANSLATIONS.put("Writing Process", "процесс написания");
        TOPIC_PHRASE_TRANSLATIONS.put("Animations & Graphics", "анимации и графика");
        TOPIC_PHRASE_TRANSLATIONS.put("Topic Selection", "выбор темы");
        TOPIC_PHRASE_TRANSLATIONS.put("Video Production", "производство видео");
        TOPIC_PHRASE_TRANSLATIONS.put("Live Streaming", "прямые эфиры");
        TOPIC_PHRASE_TRANSLATIONS.put("API References", "API references");
        TOPIC_PHRASE_TRANSLATIONS.put("User Guides", "руководства пользователя");
        TOPIC_PHRASE_TRANSLATIONS.put("Sample Projects", "примерные проекты");
        TOPIC_PHRASE_TRANSLATIONS.put("Code Samples", "примеры кода");
        TOPIC_PHRASE_TRANSLATIONS.put("Example Apps", "примерные приложения");
        TOPIC_PHRASE_TRANSLATIONS.put("Content Strategy", "контент-стратегия");
        TOPIC_PHRASE_TRANSLATIONS.put("Consistent Posting", "регулярная публикация");
        TOPIC_PHRASE_TRANSLATIONS.put("Engaging Content", "вовлекающий контент");
        TOPIC_PHRASE_TRANSLATIONS.put("Analytics and Optimization", "аналитика и оптимизация");
        TOPIC_PHRASE_TRANSLATIONS.put("Creating Brand Voice", "создание tone of voice бренда");
        TOPIC_PHRASE_TRANSLATIONS.put("Tracking Engagement", "отслеживание вовлечённости");
        TOPIC_PHRASE_TRANSLATIONS.put("Data-Driven Strategy Shift", "изменение стратегии на основе данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Community Growth", "рост сообщества");
        TOPIC_PHRASE_TRANSLATIONS.put("Engagement Rates", "показатели вовлечённости");
        TOPIC_PHRASE_TRANSLATIONS.put("Platform Specific Analytics", "аналитика конкретных платформ");
        TOPIC_PHRASE_TRANSLATIONS.put("Key Metrics", "ключевые метрики");
        TOPIC_PHRASE_TRANSLATIONS.put("Developer Satisfaction", "удовлетворённость разработчиков");
        TOPIC_PHRASE_TRANSLATIONS.put("Regular Reports", "регулярные отчёты");
        TOPIC_PHRASE_TRANSLATIONS.put("Thought Leadership", "экспертное лидерство");
        TOPIC_PHRASE_TRANSLATIONS.put("Insights & Recommendations", "инсайты и рекомендации");
        TOPIC_PHRASE_TRANSLATIONS.put("Media Appearances", "выступления в медиа");
        TOPIC_PHRASE_TRANSLATIONS.put("Building a Personal Brand", "создание личного бренда");
        TOPIC_PHRASE_TRANSLATIONS.put("Conference Speaking", "выступления на конференциях");
        TOPIC_PHRASE_TRANSLATIONS.put("Networking Strategies", "стратегии нетворкинга");
        TOPIC_PHRASE_TRANSLATIONS.put("Fundamental IT Skills", "базовые IT-навыки");
        TOPIC_PHRASE_TRANSLATIONS.put("OS-Independent Troubleshooting", "диагностика вне зависимости от ОС");
        TOPIC_PHRASE_TRANSLATIONS.put("Basics of Computer Networking", "основы компьютерных сетей");
        TOPIC_PHRASE_TRANSLATIONS.put("Basics of Subnetting", "основы subnetting");
        TOPIC_PHRASE_TRANSLATIONS.put("Installation and Configuration", "установка и настройка");
        TOPIC_PHRASE_TRANSLATIONS.put("Different Versions and Differences", "версии и их отличия");
        TOPIC_PHRASE_TRANSLATIONS.put("Navigating using GUI and CLI", "навигация через GUI и CLI");
        TOPIC_PHRASE_TRANSLATIONS.put("Understand Permissions", "понимание прав доступа");
        TOPIC_PHRASE_TRANSLATIONS.put("Installing Software and Applications", "установка ПО и приложений");
        TOPIC_PHRASE_TRANSLATIONS.put("Performing CRUD on Files", "CRUD-операции с файлами");
        TOPIC_PHRASE_TRANSLATIONS.put("Common Commands", "частые команды");
        TOPIC_PHRASE_TRANSLATIONS.put("Packet Sniffers", "packet sniffers");
        TOPIC_PHRASE_TRANSLATIONS.put("Networking Knowledge", "знания сетей");
        TOPIC_PHRASE_TRANSLATIONS.put("Port Scanners", "сканеры портов");
        TOPIC_PHRASE_TRANSLATIONS.put("Protocol Analyzers", "анализаторы протоколов");
        TOPIC_PHRASE_TRANSLATIONS.put("Understand the OSI Model", "понимание модели OSI");
        TOPIC_PHRASE_TRANSLATIONS.put("Common Protocols and their Uses", "частые протоколы и их применение");
        TOPIC_PHRASE_TRANSLATIONS.put("Common Ports and their Uses", "частые порты и их применение");
        TOPIC_PHRASE_TRANSLATIONS.put("SSL and TLS Basics", "основы SSL и TLS");
        TOPIC_PHRASE_TRANSLATIONS.put("Basics of NAS and SAN", "основы NAS и SAN");
        TOPIC_PHRASE_TRANSLATIONS.put("Core Concepts of Zero Trust", "ключевые концепции Zero Trust");
        TOPIC_PHRASE_TRANSLATIONS.put("Understand Common Hacking Tools", "понимание популярных hacking-инструментов");
        TOPIC_PHRASE_TRANSLATIONS.put("Roles of Compliance and Auditors", "роли compliance и аудиторов");
        TOPIC_PHRASE_TRANSLATIONS.put("Blue / Red / Purple Teams", "blue, red и purple teams");
        TOPIC_PHRASE_TRANSLATIONS.put("Understand Common Exploit Frameworks", "понимание популярных exploit-фреймворков");
        TOPIC_PHRASE_TRANSLATIONS.put("Understand the Definition of Risk", "понимание определения риска");
        TOPIC_PHRASE_TRANSLATIONS.put("False Negative / False Positive", "false negative и false positive");
        TOPIC_PHRASE_TRANSLATIONS.put("Understand Concept of Defense in Depth", "понимание концепции эшелонированной защиты");
        TOPIC_PHRASE_TRANSLATIONS.put("Understand Backups and Resiliency", "понимание backups и отказоустойчивости");
        TOPIC_PHRASE_TRANSLATIONS.put("Understand Concept of Runbooks", "понимание runbooks");
        TOPIC_PHRASE_TRANSLATIONS.put("Basics of Threat Intel, OSINT", "основы threat intelligence и OSINT");
        TOPIC_PHRASE_TRANSLATIONS.put("Understand Basics of Forensics", "понимание основ форензики");
        TOPIC_PHRASE_TRANSLATIONS.put("Understand CIA Triad", "понимание CIA triad");
        TOPIC_PHRASE_TRANSLATIONS.put("Basics of Vulnerability Management", "основы управления уязвимостями");
        TOPIC_PHRASE_TRANSLATIONS.put("Understand Concept of Isolation", "понимание изоляции");
        TOPIC_PHRASE_TRANSLATIONS.put("Privilege Escalation", "повышение привилегий");
        TOPIC_PHRASE_TRANSLATIONS.put("Basics of Reverse Engineering", "основы реверс-инжиниринга");
        TOPIC_PHRASE_TRANSLATIONS.put("How the Web Works", "как работает веб");
        TOPIC_PHRASE_TRANSLATIONS.put("What is a Repository", "что такое репозиторий");
        TOPIC_PHRASE_TRANSLATIONS.put("Working Directory", "рабочая директория");
        TOPIC_PHRASE_TRANSLATIONS.put("Staging Area", "staging area");
        TOPIC_PHRASE_TRANSLATIONS.put("Managing Remotes", "управление remote-репозиториями");
        TOPIC_PHRASE_TRANSLATIONS.put("Committing Changes", "создание коммитов");
        TOPIC_PHRASE_TRANSLATIONS.put("Creating Branch", "создание ветки");
        TOPIC_PHRASE_TRANSLATIONS.put("Pushing / Pulling Changes", "отправка и получение изменений");
        TOPIC_PHRASE_TRANSLATIONS.put("Renaming Branch", "переименование ветки");
        TOPIC_PHRASE_TRANSLATIONS.put("Fetch without Merge", "fetch без merge");
        TOPIC_PHRASE_TRANSLATIONS.put("Deleting Branch", "удаление ветки");
        TOPIC_PHRASE_TRANSLATIONS.put("Viewing Commit History", "просмотр истории коммитов");
        TOPIC_PHRASE_TRANSLATIONS.put("Checkout Branch", "переключение на ветку");
        TOPIC_PHRASE_TRANSLATIONS.put("Merge Strategies", "стратегии merge");
        TOPIC_PHRASE_TRANSLATIONS.put("Handling Conflicts", "разрешение конфликтов");
        TOPIC_PHRASE_TRANSLATIONS.put("Setting up Profile", "настройка профиля");
        TOPIC_PHRASE_TRANSLATIONS.put("Profile Readme", "README профиля");
        TOPIC_PHRASE_TRANSLATIONS.put("Cherry Picking Commits", "выборочный перенос коммитов");
        TOPIC_PHRASE_TRANSLATIONS.put("Creating Repositories", "создание репозиториев");
        TOPIC_PHRASE_TRANSLATIONS.put("Private vs Public", "приватный и публичный доступ: сравнение");
        TOPIC_PHRASE_TRANSLATIONS.put("Forking vs Cloning", "fork и clone: сравнение");
        TOPIC_PHRASE_TRANSLATIONS.put("Commit Messages", "сообщения коммитов");
        TOPIC_PHRASE_TRANSLATIONS.put("Branch Naming", "именование веток");
        TOPIC_PHRASE_TRANSLATIONS.put("Project Readme", "README проекта");
        TOPIC_PHRASE_TRANSLATIONS.put("Pull Requests", "запросы на слияние (Pull Request)");
        TOPIC_PHRASE_TRANSLATIONS.put("Code Reviews", "код-ревью");
        TOPIC_PHRASE_TRANSLATIONS.put("Labelling Issues / PRs", "метки для issues и PR");
        TOPIC_PHRASE_TRANSLATIONS.put("Saved Replies", "сохранённые ответы");
        TOPIC_PHRASE_TRANSLATIONS.put("Clean Git History", "чистая история Git");
        TOPIC_PHRASE_TRANSLATIONS.put("Working in a Team", "работа в команде");
        TOPIC_PHRASE_TRANSLATIONS.put("Linear vs Non-Linear", "линейный и нелинейный подход: сравнение");
        TOPIC_PHRASE_TRANSLATIONS.put("Collaborators / Members", "коллабораторы и участники");
        TOPIC_PHRASE_TRANSLATIONS.put("Teams within Organization", "команды внутри организации");
        TOPIC_PHRASE_TRANSLATIONS.put("Viewing Diffs", "просмотр diff");
        TOPIC_PHRASE_TRANSLATIONS.put("Between Commits", "между коммитами");
        TOPIC_PHRASE_TRANSLATIONS.put("Between Branches", "между ветками");
        TOPIC_PHRASE_TRANSLATIONS.put("Staged Changes", "staged-изменения");
        TOPIC_PHRASE_TRANSLATIONS.put("Unstaged Changes", "unstaged-изменения");
        TOPIC_PHRASE_TRANSLATIONS.put("Rewriting History", "переписывание истории");
        TOPIC_PHRASE_TRANSLATIONS.put("Undoing Changes", "отмена изменений");
        TOPIC_PHRASE_TRANSLATIONS.put("Anonymous Functions", "анонимные функции");
        TOPIC_PHRASE_TRANSLATIONS.put("Variable Declarations", "объявление переменных");
        TOPIC_PHRASE_TRANSLATIONS.put("Variable Scopes", "области видимости переменных");
        TOPIC_PHRASE_TRANSLATIONS.put("Arrow Functions", "стрелочные функции");
        TOPIC_PHRASE_TRANSLATIONS.put("Arithmetic Operators", "арифметические операторы");
        TOPIC_PHRASE_TRANSLATIONS.put("Bitwise Operators", "побитовые операторы");
        TOPIC_PHRASE_TRANSLATIONS.put("Logical Operators", "логические операторы");
        TOPIC_PHRASE_TRANSLATIONS.put("Comparison Operators", "операторы сравнения");
        TOPIC_PHRASE_TRANSLATIONS.put("Assignment Operators", "операторы присваивания");
        TOPIC_PHRASE_TRANSLATIONS.put("Conditional Statements", "условные операторы");
        TOPIC_PHRASE_TRANSLATIONS.put("Conditional Rendering", "условный рендеринг");
        TOPIC_PHRASE_TRANSLATIONS.put("Control Flow", "управление потоком выполнения");
        TOPIC_PHRASE_TRANSLATIONS.put("Classes and Objects", "классы и объекты");
        TOPIC_PHRASE_TRANSLATIONS.put("Abstract Classes", "абстрактные классы");
        TOPIC_PHRASE_TRANSLATIONS.put("Abstraction", "абстракция");
        TOPIC_PHRASE_TRANSLATIONS.put("Encapsulation", "инкапсуляция");
        TOPIC_PHRASE_TRANSLATIONS.put("Polymorphism", "полиморфизм");
        TOPIC_PHRASE_TRANSLATIONS.put("Exception Handling", "обработка исключений");
        TOPIC_PHRASE_TRANSLATIONS.put("Handling Exceptions", "обработка исключений");
        TOPIC_PHRASE_TRANSLATIONS.put("Function Overloading", "перегрузка функций");
        TOPIC_PHRASE_TRANSLATIONS.put("Function Types", "типы функций");
        TOPIC_PHRASE_TRANSLATIONS.put("Extension Functions", "функции-расширения");
        TOPIC_PHRASE_TRANSLATIONS.put("Properties", "свойства");
        TOPIC_PHRASE_TRANSLATIONS.put("Coroutines", "корутины");
        TOPIC_PHRASE_TRANSLATIONS.put("Garbage Collection", "сборка мусора");
        TOPIC_PHRASE_TRANSLATIONS.put("Performance Optimization", "оптимизация производительности");
        TOPIC_PHRASE_TRANSLATIONS.put("Regular Expressions", "регулярные выражения");
        TOPIC_PHRASE_TRANSLATIONS.put("File Permissions", "права доступа к файлам");
        TOPIC_PHRASE_TRANSLATIONS.put("Exit Codes", "коды завершения");
        TOPIC_PHRASE_TRANSLATIONS.put("Date & Time", "дата и время");
        TOPIC_PHRASE_TRANSLATIONS.put("Collections", "коллекции");
        TOPIC_PHRASE_TRANSLATIONS.put("Arguments", "аргументы");
        TOPIC_PHRASE_TRANSLATIONS.put("Constants & Variables", "константы и переменные");
        TOPIC_PHRASE_TRANSLATIONS.put("Data types", "типы данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Types", "типы данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Component Anatomy", "устройство компонента");
        TOPIC_PHRASE_TRANSLATIONS.put("Creating Components", "создание компонентов");
        TOPIC_PHRASE_TRANSLATIONS.put("Creating Modules", "создание модулей");
        TOPIC_PHRASE_TRANSLATIONS.put("Feature Modules", "feature-модули");
        TOPIC_PHRASE_TRANSLATIONS.put("Lazy Loading Modules", "lazy loading модулей");
        TOPIC_PHRASE_TRANSLATIONS.put("Parent-Child Interaction", "взаимодействие родителя и дочернего компонента");
        TOPIC_PHRASE_TRANSLATIONS.put("Understand Binding", "понимание binding");
        TOPIC_PHRASE_TRANSLATIONS.put("Dynamic Components", "динамические компоненты");
        TOPIC_PHRASE_TRANSLATIONS.put("Property Binding", "binding свойств");
        TOPIC_PHRASE_TRANSLATIONS.put("Structural Directives", "структурные директивы");
        TOPIC_PHRASE_TRANSLATIONS.put("Attribute Binding", "binding атрибутов");
        TOPIC_PHRASE_TRANSLATIONS.put("Template Syntax", "синтаксис шаблонов");
        TOPIC_PHRASE_TRANSLATIONS.put("Attribute Directives", "атрибутные директивы");
        TOPIC_PHRASE_TRANSLATIONS.put("Event Binding", "binding событий");
        TOPIC_PHRASE_TRANSLATIONS.put("Change Detection", "обнаружение изменений");
        TOPIC_PHRASE_TRANSLATIONS.put("Common Pipes", "стандартные pipes");
        TOPIC_PHRASE_TRANSLATIONS.put("Custom Pipes", "пользовательские pipes");
        TOPIC_PHRASE_TRANSLATIONS.put("Reactive Forms", "реактивные формы");
        TOPIC_PHRASE_TRANSLATIONS.put("Typed Forms", "типизированные формы");
        TOPIC_PHRASE_TRANSLATIONS.put("Template-driven Forms", "формы на основе шаблонов");
        TOPIC_PHRASE_TRANSLATIONS.put("Router Links", "router links");
        TOPIC_PHRASE_TRANSLATIONS.put("Dynamic Forms", "динамические формы");
        TOPIC_PHRASE_TRANSLATIONS.put("Router Events", "события роутера");
        TOPIC_PHRASE_TRANSLATIONS.put("HTTP Client", "HTTP-клиент");
        TOPIC_PHRASE_TRANSLATIONS.put("Custom Validators", "пользовательские валидаторы");
        TOPIC_PHRASE_TRANSLATIONS.put("Control Value Accessor", "Control Value Accessor");
        TOPIC_PHRASE_TRANSLATIONS.put("Observable Pattern", "паттерн Observable");
        TOPIC_PHRASE_TRANSLATIONS.put("Writing Interceptors", "создание интерсепторов");
        TOPIC_PHRASE_TRANSLATIONS.put("Inputs as Signals", "inputs как signals");
        TOPIC_PHRASE_TRANSLATIONS.put("Queries as Signals", "queries как signals");
        TOPIC_PHRASE_TRANSLATIONS.put("Model Inputs", "model inputs");
        TOPIC_PHRASE_TRANSLATIONS.put("Language Service", "language service");
        TOPIC_PHRASE_TRANSLATIONS.put("Local Setup", "локальная настройка");
        TOPIC_PHRASE_TRANSLATIONS.put("Using Libraries", "использование библиотек");
        TOPIC_PHRASE_TRANSLATIONS.put("Creating Libraries", "создание библиотек");
        TOPIC_PHRASE_TRANSLATIONS.put("Build Environments", "окружения сборки");
        TOPIC_PHRASE_TRANSLATIONS.put("Cross-site Scripting", "межсайтовый скриптинг");
        TOPIC_PHRASE_TRANSLATIONS.put("Cross-site Request Forgery", "межсайтовая подделка запроса");
        TOPIC_PHRASE_TRANSLATIONS.put("HTTP Vulnerabilities", "уязвимости HTTP");
        TOPIC_PHRASE_TRANSLATIONS.put("Trusting Safe Values", "доверенные безопасные значения");
        TOPIC_PHRASE_TRANSLATIONS.put("Image Optimization", "оптимизация изображений");
        TOPIC_PHRASE_TRANSLATIONS.put("Box Model", "блочная модель");
        TOPIC_PHRASE_TRANSLATIONS.put("Grid Layout", "grid-раскладка");
        TOPIC_PHRASE_TRANSLATIONS.put("Media Queries", "media queries");
        TOPIC_PHRASE_TRANSLATIONS.put("Responsive Typography", "адаптивная типографика");
        TOPIC_PHRASE_TRANSLATIONS.put("Background Color", "цвет фона");
        TOPIC_PHRASE_TRANSLATIONS.put("Background Image", "фоновое изображение");
        TOPIC_PHRASE_TRANSLATIONS.put("Background Position", "позиция фона");
        TOPIC_PHRASE_TRANSLATIONS.put("Background Gradient", "градиент фона");
        TOPIC_PHRASE_TRANSLATIONS.put("Absolute Positioning", "абсолютное позиционирование");
        TOPIC_PHRASE_TRANSLATIONS.put("Relative Positioning", "относительное позиционирование");
        TOPIC_PHRASE_TRANSLATIONS.put("Fixed Positioning", "фиксированное позиционирование");
        TOPIC_PHRASE_TRANSLATIONS.put("Sticky Positioning", "sticky-позиционирование");
        TOPIC_PHRASE_TRANSLATIONS.put("Text Alignment", "выравнивание текста");
        TOPIC_PHRASE_TRANSLATIONS.put("Text Decoration", "оформление текста");
        TOPIC_PHRASE_TRANSLATIONS.put("Text Styling", "стилизация текста");
        TOPIC_PHRASE_TRANSLATIONS.put("Text Transform", "трансформация текста");
        TOPIC_PHRASE_TRANSLATIONS.put("Text Shadows", "тени текста");
        TOPIC_PHRASE_TRANSLATIONS.put("Width and Height", "ширина и высота");
        TOPIC_PHRASE_TRANSLATIONS.put("Container Queries", "container queries");
        TOPIC_PHRASE_TRANSLATIONS.put("Basic HTML Tags", "базовые HTML-теги");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Attributes", "data-атрибуты");
        TOPIC_PHRASE_TRANSLATIONS.put("Embedding Media", "встраивание медиа");
        TOPIC_PHRASE_TRANSLATIONS.put("File Uploads", "загрузка файлов");
        TOPIC_PHRASE_TRANSLATIONS.put("Labels and Inputs", "labels и inputs");
        TOPIC_PHRASE_TRANSLATIONS.put("HTML Form Constraints", "ограничения HTML-форм");
        TOPIC_PHRASE_TRANSLATIONS.put("Tags and Attributes", "теги и атрибуты");
        TOPIC_PHRASE_TRANSLATIONS.put("Search Engine Optimization (SEO)", "поисковая оптимизация (SEO)");
        TOPIC_PHRASE_TRANSLATIONS.put("Basic SQL Syntax", "базовый синтаксис SQL");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Manipulation Language (DML)", "язык манипулирования данными (DML)");
        TOPIC_PHRASE_TRANSLATIONS.put("Alter Table", "ALTER TABLE");
        TOPIC_PHRASE_TRANSLATIONS.put("Create Table", "CREATE TABLE");
        TOPIC_PHRASE_TRANSLATIONS.put("Drop Table", "DROP TABLE");
        TOPIC_PHRASE_TRANSLATIONS.put("Primary Key", "первичный ключ");
        TOPIC_PHRASE_TRANSLATIONS.put("Foreign Key", "внешний ключ");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Constraints", "ограничения данных");
        TOPIC_PHRASE_TRANSLATIONS.put("JOIN Queries", "JOIN-запросы");
        TOPIC_PHRASE_TRANSLATIONS.put("Nested Subqueries", "вложенные подзапросы");
        TOPIC_PHRASE_TRANSLATIONS.put("Managing Indexes", "управление индексами");
        TOPIC_PHRASE_TRANSLATIONS.put("Creating Views", "создание views");
        TOPIC_PHRASE_TRANSLATIONS.put("Modifying Views", "изменение views");
        TOPIC_PHRASE_TRANSLATIONS.put("Dropping Views", "удаление views");
        TOPIC_PHRASE_TRANSLATIONS.put("Transaction Isolation Levels", "уровни изоляции транзакций");
        TOPIC_PHRASE_TRANSLATIONS.put("Stored Procedures & Functions", "хранимые процедуры и функции");
        TOPIC_PHRASE_TRANSLATIONS.put("Using Indexes", "использование индексов");
        TOPIC_PHRASE_TRANSLATIONS.put("Optimizing Joins", "оптимизация join");
        TOPIC_PHRASE_TRANSLATIONS.put("Window Functions", "оконные функции");
        TOPIC_PHRASE_TRANSLATIONS.put("Recursive Queries", "рекурсивные запросы");
        TOPIC_PHRASE_TRANSLATIONS.put("Common Table Expressions", "common table expressions");
        TOPIC_PHRASE_TRANSLATIONS.put("Selective Projection", "выборочная проекция");
        TOPIC_PHRASE_TRANSLATIONS.put("Linked List", "связный список");
        TOPIC_PHRASE_TRANSLATIONS.put("Linked Lists", "связные списки");
        TOPIC_PHRASE_TRANSLATIONS.put("Hash Table", "хеш-таблица");
        TOPIC_PHRASE_TRANSLATIONS.put("Hash Tables", "хеш-таблицы");
        TOPIC_PHRASE_TRANSLATIONS.put("Directed Graph", "ориентированный граф");
        TOPIC_PHRASE_TRANSLATIONS.put("Undirected Graph", "неориентированный граф");
        TOPIC_PHRASE_TRANSLATIONS.put("Binary Tree", "бинарное дерево");
        TOPIC_PHRASE_TRANSLATIONS.put("Binary Search Tree", "двоичное дерево поиска");
        TOPIC_PHRASE_TRANSLATIONS.put("Spanning Tree", "остовное дерево");
        TOPIC_PHRASE_TRANSLATIONS.put("Complete Binary Tree", "полное бинарное дерево");
        TOPIC_PHRASE_TRANSLATIONS.put("Balanced Tree", "сбалансированное дерево");
        TOPIC_PHRASE_TRANSLATIONS.put("Unbalanced Tree", "несбалансированное дерево");
        TOPIC_PHRASE_TRANSLATIONS.put("Asymptotic Notation", "асимптотическая нотация");
        TOPIC_PHRASE_TRANSLATIONS.put("Bubble Sort", "сортировка пузырьком");
        TOPIC_PHRASE_TRANSLATIONS.put("Tail Recursion", "хвостовая рекурсия");
        TOPIC_PHRASE_TRANSLATIONS.put("Binary Search", "двоичный поиск");
        TOPIC_PHRASE_TRANSLATIONS.put("Quick Sort", "быстрая сортировка");
        TOPIC_PHRASE_TRANSLATIONS.put("Linear Search", "линейный поиск");
        TOPIC_PHRASE_TRANSLATIONS.put("Merge Sort", "сортировка слиянием");
        TOPIC_PHRASE_TRANSLATIONS.put("LRU Cache", "LRU-кэш");
        TOPIC_PHRASE_TRANSLATIONS.put("LFU Cache", "LFU-кэш");
        TOPIC_PHRASE_TRANSLATIONS.put("Dijkstra's Algorithm", "алгоритм Дейкстры");
        TOPIC_PHRASE_TRANSLATIONS.put("Kruskal's Algorithm", "алгоритм Краскала");
        TOPIC_PHRASE_TRANSLATIONS.put("Prim's Algorithm", "алгоритм Прима");
        TOPIC_PHRASE_TRANSLATIONS.put("Floating Point Math", "арифметика с плавающей точкой");
        TOPIC_PHRASE_TRANSLATIONS.put("Performance vs Scalability", "производительность и масштабируемость: сравнение");
        TOPIC_PHRASE_TRANSLATIONS.put("Latency vs Throughput", "задержка и пропускная способность: сравнение");
        TOPIC_PHRASE_TRANSLATIONS.put("Availability vs Consistency", "доступность и согласованность: сравнение");
        TOPIC_PHRASE_TRANSLATIONS.put("Weak Consistency", "слабая согласованность");
        TOPIC_PHRASE_TRANSLATIONS.put("Eventual Consistency", "eventual consistency");
        TOPIC_PHRASE_TRANSLATIONS.put("Strong Consistency", "строгая согласованность");
        TOPIC_PHRASE_TRANSLATIONS.put("Load Balancers", "балансировщики нагрузки");
        TOPIC_PHRASE_TRANSLATIONS.put("Load Balancing", "балансировка нагрузки");
        TOPIC_PHRASE_TRANSLATIONS.put("Horizontal Scaling", "горизонтальное масштабирование");
        TOPIC_PHRASE_TRANSLATIONS.put("Key-Value Store", "key-value хранилище");
        TOPIC_PHRASE_TRANSLATIONS.put("Document Store", "документное хранилище");
        TOPIC_PHRASE_TRANSLATIONS.put("Wide Column Store", "wide-column хранилище");
        TOPIC_PHRASE_TRANSLATIONS.put("Task Queues", "очереди задач");
        TOPIC_PHRASE_TRANSLATIONS.put("Cache Aside", "паттерн Cache Aside");
        TOPIC_PHRASE_TRANSLATIONS.put("Client Caching", "кэширование на клиенте");
        TOPIC_PHRASE_TRANSLATIONS.put("Idempotent Operations", "идемпотентные операции");
        TOPIC_PHRASE_TRANSLATIONS.put("Web Server Caching", "кэширование на веб-сервере");
        TOPIC_PHRASE_TRANSLATIONS.put("Database Caching", "кэширование базы данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Leader Election", "выбор лидера");
        TOPIC_PHRASE_TRANSLATIONS.put("Materialized View", "материализованное представление");
        TOPIC_PHRASE_TRANSLATIONS.put("External Config Store", "внешнее хранилище конфигурации");
        TOPIC_PHRASE_TRANSLATIONS.put("Basic Commands", "базовые команды");
        TOPIC_PHRASE_TRANSLATIONS.put("Moving Files / Directories", "перемещение файлов и директорий");
        TOPIC_PHRASE_TRANSLATIONS.put("Creating & Deleting Files / Dirs", "создание и удаление файлов и директорий");
        TOPIC_PHRASE_TRANSLATIONS.put("Working with Files", "работа с файлами");
        TOPIC_PHRASE_TRANSLATIONS.put("Copying and Renaming", "копирование и переименование");
        TOPIC_PHRASE_TRANSLATIONS.put("Soft Links / Hard Links", "символические и жёсткие ссылки");
        TOPIC_PHRASE_TRANSLATIONS.put("Users and Groups", "пользователи и группы");
        TOPIC_PHRASE_TRANSLATIONS.put("Managing Permissions", "управление правами доступа");
        TOPIC_PHRASE_TRANSLATIONS.put("Disks and Filesystems", "диски и файловые системы");
        TOPIC_PHRASE_TRANSLATIONS.put("Finding & Installing Packages", "поиск и установка пакетов");
        TOPIC_PHRASE_TRANSLATIONS.put("Install / Remove / Upgrade Packages", "установка, удаление и обновление пакетов");
        TOPIC_PHRASE_TRANSLATIONS.put("DNS Resolution", "разрешение DNS");
        TOPIC_PHRASE_TRANSLATIONS.put("File Transfer", "передача файлов");
        TOPIC_PHRASE_TRANSLATIONS.put("Positional Parameters", "позиционные параметры");
        TOPIC_PHRASE_TRANSLATIONS.put("Arithmetic Expansion", "арифметическое расширение");
        TOPIC_PHRASE_TRANSLATIONS.put("Associative Arrays", "ассоциативные массивы");
        TOPIC_PHRASE_TRANSLATIONS.put("Background jobs", "фоновые задачи");
        TOPIC_PHRASE_TRANSLATIONS.put("Bash Script Anatomy", "структура Bash-скрипта");
        TOPIC_PHRASE_TRANSLATIONS.put("Basic Editor Operations", "базовые операции в редакторе");
        TOPIC_PHRASE_TRANSLATIONS.put("Command Substitution", "подстановка команд");
        TOPIC_PHRASE_TRANSLATIONS.put("Variables in Shell Scripting", "переменные в shell-скриптах");
        TOPIC_PHRASE_TRANSLATIONS.put("Environment vs. Shell Variables", "переменные окружения и shell-переменные: сравнение");
        TOPIC_PHRASE_TRANSLATIONS.put("Error Logging", "логирование ошибок");
        TOPIC_PHRASE_TRANSLATIONS.put("Error Redirection", "перенаправление ошибок");
        TOPIC_PHRASE_TRANSLATIONS.put("File Test Operators", "операторы проверки файлов");
        TOPIC_PHRASE_TRANSLATIONS.put("Here Documents", "here documents");
        TOPIC_PHRASE_TRANSLATIONS.put("Here Strings", "here strings");
        TOPIC_PHRASE_TRANSLATIONS.put("Input Redirection", "перенаправление ввода");
        TOPIC_PHRASE_TRANSLATIONS.put("Output Redirection", "перенаправление вывода");
        TOPIC_PHRASE_TRANSLATIONS.put("Redirects & Pipelines", "redirects и pipelines");
        TOPIC_PHRASE_TRANSLATIONS.put("Running Shell Scripts", "запуск shell-скриптов");
        TOPIC_PHRASE_TRANSLATIONS.put("Shared Responsibility Model", "модель разделения ответственности");
        TOPIC_PHRASE_TRANSLATIONS.put("Well Architected Framework", "Well-Architected Framework");
        TOPIC_PHRASE_TRANSLATIONS.put("Route Tables", "таблицы маршрутизации");
        TOPIC_PHRASE_TRANSLATIONS.put("Security Groups", "группы безопасности");
        TOPIC_PHRASE_TRANSLATIONS.put("Purchasing Options", "варианты покупки");
        TOPIC_PHRASE_TRANSLATIONS.put("Health Checks", "проверки состояния");
        TOPIC_PHRASE_TRANSLATIONS.put("Scaling Policies", "политики масштабирования");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Modeling", "моделирование данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Project Initialization", "инициализация проекта");
        TOPIC_PHRASE_TRANSLATIONS.put("Configuring Providers", "настройка провайдеров");
        TOPIC_PHRASE_TRANSLATIONS.put("Output Syntax", "синтаксис output");
        TOPIC_PHRASE_TRANSLATIONS.put("Sensitive Outputs", "чувствительные outputs");
        TOPIC_PHRASE_TRANSLATIONS.put("Input Variables", "входные переменные");
        TOPIC_PHRASE_TRANSLATIONS.put("Type Constraints", "ограничения типов");
        TOPIC_PHRASE_TRANSLATIONS.put("Meta Arguments", "мета-аргументы");
        TOPIC_PHRASE_TRANSLATIONS.put("State Locking", "блокировка state");
        TOPIC_PHRASE_TRANSLATIONS.put("Format & Validate", "форматирование и валидация");
        TOPIC_PHRASE_TRANSLATIONS.put("Remote State", "удалённый state");
        TOPIC_PHRASE_TRANSLATIONS.put("Creation / Destroy Time", "время создания и удаления");
        TOPIC_PHRASE_TRANSLATIONS.put("Root vs Child Modules", "root и child modules: сравнение");
        TOPIC_PHRASE_TRANSLATIONS.put("Inputs / Outputs", "inputs и outputs");
        TOPIC_PHRASE_TRANSLATIONS.put("Template Files", "файлы шаблонов");
        TOPIC_PHRASE_TRANSLATIONS.put("Run Tasks", "задачи запуска");
        TOPIC_PHRASE_TRANSLATIONS.put("All about Variables", "всё о переменных");
        TOPIC_PHRASE_TRANSLATIONS.put("Variable Naming Rules", "правила именования переменных");
        TOPIC_PHRASE_TRANSLATIONS.put("Object Prototype", "прототип объекта");
        TOPIC_PHRASE_TRANSLATIONS.put("Prototypal Inheritance", "прототипное наследование");
        TOPIC_PHRASE_TRANSLATIONS.put("Built-in Objects", "встроенные объекты");
        TOPIC_PHRASE_TRANSLATIONS.put("Structured Data", "структурированные данные");
        TOPIC_PHRASE_TRANSLATIONS.put("Type Casting", "приведение типов");
        TOPIC_PHRASE_TRANSLATIONS.put("Type Conversion vs Coercion", "преобразование типов и неявное приведение: сравнение");
        TOPIC_PHRASE_TRANSLATIONS.put("Implicit Type Casting", "неявное приведение типов");
        TOPIC_PHRASE_TRANSLATIONS.put("Explicit Type Casting", "явное приведение типов");
        TOPIC_PHRASE_TRANSLATIONS.put("Keyed Collections", "коллекции по ключу");
        TOPIC_PHRASE_TRANSLATIONS.put("Indexed Collections", "индексируемые коллекции");
        TOPIC_PHRASE_TRANSLATIONS.put("Equality Comparisons", "сравнение на равенство");
        TOPIC_PHRASE_TRANSLATIONS.put("Loops and Iterations", "циклы и итерации");
        TOPIC_PHRASE_TRANSLATIONS.put("Expressions & Operators", "выражения и операторы");
        TOPIC_PHRASE_TRANSLATIONS.put("Conditional Operators", "тернарные и условные операторы");
        TOPIC_PHRASE_TRANSLATIONS.put("Exceptional Handling", "обработка исключительных ситуаций");
        TOPIC_PHRASE_TRANSLATIONS.put("Function Parameters", "параметры функций");
        TOPIC_PHRASE_TRANSLATIONS.put("Scope & Function Stack", "область видимости и стек функций");
        TOPIC_PHRASE_TRANSLATIONS.put("Asynchronous JavaScript", "асинхронный JavaScript");
        TOPIC_PHRASE_TRANSLATIONS.put("Explicit Binding", "явное связывание");
        TOPIC_PHRASE_TRANSLATIONS.put("Working with APIs", "работа с API");
        TOPIC_PHRASE_TRANSLATIONS.put("Iterators and Generators", "итераторы и генераторы");
        TOPIC_PHRASE_TRANSLATIONS.put("Modules in JavaScript", "модули в JavaScript");
        TOPIC_PHRASE_TRANSLATIONS.put("Memory Management", "управление памятью");
        TOPIC_PHRASE_TRANSLATIONS.put("Type Inference", "выведение типов");
        TOPIC_PHRASE_TRANSLATIONS.put("Type Compatibility", "совместимость типов");
        TOPIC_PHRASE_TRANSLATIONS.put("Type Guards / Narrowing", "type guards и narrowing");
        TOPIC_PHRASE_TRANSLATIONS.put("Types vs Interfaces", "типы и интерфейсы: сравнение");
        TOPIC_PHRASE_TRANSLATIONS.put("Typing Functions", "типизация функций");
        TOPIC_PHRASE_TRANSLATIONS.put("Access Modifiers", "модификаторы доступа");
        TOPIC_PHRASE_TRANSLATIONS.put("Method Overriding", "переопределение методов");
        TOPIC_PHRASE_TRANSLATIONS.put("TypeScript Modules", "модули TypeScript");
        TOPIC_PHRASE_TRANSLATIONS.put("Useful Packages", "полезные пакеты");
        TOPIC_PHRASE_TRANSLATIONS.put("Strings and Methods", "строки и методы");
        TOPIC_PHRASE_TRANSLATIONS.put("Basic Syntax", "базовый синтаксис");
        TOPIC_PHRASE_TRANSLATIONS.put("Math Operations", "математические операции");
        TOPIC_PHRASE_TRANSLATIONS.put("Lifecycle of a Program", "жизненный цикл программы");
        TOPIC_PHRASE_TRANSLATIONS.put("Variables and Scopes", "переменные и области видимости");
        TOPIC_PHRASE_TRANSLATIONS.put("Attributes and Methods", "атрибуты и методы");
        TOPIC_PHRASE_TRANSLATIONS.put("Object Lifecycle", "жизненный цикл объекта");
        TOPIC_PHRASE_TRANSLATIONS.put("Final Keyword", "ключевое слово final");
        TOPIC_PHRASE_TRANSLATIONS.put("Lambda Expressions", "lambda-выражения");
        TOPIC_PHRASE_TRANSLATIONS.put("Method Chaining", "цепочки вызовов методов");
        TOPIC_PHRASE_TRANSLATIONS.put("Nested Classes", "вложенные классы");
        TOPIC_PHRASE_TRANSLATIONS.put("Static vs Dynamic Binding", "статическое и динамическое связывание: сравнение");
        TOPIC_PHRASE_TRANSLATIONS.put("Pass by Value / Pass by Reference", "передача по значению и по ссылке: сравнение");
        TOPIC_PHRASE_TRANSLATIONS.put("Java Memory Model", "модель памяти Java");
        TOPIC_PHRASE_TRANSLATIONS.put("I/O Operations", "операции ввода-вывода");
        TOPIC_PHRASE_TRANSLATIONS.put("Generic Collections", "обобщённые коллекции");
        TOPIC_PHRASE_TRANSLATIONS.put("High Order Functions", "функции высшего порядка");
        TOPIC_PHRASE_TRANSLATIONS.put("Functional Interfaces", "функциональные интерфейсы");
        TOPIC_PHRASE_TRANSLATIONS.put("Functional Composition", "функциональная композиция");
        TOPIC_PHRASE_TRANSLATIONS.put("Logging Frameworks", "logging-фреймворки");
        TOPIC_PHRASE_TRANSLATIONS.put("Variables and Data Types", "переменные и типы данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Functions, Builtin Functions", "функции и встроенные функции");
        TOPIC_PHRASE_TRANSLATIONS.put("Object Oriented Programming", "объектно-ориентированное программирование");
        TOPIC_PHRASE_TRANSLATIONS.put("Arrays and Linked Lists", "массивы и связные списки");
        TOPIC_PHRASE_TRANSLATIONS.put("Heaps, Stacks and Queues", "кучи, стеки и очереди");
        TOPIC_PHRASE_TRANSLATIONS.put("Common Packages", "популярные пакеты");
        TOPIC_PHRASE_TRANSLATIONS.put("List Comprehensions", "list comprehensions");
        TOPIC_PHRASE_TRANSLATIONS.put("Generator Expressions", "генераторные выражения");
        TOPIC_PHRASE_TRANSLATIONS.put("Context Manager", "контекстный менеджер");
        TOPIC_PHRASE_TRANSLATIONS.put("Code Formatting", "форматирование кода");
        TOPIC_PHRASE_TRANSLATIONS.put("Asynchrony", "асинхронность");
        TOPIC_PHRASE_TRANSLATIONS.put("Build Tools", "инструменты сборки");
        TOPIC_PHRASE_TRANSLATIONS.put("Anonymous Functions", "анонимные функции");
        TOPIC_PHRASE_TRANSLATIONS.put("Buffered vs Unbuffered", "буферизованный и небуферизованный режим: сравнение");
        TOPIC_PHRASE_TRANSLATIONS.put("Building CLIs", "создание CLI");
        TOPIC_PHRASE_TRANSLATIONS.put("Building Executables", "создание исполняемых файлов");
        TOPIC_PHRASE_TRANSLATIONS.put("Call by Value", "передача по значению");
        TOPIC_PHRASE_TRANSLATIONS.put("Concurrency Patterns", "паттерны конкурентности");
        TOPIC_PHRASE_TRANSLATIONS.put("Common Usecases", "типовые сценарии использования");
        TOPIC_PHRASE_TRANSLATIONS.put("Complex Numbers", "комплексные числа");
        TOPIC_PHRASE_TRANSLATIONS.put("Error Handling Basics", "основы обработки ошибок");
        TOPIC_PHRASE_TRANSLATIONS.put("Go Toolchain and Tools", "toolchain и инструменты Go");
        TOPIC_PHRASE_TRANSLATIONS.put("History of Go", "история Go");
        TOPIC_PHRASE_TRANSLATIONS.put("Introduction to Go", "введение в Go");
        TOPIC_PHRASE_TRANSLATIONS.put("Methods vs Functions", "методы и функции: сравнение");
        TOPIC_PHRASE_TRANSLATIONS.put("Modules & Dependencies", "модули и зависимости");
        TOPIC_PHRASE_TRANSLATIONS.put("Multiple Return Values", "множественные возвращаемые значения");
        TOPIC_PHRASE_TRANSLATIONS.put("Ownership Rules & Memory Safety", "правила владения и безопасность памяти");
        TOPIC_PHRASE_TRANSLATIONS.put("Pattern Matching & Destructuring", "pattern matching и деструктуризация");
        TOPIC_PHRASE_TRANSLATIONS.put("Language Basics", "основы языка");
        TOPIC_PHRASE_TRANSLATIONS.put("Error Handling", "обработка ошибок");
        TOPIC_PHRASE_TRANSLATIONS.put("Option and Result Enumerations", "перечисления Option и Result");
        TOPIC_PHRASE_TRANSLATIONS.put("Dependency Management with Cargo", "управление зависимостями через Cargo");
        TOPIC_PHRASE_TRANSLATIONS.put("Borrowing, References and Slices", "заимствование, ссылки и срезы");
        TOPIC_PHRASE_TRANSLATIONS.put("Deep Dive: Stack vs Heap", "подробно: stack и heap");
        TOPIC_PHRASE_TRANSLATIONS.put("Threads, Channels and Message Passing", "потоки, каналы и передача сообщений");
        TOPIC_PHRASE_TRANSLATIONS.put("Traits & Generics", "traits и generics");
        TOPIC_PHRASE_TRANSLATIONS.put("Trait Definitions & Implementations", "определение и реализация traits");
        TOPIC_PHRASE_TRANSLATIONS.put("Trait Bounds and Associated Types", "ограничения traits и связанные типы");
        TOPIC_PHRASE_TRANSLATIONS.put("Lifetimes & Borrow Checker", "lifetime и borrow checker");
        TOPIC_PHRASE_TRANSLATIONS.put("Asynchronous Programming", "асинхронное программирование");
        TOPIC_PHRASE_TRANSLATIONS.put("Serialization / Deserialization", "сериализация и десериализация");
        TOPIC_PHRASE_TRANSLATIONS.put("Database and ORM", "база данных и ORM");
        TOPIC_PHRASE_TRANSLATIONS.put("Performance and Profiling", "производительность и профилирование");
        TOPIC_PHRASE_TRANSLATIONS.put("Control Flow and Constructs", "управляющие конструкции");
        TOPIC_PHRASE_TRANSLATIONS.put("Functions and Method Syntax", "синтаксис функций и методов");
        TOPIC_PHRASE_TRANSLATIONS.put("Installing Rust and Cargo", "установка Rust и Cargo");
        TOPIC_PHRASE_TRANSLATIONS.put("IDEs and Rust Toolchains", "IDE и toolchain Rust");
        TOPIC_PHRASE_TRANSLATIONS.put("What is Rust?", "что такое Rust?");
        TOPIC_PHRASE_TRANSLATIONS.put("Why use Rust?", "зачем использовать Rust?");
        TOPIC_PHRASE_TRANSLATIONS.put("What is C++?", "что такое C++?");
        TOPIC_PHRASE_TRANSLATIONS.put("Why use C++?", "зачем использовать C++?");
        TOPIC_PHRASE_TRANSLATIONS.put("Setting up your Environment", "настройка окружения");
        TOPIC_PHRASE_TRANSLATIONS.put("Basic Operations", "базовые операции");
        TOPIC_PHRASE_TRANSLATIONS.put("Control Flow & Statements", "управляющие конструкции и операторы");
        TOPIC_PHRASE_TRANSLATIONS.put("Running your First Program", "запуск первой программы");
        TOPIC_PHRASE_TRANSLATIONS.put("Pointers and References", "указатели и ссылки");
        TOPIC_PHRASE_TRANSLATIONS.put("Static Typing", "статическая типизация");
        TOPIC_PHRASE_TRANSLATIONS.put("Operator Overloading", "перегрузка операторов");
        TOPIC_PHRASE_TRANSLATIONS.put("Memory Model", "модель памяти");
        TOPIC_PHRASE_TRANSLATIONS.put("Lifetime of Objects", "жизненный цикл объектов");
        TOPIC_PHRASE_TRANSLATIONS.put("Smart Pointers", "умные указатели");
        TOPIC_PHRASE_TRANSLATIONS.put("Forward Declaration", "предварительное объявление");
        TOPIC_PHRASE_TRANSLATIONS.put("Dynamic Polymorphism", "динамический полиморфизм");
        TOPIC_PHRASE_TRANSLATIONS.put("Object Oriented Programming", "объектно-ориентированное программирование");
        TOPIC_PHRASE_TRANSLATIONS.put("Structuring Codebase", "структурирование кодовой базы");
        TOPIC_PHRASE_TRANSLATIONS.put("Structures and Classes", "структуры и классы");
        TOPIC_PHRASE_TRANSLATIONS.put("Memory Leakage", "утечки памяти");
        TOPIC_PHRASE_TRANSLATIONS.put("Multiple Inheritance", "множественное наследование");
        TOPIC_PHRASE_TRANSLATIONS.put("Diamond Inheritance", "ромбовидное наследование");
        TOPIC_PHRASE_TRANSLATIONS.put("Rule of Zero, Five, Three", "правило нуля, пяти и трёх");
        TOPIC_PHRASE_TRANSLATIONS.put("Type Traits", "type traits");
        TOPIC_PHRASE_TRANSLATIONS.put("Template Specialization", "специализация шаблонов");
        TOPIC_PHRASE_TRANSLATIONS.put("Undefined Behavior (UB)", "неопределённое поведение (UB)");
        TOPIC_PHRASE_TRANSLATIONS.put("Standard Library + STL", "стандартная библиотека и STL");
        TOPIC_PHRASE_TRANSLATIONS.put("Compiler Stages", "этапы компиляции");
        TOPIC_PHRASE_TRANSLATIONS.put("Compilers and Features", "компиляторы и возможности");
        TOPIC_PHRASE_TRANSLATIONS.put("Working with Libraries", "работа с библиотеками");
        TOPIC_PHRASE_TRANSLATIONS.put("Variables and Scope", "переменные и область видимости");
        TOPIC_PHRASE_TRANSLATIONS.put("HTTP Methods", "HTTP-методы");
        TOPIC_PHRASE_TRANSLATIONS.put("Default / Optional Params", "параметры по умолчанию и необязательные параметры");
        TOPIC_PHRASE_TRANSLATIONS.put("Parameters / Return Values", "параметры и возвращаемые значения");
        TOPIC_PHRASE_TRANSLATIONS.put("State Management", "управление состоянием");
        TOPIC_PHRASE_TRANSLATIONS.put("Input Validation", "валидация ввода");
        TOPIC_PHRASE_TRANSLATIONS.put("SQL Injection", "SQL-инъекция");
        TOPIC_PHRASE_TRANSLATIONS.put("Auth Mechanisms", "механизмы аутентификации");
        TOPIC_PHRASE_TRANSLATIONS.put("Password Hashing", "хеширование паролей");
        TOPIC_PHRASE_TRANSLATIONS.put("Database Transactions", "транзакции базы данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Sanitization Techniques", "техники санитизации");
        TOPIC_PHRASE_TRANSLATIONS.put("Connection Pooling", "пул соединений");
        TOPIC_PHRASE_TRANSLATIONS.put("Database Migrations", "миграции базы данных");
        TOPIC_PHRASE_TRANSLATIONS.put("OOP Fundamentals", "основы ООП");
        TOPIC_PHRASE_TRANSLATIONS.put("Properties and Methods", "свойства и методы");
        TOPIC_PHRASE_TRANSLATIONS.put("Static Methods and Properties", "статические методы и свойства");
        TOPIC_PHRASE_TRANSLATIONS.put("Type Declarations", "объявления типов");
        TOPIC_PHRASE_TRANSLATIONS.put("Static Analysis", "статический анализ");
        TOPIC_PHRASE_TRANSLATIONS.put("Profiling Techniques", "техники профилирования");
        TOPIC_PHRASE_TRANSLATIONS.put("Configuration Tuning", "тонкая настройка конфигурации");
        TOPIC_PHRASE_TRANSLATIONS.put("Configuration Files", "файлы конфигурации");
        TOPIC_PHRASE_TRANSLATIONS.put("Opcode Caching", "кэширование opcode");
        TOPIC_PHRASE_TRANSLATIONS.put("Navigation Basics", "основы навигации");
        TOPIC_PHRASE_TRANSLATIONS.put("Directory Hierarchy Overview", "обзор иерархии директорий");
        TOPIC_PHRASE_TRANSLATIONS.put("Shell and Other Basics", "основы shell и сопутствующих инструментов");
        TOPIC_PHRASE_TRANSLATIONS.put("Redirects", "перенаправления");
        TOPIC_PHRASE_TRANSLATIONS.put("Process Management", "управление процессами");
        TOPIC_PHRASE_TRANSLATIONS.put("Background / Foreground Processes", "фоновые и foreground-процессы");
        TOPIC_PHRASE_TRANSLATIONS.put("User Management", "управление пользователями");
        TOPIC_PHRASE_TRANSLATIONS.put("Listing / Finding Processes", "поиск и просмотр процессов");
        TOPIC_PHRASE_TRANSLATIONS.put("Process Signals", "сигналы процессов");
        TOPIC_PHRASE_TRANSLATIONS.put("Services Running", "запущенные сервисы");
        TOPIC_PHRASE_TRANSLATIONS.put("Process Priorities", "приоритеты процессов");
        TOPIC_PHRASE_TRANSLATIONS.put("Package Management", "управление пакетами");
        TOPIC_PHRASE_TRANSLATIONS.put("Networking", "сети");
        TOPIC_PHRASE_TRANSLATIONS.put("Shell Programming", "shell-программирование");
        TOPIC_PHRASE_TRANSLATIONS.put("Packet Analysis", "анализ пакетов");
        TOPIC_PHRASE_TRANSLATIONS.put("What is JavaScript", "что такое JavaScript");
        TOPIC_PHRASE_TRANSLATIONS.put("History of JavaScript", "история JavaScript");
        TOPIC_PHRASE_TRANSLATIONS.put("JavaScript Versions", "версии JavaScript");
        TOPIC_PHRASE_TRANSLATIONS.put("How to run JavaScript", "как запускать JavaScript");
        TOPIC_PHRASE_TRANSLATIONS.put("Using Browser DevTools", "использование DevTools браузера");
        TOPIC_PHRASE_TRANSLATIONS.put("Debugging Issues", "отладка проблем");
        TOPIC_PHRASE_TRANSLATIONS.put("Debugging Memory Leaks", "отладка утечек памяти");
        TOPIC_PHRASE_TRANSLATIONS.put("Debugging Performance", "отладка производительности");
        TOPIC_PHRASE_TRANSLATIONS.put("What is Version Control?", "что такое контроль версий?");
        TOPIC_PHRASE_TRANSLATIONS.put("Why use Version Control?", "зачем использовать контроль версий?");
        TOPIC_PHRASE_TRANSLATIONS.put("Git vs Other VCS", "Git и другие VCS: сравнение");
        TOPIC_PHRASE_TRANSLATIONS.put("Local vs Global Config", "локальная и глобальная конфигурация: сравнение");
        TOPIC_PHRASE_TRANSLATIONS.put("Installing Git Locally", "локальная установка Git");
        TOPIC_PHRASE_TRANSLATIONS.put("Cloning Repositories", "клонирование репозиториев");
        TOPIC_PHRASE_TRANSLATIONS.put("Branching Basics", "основы ветвления");
        TOPIC_PHRASE_TRANSLATIONS.put("Merging Basics", "основы слияния");
        TOPIC_PHRASE_TRANSLATIONS.put("GitHub Essentials", "основы GitHub");
        TOPIC_PHRASE_TRANSLATIONS.put("Git Remotes", "удалённые репозитории Git");
        TOPIC_PHRASE_TRANSLATIONS.put("GitHub Interface", "интерфейс GitHub");
        TOPIC_PHRASE_TRANSLATIONS.put("PR from a Fork", "Pull Request из fork");
        TOPIC_PHRASE_TRANSLATIONS.put("PR Guidelines", "рекомендации по Pull Request");
        TOPIC_PHRASE_TRANSLATIONS.put("GitHub Wikis", "wiki в GitHub");
        TOPIC_PHRASE_TRANSLATIONS.put("GitHub Discussions", "обсуждения GitHub");
        TOPIC_PHRASE_TRANSLATIONS.put("GitHub Projects", "проекты GitHub");
        TOPIC_PHRASE_TRANSLATIONS.put("GitHub Organizations", "организации GitHub");
        TOPIC_PHRASE_TRANSLATIONS.put("Kanban Boards", "kanban-доски");
        TOPIC_PHRASE_TRANSLATIONS.put("Tagging", "теги");
        TOPIC_PHRASE_TRANSLATIONS.put("Managing Tags", "управление тегами");
        TOPIC_PHRASE_TRANSLATIONS.put("Pushing Tags", "отправка тегов");
        TOPIC_PHRASE_TRANSLATIONS.put("GitHub Releases", "релизы GitHub");
        TOPIC_PHRASE_TRANSLATIONS.put("Client vs Server Hooks", "клиентские и серверные hooks: сравнение");
        TOPIC_PHRASE_TRANSLATIONS.put("YAML Syntax", "синтаксис YAML");
        TOPIC_PHRASE_TRANSLATIONS.put("Workflow Triggers", "триггеры workflow");
        TOPIC_PHRASE_TRANSLATIONS.put("Workflow Runners", "runner'ы workflow");
        TOPIC_PHRASE_TRANSLATIONS.put("Workflow Context", "контекст workflow");
        TOPIC_PHRASE_TRANSLATIONS.put("Secrets and Env Vars", "секреты и переменные окружения");
        TOPIC_PHRASE_TRANSLATIONS.put("Caching Dependencies", "кэширование зависимостей");
        TOPIC_PHRASE_TRANSLATIONS.put("Storing Artifacts", "хранение артефактов");
        TOPIC_PHRASE_TRANSLATIONS.put("Workflow Status", "статус workflow");
        TOPIC_PHRASE_TRANSLATIONS.put("Deploying Static Websites", "развёртывание статических сайтов");
        TOPIC_PHRASE_TRANSLATIONS.put("Custom Domains", "пользовательские домены");
        TOPIC_PHRASE_TRANSLATIONS.put("What is Node.js?", "что такое Node.js?");
        TOPIC_PHRASE_TRANSLATIONS.put("Why use Node.js?", "зачем использовать Node.js?");
        TOPIC_PHRASE_TRANSLATIONS.put("History of Node.js", "история Node.js");
        TOPIC_PHRASE_TRANSLATIONS.put("Node.js vs Browser", "Node.js и браузер: сравнение");
        TOPIC_PHRASE_TRANSLATIONS.put("Creating & Importing", "создание и импорт");
        TOPIC_PHRASE_TRANSLATIONS.put("Running Node.js Code", "запуск кода Node.js");
        TOPIC_PHRASE_TRANSLATIONS.put("Global Installation", "глобальная установка");
        TOPIC_PHRASE_TRANSLATIONS.put("Local Installation", "локальная установка");
        TOPIC_PHRASE_TRANSLATIONS.put("Event Emitter", "генератор событий");
        TOPIC_PHRASE_TRANSLATIONS.put("User Specified Errors", "пользовательские ошибки");
        TOPIC_PHRASE_TRANSLATIONS.put("Running Scripts", "запуск скриптов");
        TOPIC_PHRASE_TRANSLATIONS.put("Assertion Errors", "ошибки assertion");
        TOPIC_PHRASE_TRANSLATIONS.put("JavaScript Errors", "ошибки JavaScript");
        TOPIC_PHRASE_TRANSLATIONS.put("Creating Packages", "создание пакетов");
        TOPIC_PHRASE_TRANSLATIONS.put("Handling Async Errors", "обработка асинхронных ошибок");
        TOPIC_PHRASE_TRANSLATIONS.put("Callstack / Stack Trace", "call stack и stack trace");
        TOPIC_PHRASE_TRANSLATIONS.put("Command Line Apps", "приложения командной строки");
        TOPIC_PHRASE_TRANSLATIONS.put("Building & Consuming APIs", "создание и использование API");
        TOPIC_PHRASE_TRANSLATIONS.put("Monitor Changes (Dev)", "отслеживание изменений в dev-режиме");
        TOPIC_PHRASE_TRANSLATIONS.put("Template Engines", "шаблонизаторы");
        TOPIC_PHRASE_TRANSLATIONS.put("Keep app Running", "поддержание приложения в работе");
        TOPIC_PHRASE_TRANSLATIONS.put("Using APM", "использование APM");
        TOPIC_PHRASE_TRANSLATIONS.put("What is C++?", "что такое C++?");
        TOPIC_PHRASE_TRANSLATIONS.put("Why use C++?", "зачем использовать C++?");
        TOPIC_PHRASE_TRANSLATIONS.put("Dynamic Typing", "динамическая типизация");
        TOPIC_PHRASE_TRANSLATIONS.put("Namespaces", "пространства имён");
        TOPIC_PHRASE_TRANSLATIONS.put("Macros", "макросы");
        TOPIC_PHRASE_TRANSLATIONS.put("Containers", "контейнеры");
        TOPIC_PHRASE_TRANSLATIONS.put("Debuggers", "отладчики");
        TOPIC_PHRASE_TRANSLATIONS.put("Standards", "стандарты");
        TOPIC_PHRASE_TRANSLATIONS.put("Compilers", "компиляторы");
        TOPIC_PHRASE_TRANSLATIONS.put("What is Rust?", "что такое Rust?");
        TOPIC_PHRASE_TRANSLATIONS.put("Why use Rust?", "зачем использовать Rust?");
        TOPIC_PHRASE_TRANSLATIONS.put("Evolution and History", "эволюция и история");
        TOPIC_PHRASE_TRANSLATIONS.put("Basic HTML Tags", "базовые HTML-теги");
        TOPIC_PHRASE_TRANSLATIONS.put("Semantic Markup", "семантическая разметка");
        TOPIC_PHRASE_TRANSLATIONS.put("Markup Languages", "языки разметки");
        TOPIC_PHRASE_TRANSLATIONS.put("Case Insensitivity", "регистронезависимость");
        TOPIC_PHRASE_TRANSLATIONS.put("Background", "фон");
        TOPIC_PHRASE_TRANSLATIONS.put("Border", "граница");
        TOPIC_PHRASE_TRANSLATIONS.put("Color", "цвет");
        TOPIC_PHRASE_TRANSLATIONS.put("Display", "display");
        TOPIC_PHRASE_TRANSLATIONS.put("Margin", "внешний отступ");
        TOPIC_PHRASE_TRANSLATIONS.put("Outline", "outline");
        TOPIC_PHRASE_TRANSLATIONS.put("Padding", "внутренний отступ");
        TOPIC_PHRASE_TRANSLATIONS.put("Position", "позиционирование");
        TOPIC_PHRASE_TRANSLATIONS.put("Transforms", "трансформации");
        TOPIC_PHRASE_TRANSLATIONS.put("Transitions", "переходы");
        TOPIC_PHRASE_TRANSLATIONS.put("Visibility", "видимость");
        TOPIC_PHRASE_TRANSLATIONS.put("SQL vs NoSQL Databases", "SQL и NoSQL базы данных: сравнение");
        TOPIC_PHRASE_TRANSLATIONS.put("SQL Keywords", "ключевые слова SQL");
        TOPIC_PHRASE_TRANSLATIONS.put("Aggregate Queries", "агрегирующие запросы");
        TOPIC_PHRASE_TRANSLATIONS.put("Subqueries", "подзапросы");
        TOPIC_PHRASE_TRANSLATIONS.put("Advanced Functions", "расширенные функции");
        TOPIC_PHRASE_TRANSLATIONS.put("Query Optimization", "оптимизация запросов");
        TOPIC_PHRASE_TRANSLATIONS.put("Advanced SQL", "продвинутый SQL");
        TOPIC_PHRASE_TRANSLATIONS.put("Editing Files", "редактирование файлов");
        TOPIC_PHRASE_TRANSLATIONS.put("Archiving and Compressing", "архивация и сжатие");
        TOPIC_PHRASE_TRANSLATIONS.put("Text Processing", "обработка текста");
        TOPIC_PHRASE_TRANSLATIONS.put("Server Review", "обзор сервера");
        TOPIC_PHRASE_TRANSLATIONS.put("Uptime and Load", "время работы и нагрузка");
        TOPIC_PHRASE_TRANSLATIONS.put("Authentication Logs", "логи аутентификации");
        TOPIC_PHRASE_TRANSLATIONS.put("Available Memory / Disk", "доступная память и диск");
        TOPIC_PHRASE_TRANSLATIONS.put("Filesystems", "файловые системы");
        TOPIC_PHRASE_TRANSLATIONS.put("Booting Linux", "загрузка Linux");
        TOPIC_PHRASE_TRANSLATIONS.put("Troubleshooting", "диагностика проблем");
        TOPIC_PHRASE_TRANSLATIONS.put("Container Runtime", "runtime контейнеров");
        TOPIC_PHRASE_TRANSLATIONS.put("Arithmetic Operators in Bash", "арифметические операторы в Bash");
        TOPIC_PHRASE_TRANSLATIONS.put("Bash Data Types", "типы данных в Bash");
        TOPIC_PHRASE_TRANSLATIONS.put("Bash Operators", "операторы Bash");
        TOPIC_PHRASE_TRANSLATIONS.put("Case Statements", "операторы case");
        TOPIC_PHRASE_TRANSLATIONS.put("Case Conversion", "смена регистра");
        TOPIC_PHRASE_TRANSLATIONS.put("Files & Directories", "файлы и директории");
        TOPIC_PHRASE_TRANSLATIONS.put("For Loops", "циклы for");
        TOPIC_PHRASE_TRANSLATIONS.put("Jobs", "задачи");
        TOPIC_PHRASE_TRANSLATIONS.put("Navigate Between Directories", "навигация между директориями");
        TOPIC_PHRASE_TRANSLATIONS.put("Pattern Replacement", "замена по шаблону");
        TOPIC_PHRASE_TRANSLATIONS.put("Popular Shells", "популярные оболочки");
        TOPIC_PHRASE_TRANSLATIONS.put("Special Variables", "специальные переменные");
        TOPIC_PHRASE_TRANSLATIONS.put("Text Editors", "текстовые редакторы");
        TOPIC_PHRASE_TRANSLATIONS.put("Hooks", "хуки");
        TOPIC_PHRASE_TRANSLATIONS.put("Modules", "модули");
        TOPIC_PHRASE_TRANSLATIONS.put("Caching Strategies", "стратегии кэширования");
        TOPIC_PHRASE_TRANSLATIONS.put("Classes", "классы");
        TOPIC_PHRASE_TRANSLATIONS.put("Class", "класс");
        TOPIC_PHRASE_TRANSLATIONS.put("Context", "контекст");
        TOPIC_PHRASE_TRANSLATIONS.put("Contribution Guidelines", "рекомендации по вкладу");
        TOPIC_PHRASE_TRANSLATIONS.put("CSS Modules", "CSS-модули");
        TOPIC_PHRASE_TRANSLATIONS.put("Directives", "директивы");
        TOPIC_PHRASE_TRANSLATIONS.put("Middleware", "middleware");
        TOPIC_PHRASE_TRANSLATIONS.put("Terminology", "терминология");
        TOPIC_PHRASE_TRANSLATIONS.put("Tests", "тесты");
        TOPIC_PHRASE_TRANSLATIONS.put("Text", "текст");
        TOPIC_PHRASE_TRANSLATIONS.put("Access Specifiers", "спецификаторы доступа");
        TOPIC_PHRASE_TRANSLATIONS.put("Aggregations", "агрегации");
        TOPIC_PHRASE_TRANSLATIONS.put("Animation", "анимация");
        TOPIC_PHRASE_TRANSLATIONS.put("Annotations", "аннотации");
        TOPIC_PHRASE_TRANSLATIONS.put("API Calls", "вызовы API");
        TOPIC_PHRASE_TRANSLATIONS.put("Autoscaling", "автомасштабирование");
        TOPIC_PHRASE_TRANSLATIONS.put("Code Style", "стиль кода");
        TOPIC_PHRASE_TRANSLATIONS.put("Configuration", "конфигурация");
        TOPIC_PHRASE_TRANSLATIONS.put("Cookies", "cookies");
        TOPIC_PHRASE_TRANSLATIONS.put("Cryptography", "криптография");
        TOPIC_PHRASE_TRANSLATIONS.put("Decorators", "декораторы");
        TOPIC_PHRASE_TRANSLATIONS.put("Event-Driven", "событийно-ориентированный подход");
        TOPIC_PHRASE_TRANSLATIONS.put("Fields", "поля");
        TOPIC_PHRASE_TRANSLATIONS.put("Filter", "фильтр");
        TOPIC_PHRASE_TRANSLATIONS.put("Filters", "фильтры");
        TOPIC_PHRASE_TRANSLATIONS.put("Grid", "сетка");
        TOPIC_PHRASE_TRANSLATIONS.put("Heap", "куча");
        TOPIC_PHRASE_TRANSLATIONS.put("Heap Sort", "сортировка кучей");
        TOPIC_PHRASE_TRANSLATIONS.put("Indexes", "индексы");
        TOPIC_PHRASE_TRANSLATIONS.put("Indexing", "индексация");
        TOPIC_PHRASE_TRANSLATIONS.put("Lazy Loading", "ленивая загрузка");
        TOPIC_PHRASE_TRANSLATIONS.put("Metadata", "метаданные");
        TOPIC_PHRASE_TRANSLATIONS.put("Metrics", "метрики");
        TOPIC_PHRASE_TRANSLATIONS.put("Modal", "модальное окно");
        TOPIC_PHRASE_TRANSLATIONS.put("Models", "модели");
        TOPIC_PHRASE_TRANSLATIONS.put("Mutex", "мьютекс");
        TOPIC_PHRASE_TRANSLATIONS.put("Ordering", "упорядочивание");
        TOPIC_PHRASE_TRANSLATIONS.put("Packages", "пакеты");
        TOPIC_PHRASE_TRANSLATIONS.put("Policies", "политики");
        TOPIC_PHRASE_TRANSLATIONS.put("Pre-Order Traversal", "прямой обход");
        TOPIC_PHRASE_TRANSLATIONS.put("In-Order Traversal", "симметричный обход");
        TOPIC_PHRASE_TRANSLATIONS.put("Post Order Traversal", "обратный обход");
        TOPIC_PHRASE_TRANSLATIONS.put("Promises", "промисы");
        TOPIC_PHRASE_TRANSLATIONS.put("Provider", "провайдер");
        TOPIC_PHRASE_TRANSLATIONS.put("Pure functions", "чистые функции");
        TOPIC_PHRASE_TRANSLATIONS.put("Push Notifications", "push-уведомления");
        TOPIC_PHRASE_TRANSLATIONS.put("Ranges", "диапазоны");
        TOPIC_PHRASE_TRANSLATIONS.put("Sessions", "сессии");
        TOPIC_PHRASE_TRANSLATIONS.put("Signals", "сигналы");
        TOPIC_PHRASE_TRANSLATIONS.put("Semantic Versioning", "семантическое версионирование");
        TOPIC_PHRASE_TRANSLATIONS.put("Server-Side Rendering (SSR)", "серверный рендеринг (SSR)");
        TOPIC_PHRASE_TRANSLATIONS.put("Custom Fields", "пользовательские поля");
        TOPIC_PHRASE_TRANSLATIONS.put("Named Routes", "именованные маршруты");
        TOPIC_PHRASE_TRANSLATIONS.put("Native Drivers", "нативные драйверы");
        TOPIC_PHRASE_TRANSLATIONS.put("Data Clustering", "кластеризация данных");
        TOPIC_PHRASE_TRANSLATIONS.put("Socket", "сокет");
        TOPIC_PHRASE_TRANSLATIONS.put("Architecture", "архитектура");
        TOPIC_PHRASE_TRANSLATIONS.put("Design", "дизайн");
        TOPIC_PHRASE_TRANSLATIONS.put("Management", "управление");
        TOPIC_PHRASE_TRANSLATIONS.put("Analysis", "анализ");
        TOPIC_PHRASE_TRANSLATIONS.put("Analytics", "аналитика");
        TOPIC_PHRASE_TRANSLATIONS.put("Testing", "тестирование");
        TOPIC_PHRASE_TRANSLATIONS.put("Monitoring", "мониторинг");
        TOPIC_PHRASE_TRANSLATIONS.put("Reporting", "отчётность");
        TOPIC_PHRASE_TRANSLATIONS.put("Planning", "планирование");
        TOPIC_PHRASE_TRANSLATIONS.put("Tools", "инструменты");
        TOPIC_PHRASE_TRANSLATIONS.put("Services", "сервисы");
        TOPIC_PHRASE_TRANSLATIONS.put("Systems", "системы");
        TOPIC_PHRASE_TRANSLATIONS.put("System", "система");
        TOPIC_PHRASE_TRANSLATIONS.put("Applications", "приложения");
        TOPIC_PHRASE_TRANSLATIONS.put("Apps", "приложения");
        TOPIC_PHRASE_TRANSLATIONS.put("Concepts", "концепции");
        TOPIC_PHRASE_TRANSLATIONS.put("Principles", "принципы");
        TOPIC_PHRASE_TRANSLATIONS.put("Techniques", "техники");
        TOPIC_PHRASE_TRANSLATIONS.put("Methods", "методы");
        TOPIC_PHRASE_TRANSLATIONS.put("Knowledge", "знания");
        TOPIC_PHRASE_TRANSLATIONS.put("Basics", "основы");
        TOPIC_PHRASE_TRANSLATIONS.put("Fundamentals", "основы");
        TOPIC_PHRASE_TRANSLATIONS.put("Overview", "обзор");
        TOPIC_PHRASE_TRANSLATIONS.put("Introduction", "введение");
        TOPIC_PHRASE_TRANSLATIONS.put("Advanced", "продвинутый уровень");

        TITLE_SEGMENT_OVERRIDES.put("Introduction to ", "\u0412\u0432\u0435\u0434\u0435\u043d\u0438\u0435 \u0432 ");
        TITLE_SEGMENT_OVERRIDES.put("Getting Started with ", "\u041d\u0430\u0447\u0430\u043b\u043e \u0440\u0430\u0431\u043e\u0442\u044b \u0441 ");
        TITLE_SEGMENT_OVERRIDES.put("Working with ", "\u0420\u0430\u0431\u043e\u0442\u0430 \u0441 ");
        TITLE_SEGMENT_OVERRIDES.put("Basics of ", "\u041e\u0441\u043d\u043e\u0432\u044b ");
        TITLE_SEGMENT_OVERRIDES.put("Fundamentals of ", "\u041e\u0441\u043d\u043e\u0432\u044b ");
        TITLE_SEGMENT_OVERRIDES.put("Overview of ", "\u041e\u0431\u0437\u043e\u0440 ");
        TITLE_SEGMENT_OVERRIDES.put("Advanced ", "\u041f\u0440\u043e\u0434\u0432\u0438\u043d\u0443\u0442\u044b\u0439 ");
        TITLE_SEGMENT_OVERRIDES.put("Best Practices", "\u041b\u0443\u0447\u0448\u0438\u0435 \u043f\u0440\u0430\u043a\u0442\u0438\u043a\u0438");
        TITLE_SEGMENT_OVERRIDES.put("Fundamentals", "Основы");
        TITLE_SEGMENT_OVERRIDES.put("Basics", "Основы");
        TITLE_SEGMENT_OVERRIDES.put("Best Practices", "Лучшие практики");
        TITLE_SEGMENT_OVERRIDES.put(" and ", " \u0438 ");
        TITLE_SEGMENT_OVERRIDES.put(" & ", " и ");
        TITLE_SEGMENT_OVERRIDES.put(" vs. ", " и ");
        TITLE_SEGMENT_OVERRIDES.put(" vs ", " и ");
    }

    private KnowledgeBaseLocalizationUtil() {
    }

    public static String localizeRoleName(String roleCode, String roleName) {
        if (isBlank(roleName)) {
            return roleName;
        }

        String byCode = ROLE_CODE_OVERRIDES.get(roleCode);
        if (byCode != null) {
            return byCode;
        }

        String normalized = roleName.trim();
        if (normalized.endsWith(" Developer")) {
            return normalized.substring(0, normalized.length() - " Developer".length()) + "-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a";
        }
        if (normalized.endsWith(" Engineer")) {
            return normalized.substring(0, normalized.length() - " Engineer".length()) + "-\u0438\u043d\u0436\u0435\u043d\u0435\u0440";
        }
        if (normalized.endsWith(" Analyst")) {
            return normalized.substring(0, normalized.length() - " Analyst".length()) + "-\u0430\u043d\u0430\u043b\u0438\u0442\u0438\u043a";
        }
        if (normalized.endsWith(" Architect")) {
            return normalized.substring(0, normalized.length() - " Architect".length()) + "-\u0430\u0440\u0445\u0438\u0442\u0435\u043a\u0442\u043e\u0440";
        }
        if (normalized.endsWith(" Designer")) {
            return normalized.substring(0, normalized.length() - " Designer".length()) + "-\u0434\u0438\u0437\u0430\u0439\u043d\u0435\u0440";
        }
        if (normalized.endsWith(" Beginner")) {
            return normalized.substring(0, normalized.length() - " Beginner".length()) + " \u0434\u043b\u044f \u043d\u0430\u0447\u0438\u043d\u0430\u044e\u0449\u0438\u0445";
        }
        if (normalized.endsWith(" Roadmap")) {
            return normalized.substring(0, normalized.length() - " Roadmap".length()).trim();
        }

        return normalized;
    }

    public static String localizeTopicTitle(String topicCode, String topicTitle) {
        if (isBlank(topicTitle)) {
            return topicTitle;
        }

        String normalized = topicTitle.trim();
        String contextualKeyword = localizeContextualKeywordTopic(topicCode, normalized);
        if (contextualKeyword != null) {
            return contextualKeyword;
        }

        String roleTopic = ROLE_TOPIC_TITLE_OVERRIDES.get(normalized);
        if (roleTopic != null) {
            return roleTopic;
        }

        String exact = EXACT_TOPIC_OVERRIDES.get(normalized);
        if (exact != null) {
            return exact;
        }

        String question = localizeQuestionTopicTitle(normalized);
        if (question != null) {
            return cleanup(question);
        }

        String phraseLocalized = localizeTopicByPhrases(normalized);
        if (phraseLocalized != null) {
            return cleanup(phraseLocalized);
        }

        String dynamic = localizeDynamicTopicTitle(normalized);
        if (dynamic != null) {
            return cleanup(dynamic);
        }

        String localized = normalized;
        for (Map.Entry<String, String> entry : TITLE_SEGMENT_OVERRIDES.entrySet()) {
            localized = localized.replace(entry.getKey(), entry.getValue());
        }

        return cleanup(localized);
    }

    private static String localizeContextualKeywordTopic(String topicCode, String title) {
        String code = topicCode == null ? "" : topicCode.toUpperCase();

        if (code.contains("JAVASCRIPT") || code.contains("_JS_") || code.startsWith("RM_JS")) {
            return switch (title) {
                case "var" -> "Объявление переменных через var";
                case "let" -> "Блочные переменные через let";
                case "const" -> "Объявление констант через const";
                case "==" -> "Нестрогое сравнение ==";
                case "===" -> "Строгое сравнение ===";
                case "call" -> "Метод call()";
                case "apply" -> "Метод apply()";
                case "bind" -> "Метод bind()";
                case "bigint" -> "Тип данных BigInt";
                case "Symbol" -> "Тип данных Symbol";
                case "Block" -> "Блочная область видимости";
                case "DOM APIs" -> "DOM API для работы со страницей";
                case "ESM" -> "Модули ES Modules";
                case "CommonJS" -> "Модули CommonJS";
                case "All about Variables" -> "Переменные в JavaScript";
                case "Weak Map" -> "WeakMap: слабые ссылки для ключей";
                case "Weak Set" -> "WeakSet: слабое множество объектов";
                default -> null;
            };
        }

        if (code.contains("TYPESCRIPT")) {
            return switch (title) {
                case "any" -> "Тип any";
                case "unknown" -> "Тип unknown";
                case "void" -> "Тип void";
                case "typeof" -> "Оператор typeof";
                case "as const" -> "Const assertion через as const";
                case "as any" -> "Приведение типа через as any";
                case "Awaited" -> "Utility type Awaited";
                case "Exclude" -> "Utility type Exclude";
                case "Extract" -> "Utility type Extract";
                case "Tuple" -> "Кортежи Tuple";
                case "Enum" -> "Перечисления enum";
                case "Equality" -> "Сравнение значений";
                default -> null;
            };
        }

        if (code.contains("ANGULAR")) {
            return switch (title) {
                case "@if" -> "Блок условия @if";
                case "@else" -> "Блок альтернативы @else";
                case "@else if" -> "Блок альтернативного условия @else if";
                case "@for" -> "Блок цикла @for";
                case "@switch" -> "Блок выбора @switch";
                case "@case" -> "Ветка @case";
                case "@default" -> "Ветка по умолчанию @default";
                case "@let" -> "Локальная переменная @let";
                case "@defer" -> "Отложенная загрузка @defer";
                case "@Input & @Output" -> "Входные и выходные параметры компонентов";
                case "AoT Compilation" -> "Ahead-of-Time компиляция";
                case "SSR" -> "Серверный рендеринг (SSR)";
                default -> null;
            };
        }

        if (code.contains("VUE")) {
            return switch (title) {
                case "v-if" -> "Условный рендеринг v-if";
                case "v-else" -> "Альтернативный рендеринг v-else";
                case "v-for" -> "Рендеринг списков v-for";
                case "v-model" -> "Двусторонняя привязка v-model";
                case "v-bind" -> "Привязка атрибутов v-bind";
                case "v-on" -> "Обработка событий v-on";
                case "v-show" -> "Показ и скрытие через v-show";
                case "v-slot" -> "Слоты через v-slot";
                case "v-text" -> "Текстовое содержимое через v-text";
                case "v-html" -> "Вставка HTML через v-html";
                case "v-cloak" -> "Скрытие до компиляции через v-cloak";
                case "v-pre" -> "Пропуск компиляции через v-pre";
                case "v-once" -> "Однократный рендеринг через v-once";
                case "Slots" -> "Слоты компонентов";
                case "Watchers" -> "Наблюдатели Watchers";
                default -> null;
            };
        }

        if (code.contains("SWIFT") || code.contains("IOS")) {
            return switch (title) {
                case "@State" -> "Состояние SwiftUI @State";
                case "@Binding" -> "Связь состояния @Binding";
                case "@StateObject" -> "Объект состояния @StateObject";
                case "@ObservedObject" -> "Наблюдаемый объект @ObservedObject";
                case "@EnvironmentObject" -> "Объект окружения @EnvironmentObject";
                case "VStack" -> "Вертикальный стек VStack";
                case "ZStack" -> "Слоевой стек ZStack";
                case "TabView" -> "Контейнер вкладок TabView";
                case "UIKit" -> "UIKit";
                case "SwiftUI" -> "SwiftUI";
                case "Xcode" -> "Xcode";
                case "XCTest" -> "XCTest";
                default -> null;
            };
        }

        if (code.contains("MONGODB")) {
            if (title.startsWith("$")) {
                return "Оператор MongoDB " + title;
            }
            return switch (title) {
                case "Aggregation" -> "Агрегация в MongoDB";
                case "Cursors" -> "Курсоры MongoDB";
                case "Compound" -> "Составные индексы";
                case "Expiring" -> "TTL-индексы";
                default -> null;
            };
        }

        if (code.contains("REDIS")) {
            if (title.matches("[A-Z][A-Z0-9_-]{1,}")) {
                return "Команда Redis " + title;
            }
            return switch (title) {
                case "Sets" -> "Множества Redis";
                case "Bitmaps" -> "Bitmap-структуры Redis";
                case "Usecases" -> "Сценарии использования Redis";
                case "and Getting Keys" -> "Получение ключей";
                default -> null;
            };
        }

        if (code.contains("SQL") || code.contains("POSTGRESQL")) {
            if (title.matches("[A-Z][A-Z0-9_]{1,}")) {
                return "SQL-конструкция " + title;
            }
            return switch (title) {
                case "Table" -> "Таблицы";
                case "Row" -> "Строки таблицы";
                case "Rows" -> "Строки таблицы";
                case "Columns" -> "Столбцы таблицы";
                case "Schemas" -> "Схемы базы данных";
                case "Scalar" -> "Скалярные значения";
                case "Unique" -> "Уникальные ограничения";
                default -> null;
            };
        }

        if (code.contains("PHP")) {
            if (title.startsWith("$_")) {
                return "Суперглобальный массив PHP " + title;
            }
            return switch (title) {
                case "$this" -> "Текущий объект через $this";
                case "echo" -> "Вывод через echo";
                case "print" -> "Вывод через print";
                case "var_dump" -> "Отладочный вывод var_dump()";
                case "require" -> "Подключение файлов через require";
                case "include" -> "Подключение файлов через include";
                case "foreach" -> "Цикл foreach";
                case "while" -> "Цикл while";
                case "for" -> "Цикл for";
                case "switch" -> "Оператор switch";
                case "Traits" -> "Трейты";
                case "Sessions" -> "Сессии";
                default -> null;
            };
        }

        if (code.contains("SHELL") || code.contains("BASH") || code.contains("LINUX")) {
            return switch (title) {
                case "ls" -> "Просмотр файлов через ls";
                case "pwd" -> "Текущая директория через pwd";
                case "cat" -> "Команда cat";
                case "cd" -> "Команда cd";
                case "cp" -> "Копирование через cp";
                case "mv" -> "Перемещение через mv";
                case "rm" -> "Удаление через rm";
                case "mkdir" -> "Создание директорий через mkdir";
                case "rmdir" -> "Удаление директорий через rmdir";
                case "ln" -> "Ссылки через ln";
                case "chmod" -> "Права доступа через chmod";
                case "chown" -> "Смена владельца через chown";
                case "chgrp" -> "Смена группы через chgrp";
                case "awk" -> "Обработка текста через awk";
                case "sed" -> "Потоковое редактирование через sed";
                case "grep" -> "Поиск текста через grep";
                case "find" -> "Поиск файлов через find";
                case "sort" -> "Сортировка строк через sort";
                case "uniq" -> "Удаление дублей через uniq";
                case "cut" -> "Извлечение колонок через cut";
                case "tr" -> "Замена символов через tr";
                case "tee" -> "Запись и вывод через tee";
                case "wc" -> "Подсчёт строк и слов через wc";
                case "head" -> "Начало файла через head";
                case "tail" -> "Конец файла через tail";
                case "less" -> "Просмотр файла через less";
                case "more" -> "Постраничный просмотр через more";
                case "curl" -> "HTTP-запросы через curl";
                case "wget" -> "Скачивание через wget";
                case "tar" -> "Архивация через tar";
                case "touch" -> "Создание файлов через touch";
                case "rsync" -> "Синхронизация через rsync";
                case "scp" -> "Копирование по SSH через scp";
                case "ps" -> "Процессы через ps";
                case "top" -> "Мониторинг процессов через top";
                case "kill" -> "Завершение процессов через kill";
                case "df" -> "Свободное место через df";
                case "du" -> "Размер файлов через du";
                case "set -e" -> "Режим Bash set -e";
                case "set -o" -> "Параметры Bash set -o";
                case "set -u" -> "Режим Bash set -u";
                case "set -x" -> "Трассировка Bash set -x";
                case "Bash -n" -> "Проверка синтаксиса Bash";
                case "Vi" -> "Редактор vi";
                case "Vim" -> "Редактор Vim";
                default -> null;
            };
        }

        if (code.contains("KOTLIN")) {
            return switch (title) {
                case "while" -> "Цикл while";
                case "when" -> "Выражение when";
                case "return" -> "Возврат значения через return";
                case "varargs" -> "Переменное число аргументов vararg";
                case "Booleans" -> "Логический тип Boolean";
                case "Floats" -> "Числа с плавающей точкой Float";
                default -> null;
            };
        }

        if (code.contains("GOLANG")) {
            return switch (title) {
                case "break" -> "Оператор break";
                case "continue" -> "Оператор continue";
                case "Strings" -> "Строки в Go";
                case "Slices" -> "Срезы slices";
                case "Runes" -> "Руны rune";
                case "Channels" -> "Каналы channels";
                case "time" -> "Пакет time";
                case "regexp" -> "Регулярные выражения regexp";
                default -> null;
            };
        }

        if (code.contains("CLAUDE_CODE")) {
            if (title.startsWith("/")) {
                return "Команда Claude Code " + title;
            }
            return switch (title) {
                case "Ctrl+C" -> "Горячая клавиша Ctrl+C";
                case "Ctrl+R" -> "Горячая клавиша Ctrl+R";
                default -> null;
            };
        }

        return switch (title) {
            case "3 Trees" -> "3-деревья";
            case "...other" -> "Другие элементы";
            case "[global] keyword" -> "Глобальное ключевое слово";
            case ".gitignore" -> "Файл .gitignore";
            case "--soft" -> "Мягкий reset через --soft";
            case "--watch" -> "Режим наблюдения --watch";
            default -> null;
        };
    }

    private static String localizeQuestionTopicTitle(String title) {
        if (title.matches("What is an? .+\\?")) {
            String subject = title
                    .replaceFirst("^What is an? ", "")
                    .replaceFirst("\\?$", "");
            return "Что такое " + lowerCaseFirstForSentence(localizeTopicTitle(null, subject)) + "?";
        }
        if (title.matches("What is .+\\?")) {
            String subject = title
                    .replaceFirst("^What is ", "")
                    .replaceFirst("\\?$", "");
            return "Что такое " + lowerCaseFirstForSentence(localizeTopicTitle(null, subject)) + "?";
        }
        if (title.matches("What are .+\\?")) {
            String subject = title
                    .replaceFirst("^What are ", "")
                    .replaceFirst("\\?$", "");
            return "Что такое " + lowerCaseFirstForSentence(localizeTopicTitle(null, subject)) + "?";
        }
        if (title.matches("Why use .+\\?")) {
            String subject = title
                    .replaceFirst("^Why use ", "")
                    .replaceFirst("\\?$", "");
            return "Зачем использовать " + localizeTopicTitle(null, subject) + "?";
        }
        if (title.matches("Why .+\\?")) {
            String subject = title
                    .replaceFirst("^Why ", "")
                    .replaceFirst("\\?$", "");
            return "Почему " + lowerCaseFirstForSentence(localizeTopicTitle(null, subject)) + "?";
        }
        return null;
    }

    private static String localizeTopicByPhrases(String title) {
        String localized = title;
        for (Map.Entry<String, String> entry : TOPIC_PHRASE_TRANSLATIONS.entrySet().stream()
                .sorted((left, right) -> Integer.compare(right.getKey().length(), left.getKey().length()))
                .toList()) {
            localized = localized.replaceAll(
                    "(?i)(?<![A-Za-z])" + java.util.regex.Pattern.quote(entry.getKey()) + "(?![A-Za-z])",
                    java.util.regex.Matcher.quoteReplacement(entry.getValue())
            );
        }

        if (!localized.equals(title) && localized.matches(".*[А-Яа-яЁё].*")) {
            return capitalizeFirst(localized);
        }
        return null;
    }

    private static String localizeDynamicTopicTitle(String title) {
        if (title.matches("What is an? .+\\?")) {
            String subject = title
                    .replaceFirst("^What is an? ", "")
                    .replaceFirst("\\?$", "");
            return "Что такое " + subject + "?";
        }
        if (title.matches("What are .+\\?")) {
            String subject = title
                    .replaceFirst("^What are ", "")
                    .replaceFirst("\\?$", "");
            return "Что такое " + subject + "?";
        }
        if (title.startsWith("2. ")) {
            return "2. " + localizeTopicTitle(null, title.substring(3));
        }
        if (title.startsWith("3. ")) {
            return "3. " + localizeTopicTitle(null, title.substring(3));
        }
        if (title.startsWith("Learn ")) {
            return "Изучение " + title.substring("Learn ".length());
        }
        if (title.endsWith(" Basics")) {
            return "Основы " + title.substring(0, title.length() - " Basics".length());
        }
        if (title.endsWith(" Fundamentals")) {
            return "Основы " + title.substring(0, title.length() - " Fundamentals".length());
        }
        if (title.endsWith(" Best Practices")) {
            return "Лучшие практики: " + title.substring(0, title.length() - " Best Practices".length());
        }
        if (title.contains(" vs ")) {
            return title.replace(" vs ", " и ") + ": сравнение";
        }
        if (title.contains(" vs. ")) {
            return title.replace(" vs. ", " и ") + ": сравнение";
        }
        if (title.startsWith("Types of ")) {
            return "Типы: " + title.substring("Types of ".length());
        }
        if (title.startsWith("Introduction")) {
            return "Введение" + title.substring("Introduction".length());
        }
        String prefixed = localizeKnownPrefix(title);
        if (prefixed != null) {
            return prefixed;
        }
        String suffixed = localizeKnownSuffix(title);
        if (suffixed != null) {
            return suffixed;
        }

        return null;
    }

    private static String localizeKnownPrefix(String title) {
        String[][] prefixes = {
                {"Data ", "Данные"},
                {"Cloud ", "Облако"},
                {"User ", "Пользователь"},
                {"Team ", "Команда"},
                {"Product ", "Продукт"},
                {"Content ", "Контент"},
                {"Business ", "Бизнес"},
                {"Market ", "Рынок"},
                {"Security ", "Безопасность"},
                {"Technical ", "Техническая часть"},
                {"Programming ", "Программирование"},
                {"Database ", "База данных"},
                {"Server ", "Сервер"},
                {"Client ", "Клиент"},
                {"Model ", "Модель"},
                {"Feature ", "Функциональность"},
                {"System ", "Система"},
                {"Project ", "Проект"},
                {"Risk ", "Риск"},
                {"Incident ", "Инцидент"},
                {"Identity ", "Идентификация"},
                {"Access ", "Доступ"},
                {"Code ", "Код"},
                {"Release ", "Релиз"}
        };

        for (String[] prefix : prefixes) {
            if (title.startsWith(prefix[0]) && title.length() > prefix[0].length()) {
                return prefix[1] + ": " + localizeTopicTitle(null, title.substring(prefix[0].length()));
            }
        }
        return null;
    }

    private static String localizeKnownSuffix(String title) {
        String[][] suffixes = {
                {" Testing", "Тестирование"},
                {" Management", "Управление"},
                {" Analysis", "Анализ"},
                {" Analytics", "Аналитика"},
                {" Design", "Дизайн"},
                {" Architecture", "Архитектура"},
                {" Development", "Разработка"},
                {" Security", "Безопасность"},
                {" Monitoring", "Мониторинг"},
                {" Operations", "Операции"},
                {" Skills", "Навыки"},
                {" Responsibilities", "Обязанности"},
                {" Tools", "Инструменты"},
                {" Services", "Сервисы"},
                {" Systems", "Системы"},
                {" System", "Система"},
                {" Apps", "Приложения"},
                {" Applications", "Приложения"},
                {" Model", "Модель"},
                {" Models", "Модели"},
                {" Pattern", "Паттерн"},
                {" Patterns", "Паттерны"},
                {" Strategy", "Стратегия"},
                {" Strategies", "Стратегии"},
                {" Techniques", "Техники"},
                {" Concepts", "Концепции"},
                {" Principles", "Принципы"},
                {" Practices", "Практики"},
                {" Methods", "Методы"},
                {" Planning", "Планирование"},
                {" Reporting", "Отчётность"},
                {" Documentation", "Документация"},
                {" Communication", "Коммуникация"},
                {" Lifecycle", "Жизненный цикл"},
                {" Process", "Процесс"},
                {" Processes", "Процессы"},
                {" Policy", "Политика"},
                {" Policies", "Политики"},
                {" Database", "База данных"},
                {" Databases", "Базы данных"},
                {" Storage", "Хранилище"},
                {" Scaling", "Масштабирование"},
                {" Replication", "Репликация"},
                {" Migration", "Миграция"},
                {" Search", "Поиск"},
                {" Queues", "Очереди"},
                {" Events", "События"}
        };

        for (String[] suffix : suffixes) {
            if (title.endsWith(suffix[0]) && title.length() > suffix[0].length()) {
                return suffix[1] + ": " + localizeTopicTitle(null, title.substring(0, title.length() - suffix[0].length()));
            }
        }
        return null;
    }

    private static String lowerCaseFirstForSentence(String value) {
        if (isBlank(value)) {
            return value;
        }
        char first = value.charAt(0);
        if (Character.UnicodeScript.of(first) == Character.UnicodeScript.CYRILLIC) {
            return Character.toLowerCase(first) + value.substring(1);
        }
        return value;
    }

    public static String roadmapCategory(String roleCode) {
        return ROLE_BASED_ROADMAP_CODES.contains(roleCode)
                ? ROLE_BASED_CATEGORY
                : SKILL_BASED_CATEGORY;
    }

    public static String roadmapCategoryLabel(String roleCode) {
        return ROLE_BASED_ROADMAP_CODES.contains(roleCode)
                ? "\u041d\u0430\u043f\u0440\u0430\u0432\u043b\u0435\u043d\u0438\u044f \u043f\u043e \u0440\u043e\u043b\u044f\u043c"
                : "\u041d\u0430\u043f\u0440\u0430\u0432\u043b\u0435\u043d\u0438\u044f \u043f\u043e \u043d\u0430\u0432\u044b\u043a\u0430\u043c";
    }

    public static boolean isMvpRoadmap(String roleCode) {
        return MVP_ROLE_BASED_ROADMAP_CODES.contains(roleCode)
                || MVP_SKILL_BASED_ROADMAP_CODES.contains(roleCode);
    }

    public static String localizeDescription(String description,
                                             String originalRoleName,
                                             String localizedRoleName,
                                             String originalTopicTitle,
                                             String localizedTopicTitle) {
        if (isBlank(description)) {
            return description;
        }

        String localized = description
                .replace("\u0438\u0437 \u043a\u0430\u0442\u0430\u043b\u043e\u0433\u0430 roadmap.sh.", "")
                .replace("\u0418\u0437 \u043a\u0430\u0442\u0430\u043b\u043e\u0433\u0430 roadmap.sh.", "")
                .replace("\u0438\u0437 \u043a\u0430\u0442\u0430\u043b\u043e\u0433\u0430 roadmap.sh", "")
                .replace("\u0418\u0437 \u043a\u0430\u0442\u0430\u043b\u043e\u0433\u0430 roadmap.sh", "")
                .replace("Added from roadmap.sh.", "")
                .replace("Added from roadmap.sh", "")
                .replace("\u0414\u043e\u0431\u0430\u0432\u043b\u0435\u043d\u043e \u0438\u0437 roadmap.sh.", "")
                .replace("\u0414\u043e\u0431\u0430\u0432\u043b\u0435\u043d\u043e \u0438\u0437 roadmap.sh", "")
                .replace("\u0434\u043e\u0431\u0430\u0432\u043b\u0435\u043d\u043e \u0438\u0437 roadmap.sh.", "")
                .replace("\u0434\u043e\u0431\u0430\u0432\u043b\u0435\u043d\u043e \u0438\u0437 roadmap.sh", "")
                .replace("\u0422\u0435\u043c\u044b \u0438 \u043c\u0430\u0442\u0435\u0440\u0438\u0430\u043b\u044b \u0430\u0434\u0430\u043f\u0442\u0438\u0440\u043e\u0432\u0430\u043d\u044b \u0434\u043b\u044f \u0440\u0443\u0441\u0441\u043a\u043e\u044f\u0437\u044b\u0447\u043d\u043e\u0433\u043e \u0438\u043d\u0442\u0435\u0440\u0444\u0435\u0439\u0441\u0430 \u0441\u0435\u0440\u0432\u0438\u0441\u0430.", "")
                .replace("\u0422\u0435\u043c\u044b \u0438 \u043c\u0430\u0442\u0435\u0440\u0438\u0430\u043b\u044b \u0430\u0434\u0430\u043f\u0442\u0438\u0440\u043e\u0432\u0430\u043d\u044b \u0434\u043b\u044f \u0440\u0443\u0441\u0441\u043a\u043e\u044f\u0437\u044b\u0447\u043d\u043e\u0433\u043e \u0438\u043d\u0442\u0435\u0440\u0444\u0435\u0439\u0441\u0430 \u0441\u0435\u0440\u0432\u0438\u0441\u0430", "")
                .replace("Roadmap for ", "\u041d\u0430\u043f\u0440\u0430\u0432\u043b\u0435\u043d\u0438\u0435 ")
                .replace("Topic from the roadmap for ", "\u0422\u0435\u043c\u0430 \u043d\u0430\u043f\u0440\u0430\u0432\u043b\u0435\u043d\u0438\u044f ")
                .replace("Subtopic from the roadmap for ", "\u041f\u043e\u0434\u0442\u0435\u043c\u0430 \u043d\u0430\u043f\u0440\u0430\u0432\u043b\u0435\u043d\u0438\u044f ")
                .replace("\u0422\u0435\u043c\u0430 roadmap \u043f\u043e \u043d\u0430\u043f\u0440\u0430\u0432\u043b\u0435\u043d\u0438\u044e", "\u0422\u0435\u043c\u0430 \u043d\u0430\u043f\u0440\u0430\u0432\u043b\u0435\u043d\u0438\u044f")
                .replace("\u041f\u043e\u0434\u0442\u0435\u043c\u0430 roadmap \u043f\u043e \u043d\u0430\u043f\u0440\u0430\u0432\u043b\u0435\u043d\u0438\u044e", "\u041f\u043e\u0434\u0442\u0435\u043c\u0430 \u043d\u0430\u043f\u0440\u0430\u0432\u043b\u0435\u043d\u0438\u044f")
                .replace("\u0422\u0435\u043c\u0430 roadmap", "\u0422\u0435\u043c\u0430")
                .replace("\u041f\u043e\u0434\u0442\u0435\u043c\u0430 roadmap", "\u041f\u043e\u0434\u0442\u0435\u043c\u0430");

        if (!isBlank(originalRoleName) && !isBlank(localizedRoleName)) {
            localized = localized.replace(originalRoleName, localizedRoleName);
        }

        if (!isBlank(originalTopicTitle) && !isBlank(localizedTopicTitle)) {
            localized = localized.replace(originalTopicTitle, localizedTopicTitle);
        }

        return cleanup(localized);
    }

    public static String hideSourceProvider(String provider) {
        if (isBlank(provider)) {
            return provider;
        }
        return "roadmap.sh".equalsIgnoreCase(provider.trim()) ? null : provider;
    }

    private static String capitalizeFirst(String value) {
        if (isBlank(value)) {
            return value;
        }
        return value.substring(0, 1).toUpperCase() + value.substring(1);
    }

    private static String cleanup(String value) {
        if (value == null) {
            return null;
        }

        String cleaned = value
                .replaceAll("\\s{2,}", " ")
                .replace(" .", ".")
                .replace(" ,", ",")
                .replace(" :", ":")
                .replace("\u00ab ", "\u00ab")
                .replace(" \u00bb", "\u00bb")
                .trim();

        if (cleaned.endsWith(":")) {
            cleaned = cleaned.substring(0, cleaned.length() - 1).trim();
        }

        return cleaned;
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
