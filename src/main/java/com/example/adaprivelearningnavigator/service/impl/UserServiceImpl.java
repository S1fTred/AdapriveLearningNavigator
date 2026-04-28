package com.example.adaprivelearningnavigator.service.impl;

import com.example.adaprivelearningnavigator.domain.userPart.User;
import com.example.adaprivelearningnavigator.repo.UserRepository;
import com.example.adaprivelearningnavigator.service.UserService;
import com.example.adaprivelearningnavigator.service.dto.user.UserProfileResponse;
import com.example.adaprivelearningnavigator.service.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getCurrentProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        return UserProfileResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
