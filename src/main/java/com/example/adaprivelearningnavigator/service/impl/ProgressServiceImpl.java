package com.example.adaprivelearningnavigator.service.impl;

import com.example.adaprivelearningnavigator.domain.compositeKeys.TopicProgressId;
import com.example.adaprivelearningnavigator.domain.enums.EntityStatus;
import com.example.adaprivelearningnavigator.domain.enums.ProgressStatus;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.Topic;
import com.example.adaprivelearningnavigator.domain.planWhatIf.Plan;
import com.example.adaprivelearningnavigator.domain.quizAndProgress.TopicProgress;
import com.example.adaprivelearningnavigator.repo.PlanRepository;
import com.example.adaprivelearningnavigator.repo.PlanStepRepository;
import com.example.adaprivelearningnavigator.repo.TopicProgressRepository;
import com.example.adaprivelearningnavigator.repo.TopicRepository;
import com.example.adaprivelearningnavigator.service.ProgressService;
import com.example.adaprivelearningnavigator.service.dto.progress.TopicProgressResponse;
import com.example.adaprivelearningnavigator.service.dto.progress.TopicProgressUpsertRequest;
import com.example.adaprivelearningnavigator.service.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
public class ProgressServiceImpl implements ProgressService {

    private final PlanRepository planRepository;
    private final PlanStepRepository planStepRepository;
    private final TopicRepository topicRepository;
    private final TopicProgressRepository topicProgressRepository;

    public ProgressServiceImpl(PlanRepository planRepository,
                               PlanStepRepository planStepRepository,
                               TopicRepository topicRepository,
                               TopicProgressRepository topicProgressRepository) {
        this.planRepository = planRepository;
        this.planStepRepository = planStepRepository;
        this.topicRepository = topicRepository;
        this.topicProgressRepository = topicProgressRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopicProgressResponse> getPlanProgress(Long userId, Long planId) {
        requireOwnedPlan(userId, planId);
        return topicProgressRepository.findAllByPlan_IdOrderByUpdatedAtDesc(planId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public TopicProgressResponse upsertTopicProgress(Long userId, Long planId, TopicProgressUpsertRequest request) {
        Plan plan = requireOwnedPlan(userId, planId);
        Topic topic = topicRepository.findById(request.topicId())
                .orElseThrow(() -> new NotFoundException("Тема не найдена"));

        if (!isTopicInPlan(planId, request.topicId())) {
            throw new NotFoundException("Тема не найдена в выбранном плане");
        }

        TopicProgressId progressId = new TopicProgressId(planId, request.topicId());
        TopicProgress progress = topicProgressRepository.findById(progressId)
                .orElseGet(() -> TopicProgress.builder()
                        .id(progressId)
                        .plan(plan)
                        .topic(topic)
                        .build());

        progress.setStatus(request.status() != null ? request.status() : ProgressStatus.NOT_STARTED);
        progress.setUpdatedAt(Instant.now());

        return toResponse(topicProgressRepository.save(progress));
    }

    private Plan requireOwnedPlan(Long userId, Long planId) {
        return planRepository.findById(planId)
                .filter(plan -> Objects.equals(plan.getUser().getId(), userId))
                .filter(plan -> !EntityStatus.DELETED.name().equalsIgnoreCase(plan.getStatus()))
                .orElseThrow(() -> new NotFoundException("План не найден"));
    }

    private boolean isTopicInPlan(Long planId, Long topicId) {
        return planStepRepository.existsByPlanWeek_Plan_IdAndTopic_Id(planId, topicId);
    }

    private TopicProgressResponse toResponse(TopicProgress progress) {
        return TopicProgressResponse.builder()
                .topicId(progress.getTopic().getId())
                .status(progress.getStatus())
                .updatedAt(progress.getUpdatedAt())
                .build();
    }
}
