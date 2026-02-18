package com.example.adaprivelearningnavigator.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "plan_weeks",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_plan_week_index",
                columnNames = {"plan_id", "week_index"}
        )
)
public class PlanWeek {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_week_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @Column(name = "week_index", nullable = false)
    private Integer weekIndex;

    @Column(name = "hours_budget", nullable = false, precision = 6, scale = 2)
    private BigDecimal hoursBudget;

    @Column(name = "hours_planned", precision = 6, scale = 2)
    private BigDecimal hoursPlanned;
}
