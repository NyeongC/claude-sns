package com.ccn.sns.sns_project.controller.dto;

import com.ccn.sns.sns_project.domain.post.Post;

import java.time.format.DateTimeFormatter;

public record PostResponse(
        Long id,
        String authorUsername,
        String content,
        long likeCount,
        long repostCount,
        long commentCount,
        String createdAt,
        boolean liked,
        boolean reposted
) {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("M/d HH:mm");

    public static PostResponse from(Post post, boolean liked, boolean reposted) {
        String formattedDate = post.getCreatedAt() != null
                ? post.getCreatedAt().format(FORMATTER)
                : "";
        return new PostResponse(
                post.getId(),
                post.getAuthor().getUsername(),
                post.getContent(),
                post.getLikeCount(),
                post.getRepostCount(),
                post.getCommentCount(),
                formattedDate,
                liked,
                reposted
        );
    }
}
