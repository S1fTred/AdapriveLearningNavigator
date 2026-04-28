package com.example.adaprivelearningnavigator.repo;

import com.example.adaprivelearningnavigator.domain.planWhatIf.PlanStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanStepRepository extends JpaRepository<PlanStep, Long> {
    List<PlanStep> findAllByPlanWeek_IdOrderByOrderInWeekAsc(Long planWeekId);

    List<PlanStep> findAllByPlanWeek_Plan_IdOrderByPlanWeek_WeekIndexAscOrderInWeekAsc(Long planId);

    List<PlanStep> findAllByTopic_Id(Long topicId);

    boolean existsByPlanWeek_Plan_IdAndTopic_Id(Long planId, Long topicId);
}
