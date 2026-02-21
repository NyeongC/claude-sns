package com.ccn.sns.sns_project.controller;

import com.ccn.sns.sns_project.domain.like.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeApiController {

    private final LikeService likeService;

    @PostMapping("/api/v1/posts/{postId}/likes")
    public ResponseEntity<Void> like(@PathVariable Long postId, Authentication authentication) {
        likeService.like(authentication.getName(), postId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/v1/posts/{postId}/likes")
    public ResponseEntity<Void> unlike(@PathVariable Long postId, Authentication authentication) {
        likeService.unlike(authentication.getName(), postId);
        return ResponseEntity.ok().build();
    }
}
