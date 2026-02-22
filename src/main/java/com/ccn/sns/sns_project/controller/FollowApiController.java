package com.ccn.sns.sns_project.controller;

import com.ccn.sns.sns_project.config.auth.AuthUser;
import com.ccn.sns.sns_project.controller.dto.FollowCountResponse;
import com.ccn.sns.sns_project.controller.dto.FollowUserResponse;
import com.ccn.sns.sns_project.domain.follow.FollowService;
import com.ccn.sns.sns_project.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Void> follow(@PathVariable Long targetUserId, @AuthUser User user) {
        followService.follow(user.getUsername(), targetUserId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/v1/follows/{targetUserId}")
    public ResponseEntity<Void> unfollow(@PathVariable Long targetUserId, @AuthUser User user) {
        followService.unfollow(user.getUsername(), targetUserId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/v1/follows/followers")
    public ResponseEntity<List<FollowUserResponse>> getFollowers(@AuthUser User user) {
        return ResponseEntity.ok(followService.getFollowers(user.getUsername()));
    }

    @GetMapping("/api/v1/follows/followees")
    public ResponseEntity<List<FollowUserResponse>> getFollowees(@AuthUser User user) {
        return ResponseEntity.ok(followService.getFollowees(user.getUsername()));
    }

    @GetMapping("/api/v1/follows/count")
    public ResponseEntity<FollowCountResponse> getFollowCount(@AuthUser User user) {
        return ResponseEntity.ok(followService.getFollowCount(user.getUsername()));
    }

    @PostMapping("/api/v1/follows/username/{targetUsername}")
    public ResponseEntity<Void> followByUsername(@PathVariable String targetUsername, @AuthUser User user) {
        followService.followByUsername(user.getUsername(), targetUsername);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/v1/follows/username/{targetUsername}")
    public ResponseEntity<Void> unfollowByUsername(@PathVariable String targetUsername, @AuthUser User user) {
        followService.unfollowByUsername(user.getUsername(), targetUsername);
        return ResponseEntity.ok().build();
    }
}
