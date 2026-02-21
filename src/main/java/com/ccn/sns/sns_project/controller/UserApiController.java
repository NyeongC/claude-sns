package com.ccn.sns.sns_project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ccn.sns.sns_project.controller.dto.UserSignupRequest;
import com.ccn.sns.sns_project.controller.dto.UserSignupResponse;
import com.ccn.sns.sns_project.domain.user.UserService;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @PostMapping("/api/v1/users/signup")
    public ResponseEntity<UserSignupResponse> signup(@Valid @RequestBody UserSignupRequest request) {
        UserSignupResponse response = userService.signup(request);
        return ResponseEntity.ok(response);
    }
}
