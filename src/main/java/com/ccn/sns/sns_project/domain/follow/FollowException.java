package com.ccn.sns.sns_project.domain.follow;

import org.springframework.http.HttpStatus;

public class FollowException extends RuntimeException {

    private final HttpStatus status;

    public FollowException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
