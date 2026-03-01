package com.example.adaprivelearningnavigator.domain.knowledgeBase;

import com.example.adaprivelearningnavigator.domain.enums.EntityStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "role_goals", indexes = {
        @Index(name = "idx_role_goals_code", columnList = "code", unique = true)
})
public class RoleGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private EntityStatus status;
}
