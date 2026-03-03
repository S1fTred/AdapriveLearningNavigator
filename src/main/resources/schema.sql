-- =====================================================
-- ALN (Adaptive Learning Navigator)
-- Schema.sql
-- =====================================================

-- ================== USERS ==================
CREATE TABLE users (
                       id              UUID PRIMARY KEY,
                       email           VARCHAR(255) NOT NULL UNIQUE,
                       password_hash   VARCHAR(255) NOT NULL,
                       display_name    VARCHAR(120),
                       created_at      TIMESTAMP WITH TIME ZONE DEFAULT now(),
                       updated_at      TIMESTAMP WITH TIME ZONE
);

CREATE INDEX idx_users_email ON users(email);

-- ================== USER PREFERENCES ==================
CREATE TABLE user_preferences (
                                  user_id                UUID PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
                                  preferred_language     VARCHAR(10),
                                  hours_per_week_default INT CHECK (hours_per_week_default BETWEEN 1 AND 80),
                                  created_at             TIMESTAMP WITH TIME ZONE DEFAULT now(),
                                  updated_at             TIMESTAMP WITH TIME ZONE
);

-- Нормализованные предпочтения типов ресурсов
CREATE TABLE user_preferred_resource_types (
                                               user_id       UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                               resource_type VARCHAR(30) NOT NULL,
                                               PRIMARY KEY (user_id, resource_type),
                                               CHECK (resource_type IN ('VIDEO','ARTICLE','COURSE','BOOK','INTERACTIVE'))
);

CREATE INDEX idx_user_pref_types_user ON user_preferred_resource_types(user_id);

-- ================== TOPICS ==================
CREATE TABLE topics (
                        id              UUID PRIMARY KEY,
                        code            VARCHAR(50) NOT NULL UNIQUE,
                        title           VARCHAR(200) NOT NULL,
                        description     TEXT,
                        level           VARCHAR(20),
                        is_core         BOOLEAN NOT NULL DEFAULT FALSE,
                        estimated_hours NUMERIC(6,2) CHECK (estimated_hours >= 0),
                        status          VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
                        created_at      TIMESTAMP WITH TIME ZONE DEFAULT now(),
                        updated_at      TIMESTAMP WITH TIME ZONE,

                        CHECK (level IS NULL OR level IN ('BASIC','INTERMEDIATE','ADVANCED')),
                        CHECK (status IN ('DRAFT','ACTIVE','ARCHIVED'))
);

CREATE INDEX idx_topics_status ON topics(status);

-- ================== TOPIC PREREQUISITES (GRAPH) ==================
CREATE TABLE topic_prereqs (
                               id               UUID PRIMARY KEY,
                               prereq_topic_id  UUID NOT NULL REFERENCES topics(id) ON DELETE CASCADE,
                               next_topic_id    UUID NOT NULL REFERENCES topics(id) ON DELETE CASCADE,
                               relation_type    VARCHAR(20) NOT NULL DEFAULT 'REQUIRED',

                               CHECK (relation_type IN ('REQUIRED','RECOMMENDED')),
                               CHECK (prereq_topic_id <> next_topic_id),
                               UNIQUE (prereq_topic_id, next_topic_id)
);

CREATE INDEX idx_topic_prereq_prereq ON topic_prereqs(prereq_topic_id);
CREATE INDEX idx_topic_prereq_next ON topic_prereqs(next_topic_id);

-- ================== ROLE GOALS ==================
CREATE TABLE role_goals (
                            id          UUID PRIMARY KEY,
                            code        VARCHAR(50) NOT NULL UNIQUE,
                            name        VARCHAR(200) NOT NULL,
                            description TEXT,
                            status      VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
                            CHECK (status IN ('DRAFT','ACTIVE','ARCHIVED'))
);

-- Темы роли
CREATE TABLE role_topics (
                             role_id     UUID NOT NULL REFERENCES role_goals(id) ON DELETE CASCADE,
                             topic_id    UUID NOT NULL REFERENCES topics(id) ON DELETE CASCADE,
                             priority    INT,
                             is_required BOOLEAN NOT NULL DEFAULT TRUE,
                             PRIMARY KEY (role_id, topic_id),
                             CHECK (priority IS NULL OR priority >= 1)
);

CREATE INDEX idx_role_topics_role ON role_topics(role_id);
CREATE INDEX idx_role_topics_topic ON role_topics(topic_id);

-- ================== RESOURCES ==================
CREATE TABLE resources (
                           id               UUID PRIMARY KEY,
                           title            VARCHAR(255) NOT NULL,
                           url              VARCHAR(1000) NOT NULL,
                           type             VARCHAR(30) NOT NULL,
                           language         VARCHAR(10),
                           duration_minutes INT CHECK (duration_minutes >= 0),
                           provider         VARCHAR(120),
                           difficulty       VARCHAR(20),
                           status           VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
                           created_at       TIMESTAMP WITH TIME ZONE DEFAULT now(),
                           updated_at       TIMESTAMP WITH TIME ZONE,

                           CHECK (type IN ('VIDEO','ARTICLE','COURSE','BOOK','INTERACTIVE')),
                           CHECK (status IN ('DRAFT','ACTIVE','ARCHIVED'))
);

