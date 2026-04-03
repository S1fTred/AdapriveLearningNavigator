package com.example.adaprivelearningnavigator.service;

import com.example.adaprivelearningnavigator.service.dto.common.PageResponse;
import com.example.adaprivelearningnavigator.service.dto.role_goal.RoleGoalCreateRequest;
import com.example.adaprivelearningnavigator.service.dto.role_goal.RoleGoalResponse;
import com.example.adaprivelearningnavigator.service.dto.role_goal.RoleTopicRequest;
import com.example.adaprivelearningnavigator.service.dto.role_goal.RoleTopicResponse;

import java.util.List;

public interface RoleGoalService {
    PageResponse<RoleGoalResponse> getRoleGoals(int page, int size);

    RoleGoalResponse getRoleGoal(Long roleId);

    RoleGoalResponse createRoleGoal(RoleGoalCreateRequest request);

    List<RoleTopicResponse> getRoleTopics(Long roleId);

    RoleTopicResponse addRoleTopic(Long roleId, RoleTopicRequest request);
}
