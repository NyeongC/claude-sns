package com.ccn.sns.sns_project.controller;

import com.ccn.sns.sns_project.domain.repost.RepostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RepostApiController {

    private final RepostService repostService;

    @PostMapping("/api/v1/posts/{postId}/reposts")
    public ResponseEntity<Void> repost(@PathVariable Long postId, Authentication authentication) {
        repostService.repost(authentication.getName(), postId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/v1/posts/{postId}/reposts")
    public ResponseEntity<Void> unrepost(@PathVariable Long postId, Authentication authentication) {
        repostService.unrepost(authentication.getName(), postId);
        return ResponseEntity.ok().build();
    }
}
