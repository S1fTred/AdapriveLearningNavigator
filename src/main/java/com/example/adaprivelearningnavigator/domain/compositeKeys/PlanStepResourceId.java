package com.example.adaprivelearningnavigator.domain.compositeKeys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class PlanStepResourceId implements Serializable {
    @Column(name = "plan_step_id")
    private Long planStepId;

    @Column(name = "resource_id")
    private Long resourceId;
}