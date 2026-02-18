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
public class UserKnownTopicId implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "topic_id")
    private Long topicId;
}
