package com.ccn.sns.sns_project.domain.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ccn.sns.sns_project.controller.dto.UserSignupRequest;
import com.ccn.sns.sns_project.controller.dto.UserSignupResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSignupResponse signup(UserSignupRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new UserException("이미 존재하는 사용자명입니다.");
        }

        User user = request.toEntity(passwordEncoder.encode(request.password()));

        User savedUser = userRepository.save(user);

        return UserSignupResponse.from(savedUser);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException("사용자를 찾을 수 없습니다: " + username));
    }
}
