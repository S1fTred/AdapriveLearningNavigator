package com.example.adaprivelearningnavigator.repo;

import com.example.adaprivelearningnavigator.domain.compositeKeys.TopicProgressId;
import com.example.adaprivelearningnavigator.domain.quizAndProgress.TopicProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicProgressRepository extends JpaRepository<TopicProgress, TopicProgressId> {
    List<TopicProgress> findAllByPlan_Id(Long planId);

    boolean existsByPlan_IdAndTopic_Id(Long planId, Long topicId);
}
