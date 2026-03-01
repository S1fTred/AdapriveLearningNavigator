package com.example.adaprivelearningnavigator.domain.knowledgeBase;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tags", indexes = {
        @Index(name = "idx_tags_name", columnList = "name", unique = true)
})
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 60)
    private String name;
}