package com.example.adaprivelearningnavigator.domain;

import com.example.adaprivelearningnavigator.domain.compositeKeys.RoleTopicId;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "role_topics")
public class RoleTopic {

    @EmbeddedId
    private RoleTopicId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("roleId")
    @JoinColumn(name = "role_id", nullable = false)
    private RoleGoal role;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("topicId")
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "is_required", nullable = false)
    private boolean required;
}