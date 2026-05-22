# Adaprive Learning Navigator

## Документационные схемы

- `docs/diagrams/er-current-chen.drawio` — актуальная ER-диаграмма базы данных в нотации Чена для раздела проектирования.
- `docs/diagrams/weekly-plan-activity.drawio` — activity diagram построения недельного плана.
- `docs/diagrams/weekly-plan-sequence.drawio` — sequence diagram backend-сценария построения недельного плана.
- `docs/diagrams/weekly-plan-data-flow.drawio` — data flow diagram для данных, участвующих в построении плана.
- `docs/diagrams/weekly-plan-stages.drawio` — упрощённая схема этапов построения недельного плана.

## Аннотация

Adaprive Learning Navigator — это учебный веб-сервис для построения персональных маршрутов изучения IT-направлений. Пользователь выбирает готовую roadmap-карту из каталога, отмечает уже знакомые темы, задаёт количество часов в неделю и получает недельный план обучения. Внутри выбранной темы доступны описание, ресурсы и AI Tutor в формате чата.

Проект реализован как единое Spring Boot приложение: backend, база данных, HTML-страницы, CSS и JavaScript находятся в одном репозитории и запускаются одним приложением. Основной продуктовый сценарий уже переведён в roadmap-first формат: маршрут берётся из базы знаний, а AI используется как помощник по конкретной теме, а не как главный источник построения всей roadmap.

## Что реализовано в MVP

### Основной пользовательский сценарий

1. Пользователь регистрируется или входит в систему.
2. Открывает приватную часть сервиса.
3. В каталоге выбирает IT-направление.
4. Открывает карту направления или сразу собирает персональный план.
5. Указывает недельный лимит часов.
6. Отмечает темы, которые уже знает.
7. Сервис строит weekly plan: темы раскладываются по неделям с учётом порядка, зависимостей и лимита часов.
8. В плане пользователь отмечает прогресс по темам.
9. В профиле пользователь видит сохранённые планы и может удалить ненужные.
10. На странице roadmap пользователь может открыть тему, посмотреть ресурсы и задать вопрос AI Tutor.

### Реализованные функции

- регистрация пользователей;
- вход по email и паролю;
- refresh JWT-токенов;
- stateless-защита `/api/**` через JWT;
- многостраничный frontend внутри Spring Boot;
- главная страница;
- страницы входа и регистрации;
- приватный shell с боковой навигацией;
- сворачиваемая боковая панель;
- каталог roadmap'ов;
- разделение каталога на направления по ролям и направления по навыкам;
- MVP-фильтр каталога, который скрывает лишние beginner/legacy/экспериментальные карты из пользовательской витрины;
- поиск по каталогу;
- выбор roadmap;
- отдельная страница карты направления;
- roadmap-like визуализация тем;
- drawer-панель деталей темы;
- вкладка обзора темы;
- вкладка ресурсов;
- AI Tutor по выбранной теме;
- быстрые промпты AI Tutor;
- режим проверки знаний через AI Tutor;
- построение weekly plan по roadmap;
- исключение уже знакомых тем из weekly plan;
- сохранение параметров плана;
- автоматическое дробление слишком длинной темы на несколько недельных частей;
- отображение частей темы на frontend;
- прогресс по темам внутри плана;
- сохранение прогресса на backend;
- профиль пользователя;
- история сохранённых планов;
- удаление плана через кастомное модальное окно;
- soft delete планов без физического удаления связанных недель и шагов;
- compatibility redirect `/progress` в `/plan`;
- legacy endpoint генерации плана через AI сохранён на backend;
- backend Quiz API сохранён как технически рабочий слой проверки знаний.

## Стек технологий

### Backend

- Java 17;
- Spring Boot 4.0.2;
- Spring Web MVC;
- Spring Security;
- Spring Data JPA;
- Hibernate;
- H2 Database для локального запуска;
- PostgreSQL JDBC dependency добавлена как runtime-зависимость;
- Spring Validation;
- Spring AI 2.0.0-M1;
- Ollama как локальный LLM provider;
- JJWT для JWT-токенов;
- Lombok;
- Maven.

### Frontend

- обычные HTML-страницы;
- CSS без отдельного UI-framework;
- JavaScript ES Modules;
- `fetch` для API-запросов;
- `localStorage` для клиентской сессии, выбранного плана, выбранной roadmap и кеша roadmap-данных;
- MPA-подход: каждая крупная страница имеет отдельный HTML и отдельный page-script.

### База данных

- H2 file database в обычном локальном запуске;
- H2 in-memory database в тестах;
- схема задаётся через `src/main/resources/schema.sql`;
- JPA работает с `ddl-auto: none`, то есть структура не генерируется Hibernate автоматически.

## Структура проекта

### Корневые файлы

