package com.example.adaprivelearningnavigator.repo;

import com.example.adaprivelearningnavigator.domain.compositeKeys.UserKnownTopicId;
import com.example.adaprivelearningnavigator.domain.userPart.UserKnownTopic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserKnownTopicRepository extends JpaRepository<UserKnownTopic, UserKnownTopicId> {
    List<UserKnownTopic> findAllByUser_IdOrderByMarkedAtDesc(Long userId);

    boolean existsByUser_IdAndTopic_Id(Long userId, Long topicId);
}
