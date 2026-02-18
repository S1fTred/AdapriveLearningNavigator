package com.example.adaprivelearningnavigator.domain;

import com.example.adaprivelearningnavigator.domain.enums.ScenarioType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "plans", indexes = {
        @Index(name = "idx_plans_user", columnList = "user_id"),
        @Index(name = "idx_plans_role", columnList = "role_id"),
        @Index(name = "idx_plans_base", columnList = "base_plan_id")
})
public class Plan extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleGoal role;

    @Column(name = "status", length = 20)
    private String status; // draft/active/archived (можно тоже enum)

    // what-if ветвление (base_plan_id nullable)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_plan_id")
    private Plan basePlan;

    @Enumerated(EnumType.STRING)
    @Column(name = "scenario_type", length = 20)
    private ScenarioType scenarioType;

    @Column(name = "scenario_label", length = 120)
    private String scenarioLabel;
}