- `pom.xml` — Maven-конфигурация, зависимости и плагины сборки;
- `README.md` — основная техническая документация проекта;
- `mvnw`, `mvnw.cmd` — Maven Wrapper;
- `docs/postman` — Postman-коллекция и SQL seed для ручных API-проверок;
- `docs/sql` — SQL-скрипты для технических миграций;
- `db` — локальные файлы H2-БД;
- `src/main/resources/schema.sql` — SQL-схема БД.

### Backend-пакеты

- `api` — REST-контроллеры;
- `service` — сервисные интерфейсы;
- `service.impl` — основная бизнес-логика;
- `service.dto` — DTO запросов и ответов;
- `service.exception` — прикладные исключения и единый обработчик ошибок;
- `domain` — JPA-сущности и enum-значения;
- `repo` — Spring Data JPA repositories;
- `security` — JWT, principal, security handlers;
- `config` — конфигурация Spring, security, Jackson, time, roadmap import;
- `ai` — DTO, prompt builder, генерация AI-route и AI Tutor;
- `integration.roadmapsh` — импорт локальных roadmap manifest-файлов;
- `web` — маршрутизация HTML-страниц и автооткрытие браузера.

### Frontend-структура

HTML-страницы лежат в `src/main/resources/static`:

- `index.html` — главная;
- `login.html` — вход;
- `register.html` — регистрация;
- `dashboard.html` — каталог и сборка персонального плана;
- `roadmap.html` — отдельная карта направления;
- `plan.html` — weekly plan и прогресс;
- `profile.html` — профиль и сохранённые планы;
- `progress.html` — совместимый redirect в `/plan`.

Общие frontend-модули:

- `assets/js/core/api.js` — единая обёртка над API, refresh токенов, клиенты `authApi`, `plansApi`, `roadmapsApi`, `progressApi`, `usersApi`, `tutorApi`;
- `assets/js/core/session.js` — работа с `localStorage`, токенами, выбранной roadmap, выбранным планом и кешем;
- `assets/js/core/guard.js` — проверка авторизации на приватных страницах;
- `assets/js/core/shell.js` — боковая навигация, logout, сворачивание sidebar;
- `assets/js/core/roadmap-catalog.js` — MVP-фильтр видимых roadmap;
- `assets/js/core/roadmaps.js` — выбор активной roadmap;
- `assets/js/core/plans.js` — выбор активного плана и сводка плана;
- `assets/js/core/ui.js` — форматирование дат, часов, HTML-escaping, empty state, аннотация частей темы;
- `assets/css/app.css` — общие стили и дизайн-система.

Page scripts:

- `assets/js/pages/index.js`;
- `assets/js/pages/login.js`;
- `assets/js/pages/register.js`;
- `assets/js/pages/dashboard.js`;
- `assets/js/pages/roadmap.js`;
- `assets/js/pages/plan.js`;
- `assets/js/pages/profile.js`.

## Страницы интерфейса

### Главная `/`

Главная страница кратко объясняет назначение сервиса и ведёт пользователя к началу работы. Текст сокращён до клиентского описания без внутренней технической кухни. Цветовая схема и layout соответствуют общей стилистике проекта.

### Вход `/login`

Страница входа принимает email и пароль. После успешного ответа `/auth/login` frontend сохраняет access/refresh токены и переводит пользователя в приватную часть.

### Регистрация `/register`

Страница регистрации создаёт пользователя через `/auth/register`. После успешной регистрации пользователь сразу получает токены и может работать с сервисом.

### Кабинет и каталог `/dashboard`

Это главный рабочий экран выбора направления и сборки плана.

На странице реализовано:

- загрузка roadmap'ов через `/api/roadmaps`;
- дополнительная фильтрация MVP-набора на frontend;
- вкладки `Направления на основе ролей` и `Направления на основе навыков`;
- поиск по названию, коду и описанию roadmap;
- компактные карточки направлений;
- выбор направления;
- ссылка `К карте`;
- правая панель `Персональный план`;
- поле часов в неделю;
- компактный список известных тем с внутренней прокруткой;
- отображение шага темы, чтобы в двухколоночном списке не терялся порядок;
- сохранение черновика выбора в `localStorage`;
- сборка плана через `/api/plans/build-from-roadmap`;
- переход к уже созданному плану, если он есть для выбранного направления.

### Карта направления `/roadmap`

Страница открывается с query-параметром `roadmapId`. Если передан `planId`, карта также знает, какие темы входят в активный план.

На странице реализовано:

- сводка выбранной roadmap;
- расчёт оставшихся часов с учётом известных тем;
- кнопка открытия плана или сборки плана;
- кнопка возврата в каталог;
- визуальная roadmap-like карта тем;
- центральная ось и ответвления;
- выделение выбранной темы;
- отображение обязательности, часов, known/in-plan статусов;
- drawer-панель деталей темы;
- вкладки `Обзор`, `Ресурсы`, `AI Tutor`;
- URL-синхронизация выбранной темы через query-параметры;
- кеширование roadmap и деталей темы в `localStorage`.

### План `/plan`

