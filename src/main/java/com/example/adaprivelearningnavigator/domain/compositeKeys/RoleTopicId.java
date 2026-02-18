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
public class RoleTopicId implements Serializable {
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "topic_id")
    private Long topicId;
}
