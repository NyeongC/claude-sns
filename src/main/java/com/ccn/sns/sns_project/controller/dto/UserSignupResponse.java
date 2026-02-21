package com.ccn.sns.sns_project.controller.dto;

import com.ccn.sns.sns_project.domain.user.User;

public record UserSignupResponse(
        Long id,
        String username
) {
    public static UserSignupResponse from(User user) {
        return new UserSignupResponse(user.getId(), user.getUsername());
    }
}
