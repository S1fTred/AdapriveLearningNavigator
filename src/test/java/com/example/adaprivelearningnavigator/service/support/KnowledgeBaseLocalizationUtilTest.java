package com.example.adaprivelearningnavigator.service.support;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class KnowledgeBaseLocalizationUtilTest {

    @Test
    void shouldLocalizeRoleNamesForRoadmaps() {
        assertThat(KnowledgeBaseLocalizationUtil.localizeRoleName("android", "Android Developer"))
                .isEqualTo("Android-разработчик");
        assertThat(KnowledgeBaseLocalizationUtil.localizeRoleName("python", "Python Developer"))
                .isEqualTo("Python-разработчик");
        assertThat(KnowledgeBaseLocalizationUtil.localizeRoleName("game-developer", "Game Developer"))
                .isEqualTo("Разработчик игр");
        assertThat(KnowledgeBaseLocalizationUtil.localizeRoleName("devops", "DevOps"))
                .isEqualTo("DevOps-инженер");
        assertThat(KnowledgeBaseLocalizationUtil.localizeRoleName("machine-learning", "Machine Learning"))
                .isEqualTo("ML-инженер");
        assertThat(KnowledgeBaseLocalizationUtil.localizeRoleName("postgresql-dba", "PostgreSQL DBA"))
                .isEqualTo("Администратор PostgreSQL");
        assertThat(KnowledgeBaseLocalizationUtil.localizeRoleName("ai-product-builder", "AI Product Builder"))
                .isEqualTo("Разработчик AI-продуктов");
        assertThat(KnowledgeBaseLocalizationUtil.localizeRoleName("ai-data-scientist", "AI Data Scientist"))
                .isEqualTo("AI-специалист по Data Science");
    }

    @Test
    void shouldLocalizeCommonTopicTitles() {
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_ANDROID_LANG", "Pick a Language"))
                .isEqualTo("Выбор языка");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_ANDROID_OOP", "Basics of OOP"))
                .isEqualTo("Основы ООП");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_ANDROID_VERSION", "Version Control"))
                .isEqualTo("Контроль версий");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_API_ROUTING", "Routing"))
                .isEqualTo("Маршрутизация");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_DB_QUERY_OPT", "Query Optimization"))
                .isEqualTo("Оптимизация запросов");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_ARCH_MICROSERVICES", "Microservices"))
                .isEqualTo("Микросервисы");
    }

    @Test
    void shouldLocalizeRepresentativeRoleBasedTopicTitles() {
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_FRONTEND_INTERNET_WORK", "How does the internet work?"))
                .isEqualTo("Как работает интернет?");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_FRONTEND_SEMANTIC_HTML", "Writing Semantic HTML"))
                .isEqualTo("Семантическая HTML-разметка");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_FRONTEND_CSP", "Content Security Policy"))
                .isEqualTo("Политика безопасности контента");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_BACKEND_NOSQL", "NoSQL Databases"))
                .isEqualTo("NoSQL-базы данных");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_DEVOPS_OS", "Operating System"))
                .isEqualTo("Операционная система");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_DEVOPS_PROTOCOLS", "Networking & Protocols"))
                .isEqualTo("Сети и протоколы");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_DEVSECOPS_DDOS", "DDoS Mitigation Strategy"))
                .isEqualTo("Стратегия защиты от DDoS");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_DEVSECOPS_LEAST_PRIVILEGE", "Least Privilege"))
                .isEqualTo("Принцип наименьших привилегий");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_DATA_ENGINEER_LIFECYCLE", "Data Engineering Lifecycle"))
                .isEqualTo("Жизненный цикл инженерии данных");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_ML_PROBABILITY", "Basics of Probability"))
                .isEqualTo("Основы теории вероятностей");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_QA_BLACK_BOX", "Black Box Testing"))
                .isEqualTo("Тестирование чёрного ящика");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_PRODUCT_MARKET", "Market Analysis"))
                .isEqualTo("Анализ рынка");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_EM_ONE_ON_ONE", "One-on-One Meetings"))
                .isEqualTo("Встречи one-on-one");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_BI_WHAT", "What is BI?"))
                .isEqualTo("Что такое BI?");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_GAME_LOOP", "MonoBehaviour and Game Loop"))
                .isEqualTo("MonoBehaviour и игровой цикл");
    }

    @Test
    void shouldLocalizeAiEngineerTopicTitlesVisibleInKnownTopicsPicker() {
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_AI_ENGINEER_PRODUCT", "Impact on Product Development"))
                .isEqualTo("Влияние на разработку продукта");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_AI_ENGINEER_WHAT", "What is an AI Engineer?"))
                .isEqualTo("Кто такой AI-инженер?");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_AI_ENGINEER_ROLES", "Roles and Responsiblities"))
                .isEqualTo("Роли и обязанности");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_AI_ENGINEER_VS_ML", "AI Engineer vs ML Engineer"))
                .isEqualTo("AI-инженер и ML-инженер: сравнение");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_AI_ENGINEER_TRAINING", "Training"))
                .isEqualTo("Обучение моделей");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_AI_ENGINEER_RETRIEVAL", "Retrieval Process"))
                .isEqualTo("Процесс извлечения данных");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_AI_ENGINEER_IMAGE", "Image Understanding"))
                .isEqualTo("Понимание изображений");
    }

    @Test
    void shouldLocalizeRemainingRoleBasedTopicPatterns() {
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_DATA_WAREHOUSE", "Data Warehouse"))
                .isEqualTo("Хранилище данных");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_ML_EVAL", "Model Evaluation Techniques"))
                .isEqualTo("Техники оценки модели");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_QA_MANUAL", "Manual Testing"))
                .isEqualTo("Ручное тестирование");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_CYBER_OS", "Operating Systems"))
                .isEqualTo("Операционные системы");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_IOS_DEBUG", "Debugging Techniques"))
                .isEqualTo("Техники отладки");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_PRODUCT_REQ", "Product Requirements"))
                .isEqualTo("Требования к продукту");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_WRITER_CONTENT", "Content Optimization"))
                .isEqualTo("Оптимизация контента");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_DATA_WAREHOUSE_QUESTION", "What is Data Warehouse?"))
                .isEqualTo("Что такое хранилище данных?");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_ML_LINEAR_ALGEBRA", "Linear Algebra"))
                .isEqualTo("Линейная алгебра");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_DEVSECOPS_THREAT_MODELING", "Threat Modeling"))
                .isEqualTo("Моделирование угроз");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_ANDROID_INTENTS", "Implicit Intents"))
                .isEqualTo("Неявные Intent");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_PRODUCT_MVP", "Minimum Viable Product (MVP)"))
                .isEqualTo("Минимально жизнеспособный продукт (MVP)");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_DEVREL_COMMUNITY", "Community Management"))
                .isEqualTo("Управление сообществом");
    }

    @Test
    void shouldLocalizeRepresentativeSkillBasedTopicTitles() {
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_JS_VARIABLES", "Variable Declarations"))
                .isEqualTo("Объявление переменных");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_GIT_PR", "Pull Requests"))
                .isEqualTo("Запросы на слияние (Pull Request)");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_CSS_BOX_MODEL", "Box Model"))
                .isEqualTo("Блочная модель");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_HTML_WEB", "How the Web Works"))
                .isEqualTo("Как работает веб");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_SQL_SYNTAX", "Basic SQL Syntax"))
                .isEqualTo("Базовый синтаксис SQL");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_SYSTEM_DESIGN", "Latency vs Throughput"))
                .isEqualTo("Задержка и пропускная способность: сравнение");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_LINUX_COMMANDS", "Basic Commands"))
                .isEqualTo("Базовые команды");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_TERRAFORM_STATE", "Remote State"))
                .isEqualTo("Удалённый state");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_AWS_RESP", "Shared Responsibility Model"))
                .isEqualTo("Модель разделения ответственности");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_JS_HISTORY", "History of JavaScript"))
                .isEqualTo("История JavaScript");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_GIT_CLONE", "Cloning Repositories"))
                .isEqualTo("Клонирование репозиториев");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_NODE_HISTORY", "History of Node.js"))
                .isEqualTo("История Node.js");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_CPP_WHY", "Why use C++?"))
                .isEqualTo("Зачем использовать C++?");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_BASH_CASE", "Case Statements"))
                .isEqualTo("Операторы case");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_GIT_CONTRIB", "Contribution Guidelines"))
                .isEqualTo("Рекомендации по вкладу");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_TS_SEMVER", "Semantic Versioning"))
                .isEqualTo("Семантическое версионирование");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_CS_HEAP_SORT", "Heap Sort"))
                .isEqualTo("Сортировка кучей");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_NEXT_LAZY", "Lazy Loading"))
                .isEqualTo("Ленивая загрузка");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_CS_STACK", "Stack"))
                .isEqualTo("Стек");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_ANGULAR_SELECTOR", "Selector"))
                .isEqualTo("Селектор");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_SWIFT_APP", "App Lifecycle"))
                .isEqualTo("Жизненный цикл приложения");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_ASPNET_ORM", "Object Relational Mapping"))
                .isEqualTo("Объектно-реляционное отображение");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_SCALA_APPLY", "The apply method"))
                .isEqualTo("Метод apply");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_SWIFT_ARC", "Automatic Reference Counting (ARC)"))
                .isEqualTo("Автоматический подсчёт ссылок (ARC)");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_ASPNET_BASICS", "ASP.NET Core Basics"))
                .isEqualTo("Основы ASP.NET Core");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_ASPNET_FILTERS", "Filters and Attributes"))
                .isEqualTo("Фильтры и атрибуты");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_SCALA_DOCS", "Docs"))
                .isEqualTo("Документация");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_SWIFT_INIT", "Initialization"))
                .isEqualTo("Инициализация");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_ASPNET_CLOUD", "Cloud"))
                .isEqualTo("Облако");
    }

    @Test
    void shouldLocalizeFormulaDescriptions() {
        String localized = KnowledgeBaseLocalizationUtil.localizeDescription(
                "Тема roadmap по направлению «Android Developer»: Pick a Language. К теме прикреплены материалы для самостоятельного изучения.",
                "Android Developer",
                "Android-разработчик",
                "Pick a Language",
                "Выбор языка"
        );

        assertThat(localized).contains("Android-разработчик");
        assertThat(localized).contains("Выбор языка");
        assertThat(localized).doesNotContain("Android Developer");
        assertThat(localized).doesNotContain("Pick a Language");
        assertThat(localized).doesNotContain("roadmap.sh");
    }

    @Test
    void shouldClassifyRoadmapsByRoadmapShCategories() {
        assertThat(KnowledgeBaseLocalizationUtil.roadmapCategory("android")).isEqualTo("ROLE_BASED");
        assertThat(KnowledgeBaseLocalizationUtil.roadmapCategoryLabel("android")).isEqualTo("Направления по ролям");
        assertThat(KnowledgeBaseLocalizationUtil.roadmapCategory("java-backend")).isEqualTo("ROLE_BASED");
        assertThat(KnowledgeBaseLocalizationUtil.roadmapCategory("java-mobile")).isEqualTo("ROLE_BASED");
        assertThat(KnowledgeBaseLocalizationUtil.roadmapCategory("react")).isEqualTo("SKILL_BASED");
        assertThat(KnowledgeBaseLocalizationUtil.roadmapCategoryLabel("react")).isEqualTo("Направления по навыкам");
    }
    @Test
    void shouldExposeOnlyRoadmapShMvpCatalogRoadmaps() {
        assertThat(KnowledgeBaseLocalizationUtil.isMvpRoadmap("frontend")).isTrue();
        assertThat(KnowledgeBaseLocalizationUtil.isMvpRoadmap("ai-engineer")).isTrue();
        assertThat(KnowledgeBaseLocalizationUtil.isMvpRoadmap("sql")).isTrue();
        assertThat(KnowledgeBaseLocalizationUtil.isMvpRoadmap("claude-code")).isTrue();

        assertThat(KnowledgeBaseLocalizationUtil.isMvpRoadmap("backend-beginner")).isFalse();
        assertThat(KnowledgeBaseLocalizationUtil.isMvpRoadmap("java-backend")).isFalse();
        assertThat(KnowledgeBaseLocalizationUtil.isMvpRoadmap("java-mobile")).isFalse();
        assertThat(KnowledgeBaseLocalizationUtil.isMvpRoadmap("scala")).isFalse();
        assertThat(KnowledgeBaseLocalizationUtil.isMvpRoadmap("openclaw")).isFalse();
    }
}
