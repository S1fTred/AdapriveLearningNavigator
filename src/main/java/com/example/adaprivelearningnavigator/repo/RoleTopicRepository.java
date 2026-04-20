package com.example.adaprivelearningnavigator.repo;

import com.example.adaprivelearningnavigator.domain.compositeKeys.RoleTopicId;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.RoleTopic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleTopicRepository extends JpaRepository<RoleTopic, RoleTopicId> {
    List<RoleTopic> findAllByRole_IdOrderByPriorityAsc(Long roleId);

    List<RoleTopic> findAllByTopic_Id(Long topicId);

    boolean existsByRole_IdAndTopic_Id(Long roleId, Long topicId);

    Optional<RoleTopic> findByRole_IdAndTopic_Id(Long roleId, Long topicId);
}
