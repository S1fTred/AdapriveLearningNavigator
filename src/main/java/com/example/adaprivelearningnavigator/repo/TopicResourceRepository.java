package com.example.adaprivelearningnavigator.repo;

import com.example.adaprivelearningnavigator.domain.compositeKeys.TopicResourceId;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.TopicResource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicResourceRepository extends JpaRepository<TopicResource, TopicResourceId> {
    List<TopicResource> findAllByTopic_IdOrderByRankAsc(Long topicId);

    List<TopicResource> findAllByResource_Id(Long resourceId);

    boolean existsByTopic_IdAndResource_Id(Long topicId, Long resourceId);
}
