-- Seed data for manual Postman checks of ALN API.
-- Run after auth_password_hash_migration.sql and before testing /api/plans/generate-with-ai.

MERGE INTO role_goals (role_id, code, name, description, status)
KEY(role_id)
VALUES
  (9201, 'java-backend', 'Java Backend Developer', 'Manual test role', 'ACTIVE'),
  (9202, 'java-mobile', 'Java Mobile Developer', 'Manual ambiguity role', 'ACTIVE');

MERGE INTO topics (topic_id, code, title, description, level, is_core, estimated_hours, status, created_at, updated_at)
KEY(topic_id)
VALUES
  (9101, 'GIT', 'Git', 'Version control basics', 'BASIC', TRUE, 2.00, 'ACTIVE', CURRENT_TIMESTAMP, NULL),
  (9102, 'JAVA_BASICS', 'Java Basics', 'Syntax and core concepts', 'BASIC', TRUE, 6.00, 'ACTIVE', CURRENT_TIMESTAMP, NULL),
  (9103, 'OOP_JAVA', 'OOP in Java', 'Classes and OOP', 'BASIC', TRUE, 5.00, 'ACTIVE', CURRENT_TIMESTAMP, NULL),
  (9104, 'HTTP', 'HTTP Basics', 'HTTP and REST basics', 'BASIC', TRUE, 4.00, 'ACTIVE', CURRENT_TIMESTAMP, NULL),
  (9105, 'SQL', 'SQL Basics', 'Queries and relational basics', 'BASIC', TRUE, 5.00, 'ACTIVE', CURRENT_TIMESTAMP, NULL),
  (9106, 'MAVEN', 'Maven', 'Build tool basics', 'BASIC', FALSE, 2.00, 'ACTIVE', CURRENT_TIMESTAMP, NULL),
  (9107, 'SPRING_BOOT', 'Spring Boot', 'Spring Boot fundamentals', 'INTERMEDIATE', TRUE, 8.00, 'ACTIVE', CURRENT_TIMESTAMP, NULL),
  (9108, 'JPA', 'JPA/Hibernate', 'Persistence basics', 'INTERMEDIATE', TRUE, 8.00, 'ACTIVE', CURRENT_TIMESTAMP, NULL);

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
