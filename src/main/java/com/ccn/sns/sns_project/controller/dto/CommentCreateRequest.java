package com.ccn.sns.sns_project.controller.dto;

import com.ccn.sns.sns_project.domain.comment.Comment;
import com.ccn.sns.sns_project.domain.post.Post;
import com.ccn.sns.sns_project.domain.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentCreateRequest(
        @NotBlank @Size(max = 280) String content
) {
    public Comment toEntity(User author, Post post) {
        return new Comment(author, post, this.content());
    }
}