CREATE INDEX idx_resources_type ON resources(type);

-- Связь тема–ресурс
CREATE TABLE topic_resources (
                                 topic_id    UUID NOT NULL REFERENCES topics(id) ON DELETE CASCADE,
                                 resource_id UUID NOT NULL REFERENCES resources(id) ON DELETE CASCADE,
                                 rank        INT,
                                 PRIMARY KEY (topic_id, resource_id),
                                 CHECK (rank IS NULL OR rank >= 1)
);

CREATE INDEX idx_topic_resources_topic ON topic_resources(topic_id);
CREATE INDEX idx_topic_resources_resource ON topic_resources(resource_id);

-- ================== USER KNOWN TOPICS ==================
CREATE TABLE user_known_topics (
                                   user_id    UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                   topic_id   UUID NOT NULL REFERENCES topics(id) ON DELETE CASCADE,
                                   source     VARCHAR(20),
                                   confidence NUMERIC(3,2) CHECK (confidence BETWEEN 0 AND 1),
                                   marked_at  TIMESTAMP WITH TIME ZONE DEFAULT now(),
                                   PRIMARY KEY (user_id, topic_id),
                                   CHECK (source IS NULL OR source IN ('MANUAL','QUIZ','IMPORT'))
);

CREATE INDEX idx_user_known_topics_user ON user_known_topics(user_id);
CREATE INDEX idx_user_known_topics_topic ON user_known_topics(topic_id);

-- ================== PLANS ==================
CREATE TABLE plans (
                       id             UUID PRIMARY KEY,
                       user_id        UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                       role_id        UUID NOT NULL REFERENCES role_goals(id) ON DELETE RESTRICT,
                       status         VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
                       base_plan_id   UUID REFERENCES plans(id) ON DELETE SET NULL,
                       scenario_type  VARCHAR(20) NOT NULL DEFAULT 'BASE',
                       scenario_label VARCHAR(120),
                       created_at     TIMESTAMP WITH TIME ZONE DEFAULT now(),
                       updated_at     TIMESTAMP WITH TIME ZONE,

                       CHECK (status IN ('DRAFT','ACTIVE','ARCHIVED')),
                       CHECK (scenario_type IN ('BASE','WHAT_IF'))
);

CREATE INDEX idx_plans_user ON plans(user_id);
CREATE INDEX idx_plans_role ON plans(role_id);
CREATE INDEX idx_plans_base ON plans(base_plan_id);

