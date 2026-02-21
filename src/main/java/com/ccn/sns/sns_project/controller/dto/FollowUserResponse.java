package com.ccn.sns.sns_project.controller.dto;

import com.ccn.sns.sns_project.domain.user.User;

public record FollowUserResponse(Long id, String username) {

    public static FollowUserResponse from(User user) {
        return new FollowUserResponse(user.getId(), user.getUsername());
    }
}
