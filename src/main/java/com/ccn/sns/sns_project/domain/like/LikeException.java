package com.ccn.sns.sns_project.domain.like;

import org.springframework.http.HttpStatus;

public class LikeException extends RuntimeException {

    private final HttpStatus status;

    public LikeException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
