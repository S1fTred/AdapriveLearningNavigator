package com.example.adaprivelearningnavigator.repo;

import com.example.adaprivelearningnavigator.domain.enums.EntityStatus;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.RoleGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleGoalRepository extends JpaRepository<RoleGoal, Long> {
    Optional<RoleGoal> findByCode(String code);

    boolean existsByCode(String code);

    List<RoleGoal> findAllByStatusOrderByNameAsc(EntityStatus status);
}
