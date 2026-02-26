package com.example.adaprivelearningnavigator.domain.compositeKeys;

import com.example.adaprivelearningnavigator.domain.enums.ResourceType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.io.Serializable;

@Embeddable
public class UserPreferredResourceTypeId implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "resource_type", length = 30)
    private ResourceType resourceType;
}
