package com.example.adaprivelearningnavigator.repo;

import com.example.adaprivelearningnavigator.domain.planWhatIf.PlanWeek;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlanWeekRepository extends JpaRepository<PlanWeek, Long> {
    List<PlanWeek> findAllByPlan_IdOrderByWeekIndexAsc(Long planId);

    Optional<PlanWeek> findByPlan_IdAndWeekIndex(Long planId, Integer weekIndex);
}
