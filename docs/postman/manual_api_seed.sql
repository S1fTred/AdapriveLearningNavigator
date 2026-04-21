-- Seed data for manual Postman checks of ALN API.
-- Run after auth_password_hash_migration.sql and before testing /api/plans/generate-with-ai.

MERGE INTO role_goals (role_id, code, name, description, status)
KEY(role_id)
VALUES
  (9201, 'java-backend', 'Java Backend Developer', 'Базовая дорожная карта по Java backend-разработке: язык, веб-основы, базы данных и Spring-стек.', 'ACTIVE'),
  (9202, 'java-mobile', 'Java Mobile Developer', 'Дорожная карта по Java mobile-разработке. Направление сохранено в каталоге как отдельная Java-ветка.', 'ACTIVE');

MERGE INTO topics (topic_id, code, title, description, level, is_core, estimated_hours, status, created_at, updated_at)
KEY(topic_id)
VALUES
  (9101, 'GIT', 'Git', 'Основы контроля версий: репозиторий, коммиты, ветки и базовый рабочий процесс.', 'BASIC', TRUE, 2.00, 'ACTIVE', CURRENT_TIMESTAMP, NULL),
  (9102, 'JAVA_BASICS', 'Java Basics', 'Синтаксис Java, типы данных, условия, циклы и базовые конструкции языка.', 'BASIC', TRUE, 6.00, 'ACTIVE', CURRENT_TIMESTAMP, NULL),
  (9103, 'OOP_JAVA', 'OOP in Java', 'Классы, объекты, инкапсуляция, наследование и полиморфизм в Java.', 'BASIC', TRUE, 5.00, 'ACTIVE', CURRENT_TIMESTAMP, NULL),
  (9104, 'HTTP', 'HTTP Basics', 'Основы HTTP, структура запросов и ответов, методы, статусы и REST-подход.', 'BASIC', TRUE, 4.00, 'ACTIVE', CURRENT_TIMESTAMP, NULL),
  (9105, 'SQL', 'SQL Basics', 'Базовые SQL-запросы, таблицы, связи и работа с реляционными данными.', 'BASIC', TRUE, 5.00, 'ACTIVE', CURRENT_TIMESTAMP, NULL),
  (9106, 'MAVEN', 'Maven', 'Сборка Java-проекта, зависимости, плагины и базовый жизненный цикл Maven.', 'BASIC', FALSE, 2.00, 'ACTIVE', CURRENT_TIMESTAMP, NULL),
  (9107, 'SPRING_BOOT', 'Spring Boot', 'Быстрый старт со Spring Boot: конфигурация приложения, REST API и базовая backend-архитектура.', 'INTERMEDIATE', TRUE, 8.00, 'ACTIVE', CURRENT_TIMESTAMP, NULL),
  (9108, 'JPA', 'JPA/Hibernate', 'Основы persistence-слоя: сущности, связи, репозитории и работа с JPA/Hibernate.', 'INTERMEDIATE', TRUE, 8.00, 'ACTIVE', CURRENT_TIMESTAMP, NULL);

MERGE INTO role_topics (role_id, topic_id, priority, is_required)
KEY(role_id, topic_id)
VALUES
  (9201, 9101, 1, FALSE),
  (9201, 9102, 2, TRUE),
  (9201, 9103, 3, TRUE),
  (9201, 9104, 4, TRUE),
  (9201, 9105, 5, TRUE),
  (9201, 9106, 6, FALSE),
  (9201, 9107, 7, TRUE),
  (9201, 9108, 8, TRUE);

MERGE INTO topic_prereqs (topic_prereq_id, prereq_topic_id, next_topic_id, relation_type, created_at, updated_at)
KEY(topic_prereq_id)
VALUES
  (9301, 9102, 9103, 'REQUIRED', CURRENT_TIMESTAMP, NULL),
  (9302, 9102, 9107, 'REQUIRED', CURRENT_TIMESTAMP, NULL),
  (9303, 9104, 9107, 'REQUIRED', CURRENT_TIMESTAMP, NULL),
  (9304, 9105, 9108, 'REQUIRED', CURRENT_TIMESTAMP, NULL),
  (9305, 9107, 9108, 'REQUIRED', CURRENT_TIMESTAMP, NULL);