Страница показывает сохранённый weekly plan.

Реализовано:

- выбор активного плана из query-параметра или последнего выбранного плана;
- загрузка плана через `/api/plans/{planId}`;
- загрузка прогресса через `/api/plans/{planId}/progress`;
- отображение недель;
- отображение тем внутри недели;
- отображение часов недели и часов темы;
- отображение исходного лимита часов в неделю;
- отображение частей темы, если она была разбита на несколько недель;
- подписи `Старт темы` и `Продолжение темы`;
- статусы прогресса `Не начато`, `В процессе`, `Готово`;
- сохранение прогресса через `PUT /api/plans/{planId}/progress`;
- сводка по прогрессу: процент, завершённые темы, завершённые часы, следующий шаг;
- переход к roadmap выбранного плана.

### Профиль `/profile`

Профиль показывает только пользовательски полезную информацию.

Реализовано:

- загрузка профиля через `/api/users/me`;
- email;
- display name;
- дата создания аккаунта;
- количество сохранённых планов;
- список планов;
- переход в план;
- переход к карте направления;
- переход в каталог;
- удаление плана;
- кастомное модальное подтверждение удаления.

Служебная информация вроде JWT payload, internal user id и технических сроков токенов в UI не выводится.

### Прогресс `/progress`

Отдельная страница прогресса больше не является основной. Маршрут сохранён для совместимости и перенаправляет пользователя в `/plan`, потому что прогресс встроен в weekly plan.

## Backend API

### Auth API

Base path: `/auth`

| Метод | Endpoint | Назначение |
|---|---|---|
| `POST` | `/auth/register` | Регистрация пользователя |
| `POST` | `/auth/login` | Вход по email и паролю |
| `POST` | `/auth/refresh` | Получение новой пары access/refresh токенов |

Регистрация и вход возвращают `AuthResponse` с access token и refresh token. Пароль хранится в виде BCrypt hash.

### User API

Base path: `/api/users`

| Метод | Endpoint | Назначение |
|---|---|---|
| `GET` | `/api/users/me` | Профиль текущего пользователя |

Ответ содержит id, email, display name и дату создания аккаунта.

### Roadmap API

Base path: `/api/roadmaps`

| Метод | Endpoint | Назначение |
|---|---|---|
| `GET` | `/api/roadmaps?page=0&size=24` | Список roadmap |
| `GET` | `/api/roadmaps/{roleId}` | Детальная roadmap с темами |
| `GET` | `/api/roadmaps/{roleId}/topics/{topicId}` | Детали темы внутри roadmap |

Roadmap API возвращает локализованные названия и описания. Категория roadmap вычисляется как `ROLE_BASED` или `SKILL_BASED`. В публичную витрину попадают только roadmap из MVP-набора.

Детали темы содержат:

- название темы;
- описание;
- уровень;
- признак core-темы;
- часы;
- priority;
- обязательность;
- prerequisite-темы;
- темы, которые открываются после текущей;
- ресурсы;
- краткую информацию о quiz.

### Plan API

Base path: `/api/plans`

| Метод | Endpoint | Назначение |
|---|---|---|
| `GET` | `/api/plans?page=0&size=12` | Список планов пользователя |
| `GET` | `/api/plans/{planId}` | Полный план |
| `DELETE` | `/api/plans/{planId}` | Мягкое удаление плана |
| `POST` | `/api/plans/build-from-roadmap` | Построение плана по roadmap |
| `POST` | `/api/plans/generate-with-ai` | Legacy AI-генерация плана |

`build-from-roadmap` принимает:

- `roleId`;
- `hoursPerWeek`;
- `knownTopicIds`;
- `scenarioLabel`.

`hoursPerWeek` валидируется в диапазоне от 1 до 80.

Удаление плана реализовано как soft delete: статус плана меняется на `DELETED`, после чего план скрывается из списков, не открывается по API и не принимает обновления прогресса. Связанные недели, шаги и прогресс физически не удаляются.

### Progress API

Base path: `/api/plans/{planId}/progress`

| Метод | Endpoint | Назначение |
|---|---|---|
| `GET` | `/api/plans/{planId}/progress` | Получить прогресс тем плана |
| `PUT` | `/api/plans/{planId}/progress` | Обновить статус темы |

Статусы прогресса:

- `NOT_STARTED`;
- `IN_PROGRESS`;
- `DONE`.

Прогресс привязан к конкретному плану и теме. Backend проверяет, что план принадлежит текущему пользователю и не удалён.

### AI Tutor API

Base path: `/api/roadmaps/{roleId}/topics/{topicId}/tutor`

| Метод | Endpoint | Назначение |
|---|---|---|
| `POST` | `/api/roadmaps/{roleId}/topics/{topicId}/tutor` | Задать вопрос AI Tutor по выбранной теме |

Запрос содержит:

- `question`;
- `history` — последние сообщения текущего диалога.

Ограничения запроса:

