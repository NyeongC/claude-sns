package com.ccn.sns.sns_project.controller;

import com.ccn.sns.sns_project.config.auth.AuthUser;
import com.ccn.sns.sns_project.controller.dto.CommentCreateRequest;
import com.ccn.sns.sns_project.controller.dto.CommentResponse;
import com.ccn.sns.sns_project.domain.comment.CommentService;
import com.ccn.sns.sns_project.domain.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentApiController {

    private final CommentService commentService;

    @GetMapping("/api/v1/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getComments(postId));
    }

    @PostMapping("/api/v1/posts/{postId}/comments")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentCreateRequest request,
            @AuthUser User user) {
        return ResponseEntity.ok(commentService.createComment(user.getUsername(), postId, request));
    }
}
