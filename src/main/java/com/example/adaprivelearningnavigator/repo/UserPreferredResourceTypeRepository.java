package com.example.adaprivelearningnavigator.repo;

import com.example.adaprivelearningnavigator.domain.compositeKeys.UserPreferredResourceTypeId;
import com.example.adaprivelearningnavigator.domain.userPart.UserPreferredResourceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPreferredResourceTypeRepository
        extends JpaRepository<UserPreferredResourceType, UserPreferredResourceTypeId> {

    List<UserPreferredResourceType> findAllByUser_Id(Long userId);

    void deleteAllByUser_Id(Long userId);
}
