package com.example.adaprivelearningnavigator.repo;

import com.example.adaprivelearningnavigator.domain.compositeKeys.PlanStepExplanationPrereqId;
import com.example.adaprivelearningnavigator.domain.planWhatIf.PlanStepExplanationPrereq;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanStepExplanationPrereqRepository
        extends JpaRepository<PlanStepExplanationPrereq, PlanStepExplanationPrereqId> {

    List<PlanStepExplanationPrereq> findAllByExplanation_PlanStepId(Long planStepId);
}
