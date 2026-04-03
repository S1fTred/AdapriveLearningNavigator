package com.example.adaprivelearningnavigator.repo;

import com.example.adaprivelearningnavigator.domain.compositeKeys.TopicTagId;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.TopicTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicTagRepository extends JpaRepository<TopicTag, TopicTagId> {
    List<TopicTag> findAllByTopic_Id(Long topicId);

    List<TopicTag> findAllByTag_Id(Long tagId);

    boolean existsByTopic_IdAndTag_Id(Long topicId, Long tagId);
}
