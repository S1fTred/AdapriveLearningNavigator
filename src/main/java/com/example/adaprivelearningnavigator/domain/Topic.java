package com.example.adaprivelearningnavigator.domain;

import com.example.adaprivelearningnavigator.domain.enums.EntityStatus;
import com.example.adaprivelearningnavigator.domain.enums.TopicLevel;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "topics", indexes = {
        @Index(name = "idx_topics_code", columnList = "code", unique = true),
        @Index(name = "idx_topics_status", columnList = "status")
})
public class Topic extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_id")
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", length = 20)
    private TopicLevel level;

    @Column(name = "is_core", nullable = false)
    private boolean core;

    @Column(name = "estimated_hours", precision = 6, scale = 2)
    private BigDecimal estimatedHours;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private EntityStatus status;
}