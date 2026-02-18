package com.example.adaprivelearningnavigator.domain;

import com.example.adaprivelearningnavigator.domain.compositeKeys.UserKnownTopicId;
import com.example.adaprivelearningnavigator.domain.enums.KnownSource;
import jakarta.persistence.*;
import lombok.*;


import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_known_topics")
public class UserKnownTopic {

    @EmbeddedId
    private UserKnownTopicId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("topicId")
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @Enumerated(EnumType.STRING)
    @Column(name = "source", length = 20)
    private KnownSource source;

    @Column(name = "confidence", precision = 3, scale = 2)
    private BigDecimal confidence;

    @Column(name = "marked_at", nullable = false)
    private Instant markedAt;
}
