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
public class TopicProgressId implements Serializable {
    @Column(name = "plan_id")
    private Long planId;

    @Column(name = "topic_id")
    private Long topicId;
}