- вопрос до 1200 символов;
- история до 12 сообщений;
- сообщение истории до 2400 символов;
- роли истории: `user` или `assistant`.

AI Tutor получает контекст конкретной roadmap и темы. Prompt строится так, чтобы AI отвечал по выбранной теме, учитывал prerequisites, ресурсы и предыдущие реплики текущего диалога.

### Quiz API

Base path: `/api`

| Метод | Endpoint | Назначение |
|---|---|---|
| `GET` | `/api/topics/{topicId}/quiz` | Получить quiz по теме |
| `GET` | `/api/quizzes/{quizId}/questions` | Получить вопросы quiz |
| `POST` | `/api/quizzes/attempts` | Отправить попытку |
| `GET` | `/api/quizzes/attempts?quizId=...` | Получить попытки пользователя |

В текущем UI отдельная вкладка quiz убрана. Проверка знаний для пользователя перенесена в AI Tutor. При этом backend Quiz API остаётся реализованным: он создаёт базовый quiz для темы при первом запросе, хранит вопросы, варианты и попытки.

## Архитектура backend

### Общий принцип

Backend разделён на несколько слоёв:

- controller принимает HTTP-запрос;
- service содержит бизнес-логику;
- repository работает с БД;
- domain описывает таблицы через JPA-сущности;
- DTO отделяют API-ответы от внутренней модели БД;
- exception layer приводит ошибки к единому JSON-формату.

### Контроллеры

- `AuthController` — регистрация, вход, refresh;
- `UserController` — профиль текущего пользователя;
- `RoadmapController` — каталог, детали roadmap, детали темы;
- `PlanController` — планы, сборка плана, soft delete, legacy AI-plan;
- `ProgressController` — прогресс по темам плана;
- `AiTutorController` — AI Tutor по теме;
- `QuizController` — backend quiz API;
- `PageController` — HTML routes.

### Сервисы

- `AuthServiceImpl` — проверяет уникальность email, хеширует пароль, создаёт пользователя, выдаёт JWT;
- `UserServiceImpl` — возвращает профиль текущего пользователя;
- `RoadmapServiceImpl` — строит read-model roadmap, локализует названия и описания, дополняет ресурсы;
- `PlanServiceImpl` — строит и сохраняет планы;
- `ProgressServiceImpl` — читает и обновляет прогресс;
- `QuizServiceImpl` — создаёт quiz, отдаёт вопросы, считает попытки;
- `AiTutorServiceImpl` — формирует prompt и обращается к Ollama;
- `AiRouteGenerationServiceImpl` — legacy AI-генерация маршрута;
- `AiRouteValidationService` — проверяет AI-маршрут и сопоставляет темы с KB.

### Репозитории

Для каждой основной сущности используется Spring Data JPA repository. Репозитории не содержат сложной бизнес-логики: они отвечают за выборки, сохранение и простые finder-методы.

Ключевые группы:

- user repositories;
- roadmap/knowledge repositories;
- plan repositories;
- progress repository;
- quiz repositories.

## Построение weekly plan

### Источник данных

План строится не из произвольного пользовательского текста, а из уже существующей roadmap в базе знаний:

- `role_goals` задаёт направление;
- `role_topics` связывает направление с темами и priority;
- `topics` хранит темы;
- `topic_prereqs` задаёт зависимости между темами.

### Учитывание известных тем

Пользователь выбирает известные темы в каталоге. Эти темы:

- остаются видимыми в roadmap;
- учитываются при расчёте оставшихся часов;
- не попадают в weekly plan;
- могут отображаться как уже знакомые на карте.

### Зависимости

При построении плана backend собирает scope направления и добавляет обязательные prerequisite-темы, если они нужны для корректного порядка. Если prerequisite уже отмечен как известный, он не добавляется в weekly plan как отдельный шаг.

### Порядок тем

Порядок строится на основе:

- priority из `role_topics`;
- обязательных зависимостей из `topic_prereqs`;
- topological sort.

Если в графе зависимостей обнаруживается цикл, сервис выбрасывает прикладную ошибку.

### Раскладка по неделям

План сохраняется в виде:

- plan;
- snapshot параметров;
- недели;
- шаги внутри недели;
- explanation для шага;
- prerequisite statuses для explanation.

Каждая неделя имеет:

- номер;
- бюджет часов;
- запланированные часы.

Каждый шаг имеет:

- тему;
- порядок внутри недели;
- количество часов;
- optional flag.

### Дробление длинных тем

Если тема длиннее недельного лимита, backend не возвращает ошибку. Тема разбивается на несколько последовательных шагов по неделям. На frontend такие шаги отображаются как части одной темы:

- `Часть 1 из N`;
- `Часть 2 из N`;
- `Старт темы`;
- `Продолжение темы`.

Это позволяет строить план даже при маленьком недельном лимите, например 5 или 6 часов в неделю.

### Explanation

Для шага плана сохраняется объяснение:

