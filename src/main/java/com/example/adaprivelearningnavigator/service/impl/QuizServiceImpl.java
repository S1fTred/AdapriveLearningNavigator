package com.example.adaprivelearningnavigator.service.impl;

import com.example.adaprivelearningnavigator.domain.enums.EntityStatus;
import com.example.adaprivelearningnavigator.domain.enums.QuizQuestionType;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.Topic;
import com.example.adaprivelearningnavigator.domain.quizAndProgress.Quiz;
import com.example.adaprivelearningnavigator.domain.quizAndProgress.QuizAttempt;
import com.example.adaprivelearningnavigator.domain.quizAndProgress.QuizOption;
import com.example.adaprivelearningnavigator.domain.quizAndProgress.QuizQuestion;
import com.example.adaprivelearningnavigator.domain.userPart.User;
import com.example.adaprivelearningnavigator.repo.QuizAttemptRepository;
import com.example.adaprivelearningnavigator.repo.QuizOptionRepository;
import com.example.adaprivelearningnavigator.repo.QuizQuestionRepository;
import com.example.adaprivelearningnavigator.repo.QuizRepository;
import com.example.adaprivelearningnavigator.repo.TopicRepository;
import com.example.adaprivelearningnavigator.repo.UserRepository;
import com.example.adaprivelearningnavigator.service.QuizService;
import com.example.adaprivelearningnavigator.service.dto.quiz.QuizAttemptResponse;
import com.example.adaprivelearningnavigator.service.dto.quiz.QuizOptionResponse;
import com.example.adaprivelearningnavigator.service.dto.quiz.QuizQuestionResponse;
import com.example.adaprivelearningnavigator.service.dto.quiz.QuizResponse;
import com.example.adaprivelearningnavigator.service.dto.quiz.QuizSubmitRequest;
import com.example.adaprivelearningnavigator.service.exception.BadRequestException;
import com.example.adaprivelearningnavigator.service.exception.NotFoundException;
import com.example.adaprivelearningnavigator.service.support.KnowledgeBaseLocalizationUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final QuizQuestionRepository questionRepository;
    private final QuizOptionRepository optionRepository;
    private final QuizAttemptRepository attemptRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;

    public QuizServiceImpl(QuizRepository quizRepository,
                           QuizQuestionRepository questionRepository,
                           QuizOptionRepository optionRepository,
                           QuizAttemptRepository attemptRepository,
                           TopicRepository topicRepository,
                           UserRepository userRepository) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
        this.attemptRepository = attemptRepository;
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public QuizResponse getTopicQuiz(Long topicId) {
        Quiz quiz = quizRepository.findByTopic_Id(topicId)
                .orElseGet(() -> createGeneratedQuiz(topicId));
        if (generatedQuizNeedsRefresh(quiz)) {
            refreshGeneratedQuiz(quiz);
        }
        return toQuizResponse(quiz, true);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuizQuestionResponse> getQuizQuestions(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new NotFoundException("Квиз не найден"));
        return questionsFor(quiz);
    }

    @Override
    @Transactional
    public QuizAttemptResponse submitQuiz(Long userId, QuizSubmitRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Quiz quiz = quizRepository.findById(request.quizId())
                .orElseThrow(() -> new NotFoundException("Квиз не найден"));

        List<QuizQuestion> questions = questionRepository.findAllByQuiz_IdOrderByOrderIndexAsc(quiz.getId());
        if (questions.isEmpty()) {
            throw new BadRequestException("В квизе пока нет вопросов");
        }

        Set<Long> selectedOptionIds = request.selectedOptionIds();
        int correctCount = 0;
        for (QuizQuestion question : questions) {
            Set<Long> correctOptionIds = optionRepository.findAllByQuestion_Id(question.getId()).stream()
                    .filter(QuizOption::isCorrect)
                    .map(QuizOption::getId)
                    .collect(java.util.stream.Collectors.toSet());
            Set<Long> selectedForQuestion = optionRepository.findAllByQuestion_Id(question.getId()).stream()
                    .map(QuizOption::getId)
                    .filter(selectedOptionIds::contains)
                    .collect(java.util.stream.Collectors.toSet());
            if (selectedForQuestion.equals(correctOptionIds)) {
                correctCount++;
            }
        }

        BigDecimal score = BigDecimal.valueOf(correctCount)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(questions.size()), 2, RoundingMode.HALF_UP);

        QuizAttempt attempt = attemptRepository.save(QuizAttempt.builder()
                .user(user)
                .quiz(quiz)
                .score(score)
                .correctCount(correctCount)
                .totalCount(questions.size())
                .submittedAt(Instant.now())
                .build());

        return toAttemptResponse(attempt);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuizAttemptResponse> getAttempts(Long userId, Long quizId) {
        if (quizId == null) {
            return attemptRepository.findAllByUser_IdOrderBySubmittedAtDesc(userId).stream()
                    .map(this::toAttemptResponse)
                    .toList();
        }
        return attemptRepository.findAllByUser_IdAndQuiz_IdOrderBySubmittedAtDesc(userId, quizId).stream()
                .map(this::toAttemptResponse)
                .toList();
    }

    private Quiz createGeneratedQuiz(Long topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NotFoundException("Тема не найдена"));
        String title = KnowledgeBaseLocalizationUtil.localizeTopicTitle(topic.getCode(), topic.getTitle());
        Quiz quiz = quizRepository.save(Quiz.builder()
                .topic(topic)
                .title("Проверка по теме: " + title)
                .status(EntityStatus.ACTIVE)
                .build());

        List<GeneratedQuestion> questions = generateQuestionsFor(topic, title);
        for (int index = 0; index < questions.size(); index++) {
            GeneratedQuestion question = questions.get(index);
            createSingleQuestion(quiz, index + 1, question.text(), question.correct(), question.firstWrong(), question.secondWrong());
        }

        return quiz;
    }

    private boolean generatedQuizNeedsRefresh(Quiz quiz) {
        if (quiz.getTitle() == null || !quiz.getTitle().startsWith("Проверка по теме:")) {
            return false;
        }

        return questionRepository.findAllByQuiz_IdOrderByOrderIndexAsc(quiz.getId()).stream()
                .map(QuizQuestion::getText)
                .anyMatch(text -> text != null && (
                        text.startsWith("Что лучше всего сделать при изучении темы")
                                || text.startsWith("Как понять, что тема")
                                || text.startsWith("Какой формат работы обычно полезнее")
                                || text.startsWith("Что в теме «")
                                || text.startsWith("Какой вопрос лучше задать себе после изучения темы")
                                || text.startsWith("На что стоит обратить внимание по описанию темы")
                ));
    }

    private void refreshGeneratedQuiz(Quiz quiz) {
        List<QuizQuestion> oldQuestions = questionRepository.findAllByQuiz_IdOrderByOrderIndexAsc(quiz.getId());
        for (QuizQuestion question : oldQuestions) {
            optionRepository.deleteAll(optionRepository.findAllByQuestion_Id(question.getId()));
        }
        questionRepository.deleteAll(oldQuestions);

        Topic topic = quiz.getTopic();
        String title = KnowledgeBaseLocalizationUtil.localizeTopicTitle(topic.getCode(), topic.getTitle());
        List<GeneratedQuestion> questions = generateQuestionsFor(topic, title);
        for (int index = 0; index < questions.size(); index++) {
            GeneratedQuestion question = questions.get(index);
            createSingleQuestion(quiz, index + 1, question.text(), question.correct(), question.firstWrong(), question.secondWrong());
        }
    }

    private List<GeneratedQuestion> generateQuestionsFor(Topic topic, String title) {
        String fingerprint = normalizeForMatching(topic.getCode() + " " + topic.getTitle() + " " + title + " " + topic.getDescription());
        for (QuizTemplate template : quizTemplates()) {
            if (template.matches(fingerprint)) {
                return template.questions(title);
            }
        }
        return contextualQuestions(title, topic.getDescription());
    }

    private List<QuizTemplate> quizTemplates() {
        return List.of(
                template(List.of("string", "строк", "methods", "метод"), title -> List.of(
                        question("Что важно помнить про строки в Java?",
                                "String неизменяемый: методы возвращают новую строку, а не меняют исходную",
                                "String всегда изменяется на месте без создания нового объекта",
                                "Строки нельзя сравнивать и передавать в методы"),
                        question("Как корректно сравнить содержимое двух строк в Java?",
                                "Использовать equals(), потому что он сравнивает содержимое строки",
                                "Использовать ==, потому что он всегда сравнивает текст",
                                "Сравнить длины строк, этого достаточно"),
                        question("Что обычно делает метод substring()?",
                                "Возвращает часть строки по указанным индексам",
                                "Сортирует символы строки по алфавиту",
                                "Удаляет строку из памяти")
                )),
                template(List.of("array", "массив"), title -> List.of(
                        question("Как устроен массив в большинстве языков программирования?",
                                "Это индексированная последовательность элементов одного типа или совместимой структуры",
                                "Это неупорядоченный набор пар ключ-значение",
                                "Это функция, которая возвращает несколько значений"),
                        question("С какого индекса обычно начинается доступ к элементам массива в Java, JavaScript, Python и C-подобных языках?",
                                "С 0",
                                "С 1",
                                "С длины массива"),
                        question("Что означает выход за границы массива?",
                                "Попытку обратиться к индексу, которого в массиве нет",
                                "Создание нового элемента в конце массива в любом языке",
                                "Автоматическую сортировку массива")
                )),
                template(List.of("loop", "цикл"), title -> List.of(
                        question("Для чего используются циклы?",
                                "Для повторного выполнения блока кода, пока выполняется условие или пока есть элементы",
                                "Только для объявления переменных",
                                "Только для обработки ошибок"),
                        question("Чем while отличается от for в типичном сценарии?",
                                "while удобен, когда число повторений заранее неизвестно",
                                "while всегда выполняется ровно один раз",
                                "for нельзя использовать для перебора коллекций"),
                        question("Что чаще всего приводит к бесконечному циклу?",
                                "Условие цикла никогда не становится false",
                                "Внутри цикла есть переменная",
                                "Цикл содержит больше одной строки кода")
                )),
                template(List.of("condition", "услов"), title -> List.of(
                        question("Что проверяет условная конструкция if?",
                                "Логическое выражение, по результату которого выбирается ветка выполнения",
                                "Тип файла на диске",
                                "Скорость выполнения программы"),
                        question("Когда уместен switch/case?",
                                "Когда нужно выбрать одну ветку из нескольких известных вариантов значения",
                                "Когда нужно выполнить код бесконечно",
                                "Когда нужно создать объект без конструктора"),
                        question("Что такое ветка else?",
                                "Код, который выполняется, если условие if не выполнено",
                                "Код, который выполняется перед любым if",
                                "Специальный тип переменной")
                )),
                template(List.of("oop", "object oriented", "объект", "class", "класс"), title -> List.of(
                        question("Что описывает класс в ООП?",
                                "Шаблон объекта: состояние и поведение будущих экземпляров",
                                "Только готовый файл с настройками",
                                "Только цикл выполнения программы"),
                        question("Что такое инкапсуляция?",
                                "Сокрытие внутреннего состояния объекта и доступ к нему через контролируемый интерфейс",
                                "Запрет на создание методов в классе",
                                "Автоматическое удаление всех полей объекта"),
                        question("Что такое наследование?",
                                "Механизм, при котором один класс может расширять поведение другого",
                                "Сравнение двух строк по содержимому",
                                "Подключение базы данных к приложению")
                )),
                template(List.of("exception", "исключен"), title -> List.of(
                        question("Для чего нужны исключения?",
                                "Для обработки ошибочных ситуаций без смешивания основной логики с аварийными сценариями",
                                "Для ускорения обычных арифметических операций",
                                "Для замены всех условных операторов"),
                        question("Что делает блок catch?",
                                "Перехватывает исключение подходящего типа и позволяет его обработать",
                                "Всегда завершает приложение без обработки",
                                "Создаёт новый поток выполнения"),
                        question("Зачем используется finally?",
                                "Чтобы выполнить код очистки ресурсов независимо от результата try/catch",
                                "Чтобы запретить выполнение try",
                                "Чтобы скрыть исключение от JVM во всех случаях")
                )),
                template(List.of("http", "rest", "api"), title -> List.of(
                        question("Что описывает HTTP-метод GET?",
                                "Получение ресурса без изменения состояния сервера",
                                "Удаление ресурса на сервере",
                                "Шифрование пароля пользователя"),
                        question("Что обычно означает статус 404?",
                                "Запрошенный ресурс не найден",
                                "Запрос успешно создан",
                                "Клиент не авторизован из-за истекшего токена"),
                        question("Что важно для REST API?",
                                "Работать с ресурсами через понятные URI и стандартные HTTP-методы",
                                "Хранить всю бизнес-логику только на клиенте",
                                "Использовать только один endpoint для всех действий")
                )),
                template(List.of("sql", "database", "баз", "postgres", "mysql"), title -> List.of(
                        question("Для чего используется SELECT в SQL?",
                                "Для чтения данных из таблиц",
                                "Для удаления структуры всей базы",
                                "Для создания HTTP-запроса"),
                        question("Что делает JOIN?",
                                "Объединяет строки из нескольких таблиц по связанным данным",
                                "Создаёт новый язык программирования",
                                "Запускает транзакцию без запроса"),
                        question("Зачем нужны индексы в базе данных?",
                                "Чтобы ускорять поиск и фильтрацию данных по выбранным полям",
                                "Чтобы хранить пароли в открытом виде",
                                "Чтобы автоматически переводить SQL-запросы")
                )),
                template(List.of("transaction", "транзакц"), title -> List.of(
                        question("Что гарантирует транзакция?",
                                "Группу операций, которая должна выполниться целиком или откатиться",
                                "Автоматическое ускорение любого запроса",
                                "Создание новой таблицы при каждом SELECT"),
                        question("Что означает rollback?",
                                "Откат изменений транзакции",
                                "Подключение к удалённому серверу",
                                "Удаление индекса без проверки"),
                        question("Что относится к ACID?",
                                "Атомарность, согласованность, изоляция и долговечность",
                                "HTML, CSS, JavaScript и SQL",
                                "Только скорость выполнения запроса")
                )),
                template(List.of("git", "github"), title -> List.of(
                        question("Что фиксирует git commit?",
                                "Снимок изменений в репозитории с сообщением",
                                "Только запуск приложения",
                                "Удаление всей истории проекта"),
                        question("Для чего используется git branch?",
                                "Для параллельной работы над независимой линией изменений",
                                "Для компиляции Java-кода",
                                "Для хранения пароля от базы данных"),
                        question("Что делает git merge?",
                                "Объединяет изменения из одной ветки в другую",
                                "Скачивает зависимости Maven",
                                "Открывает pull request без изменений")
                )),
                template(List.of("docker", "container", "контейнер"), title -> List.of(
                        question("Что изолирует Docker-контейнер?",
                                "Процесс приложения вместе с его окружением и зависимостями",
                                "Только монитор пользователя",
                                "Только исходный код без runtime"),
                        question("Для чего нужен Dockerfile?",
                                "Чтобы описать шаги сборки образа приложения",
                                "Чтобы хранить пользовательские JWT-токены",
                                "Чтобы заменить систему контроля версий"),
                        question("Чем образ отличается от контейнера?",
                                "Образ является шаблоном, а контейнер — запущенным экземпляром образа",
                                "Контейнер всегда существует только до сборки образа",
                                "Между ними нет разницы")
                )),
                template(List.of("kubernetes", "k8s"), title -> List.of(
                        question("Что делает Kubernetes?",
                                "Оркестрирует контейнеры: запуск, масштабирование, обновление и восстановление",
                                "Компилирует JavaScript в машинный код",
                                "Заменяет систему контроля версий"),
                        question("Что такое Pod?",
                                "Минимальная единица запуска в Kubernetes, содержащая один или несколько контейнеров",
                                "Файл с SQL-запросом",
                                "Тип HTTP-метода"),
                        question("Зачем нужен Service в Kubernetes?",
                                "Чтобы дать стабильный сетевой доступ к группе Pod'ов",
                                "Чтобы хранить только секреты в браузере",
                                "Чтобы удалить все контейнеры после деплоя")
                )),
                template(List.of("spring", "spring boot"), title -> List.of(
                        question("Что даёт Spring Boot?",
                                "Быстрый старт Spring-приложения с автоконфигурацией и встроенным сервером",
                                "Замену JVM на браузер",
                                "Автоматическое написание всех тестов без участия разработчика"),
                        question("Что такое dependency injection в Spring?",
                                "Передача зависимостей объекту контейнером, а не ручное создание внутри класса",
                                "Удаление всех зависимостей из проекта",
                                "Скачивание Docker-образа при старте"),
                        question("Для чего часто используется @RestController?",
                                "Для объявления класса, который обрабатывает HTTP-запросы и возвращает данные",
                                "Для создания таблицы в базе данных",
                                "Для настройки CSS-стилей")
                )),
                template(List.of("jpa", "hibernate", "orm"), title -> List.of(
                        question("Что решает ORM?",
                                "Связывает объектную модель приложения с таблицами реляционной базы данных",
                                "Заменяет HTTP на FTP",
                                "Удаляет необходимость проектировать данные"),
                        question("Что обозначает JPA Entity?",
                                "Класс, который отображается на таблицу или запись в базе данных",
                                "CSS-компонент интерфейса",
                                "Только временный объект без сохранения"),
                        question("Что может вызвать N+1 проблему?",
                                "Ленивая загрузка связанных данных отдельным запросом для каждой записи",
                                "Использование одного SELECT без связей",
                                "Слишком короткое название таблицы")
                )),
                template(List.of("html", "semantic"), title -> List.of(
                        question("Для чего нужен HTML?",
                                "Для описания структуры содержимого веб-страницы",
                                "Для настройки сетевого firewall",
                                "Для хранения транзакций базы данных"),
                        question("Что даёт семантическая разметка?",
                                "Лучшее понимание структуры страницы браузером, поисковиками и assistive-технологиями",
                                "Автоматическое шифрование всех данных",
                                "Ускорение SQL-запросов"),
                        question("Какой тег обычно обозначает основное содержимое страницы?",
                                "<main>",
                                "<random>",
                                "<database>")
                )),
                template(List.of("css", "flexbox", "grid"), title -> List.of(
                        question("Для чего используется CSS?",
                                "Для визуального оформления HTML-структуры",
                                "Для выполнения SQL-запросов",
                                "Для компиляции Java-классов"),
                        question("Что удобно делать с Flexbox?",
                                "Выравнивать элементы в одном направлении и распределять пространство",
                                "Создавать таблицы в базе данных",
                                "Подписывать JWT-токены"),
                        question("Когда особенно полезен CSS Grid?",
                                "Когда нужна двумерная сетка строк и колонок",
                                "Когда нужно обработать исключение в Java",
                                "Когда нужно выполнить git merge")
                )),
                template(List.of("javascript", "typescript", "node.js", "nodejs"), title -> List.of(
                        question("Что такое асинхронность в JavaScript?",
                                "Способ выполнять долгие операции без блокировки основного потока выполнения",
                                "Запрет на использование функций",
                                "Автоматическая типизация всех переменных как number"),
                        question("Что делает Promise?",
                                "Представляет результат асинхронной операции, который появится позже",
                                "Создаёт HTML-разметку без браузера",
                                "Удаляет все ошибки из кода"),
                        question("Что добавляет TypeScript поверх JavaScript?",
                                "Статическую типизацию и проверку типов на этапе разработки",
                                "Только новый браузерный движок",
                                "Автоматическую настройку базы данных")
                )),
                template(List.of("react", "vue", "angular"), title -> List.of(
                        question("Что обычно решает frontend-фреймворк или библиотека компонентов?",
                                "Помогает строить интерфейс из переиспользуемых компонентов и управлять состоянием",
                                "Заменяет базу данных на HTML",
                                "Настраивает firewall операционной системы"),
                        question("Что такое компонент?",
                                "Изолированная часть интерфейса со своей разметкой, логикой и состоянием",
                                "Только SQL-таблица",
                                "Файл с паролем пользователя"),
                        question("Зачем нужен state?",
                                "Чтобы хранить данные, от которых зависит отображение интерфейса",
                                "Чтобы навсегда отключить перерисовку страницы",
                                "Чтобы создать Docker-образ")
                )),
                template(List.of("python", "java", "kotlin", "go", "rust", "c++", "php", "ruby", "swift"), title -> List.of(
                        question("Что важно понимать при изучении языка программирования?",
                                "Синтаксис, типы данных, управление потоком, функции и работу с ошибками",
                                "Только логотип языка",
                                "Только историю создания языка без практики"),
                        question("Для чего нужны функции или методы?",
                                "Чтобы выделять повторяемую логику в именованные блоки кода",
                                "Чтобы хранить только изображения",
                                "Чтобы удалить все переменные"),
                        question("Почему важно знать стандартную библиотеку языка?",
                                "Она даёт готовые инструменты для типовых задач без лишнего кода",
                                "Она нужна только для смены темы IDE",
                                "Она запрещает писать собственные классы")
                )),
                template(List.of("linux", "bash", "shell", "command line", "cli"), title -> List.of(
                        question("Для чего нужна командная строка?",
                                "Для управления системой, файлами и инструментами через текстовые команды",
                                "Только для рисования интерфейсов",
                                "Только для хранения паролей"),
                        question("Что делает pipe | в shell?",
                                "Передаёт вывод одной команды на вход другой",
                                "Удаляет текущую директорию",
                                "Запускает браузер в режиме базы данных"),
                        question("Зачем понимать права доступа в Linux?",
                                "Чтобы контролировать, кто может читать, изменять и запускать файлы",
                                "Чтобы ускорять JavaScript-анимации",
                                "Чтобы заменить Git")
                )),
                template(List.of("security", "jwt", "oauth", "auth"), title -> List.of(
                        question("Что обычно подтверждает authentication?",
                                "Личность пользователя или сервиса",
                                "Количество строк в базе данных",
                                "Скорость загрузки CSS"),
                        question("Что обычно определяет authorization?",
                                "Какие действия разрешены уже опознанному пользователю",
                                "Какой шрифт использовать в интерфейсе",
                                "Сколько памяти занимает Docker-образ"),
                        question("Что хранит JWT?",
                                "Подписанные claims, которые можно проверить без обращения к сессии на сервере",
                                "Пароль пользователя в открытом виде как обязательное поле",
                                "Только HTML-разметку страницы")
                )),
                template(List.of("test", "testing", "qa"), title -> List.of(
                        question("Зачем нужны автоматические тесты?",
                                "Чтобы быстро проверять поведение кода и снижать риск регрессий",
                                "Чтобы полностью заменить проектирование архитектуры",
                                "Чтобы скрыть ошибки компиляции"),
                        question("Что проверяет unit-тест?",
                                "Небольшую изолированную часть логики",
                                "Только внешний вид продакшн-сайта",
                                "Работу всей инфраструктуры компании"),
                        question("Что такое regression bug?",
                                "Ошибка, которая появилась в уже работавшем функционале после изменений",
                                "Любой новый feature request",
                                "Только ошибка в названии переменной")
                )),
                template(List.of("algorithm", "алгоритм", "data structure", "структур"), title -> List.of(
                        question("Что описывает алгоритм?",
                                "Последовательность шагов для решения задачи",
                                "Только название переменной",
                                "Только внешний вид интерфейса"),
                        question("Зачем оценивать сложность алгоритма?",
                                "Чтобы понимать, как время и память растут при увеличении входных данных",
                                "Чтобы выбрать цвет кнопки",
                                "Чтобы заменить все циклы рекурсией"),
                        question("Что показывает Big O?",
                                "Асимптотический рост затрат алгоритма",
                                "Точный размер файла в байтах",
                                "Количество классов в проекте")
                )),
                template(List.of("machine learning", "ml", "llm", "ai", "нейрос", "эмбеддинг"), title -> List.of(
                        question("Что обычно означает обучение модели?",
                                "Подбор параметров модели на данных, чтобы она лучше решала задачу",
                                "Ручное написание всех ответов модели в коде",
                                "Удаление датасета после первого запуска"),
                        question("Зачем разделяют train и test данные?",
                                "Чтобы проверить, как модель работает на данных, которых не видела при обучении",
                                "Чтобы ускорить открытие браузера",
                                "Чтобы заменить признаки случайными строками"),
                        question("Что такое embedding?",
                                "Числовое представление объекта, сохраняющее смысловую близость",
                                "HTML-тег для таблицы",
                                "Тип SQL-транзакции")
                )),
                template(List.of("variable", "scope", "переменн", "област"), title -> List.of(
                        question("Что определяет область видимости переменной?",
                                "Часть программы, где переменную можно использовать по имени",
                                "Количество памяти, которое всегда занимает программа",
                                "Скорость выполнения любого цикла"),
                        question("Почему важно давать переменным понятные имена?",
                                "Так код быстрее читается и меньше зависит от комментариев",
                                "Так компилятор всегда делает программу быстрее",
                                "Так переменная автоматически становится глобальной"),
                        question("Что обычно плохо в лишних глобальных переменных?",
                                "Они создают скрытые зависимости и усложняют поиск ошибок",
                                "Они запрещены во всех языках программирования",
                                "Они не могут хранить числовые значения")
                )),
                template(List.of("collection", "list", "map", "set", "коллекц", "спис", "словар"), title -> List.of(
                        question("Когда уместно использовать список или массив?",
                                "Когда нужен упорядоченный набор элементов с доступом по позиции",
                                "Когда нужно хранить только одно булево значение",
                                "Когда нужно заменить все функции в программе"),
                        question("Что обычно хранит структура Map/Dictionary?",
                                "Пары ключ-значение для быстрого поиска значения по ключу",
                                "Только отсортированные числа без ключей",
                                "Только HTML-разметку страницы"),
                        question("Чем множество Set отличается от списка?",
                                "Set обычно хранит уникальные элементы без повторов",
                                "Set всегда хранит элементы в виде SQL-таблицы",
                                "Set нельзя использовать в прикладном коде")
                )),
                template(List.of("generic", "дженер", "type parameter"), title -> List.of(
                        question("Зачем нужны generics?",
                                "Чтобы писать обобщённый код с сохранением проверки типов",
                                "Чтобы отключать все ошибки компиляции",
                                "Чтобы автоматически запускать приложение в Docker"),
                        question("Где generics встречаются особенно часто?",
                                "В коллекциях, репозиториях, API-ответах и переиспользуемых компонентах",
                                "Только в CSS-файлах",
                                "Только в SQL-индексах"),
                        question("Что помогает избежать типизация через generics?",
                                "Лишних небезопасных приведений типов и части runtime-ошибок",
                                "Необходимости писать тесты",
                                "Любой сетевой задержки")
                )),
                template(List.of("json", "xml", "yaml"), title -> List.of(
                        question("Для чего обычно используют JSON?",
                                "Для передачи структурированных данных между клиентом, сервером и сервисами",
                                "Для компиляции Java-классов",
                                "Для описания стилей страницы вместо CSS"),
                        question("Где часто встречается YAML?",
                                "В конфигурации приложений, CI/CD и инфраструктурных файлов",
                                "Только внутри бинарных изображений",
                                "Только как формат паролей"),
                        question("Что важно при работе с форматами данных?",
                                "Понимать структуру, типы значений и правила сериализации",
                                "Всегда удалять все кавычки",
                                "Не проверять данные перед чтением")
                )),
                template(List.of("redis", "cache", "кэш"), title -> List.of(
                        question("Зачем используют кэш?",
                                "Чтобы быстрее отдавать часто запрашиваемые данные и снизить нагрузку на источник",
                                "Чтобы навсегда заменить основную базу данных в любом проекте",
                                "Чтобы автоматически исправлять ошибки бизнес-логики"),
                        question("Что важно контролировать при кэшировании?",
                                "Актуальность данных и правила инвалидирования кэша",
                                "Только цвет интерфейса",
                                "Только количество строк в README"),
                        question("Для чего часто применяют Redis?",
                                "Для кэша, временных данных, счётчиков, очередей и быстрых структур в памяти",
                                "Для компиляции TypeScript",
                                "Для верстки HTML-страниц")
                )),
                template(List.of("mongodb", "nosql"), title -> List.of(
                        question("Чем MongoDB отличается от классической реляционной таблицы?",
                                "Она хранит данные в документах с гибкой структурой",
                                "Она хранит только изображения без текста",
                                "Она не поддерживает поиск по данным"),
                        question("Что важно продумывать в NoSQL-модели?",
                                "Как приложение будет читать и обновлять данные в реальных сценариях",
                                "Только название коллекции без структуры документов",
                                "Только цвет логотипа базы данных"),
                        question("Зачем MongoDB нужны индексы?",
                                "Чтобы ускорять поиск по часто используемым полям",
                                "Чтобы заменить все документы на SQL-запросы",
                                "Чтобы запретить запись новых данных")
                )),
                template(List.of("graphql"), title -> List.of(
                        question("Что позволяет GraphQL-клиенту?",
                                "Запросить ровно те поля данных, которые нужны конкретному экрану",
                                "Запустить Docker-контейнер без образа",
                                "Скомпилировать Java-код в браузере"),
                        question("Что описывает GraphQL schema?",
                                "Типы данных, связи и доступные операции API",
                                "Только CSS-переменные интерфейса",
                                "Только настройки операционной системы"),
                        question("Какой риск часто обсуждают в GraphQL API?",
                                "Слишком тяжёлые или глубоко вложенные запросы без ограничений",
                                "Невозможность получать данные с сервера",
                                "Запрет на типизацию данных")
                )),
                template(List.of("ci/cd", "cicd", "pipeline", "github actions"), title -> List.of(
                        question("Что обычно делает CI?",
                                "Автоматически проверяет изменения: запускает сборку, тесты и статические проверки",
                                "Рисует интерфейс вместо frontend-разработчика",
                                "Хранит пароли пользователей в открытом виде"),
                        question("Что обычно добавляет CD?",
                                "Автоматизацию доставки или выката приложения в окружение",
                                "Запрет на тестирование кода",
                                "Удаление истории Git после каждого коммита"),
                        question("Почему pipeline должен быть воспроизводимым?",
                                "Чтобы одинаково проверять изменения у разных разработчиков и в CI-среде",
                                "Чтобы каждый запуск давал случайный результат",
                                "Чтобы не хранить зависимости проекта")
                )),
                template(List.of("microservice", "микросервис", "distributed"), title -> List.of(
                        question("Что является сильной стороной микросервисов?",
                                "Независимое развитие и масштабирование отдельных частей системы",
                                "Полное отсутствие сетевых ошибок",
                                "Автоматическое исчезновение всех транзакций"),
                        question("Какая сложность появляется при разделении на сервисы?",
                                "Сетевое взаимодействие, наблюдаемость, согласованность данных и деплой",
                                "Невозможность писать тесты",
                                "Запрет на использование баз данных"),
                        question("Почему границы сервисов важны?",
                                "Они определяют ответственность, владение данными и контракты взаимодействия",
                                "Они нужны только для красивой диаграммы",
                                "Они всегда выбираются случайно")
                )),
                template(List.of("android", "kotlin", "mobile"), title -> List.of(
                        question("Что важно учитывать в Android-разработке?",
                                "Жизненный цикл экранов, состояние приложения, разрешения и разные устройства",
                                "Только размер desktop-монитора",
                                "Только SQL-запросы без интерфейса"),
                        question("Для чего в Android часто используют Kotlin?",
                                "Для написания прикладной логики и UI-кода с современной типизацией",
                                "Для настройки CSS Grid в браузере",
                                "Для замены операционной системы"),
                        question("Почему мобильному приложению важна производительность?",
                                "Пользователь ожидает быстрый UI, экономию батареи и стабильную работу",
                                "Потому что сервер не может обрабатывать HTTP-запросы",
                                "Потому что Git не работает на мобильных проектах")
                )),
                template(List.of("swift", "ios", "swiftui"), title -> List.of(
                        question("Что даёт SwiftUI?",
                                "Декларативное описание интерфейса, который обновляется при изменении состояния",
                                "Автоматическую настройку SQL-индексов",
                                "Полную замену Git"),
                        question("Что важно понимать в iOS-разработке?",
                                "Жизненный цикл приложения, состояние экранов, работу с данными и платформенные ограничения",
                                "Только синтаксис HTML",
                                "Только команды Docker"),
                        question("Почему состояние важно для UI?",
                                "Потому что интерфейс должен предсказуемо отражать текущие данные приложения",
                                "Потому что состояние всегда хранится только в CSS",
                                "Потому что оно заменяет тестирование")
                )),
                template(List.of("unity", "game", "игр", "c#"), title -> List.of(
                        question("Что такое игровой цикл?",
                                "Повторяющийся процесс обновления состояния игры, обработки ввода и отрисовки кадра",
                                "SQL-транзакция для хранения пользователя",
                                "Команда Git для слияния веток"),
                        question("Для чего в Unity используются компоненты?",
                                "Чтобы добавлять объектам поведение и данные отдельными переиспользуемыми частями",
                                "Чтобы заменить все сцены одним текстовым файлом",
                                "Чтобы отключить физику движка"),
                        question("Почему в game development важна оптимизация?",
                                "Игра должна держать стабильный FPS и быстро реагировать на действия игрока",
                                "Потому что игры не используют память",
                                "Потому что C# не поддерживает классы")
                )),
                template(List.of("prompt", "agent", "ai agent"), title -> List.of(
                        question("Что важно в хорошем prompt?",
                                "Контекст, задача, ограничения, формат ответа и критерии качества",
                                "Только максимальная длина без смысла",
                                "Полное отсутствие примеров и цели"),
                        question("Что отличает AI agent от простого запроса к модели?",
                                "Агент может работать с контекстом, инструментами и последовательностью действий",
                                "Агент всегда работает без ограничений безопасности",
                                "Агент не использует языковую модель"),
                        question("Почему ответы AI нужно проверять?",
                                "Модель может ошибаться, упрощать факты или уверенно выдавать неточную информацию",
                                "Потому что модель никогда не отвечает на русском",
                                "Потому что проверка запрещена в production")
                )),
                template(List.of("ux", "ui", "design", "product", "research"), title -> List.of(
                        question("Что является целью UX-исследования?",
                                "Понять задачи, ожидания, боли и поведение пользователей",
                                "Подобрать случайный цвет без проверки",
                                "Заменить backend-разработку"),
                        question("Зачем нужна дизайн-система?",
                                "Чтобы интерфейс был последовательным, переиспользуемым и проще поддерживался командой",
                                "Чтобы каждый экран выглядел максимально по-разному",
                                "Чтобы убрать тестирование сценариев"),
                        question("Что важно в product management?",
                                "Связывать проблему пользователя, бизнес-цель, приоритеты и измеримый результат",
                                "Писать только технические классы без пользователей",
                                "Измерять успех количеством кнопок на странице")
                ))
        );
    }

    private List<GeneratedQuestion> contextualQuestions(String title, String description) {
        String cleanDescription = description == null || description.isBlank()
                ? "назначение темы, основные понятия и практическое применение"
                : KnowledgeBaseLocalizationUtil.localizeDescription(description, null, null, null, title);
        return List.of(
                question("Что в теме «" + title + "» нужно понять в первую очередь?",
                        "Назначение темы, основные понятия и ситуации, где она применяется",
                        "Только точное написание названия темы",
                        "Только количество часов в weekly plan"),
                question("Какой вопрос лучше задать себе после изучения темы «" + title + "»?",
                        "Как я применю эту тему в реальной задаче и какие ограничения у подхода?",
                        "Можно ли пропустить все связанные темы?",
                        "Как скрыть тему из roadmap без понимания?"),
                question("На что стоит обратить внимание по описанию темы?",
                        summarizeDescription(cleanDescription),
                        "На случайный порядок тем без зависимостей",
                        "Только на цвет карточки в интерфейсе")
        );
    }

    private GeneratedQuestion question(String text, String correct, String firstWrong, String secondWrong) {
        return new GeneratedQuestion(text, correct, firstWrong, secondWrong);
    }

    private QuizTemplate template(List<String> markers, QuizQuestionFactory factory) {
        return new QuizTemplate(markers, factory);
    }

    private String normalizeForMatching(String value) {
        return value == null
                ? ""
                : value.toLowerCase(Locale.ROOT).replace('_', ' ').replace('-', ' ');
    }

    private String summarizeDescription(String description) {
        String normalized = description.replaceAll("\\s+", " ").trim();
        if (normalized.length() <= 150) {
            return normalized;
        }
        return normalized.substring(0, 147).trim() + "...";
    }

    private void createSingleQuestion(Quiz quiz,
                                      int orderIndex,
                                      String text,
                                      String correct,
                                      String firstWrong,
                                      String secondWrong) {
        QuizQuestion question = questionRepository.save(QuizQuestion.builder()
                .quiz(quiz)
                .text(text)
                .type(QuizQuestionType.SINGLE)
                .orderIndex(orderIndex)
                .build());

        List<QuizOptionDraft> options = new ArrayList<>();
        if (orderIndex % 3 == 1) {
            options.add(new QuizOptionDraft(correct, true));
            options.add(new QuizOptionDraft(firstWrong, false));
            options.add(new QuizOptionDraft(secondWrong, false));
        } else if (orderIndex % 3 == 2) {
            options.add(new QuizOptionDraft(firstWrong, false));
            options.add(new QuizOptionDraft(correct, true));
            options.add(new QuizOptionDraft(secondWrong, false));
        } else {
            options.add(new QuizOptionDraft(firstWrong, false));
            options.add(new QuizOptionDraft(secondWrong, false));
            options.add(new QuizOptionDraft(correct, true));
        }

        for (QuizOptionDraft option : options) {
            optionRepository.save(QuizOption.builder()
                    .question(question)
                    .text(option.text())
                    .correct(option.correct())
                    .build());
        }
    }

    private QuizResponse toQuizResponse(Quiz quiz, boolean includeQuestions) {
        return QuizResponse.builder()
                .id(quiz.getId())
                .topicId(quiz.getTopic().getId())
                .title(quiz.getTitle())
                .status(quiz.getStatus())
                .questions(includeQuestions ? questionsFor(quiz) : List.of())
                .build();
    }

    private List<QuizQuestionResponse> questionsFor(Quiz quiz) {
        return questionRepository.findAllByQuiz_IdOrderByOrderIndexAsc(quiz.getId()).stream()
                .map(question -> QuizQuestionResponse.builder()
                        .id(question.getId())
                        .text(question.getText())
                        .type(question.getType())
                        .orderIndex(question.getOrderIndex())
                        .options(optionRepository.findAllByQuestion_Id(question.getId()).stream()
                                .map(option -> QuizOptionResponse.builder()
                                        .id(option.getId())
                                        .text(option.getText())
                                        .build())
                                .toList())
                        .build())
                .toList();
    }

    private QuizAttemptResponse toAttemptResponse(QuizAttempt attempt) {
        return QuizAttemptResponse.builder()
                .attemptId(attempt.getId())
                .quizId(attempt.getQuiz().getId())
                .score(attempt.getScore())
                .correctCount(attempt.getCorrectCount())
                .totalCount(attempt.getTotalCount())
                .submittedAt(attempt.getSubmittedAt())
                .build();
    }

    @FunctionalInterface
    private interface QuizQuestionFactory {
        List<GeneratedQuestion> questions(String title);
    }

    private record QuizTemplate(List<String> markers, QuizQuestionFactory factory) {
        boolean matches(String fingerprint) {
            return markers.stream().anyMatch(marker -> fingerprint.contains(marker.toLowerCase(Locale.ROOT)));
        }

        List<GeneratedQuestion> questions(String title) {
            return factory.questions(title);
        }
    }

    private record GeneratedQuestion(String text, String correct, String firstWrong, String secondWrong) {}

    private record QuizOptionDraft(String text, boolean correct) {}
}
