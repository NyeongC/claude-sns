package com.ccn.sns.sns_project.controller.dto;

import com.ccn.sns.sns_project.domain.post.Post;
import com.ccn.sns.sns_project.domain.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostCreateRequest(
        @NotBlank @Size(max = 280) String content
) {
    public Post toEntity(User author) {
        return new Post(author, this.content());
    }
}
