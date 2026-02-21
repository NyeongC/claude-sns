package com.ccn.sns.sns_project.domain.repost;

import org.springframework.http.HttpStatus;

public class RepostException extends RuntimeException {

    private final HttpStatus status;

    public RepostException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
