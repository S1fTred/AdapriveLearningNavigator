package com.example.adaprivelearningnavigator.domain.knowledgeBase;

import com.example.adaprivelearningnavigator.domain.compositeKeys.TopicResourceId;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "topic_resources")
public class TopicResource {

    @EmbeddedId
    private TopicResourceId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("topicId")
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("resourceId")
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;

    @Column(name = "rank")
    private Integer rank;
}