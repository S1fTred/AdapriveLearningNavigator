package com.example.adaprivelearningnavigator.repo;

import com.example.adaprivelearningnavigator.domain.enums.EntityStatus;
import com.example.adaprivelearningnavigator.domain.enums.ResourceType;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    List<Resource> findAllByStatusOrderByTitleAsc(EntityStatus status);

    List<Resource> findAllByTypeAndLanguageOrderByTitleAsc(ResourceType type, String language);

    Optional<Resource> findByUrl(String url);
}
