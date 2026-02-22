package com.ccn.sns.sns_project.config.auth;

public record AuthLoginResponse(
        String sessionId,
        String username
) {
}
