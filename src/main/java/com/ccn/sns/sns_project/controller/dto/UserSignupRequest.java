package com.ccn.sns.sns_project.controller.dto;

import com.ccn.sns.sns_project.domain.user.User;
import jakarta.validation.constraints.NotBlank;

public record UserSignupRequest(
        @NotBlank(message = "사용자명은 필수입니다.")
        String username,

        @NotBlank(message = "비밀번호는 필수입니다.")
        String password
) {
    public User toEntity(String encodedPassword) {
        return new User(this.username(), encodedPassword);
    }
}