- правило добавления;
- причина приоритета темы;
- причина ресурса;
- статусы prerequisites.

В UI технические rule-коды не показываются напрямую: frontend переводит их в человекочитаемые подписи.

## Roadmap и база знаний

### Модель roadmap

Внутри БД roadmap представлена не как один JSON, а как набор связанных сущностей:

- направление;
- темы;
- связи направление-тема;
- зависимости тема-тема;
- ресурсы;
- связи тема-ресурс;
- теги.

Такой формат позволяет строить план, проверять зависимости и переиспользовать темы между разными направлениями.

### Категории roadmap

Категория вычисляется на read-model уровне:

- `ROLE_BASED` — направления по ролям;
- `SKILL_BASED` — направления по навыкам.

Отдельной миграции БД для этого нет: категория определяется по коду roadmap в `KnowledgeBaseLocalizationUtil`.

### MVP-фильтр каталога

В базе могут находиться дополнительные импортированные roadmap. Пользовательский каталог показывает только curated MVP-набор. Фильтр реализован в двух местах:

- backend отдаёт только опубликованные направления из разрешённого набора;
- frontend дополнительно фильтрует список через `roadmap-catalog.js`.

Это защищает интерфейс от старого кеша, stale API-ответов и лишнего контента.

### Локализация

Проект ориентирован на русскоязычную аудиторию.

Локализация реализована в `KnowledgeBaseLocalizationUtil`:

- перевод названий направлений;
- перевод названий тем;
- адаптация описаний;
- сохранение канонических названий технологий;
- контекстная обработка коротких технических токенов;
- перевод часто повторяющихся фраз;
- обработка вопросительных заголовков;
- удаление технических пометок о происхождении контента.

Не переводятся насильно:

- названия языков программирования;
- названия технологий;
- названия библиотек;
- CLI-команды;
- framework API;
- устоявшиеся product/tool names.

Примеры того, что адаптируется:

- `Android Developer` → `Android-разработчик`;
- `Pick a Language` → `Выбор языка`;
- `Basics of OOP` → `Основы ООП`;
- `var` → человекочитаемое название с сохранением исходного токена.

### Ресурсы

Ресурсы работают в два слоя:

1. Сначала используются ресурсы, которые уже есть в KB и связаны с темой.
2. Если русскоязычных ресурсов меньше пяти, backend дополняет выдачу fallback-ссылками.

Fallback-ресурсы строятся на основе:

- категорийных справочников;
- Habr;
- YouTube;
- Tproger;
- Proglib;
- Stepik;
- Metanit;
- MDN RU;
- Postgres Pro;
- Microsoft Learn RU;
- других тематических источников, заданных в коде.

Fallback не перезаписывает БД. Это слой выдачи, который делает страницу темы полезнее даже там, где в KB ещё нет ручной редакторской подборки.

## AI Tutor

AI Tutor реализован как чат внутри drawer-панели выбранной темы.

### Как работает frontend

Frontend хранит историю диалога в памяти страницы по `topicId`. При переключении темы пользователь видит отдельную историю для этой темы. История не сохраняется в БД.

В UI есть:

- область сообщений;
- компактное поле ввода;
- автоувеличение поля при длинном тексте;
- кнопка отправки;
- кнопка очистки чата;
- быстрые действия.

Быстрые действия:

- объяснить тему;
- перечислить ключевые моменты;
- кратко резюмировать тему;
- объяснить как новичку;
- объяснить, зачем это важно;
- проверить знания.

`Проверь знания` просит LLM сгенерировать до 10 вопросов по текущей теме прямо в чате.

### Как работает backend

`AiTutorServiceImpl`:

- проверяет существование roadmap и темы;
- получает описание темы;
- получает prerequisites;
- получает ресурсы;
- собирает prompt;
- добавляет историю диалога;
- вызывает Ollama через Spring AI;
- возвращает ответ в `AiTutorResponse`.

AI Tutor не строит roadmap и не меняет БД. Он отвечает только в контексте выбранной темы.

### Настройки AI

В `application.yaml`:

- Ollama base URL: `http://localhost:11434`;
- модель: `qwen2.5:7b`;
- retry уменьшен до одного быстрого запроса;
- backoff выставлен коротким, чтобы при выключенной Ollama пользователь не ждал почти минуту.

Если Ollama недоступна, backend возвращает управляемую ошибку.

## Legacy AI-планирование

Endpoint `/api/plans/generate-with-ai` всё ещё реализован. Это старый сценарий, где пользователь передаёт:

- цель;
- текущий уровень;
- часы в неделю;
- известные темы текстом.

Backend:

- строит prompt;
- отправляет запрос в LLM;
- получает список тем;
- валидирует темы по существующей KB;
- проверяет порядок и prerequisites;
- при необходимости делает повторную корректировку;
- сохраняет план.

Этот сценарий не является главным пользовательским потоком интерфейса, но backend-реализация сохранена и покрыта тестами.

## Quiz API

