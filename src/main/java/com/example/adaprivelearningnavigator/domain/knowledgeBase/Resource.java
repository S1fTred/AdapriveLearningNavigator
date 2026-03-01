package com.example.adaprivelearningnavigator.domain.knowledgeBase;

import com.example.adaprivelearningnavigator.domain.AuditableEntity;
import com.example.adaprivelearningnavigator.domain.enums.EntityStatus;
import com.example.adaprivelearningnavigator.domain.enums.ResourceType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "resources", indexes = {
        @Index(name = "idx_resources_type", columnList = "type"),
        @Index(name = "idx_resources_status", columnList = "status")
})
public class Resource extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resource_id")
    private Long id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "url", nullable = false, length = 1000)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 30)
    private ResourceType type;

    @Column(name = "language", length = 10)
    private String language;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "provider", length = 120)
    private String provider;

    @Column(name = "difficulty", length = 20)
    private String difficulty;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private EntityStatus status;
}