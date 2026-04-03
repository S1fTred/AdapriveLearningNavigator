package com.example.adaprivelearningnavigator.repo;

import com.example.adaprivelearningnavigator.domain.compositeKeys.PlanStepResourceId;
import com.example.adaprivelearningnavigator.domain.planWhatIf.PlanStepResource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanStepResourceRepository extends JpaRepository<PlanStepResource, PlanStepResourceId> {
    List<PlanStepResource> findAllByPlanStep_Id(Long planStepId);

    boolean existsByPlanStep_IdAndResource_Id(Long planStepId, Long resourceId);
}