Quiz API реализован на backend и работает с таблицами:

- `quizzes`;
- `quiz_questions`;
- `quiz_options`;
- `quiz_attempts`.

Если у темы нет quiz, `QuizServiceImpl` создаёт базовую проверку при первом запросе. Генератор подбирает вопросы по теме через набор предметных шаблонов. Старые слишком общие auto-quiz вопросы распознаются и заменяются более предметными.

В текущем frontend отдельная вкладка `Квиз` не используется. Пользовательская проверка знаний вынесена в AI Tutor.

## Безопасность

### Аутентификация

Проект использует JWT:

- access token;
- refresh token;
- claim `userId`;
- claim `tokenType`;
- issuer из настроек.

Access token используется для запросов к `/api/**`. Refresh token используется только для получения новой пары токенов.

### Пароли

Пароли хешируются через `BCryptPasswordEncoder`. В БД хранится `password_hash`, исходный пароль не сохраняется.

### Security filter chain

`SecurityConfig`:

- отключает server-side session через `SessionCreationPolicy.STATELESS`;
- разрешает публичные страницы и static assets;
- разрешает `/auth/**`;
- разрешает H2 console;
- защищает `/api/**`;
- добавляет `JwtAuthenticationFilter`;
- возвращает JSON-ответы для 401 и 403 через REST handlers;
- настраивает CORS для localhost-портов 8080 и 3000.

### Frontend guard

Приватные страницы дополнительно используют `guard.js`. Если токенов нет, пользователь перенаправляется на `/login`. Это не заменяет backend security, а только улучшает UX.

## Обработка ошибок

Ошибки приводятся к единому JSON-формату через `GlobalExceptionHandler`.

Основные типы:

- `BadRequestException`;
- `ConflictException`;
- `ForbiddenException`;
- `NotFoundException`;
- `PlanBuildException`;
- `GraphCycleException`;
- `AiRouteGenerationException`;
- `AiRouteValidationException`;
- `AuthException`.

Frontend `api.js` приводит ошибочный ответ к `ApiError`, показывает сообщение пользователю и при 401 пытается обновить токены через refresh.

## Архитектура базы данных

Схема находится в `src/main/resources/schema.sql`.

### Пользователи

#### `users`

Хранит аккаунты:

- `user_id`;
- `email`;
- `password_hash`;
- `display_name`;
- `created_at`.

#### `user_preferences`

Legacy-таблица пользовательских предпочтений. В текущем UI не используется, но сохранена в схеме для совместимости.

#### `user_preferred_resource_types`

Legacy-таблица предпочтительных типов ресурсов. В текущем UI не используется.

#### `user_known_topics`

Таблица известных пользователю тем. В текущем основном frontend известные темы передаются в запрос построения плана и сохраняются в draft на клиенте, но таблица сохранена в схеме как часть модели.

### База знаний

#### `topics`

Темы roadmap:

- код;
- название;
- описание;
- уровень;
- core flag;
- оценка часов;
- статус.

#### `topic_prereqs`

Зависимости между темами:

- prerequisite topic;
- next topic;
- тип связи: `REQUIRED` или `RECOMMENDED`.

#### `role_goals`

Направления/roadmap:

- код;
- название;
- описание;
- статус.

#### `role_topics`

Связь направления и темы:

- `role_id`;
- `topic_id`;
- priority;
- required flag.

#### `resources`

Учебные материалы:

- название;
- URL;
- тип;
- язык;
- длительность;
- provider;
- difficulty;
- статус.

#### `topic_resources`

Связь темы и ресурса с rank.

#### `tags` и `topic_tags`

Теги и связь тем с тегами.

### Планы

#### `plans`

Сохранённые планы пользователя:

- пользователь;
- направление;
- статус;
- base plan;
- scenario type;
- scenario label;
- даты создания и обновления.

Статус `DELETED` используется для soft delete.

#### `plan_params_snapshots`

Snapshot параметров построения:

- `hours_per_week`;
- language preferences;
- resource type preferences;
- algo version.

#### `plan_weeks`

Недели плана:

- номер недели;
- бюджет часов;
- запланированные часы.

#### `plan_steps`

Шаги внутри недели:

- тема;
- порядок;
- часы;
- optional flag.

#### `plan_step_resources`

Связь шага плана и ресурсов.

#### `plan_step_explanations`

Объяснение, почему тема попала в план.

#### `plan_step_explanation_prereqs`

Статусы prerequisites для объяснения шага.

### Прогресс

#### `topic_progress`

Прогресс темы в рамках конкретного плана:

- plan;
- topic;
- status;
- updated_at.

### Quiz

#### `quizzes`

Quiz по теме.

#### `quiz_questions`

Вопросы quiz.

#### `quiz_options`

Варианты ответов.

#### `quiz_attempts`

Попытки пользователя и результат.

## Импорт roadmap-контента

В проекте есть локальные manifest-файлы в `src/main/resources/roadmap-sh`.

