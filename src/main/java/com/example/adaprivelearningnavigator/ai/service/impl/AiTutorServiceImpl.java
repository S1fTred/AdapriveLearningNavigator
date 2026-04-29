package com.example.adaprivelearningnavigator.ai.service.impl;

import com.example.adaprivelearningnavigator.ai.dto.AiTutorRequest;
import com.example.adaprivelearningnavigator.ai.dto.AiTutorResponse;
import com.example.adaprivelearningnavigator.ai.service.AiTutorService;
import com.example.adaprivelearningnavigator.domain.enums.PrereqRelationType;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.Topic;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.TopicPrereq;
import com.example.adaprivelearningnavigator.repo.RoleGoalRepository;
import com.example.adaprivelearningnavigator.repo.RoleTopicRepository;
import com.example.adaprivelearningnavigator.repo.TopicPrereqRepository;
import com.example.adaprivelearningnavigator.service.exception.AiRouteGenerationException;
import com.example.adaprivelearningnavigator.service.exception.NotFoundException;
import com.example.adaprivelearningnavigator.service.support.KnowledgeBaseLocalizationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class AiTutorServiceImpl implements AiTutorService {

    private static final Logger log = LoggerFactory.getLogger(AiTutorServiceImpl.class);

    private final ChatClient chatClient;
    private final RoleGoalRepository roleGoalRepository;
    private final RoleTopicRepository roleTopicRepository;
    private final TopicPrereqRepository topicPrereqRepository;

    public AiTutorServiceImpl(ChatClient.Builder chatClientBuilder,
                              RoleGoalRepository roleGoalRepository,
                              RoleTopicRepository roleTopicRepository,
                              TopicPrereqRepository topicPrereqRepository) {
        this.chatClient = chatClientBuilder.build();
        this.roleGoalRepository = roleGoalRepository;
        this.roleTopicRepository = roleTopicRepository;
        this.topicPrereqRepository = topicPrereqRepository;
    }

    @Override
    public AiTutorResponse ask(Long roleId, Long topicId, AiTutorRequest request) {
        var role = roleGoalRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException("Roadmap не найден"));
        var roleTopics = roleTopicRepository.findAllByRole_IdOrderByPriorityAsc(roleId);
        List<TopicPrereq> requiredPrereqs = topicPrereqRepository.findAll().stream()
                .filter(item -> item.getRelationType() == PrereqRelationType.REQUIRED)
                .toList();
        Topic topic = buildRoleScope(roleTopics.stream().map(item -> item.getTopic()).toList(), requiredPrereqs)
                .values()
                .stream()
                .filter(item -> item.getId().equals(topicId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Тема не найдена в выбранном roadmap"));

        String roleName = KnowledgeBaseLocalizationUtil.localizeRoleName(role.getCode(), role.getName());
        String topicTitle = KnowledgeBaseLocalizationUtil.localizeTopicTitle(topic.getCode(), topic.getTitle());
        String topicDescription = KnowledgeBaseLocalizationUtil.localizeDescription(
                topic.getDescription(),
                role.getName(),
                roleName,
                topic.getTitle(),
                topicTitle
        );
        List<String> prereqs = requiredPrereqs.stream()
                .filter(item -> item.getNextTopic().getId().equals(topicId))
                .map(item -> KnowledgeBaseLocalizationUtil.localizeTopicTitle(
                        item.getPrereqTopic().getCode(),
                        item.getPrereqTopic().getTitle()
                ))
                .distinct()
                .toList();

        try {
            String answer = chatClient.prompt()
                    .system("""
                            Ты AI Tutor внутри русскоязычного сервиса с roadmap для изучения IT.
                            Отвечай по-русски, кратко и практично.
                            Не строй новый roadmap целиком: помогай только с выбранной темой.
                            Если пользователь просит код или пример, дай небольшой пример и объясни его.
                            Если вопрос выходит за тему, мягко верни ответ к текущей теме.
                            """)
                    .user(buildUserPrompt(roleName, topicTitle, topicDescription, prereqs, request.question()))
                    .call()
                    .content();

            return AiTutorResponse.builder()
                    .roleId(roleId)
                    .topicId(topicId)
                    .topicTitle(topicTitle)
                    .answer(answer)
                    .build();
        } catch (Exception ex) {
            log.error("Ошибка AI Tutor для roleId={}, topicId={}", roleId, topicId, ex);
            throw new AiRouteGenerationException("AI Tutor сейчас недоступен. Проверьте, что Ollama запущена.", ex);
        }
    }

    private Map<Long, Topic> buildRoleScope(List<Topic> roleTopics, List<TopicPrereq> requiredPrereqs) {
        Map<Long, Topic> result = new LinkedHashMap<>();
        Deque<Topic> stack = new ArrayDeque<>(roleTopics);
        roleTopics.forEach(topic -> result.put(topic.getId(), topic));

        while (!stack.isEmpty()) {
            Topic current = stack.pop();
            requiredPrereqs.stream()
                    .filter(prereq -> prereq.getNextTopic().getId().equals(current.getId()))
                    .map(TopicPrereq::getPrereqTopic)
                    .filter(prereqTopic -> !result.containsKey(prereqTopic.getId()))
                    .forEach(prereqTopic -> {
                        result.put(prereqTopic.getId(), prereqTopic);
                        stack.push(prereqTopic);
                    });
        }

        return result;
    }

    private String buildUserPrompt(String roleName,
                                   String topicTitle,
                                   String topicDescription,
                                   List<String> prereqs,
                                   String question) {
        return """
                Roadmap: %s
                Тема: %s
                Описание темы: %s
                Предварительные темы: %s

                Вопрос пользователя:
                %s
                """.formatted(
                roleName,
                topicTitle,
                topicDescription == null || topicDescription.isBlank() ? "Описание пока не заполнено." : topicDescription,
                prereqs.isEmpty() ? "нет обязательных предварительных тем" : String.join(", ", prereqs),
                question
        );
    }
}
