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
public class TopicTagId implements Serializable {
    @Column(name = "topic_id")
    private Long topicId;

    @Column(name = "tag_id")
    private Long tagId;
}
