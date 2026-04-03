package com.example.adaprivelearningnavigator.repo;

import com.example.adaprivelearningnavigator.domain.userPart.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {
}
