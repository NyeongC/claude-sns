package com.ccn.sns.sns_project.controller;

import com.ccn.sns.sns_project.controller.dto.PostCreateRequest;
import com.ccn.sns.sns_project.controller.dto.PostResponse;
import com.ccn.sns.sns_project.domain.post.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
            Authentication authentication) {
        return ResponseEntity.ok(postService.createPost(authentication.getName(), request));
    }

    @GetMapping("/api/v1/posts")
    public ResponseEntity<List<PostResponse>> getPosts(
            @RequestParam(defaultValue = "latest") String sort,
            Authentication authentication) {
        return ResponseEntity.ok(postService.getPosts(authentication.getName(), sort));
    }
}
