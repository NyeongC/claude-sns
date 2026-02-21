package com.ccn.sns.sns_project.controller.dto;

import com.ccn.sns.sns_project.domain.comment.Comment;

import java.time.format.DateTimeFormatter;

public record CommentResponse(
        Long id,
        String authorUsername,
        String content,
        String createdAt
) {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("M/d HH:mm");

    public static CommentResponse from(Comment comment) {
        String formattedDate = comment.getCreatedAt() != null
                ? comment.getCreatedAt().format(FORMATTER)
                : "";
        return new CommentResponse(
                comment.getId(),
                comment.getAuthor().getUsername(),
                comment.getContent(),
                formattedDate
        );
    }
}
