package com.example.adaprivelearningnavigator.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_preferences")
public class UserPreference {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "preferred_language", length = 10)
    private String preferredLanguage;

    @Column(name = "hours_per_week_default")
    private Integer hoursPerWeekDefault;

    // MVP: строка/список через запятую. Позже можно нормализовать.
    @Column(name = "preferred_resource_types", length = 200)
    private String preferredResourceTypes;
}