-- ================== PLAN PARAMS SNAPSHOT ==================
-- Снимок параметров построения плана (важно для воспроизводимости и explainability)
CREATE TABLE plan_params_snapshots (
                                       plan_id        UUID PRIMARY KEY REFERENCES plans(id) ON DELETE CASCADE,
                                       hours_per_week INT NOT NULL CHECK (hours_per_week BETWEEN 1 AND 80),
                                       prefs_language VARCHAR(10),
                                       algo_version   VARCHAR(30) NOT NULL,
                                       created_at     TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- ================== PLAN WEEKS ==================
CREATE TABLE plan_weeks (
                            id            UUID PRIMARY KEY,
                            plan_id       UUID NOT NULL REFERENCES plans(id) ON DELETE CASCADE,
                            week_index    INT NOT NULL CHECK (week_index >= 1),
                            hours_budget  NUMERIC(6,2) NOT NULL CHECK (hours_budget >= 0),
                            hours_planned NUMERIC(6,2) CHECK (hours_planned IS NULL OR hours_planned >= 0),
                            UNIQUE (plan_id, week_index)
);

CREATE INDEX idx_plan_weeks_plan ON plan_weeks(plan_id);

-- ================== PLAN STEPS ==================
CREATE TABLE plan_steps (
                            id            UUID PRIMARY KEY,
                            plan_week_id  UUID NOT NULL REFERENCES plan_weeks(id) ON DELETE CASCADE,
                            topic_id      UUID NOT NULL REFERENCES topics(id) ON DELETE RESTRICT,
                            order_in_week INT NOT NULL CHECK (order_in_week >= 1),
                            planned_hours NUMERIC(6,2) NOT NULL CHECK (planned_hours > 0),
                            is_optional   BOOLEAN NOT NULL DEFAULT FALSE,
                            UNIQUE (plan_week_id, order_in_week),
                            UNIQUE (plan_week_id, topic_id)
);

CREATE INDEX idx_plan_steps_week ON plan_steps(plan_week_id);
CREATE INDEX idx_plan_steps_topic ON plan_steps(topic_id);

-- ================== PLAN STEP RESOURCES ==================
-- Ресурсы, закреплённые за конкретным шагом плана
CREATE TABLE plan_step_resources (
                                     plan_step_id UUID NOT NULL REFERENCES plan_steps(id) ON DELETE CASCADE,
                                     resource_id  UUID NOT NULL REFERENCES resources(id) ON DELETE RESTRICT,
                                     is_primary   BOOLEAN NOT NULL DEFAULT FALSE,
                                     PRIMARY KEY (plan_step_id, resource_id)
);

CREATE INDEX idx_plan_step_resources_step ON plan_step_resources(plan_step_id);
CREATE INDEX idx_plan_step_resources_resource ON plan_step_resources(resource_id);

-- ================== PLAN STEP EXPLANATIONS ==================
-- Объяснение "почему этот шаг выбран"
CREATE TABLE plan_step_explanations (
                                        plan_step_id          UUID PRIMARY KEY REFERENCES plan_steps(id) ON DELETE CASCADE,
                                        rule_applied          VARCHAR(80),
                                        topic_priority_reason VARCHAR(200),
                                        resource_reason       VARCHAR(200)
);

-- Пререквизиты в объяснении (статусы для explainability)
CREATE TABLE plan_step_explanation_prereqs (
                                               plan_step_id    UUID NOT NULL REFERENCES plan_step_explanations(plan_step_id) ON DELETE CASCADE,
                                               prereq_topic_id UUID NOT NULL REFERENCES topics(id) ON DELETE RESTRICT,
                                               prereq_status   VARCHAR(30) NOT NULL,
                                               PRIMARY KEY (plan_step_id, prereq_topic_id),
                                               CHECK (prereq_status IN ('DONE','IN_PREVIOUS_STEPS','MISSING'))
);

CREATE INDEX idx_plan_step_expl_prereqs_step ON plan_step_explanation_prereqs(plan_step_id);
CREATE INDEX idx_plan_step_expl_prereqs_topic ON plan_step_explanation_prereqs(prereq_topic_id);

-- ================== TOPIC PROGRESS ==================
CREATE TABLE topic_progress (
                                plan_id    UUID NOT NULL REFERENCES plans(id) ON DELETE CASCADE,
                                topic_id   UUID NOT NULL REFERENCES topics(id) ON DELETE RESTRICT,
                                status     VARCHAR(20) NOT NULL,
                                updated_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
                                PRIMARY KEY (plan_id, topic_id),
                                CHECK (status IN ('NOT_STARTED','IN_PROGRESS','DONE'))
);

CREATE INDEX idx_topic_progress_plan ON topic_progress(plan_id);
CREATE INDEX idx_topic_progress_topic ON topic_progress(topic_id);

-- ================== QUIZZES ==================
CREATE TABLE quizzes (
                         id       UUID PRIMARY KEY,
                         topic_id UUID NOT NULL REFERENCES topics(id) ON DELETE CASCADE,
                         title    VARCHAR(200) NOT NULL,
                         status   VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
                         CHECK (status IN ('DRAFT','ACTIVE','ARCHIVED'))
);

CREATE INDEX idx_quizzes_topic ON quizzes(topic_id);

CREATE TABLE quiz_questions (
                                id          UUID PRIMARY KEY,
                                quiz_id     UUID NOT NULL REFERENCES quizzes(id) ON DELETE CASCADE,
                                text        TEXT NOT NULL,
                                type        VARCHAR(20) NOT NULL,
                                order_index INT NOT NULL CHECK (order_index >= 1),
                                CHECK (type IN ('SINGLE','MULTI')),
                                UNIQUE (quiz_id, order_index)
);

CREATE INDEX idx_quiz_questions_quiz ON quiz_questions(quiz_id);

CREATE TABLE quiz_options (
                              id          UUID PRIMARY KEY,
                              question_id UUID NOT NULL REFERENCES quiz_questions(id) ON DELETE CASCADE,
                              text        TEXT NOT NULL,
                              is_correct  BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_quiz_options_question ON quiz_options(question_id);

CREATE TABLE quiz_attempts (
                               id            UUID PRIMARY KEY,
                               user_id       UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                               quiz_id       UUID NOT NULL REFERENCES quizzes(id) ON DELETE CASCADE,
                               score         NUMERIC(5,2),
                               correct_count INT,
                               total_count   INT,
                               submitted_at  TIMESTAMP WITH TIME ZONE DEFAULT now(),

                               CHECK (score IS NULL OR score >= 0),
                               CHECK (correct_count IS NULL OR correct_count >= 0),
                               CHECK (total_count IS NULL OR total_count >= 0),
                               CHECK (
                                   correct_count IS NULL OR total_count IS NULL OR correct_count <= total_count
                                   )
);

CREATE INDEX idx_quiz_attempts_user ON quiz_attempts(user_id);
CREATE INDEX idx_quiz_attempts_quiz ON quiz_attempts(quiz_id);