package com.example.adaprivelearningnavigator.domain.planWhatIf;

import com.example.adaprivelearningnavigator.domain.knowledgeBase.Topic;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "plan_steps",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_step_week_order", columnNames = {"plan_week_id", "order_in_week"}),
                @UniqueConstraint(name = "uq_step_week_topic", columnNames = {"plan_week_id", "topic_id"})
        }
)
public class PlanStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_step_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plan_week_id", nullable = false)
    private PlanWeek planWeek;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @Column(name = "order_in_week", nullable = false)
    private Integer orderInWeek;

    @Column(name = "planned_hours", nullable = false, precision = 6, scale = 2)
    private BigDecimal plannedHours;

    @Column(name = "is_optional", nullable = false)
    private boolean optional;
}
