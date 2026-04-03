package com.example.adaprivelearningnavigator.repo;

import com.example.adaprivelearningnavigator.domain.enums.EntityStatus;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    Optional<Topic> findByCode(String code);

    boolean existsByCode(String code);

    List<Topic> findAllByStatusOrderByTitleAsc(EntityStatus status);
}
