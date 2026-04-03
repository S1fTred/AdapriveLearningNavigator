package com.example.adaprivelearningnavigator.repo;

import com.example.adaprivelearningnavigator.domain.enums.ScenarioType;
import com.example.adaprivelearningnavigator.domain.planWhatIf.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    List<Plan> findAllByUser_IdOrderByCreatedAtDesc(Long userId);

    List<Plan> findAllByUser_IdAndScenarioTypeOrderByCreatedAtDesc(Long userId, ScenarioType scenarioType);

    List<Plan> findAllByBasePlan_IdOrderByCreatedAtDesc(Long basePlanId);
}
