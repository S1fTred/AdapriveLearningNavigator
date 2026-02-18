package com.example.adaprivelearningnavigator.domain;

import com.example.adaprivelearningnavigator.domain.compositeKeys.TopicProgressId;
import com.example.adaprivelearningnavigator.domain.enums.ProgressStatus;
import jakarta.persistence.*;
import lombok.*;


import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "topic_progress")
public class TopicProgress {

    @EmbeddedId
    private TopicProgressId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("planId")
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("topicId")
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ProgressStatus status;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