### Catalog manifest

`src/main/resources/roadmap-sh/catalog.json` описывает список направлений.

### Roadmap manifests

`src/main/resources/roadmap-sh/roadmaps/*.json` описывают отдельные roadmap:

- role code;
- название;
- описание;
- темы;
- связи с ресурсами;
- prerequisites.

### Sync-сервисы

- `ClasspathRoadmapShCatalogSource` читает catalog manifest;
- `ClasspathRoadmapShRoadmapSource` читает roadmap manifests;
- `RoadmapShCatalogSyncService` синхронизирует направления;
- `RoadmapShRoadmapSyncService` синхронизирует темы, связи и ресурсы;
- `RoadmapShCatalogSyncRunner` запускает catalog sync при включённой настройке;
- `RoadmapShRoadmapSyncRunner` запускает roadmap sync при включённой настройке;
- `RoadmapShBootstrapRunner` может запускать bootstrap-режим.

### Текущий режим запуска

В обычном `application.yaml` синхронизация выключена:

- `app.roadmap-sh.bootstrap.enabled: false`;
- `app.roadmap-sh.catalog-sync.enabled: false`;
- `app.roadmap-sh.roadmap-sync.enabled: false`.

Это значит, что обычный старт приложения не пересобирает KB. Приложение работает с уже подготовленной локальной БД.

Для отдельного bootstrap-режима есть `application-roadmap-bootstrap.yaml`. В нём включён bootstrap runner, а сервер запускается на случайном порту и без автооткрытия браузера.

## Конфигурация приложения

Основной файл: `src/main/resources/application.yaml`.

### Сервер

- порт: `8080`.

### База данных

Обычный запуск:

- `jdbc:h2:file:./db/aln_db`;
- PostgreSQL compatibility mode;
- `AUTO_SERVER=TRUE`;
- пользователь `aln`;
- пароль `aln`;
- `spring.sql.init.mode: never`;
- `spring.jpa.hibernate.ddl-auto: none`.

Тесты:

- `jdbc:h2:mem:aln_test`;
- `spring.sql.init.mode: always`;
- схема загружается из `schema.sql`.

### H2 Console

В dev-конфигурации H2 console включена:

- `/h2-console`.

### JWT

Настройки:

- `security.jwt.secret`;
- `security.jwt.issuer`;
- `security.jwt.access-ttl`;
- `security.jwt.refresh-ttl`.

По умолчанию dev-secret задаётся через fallback, но для нормального запуска секрет можно переопределить переменной `JWT_SECRET`.

### Browser auto-open

Компонент `BrowserLaunchOnStartup` пытается открыть сайт после старта приложения.

Настройки:

- `app.browser.auto-open`;
- `app.browser.url-path`.

Механизм:

- сначала пробует Desktop API;
- если Desktop API недоступен, использует системные команды;
- на Windows пробует `cmd /c start`, PowerShell `Start-Process`, `explorer.exe`;
- на macOS использует `open`;
- на Linux использует `xdg-open`.

## DTO и ответы API

### Roadmap responses

`RoadmapSummaryResponse`:

- id;
- code;
- name;
- category;
- category label;
- description;
- topic count;
- required topic count;
- total estimated hours.

`RoadmapDetailResponse` дополнительно содержит список тем.

`RoadmapTopicDetailResponse` содержит детали темы, prerequisites, unlocks, resources и quiz summary.

### Plan responses

`PlanShortResponse` используется для списков.

`PlanFullResponse` содержит:

- id;
- role info;
- status;
- scenario type;
- scenario label;
- base plan id;
- created at;
- params snapshot;
- weeks.

Недели содержат шаги, а шаги содержат тему, часы, ресурсы и explanation.

### Progress responses

`TopicProgressResponse` возвращает:

- plan id;
- topic id;
- status;
- updated at.

### User profile response

`UserProfileResponse` возвращает:

- id;
- email;
- display name;
- created at.

Frontend не обязан показывать все поля из ответа. Например internal id не выводится в пользовательском UI.

## Frontend-состояние

Frontend хранит часть состояния в `localStorage`.

Ключи:

- storage version;
- access token;
- refresh token;
- user profile;
- selected plan id;
- selected roadmap id;
- cached plans;
- cached roadmaps;
- cached roadmap topics;
- plan draft.

При смене `STORAGE_VERSION` сбрасывается кеш roadmap, чтобы пользователь не видел старые локализованные названия или скрытые roadmap из старого кеша.

## Дизайн-система

Основная палитра:

- `#326273` — основной фон;
- `#FFFFFF` — карточки и рабочие поверхности;
- `#000000` — основной текст и сильные акценты;
- `#5C9EAD` — активные действия и акцентные элементы.

Подход к UI:

- лаконичные карточки;
- крупная типографика на главной;
- roadmap как главный визуальный объект;
- минимум служебной информации для пользователя;
- русскоязычные формулировки;
- технические коды скрываются из UI;
- sidebar можно свернуть;
- приватные страницы используют общий shell.

