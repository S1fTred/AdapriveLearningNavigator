package com.example.adaprivelearningnavigator.domain.knowledgeBase;

import com.example.adaprivelearningnavigator.domain.AuditableEntity;
import com.example.adaprivelearningnavigator.domain.enums.PrereqRelationType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "topic_prereqs",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_topic_prereq_pair",
                columnNames = {"prereq_topic_id", "next_topic_id"}
        ),
        indexes = {
                @Index(name = "idx_prereq_prereq", columnList = "prereq_topic_id"),
                @Index(name = "idx_prereq_next", columnList = "next_topic_id")
        }
)
public class TopicPrereq extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_prereq_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "prereq_topic_id", nullable = false)
    private Topic prereqTopic;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "next_topic_id", nullable = false)
    private Topic nextTopic;

    @Enumerated(EnumType.STRING)
    @Column(name = "relation_type", length = 20)
    private PrereqRelationType relationType;
}