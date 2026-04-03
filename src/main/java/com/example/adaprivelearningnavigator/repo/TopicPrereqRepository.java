package com.example.adaprivelearningnavigator.repo;

import com.example.adaprivelearningnavigator.domain.knowledgeBase.TopicPrereq;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicPrereqRepository extends JpaRepository<TopicPrereq, Long> {
    List<TopicPrereq> findAllByNextTopic_Id(Long nextTopicId);

    List<TopicPrereq> findAllByPrereqTopic_Id(Long prereqTopicId);

    boolean existsByPrereqTopic_IdAndNextTopic_Id(Long prereqTopicId, Long nextTopicId);
}