## Автотесты

Тесты лежат в `src/test/java`.

Покрытые зоны:

- старт Spring context;
- auth integration;
- auth service;
- JWT service;
- JWT filter;
- security handlers;
- security integration;
- global exception handler;
- page routing;
- plan controller;
- plan service;
- roadmap controller;
- roadmap service;
- roadmap/plan integration на KB;
- quiz service;
- AI prompt builder;
- AI route generation service;
- AI route validation;
- AI Tutor service;
- roadmap.sh catalog source;
- roadmap.sh roadmap source;
- catalog sync service;
- roadmap sync service;
- localization util;
- persistence shared primary keys;
- enum JSON serialization.

Примеры тестовых классов:

- `AuthIntegrationTest`;
- `AuthServiceImplTest`;
- `JwtServiceImplTest`;
- `SecurityIntegrationTest`;
- `PlanControllerTest`;
- `PlanServiceImplTest`;
- `RoadmapControllerTest`;
- `RoadmapServiceImplTest`;
- `RoadmapPlanVerificationIntegrationTest`;
- `QuizServiceImplTest`;
- `AiTutorServiceImplTest`;
- `KnowledgeBaseLocalizationUtilTest`;
- `PageControllerTest`.

## Ручное API-тестирование

Для Postman есть:

- `docs/postman/AdapriveLearningNavigator.postman_collection.json`;
- `docs/postman/AdapriveLearningNavigator.local.postman_environment.json`;
- `docs/postman/manual_api_seed.sql`;
- `docs/postman/README.md`.

Коллекция покрывает регистрацию, вход, refresh, проверки планов и live AI-сценарий.

## Запуск проекта

### Обычный запуск

```bash
./mvnw spring-boot:run
```

На Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

После старта приложение доступно по адресу:

```text
http://localhost:8080
```

Если `app.browser.auto-open=true`, приложение попытается открыть браузер автоматически.

### Запуск тестов

```bash
./mvnw test
```

В текущей локальной среде также использовался прямой запуск Maven из wrapper cache с workspace-local Maven repository:

```powershell
& "$env:USERPROFILE\.m2\wrapper\dists\apache-maven-3.9.12-bin\5nmfsn99br87k5d4ajlekdq10k\apache-maven-3.9.12\bin\mvn.cmd" test "-Dmaven.repo.local=.m2/repository"
```

## Технические решения, которые важно знать

### Почему roadmap хранится в БД, а не только в JSON

JSON manifest используется как источник наполнения. После импорта данные живут в нормализованной БД. Это нужно, чтобы:

- переиспользовать темы;
- строить зависимости;
- строить weekly plan;
- связывать темы с ресурсами;
- хранить прогресс пользователя;
- не зависеть от AI при построении основного маршрута.

### Почему прогресс находится в плане

Прогресс привязан не просто к теме, а к конкретному плану. Один и тот же пользователь может построить разные планы по одному направлению, поэтому статус темы хранится в контексте `plan_id + topic_id`.

### Почему удаление плана мягкое

Soft delete сохраняет данные плана и связанные сущности. Это безопаснее для локальной БД и не ломает внешние связи между неделями, шагами, progress и explanation.

### Почему AI Tutor не сохраняет историю в БД

В текущем MVP история чата нужна только в рамках текущей работы с темой на странице. Поэтому она хранится на frontend в памяти страницы. Backend получает последние сообщения в запросе и отвечает с учётом этой истории.

### Почему Quiz API оставлен

Пользовательский UI проверки знаний перенесён в AI Tutor, но backend Quiz API реализован и протестирован. Он оставлен как рабочий слой, потому что связан с таблицами quiz и попытками пользователя.

### Почему в каталоге показывается не всё, что есть в KB

В KB может быть больше roadmap, чем нужно пользователю в MVP. Каталог показывает curated-набор, чтобы интерфейс не перегружался лишними или экспериментальными направлениями.

### Почему локализация сделана на read-model уровне

Локализация в `KnowledgeBaseLocalizationUtil` позволяет адаптировать user-facing названия и описания без массового ручного переписывания всех записей БД. Канонические технические коды и исходные названия при этом остаются доступными внутри системы.

## Что не выводится пользователю

В UI намеренно скрываются:

- JWT payload;
- internal user id в профиле;
- технические коды `RM_...`;
- rule-коды вроде `AI_LED_ROUTE_BACKEND_VALIDATED`;
- подписи о происхождении контента из roadmap.sh;
- служебная информация о backend-реализации.

Эти данные могут существовать внутри API или БД, но интерфейс старается показывать пользователю понятные русскоязычные формулировки.

## Правило поддержки документации

`README.md` является главным файлом документации проекта.

При изменении функционала, API, схемы БД, frontend-структуры, AI Tutor, планировщика или roadmap-логики README нужно обновлять вместе с кодом.
