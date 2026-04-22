package com.example.adaprivelearningnavigator.service.support;

import java.util.LinkedHashMap;
import java.util.Map;

public final class KnowledgeBaseLocalizationUtil {

    private static final Map<String, String> ROLE_CODE_OVERRIDES = Map.ofEntries(
            Map.entry("android", "Android-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("angular", "Angular-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("api-design", "\u041f\u0440\u043e\u0435\u043a\u0442\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u0435 API"),
            Map.entry("aspnet-core", "ASP.NET Core-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("backend", "Backend-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("backend-beginner", "Backend \u0434\u043b\u044f \u043d\u0430\u0447\u0438\u043d\u0430\u044e\u0449\u0438\u0445"),
            Map.entry("bi-analyst", "BI-\u0430\u043d\u0430\u043b\u0438\u0442\u0438\u043a"),
            Map.entry("blockchain", "Blockchain-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("computer-science", "\u0418\u043d\u0444\u043e\u0440\u043c\u0430\u0442\u0438\u043a\u0430"),
            Map.entry("cpp", "C++-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("cyber-security", "\u0421\u043f\u0435\u0446\u0438\u0430\u043b\u0438\u0441\u0442 \u043f\u043e \u043a\u0438\u0431\u0435\u0440\u0431\u0435\u0437\u043e\u043f\u0430\u0441\u043d\u043e\u0441\u0442\u0438"),
            Map.entry("data-analyst", "\u0410\u043d\u0430\u043b\u0438\u0442\u0438\u043a \u0434\u0430\u043d\u043d\u044b\u0445"),
            Map.entry("data-engineer", "\u0418\u043d\u0436\u0435\u043d\u0435\u0440 \u0434\u0430\u043d\u043d\u044b\u0445"),
            Map.entry("datastructures-and-algorithms", "\u0421\u0442\u0440\u0443\u043a\u0442\u0443\u0440\u044b \u0434\u0430\u043d\u043d\u044b\u0445 \u0438 \u0430\u043b\u0433\u043e\u0440\u0438\u0442\u043c\u044b"),
            Map.entry("design-system", "\u0414\u0438\u0437\u0430\u0439\u043d-\u0441\u0438\u0441\u0442\u0435\u043c\u044b"),
            Map.entry("devops", "DevOps"),
            Map.entry("devops-beginner", "DevOps \u0434\u043b\u044f \u043d\u0430\u0447\u0438\u043d\u0430\u044e\u0449\u0438\u0445"),
            Map.entry("engineering-manager", "\u0418\u043d\u0436\u0435\u043d\u0435\u0440\u043d\u044b\u0439 \u043c\u0435\u043d\u0435\u0434\u0436\u0435\u0440"),
            Map.entry("flutter", "Flutter-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("frontend", "Frontend-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("frontend-beginner", "Frontend \u0434\u043b\u044f \u043d\u0430\u0447\u0438\u043d\u0430\u044e\u0449\u0438\u0445"),
            Map.entry("full-stack", "Full Stack-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("game-developer", "\u0420\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a \u0438\u0433\u0440"),
            Map.entry("git-github", "Git \u0438 GitHub"),
            Map.entry("git-github-beginner", "Git \u0438 GitHub \u0434\u043b\u044f \u043d\u0430\u0447\u0438\u043d\u0430\u044e\u0449\u0438\u0445"),
            Map.entry("golang", "Go-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("ios", "iOS-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("java", "Java-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("java-backend", "Java Backend-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("java-mobile", "Java Mobile-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("javascript", "JavaScript"),
            Map.entry("leetcode", "LeetCode"),
            Map.entry("linux", "Linux"),
            Map.entry("machine-learning", "\u041c\u0430\u0448\u0438\u043d\u043d\u043e\u0435 \u043e\u0431\u0443\u0447\u0435\u043d\u0438\u0435"),
            Map.entry("mlops", "MLOps"),
            Map.entry("nodejs", "Node.js-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("php", "PHP-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("product-manager", "\u041f\u0440\u043e\u0434\u0443\u043a\u0442\u043e\u0432\u044b\u0439 \u043c\u0435\u043d\u0435\u0434\u0436\u0435\u0440"),
            Map.entry("prompt-engineering", "\u041f\u0440\u043e\u043c\u043f\u0442-\u0438\u043d\u0436\u0438\u043d\u0438\u0440\u0438\u043d\u0433"),
            Map.entry("python", "Python-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("qa", "QA-\u0438\u043d\u0436\u0435\u043d\u0435\u0440"),
            Map.entry("react", "React-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("react-native", "React Native-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("ruby-on-rails", "Ruby on Rails-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("rust", "Rust-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("server-side-game-developer", "\u0421\u0435\u0440\u0432\u0435\u0440\u043d\u044b\u0439 \u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a \u0438\u0433\u0440"),
            Map.entry("shell-bash", "Bash"),
            Map.entry("software-architect", "\u0410\u0440\u0445\u0438\u0442\u0435\u043a\u0442\u043e\u0440 \u041f\u041e"),
            Map.entry("software-design-architecture", "\u041f\u0440\u043e\u0435\u043a\u0442\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u0435 \u0438 \u0430\u0440\u0445\u0438\u0442\u0435\u043a\u0442\u0443\u0440\u0430 \u041f\u041e"),
            Map.entry("spring-boot", "Spring Boot-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a"),
            Map.entry("swift-ui", "SwiftUI"),
            Map.entry("system-design", "System Design"),
            Map.entry("technical-writer", "\u0422\u0435\u0445\u043d\u0438\u0447\u0435\u0441\u043a\u0438\u0439 \u043f\u0438\u0441\u0430\u0442\u0435\u043b\u044c"),
            Map.entry("ux-design", "UX-\u0434\u0438\u0437\u0430\u0439\u043d"),
            Map.entry("vue", "Vue-\u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a")
    );

    private static final Map<String, String> EXACT_TOPIC_OVERRIDES = Map.ofEntries(
            Map.entry("Pick a Language", "\u0412\u044b\u0431\u043e\u0440 \u044f\u0437\u044b\u043a\u0430"),
            Map.entry("Basics of OOP", "\u041e\u0441\u043d\u043e\u0432\u044b \u041e\u041e\u041f"),
            Map.entry("The Fundamentals", "\u041e\u0441\u043d\u043e\u0432\u044b"),
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
            Map.entry("State Management", "\u0423\u043f\u0440\u0430\u0432\u043b\u0435\u043d\u0438\u0435 \u0441\u043e\u0441\u0442\u043e\u044f\u043d\u0438\u0435\u043c"),
            Map.entry("Linters and Formatters", "\u041b\u0438\u043d\u0442\u0435\u0440\u044b \u0438 \u0444\u043e\u0440\u043c\u0430\u0442\u0442\u0435\u0440\u044b"),
            Map.entry("Authentication", "\u0410\u0443\u0442\u0435\u043d\u0442\u0438\u0444\u0438\u043a\u0430\u0446\u0438\u044f"),
            Map.entry("Authorization", "\u0410\u0432\u0442\u043e\u0440\u0438\u0437\u0430\u0446\u0438\u044f"),
            Map.entry("Networking", "\u0421\u0435\u0442\u0435\u0432\u043e\u0435 \u0432\u0437\u0430\u0438\u043c\u043e\u0434\u0435\u0439\u0441\u0442\u0432\u0438\u0435"),
            Map.entry("Storage", "\u0425\u0440\u0430\u043d\u0435\u043d\u0438\u0435 \u0434\u0430\u043d\u043d\u044b\u0445"),
            Map.entry("Caching", "\u041a\u044d\u0448\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u0435"),
            Map.entry("Testing", "\u0422\u0435\u0441\u0442\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u0435"),
            Map.entry("Debugging", "\u041e\u0442\u043b\u0430\u0434\u043a\u0430"),
            Map.entry("Distribution", "\u041f\u0443\u0431\u043b\u0438\u043a\u0430\u0446\u0438\u044f"),
            Map.entry("Performance", "\u041f\u0440\u043e\u0438\u0437\u0432\u043e\u0434\u0438\u0442\u0435\u043b\u044c\u043d\u043e\u0441\u0442\u044c"),
            Map.entry("Security", "\u0411\u0435\u0437\u043e\u043f\u0430\u0441\u043d\u043e\u0441\u0442\u044c"),
            Map.entry("Monitoring", "\u041c\u043e\u043d\u0438\u0442\u043e\u0440\u0438\u043d\u0433"),
            Map.entry("Logging", "\u041b\u043e\u0433\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u0435"),
            Map.entry("Concurrency", "\u041a\u043e\u043d\u043a\u0443\u0440\u0435\u043d\u0442\u043d\u043e\u0441\u0442\u044c"),
            Map.entry("Multithreading", "\u041c\u043d\u043e\u0433\u043e\u043f\u043e\u0442\u043e\u0447\u043d\u043e\u0441\u0442\u044c"),
            Map.entry("Architecture", "\u0410\u0440\u0445\u0438\u0442\u0435\u043a\u0442\u0443\u0440\u0430"),
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
    );

    private static final LinkedHashMap<String, String> TITLE_SEGMENT_OVERRIDES = new LinkedHashMap<>();

    static {
        TITLE_SEGMENT_OVERRIDES.put("Introduction to ", "\u0412\u0432\u0435\u0434\u0435\u043d\u0438\u0435 \u0432 ");
        TITLE_SEGMENT_OVERRIDES.put("Getting Started with ", "\u041d\u0430\u0447\u0430\u043b\u043e \u0440\u0430\u0431\u043e\u0442\u044b \u0441 ");
        TITLE_SEGMENT_OVERRIDES.put("Working with ", "\u0420\u0430\u0431\u043e\u0442\u0430 \u0441 ");
        TITLE_SEGMENT_OVERRIDES.put("Basics of ", "\u041e\u0441\u043d\u043e\u0432\u044b ");
        TITLE_SEGMENT_OVERRIDES.put("Fundamentals of ", "\u041e\u0441\u043d\u043e\u0432\u044b ");
        TITLE_SEGMENT_OVERRIDES.put("Overview of ", "\u041e\u0431\u0437\u043e\u0440 ");
        TITLE_SEGMENT_OVERRIDES.put("Advanced ", "\u041f\u0440\u043e\u0434\u0432\u0438\u043d\u0443\u0442\u044b\u0439 ");
        TITLE_SEGMENT_OVERRIDES.put("Best Practices", "\u041b\u0443\u0447\u0448\u0438\u0435 \u043f\u0440\u0430\u043a\u0442\u0438\u043a\u0438");
        TITLE_SEGMENT_OVERRIDES.put(" and ", " \u0438 ");
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

        String exact = EXACT_TOPIC_OVERRIDES.get(topicTitle);
        if (exact != null) {
            return exact;
        }

        String localized = topicTitle.trim();
        for (Map.Entry<String, String> entry : TITLE_SEGMENT_OVERRIDES.entrySet()) {
            localized = localized.replace(entry.getKey(), entry.getValue());
        }

        return cleanup(localized);
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
