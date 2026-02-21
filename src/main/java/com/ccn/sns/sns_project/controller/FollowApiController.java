package com.ccn.sns.sns_project.controller;

import com.ccn.sns.sns_project.controller.dto.FollowCountResponse;
import com.ccn.sns.sns_project.controller.dto.FollowUserResponse;
import com.ccn.sns.sns_project.domain.follow.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FollowApiController {

    private final FollowService followService;

    @PostMapping("/api/v1/follows/{targetUserId}")
    public ResponseEntity<Void> follow(@PathVariable Long targetUserId, Authentication authentication) {
        followService.follow(authentication.getName(), targetUserId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/v1/follows/{targetUserId}")
    public ResponseEntity<Void> unfollow(@PathVariable Long targetUserId, Authentication authentication) {
        followService.unfollow(authentication.getName(), targetUserId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/v1/follows/followers")
    public ResponseEntity<List<FollowUserResponse>> getFollowers(Authentication authentication) {
        return ResponseEntity.ok(followService.getFollowers(authentication.getName()));
    }

    @GetMapping("/api/v1/follows/followees")
    public ResponseEntity<List<FollowUserResponse>> getFollowees(Authentication authentication) {
        return ResponseEntity.ok(followService.getFollowees(authentication.getName()));
    }

    @GetMapping("/api/v1/follows/count")
    public ResponseEntity<FollowCountResponse> getFollowCount(Authentication authentication) {
        return ResponseEntity.ok(followService.getFollowCount(authentication.getName()));
    }

    @PostMapping("/api/v1/follows/username/{targetUsername}")
    public ResponseEntity<Void> followByUsername(@PathVariable String targetUsername, Authentication authentication) {
        followService.followByUsername(authentication.getName(), targetUsername);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/v1/follows/username/{targetUsername}")
    public ResponseEntity<Void> unfollowByUsername(@PathVariable String targetUsername, Authentication authentication) {
        followService.unfollowByUsername(authentication.getName(), targetUsername);
        return ResponseEntity.ok().build();
    }
}
