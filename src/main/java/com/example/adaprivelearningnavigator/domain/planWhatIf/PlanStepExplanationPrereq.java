package com.example.adaprivelearningnavigator.domain.planWhatIf;

import com.example.adaprivelearningnavigator.domain.compositeKeys.PlanStepExplanationPrereqId;
import com.example.adaprivelearningnavigator.domain.enums.PrereqStatus;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.Topic;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "plan_step_explanation_prereqs")
public class PlanStepExplanationPrereq {

    @EmbeddedId
    private PlanStepExplanationPrereqId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("planStepId")
    @JoinColumn(name = "plan_step_id", nullable = false)
    private PlanStepExplanation explanation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("prereqTopicId")
    @JoinColumn(name = "prereq_topic_id", nullable = false)
    private Topic prereqTopic;

    @Enumerated(EnumType.STRING)
    @Column(name = "prereq_status", length = 30)
    private PrereqStatus prereqStatus;
}