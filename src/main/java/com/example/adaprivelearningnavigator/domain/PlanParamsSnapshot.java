package com.example.adaprivelearningnavigator.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "plan_params_snapshots")
public class PlanParamsSnapshot {

    @Id
    @Column(name = "plan_id")
    private Long planId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @Column(name = "hours_per_week", nullable = false)
    private Integer hoursPerWeek;

    @Column(name = "prefs_language", length = 10)
    private String prefsLanguage;

    @Column(name = "prefs_resource_types", length = 200)
    private String prefsResourceTypes;

    @Column(name = "algo_version", nullable = false, length = 20)
    private String algoVersion;
}