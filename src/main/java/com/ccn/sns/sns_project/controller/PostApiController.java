package com.ccn.sns.sns_project.controller;

import com.ccn.sns.sns_project.config.auth.AuthUser;
import com.ccn.sns.sns_project.controller.dto.PostCreateRequest;
import com.ccn.sns.sns_project.controller.dto.PostResponse;
import com.ccn.sns.sns_project.domain.post.PostService;
import com.ccn.sns.sns_project.domain.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostApiController {

    private final PostService postService;

    @PostMapping("/api/v1/posts")
    public ResponseEntity<PostResponse> createPost(
            @Valid @RequestBody PostCreateRequest request,
            @AuthUser User user) {
        return ResponseEntity.ok(postService.createPost(user.getUsername(), request));
    }

    @GetMapping("/api/v1/posts")
    public ResponseEntity<List<PostResponse>> getPosts(
            @RequestParam(defaultValue = "latest") String sort,
            @AuthUser User user) {
        return ResponseEntity.ok(postService.getPosts(user.getUsername(), sort));
    }
}
