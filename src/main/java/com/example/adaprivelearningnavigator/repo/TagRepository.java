package com.example.adaprivelearningnavigator.repo;

import com.example.adaprivelearningnavigator.domain.knowledgeBase.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);

    boolean existsByName(String name);
}
