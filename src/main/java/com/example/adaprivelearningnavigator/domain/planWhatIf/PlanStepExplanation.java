package com.example.adaprivelearningnavigator.domain.planWhatIf;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "plan_step_explanations")
public class PlanStepExplanation {

    @Id
    @Column(name = "plan_step_id")
    private Long planStepId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "plan_step_id", nullable = false)
    private PlanStep planStep;

    @Column(name = "rule_applied", length = 80)
    private String ruleApplied;

    @Column(name = "topic_priority_reason", length = 200)
    private String topicPriorityReason;

    @Column(name = "resource_reason", length = 200)
    private String resourceReason;
}