package com.ccn.sns.sns_project.controller;

import com.ccn.sns.sns_project.config.auth.AuthUser;
import com.ccn.sns.sns_project.domain.like.LikeService;
import com.ccn.sns.sns_project.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeApiController {

    private final LikeService likeService;

    @PostMapping("/api/v1/posts/{postId}/likes")
    public ResponseEntity<Void> like(@PathVariable Long postId, @AuthUser User user) {
        likeService.like(user.getUsername(), postId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/v1/posts/{postId}/likes")
    public ResponseEntity<Void> unlike(@PathVariable Long postId, @AuthUser User user) {
        likeService.unlike(user.getUsername(), postId);
        return ResponseEntity.ok().build();
    }
}
