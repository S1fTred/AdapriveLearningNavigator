package com.example.adaprivelearningnavigator.domain.userPart;

import com.example.adaprivelearningnavigator.domain.compositeKeys.UserPreferredResourceTypeId;
import jakarta.persistence.*;

@Entity
@Table(name = "user_preferred_resource_types")
public class UserPreferredResourceType {

    @EmbeddedId
    private UserPreferredResourceTypeId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
