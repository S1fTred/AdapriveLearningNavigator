package com.example.adaprivelearningnavigator.domain.planWhatIf;

import com.example.adaprivelearningnavigator.domain.compositeKeys.PlanStepResourceId;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.Resource;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "plan_step_resources")
public class PlanStepResource {

    @EmbeddedId
    private PlanStepResourceId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("planStepId")
    @JoinColumn(name = "plan_step_id", nullable = false)
    private PlanStep planStep;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("resourceId")
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;

    @Column(name = "is_primary", nullable = false)
    private boolean primary;
}
