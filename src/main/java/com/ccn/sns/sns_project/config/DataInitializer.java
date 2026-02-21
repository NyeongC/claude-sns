package com.ccn.sns.sns_project.config;

import com.ccn.sns.sns_project.domain.follow.FollowService;
import com.ccn.sns.sns_project.domain.post.Post;
import com.ccn.sns.sns_project.domain.post.PostRepository;
import com.ccn.sns.sns_project.domain.user.User;
import com.ccn.sns.sns_project.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FollowService followService;
    private final PasswordEncoder passwordEncoder;
    private final AppDataProperties appDataProperties;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        String defaultPassword = passwordEncoder.encode(appDataProperties.defaultPassword());

        User ccn     = userRepository.save(new User("ccn",     defaultPassword));
        User alice   = userRepository.save(new User("alice",   defaultPassword));
        User bob     = userRepository.save(new User("bob",     defaultPassword));
        User charlie = userRepository.save(new User("charlie", defaultPassword));

        // 팔로우 관계 초기화
        followService.follow(ccn.getUsername(),     alice.getId());
        followService.follow(ccn.getUsername(),     bob.getId());
        followService.follow(alice.getUsername(),   ccn.getId());
        followService.follow(alice.getUsername(),   charlie.getId());
        followService.follow(bob.getUsername(),     ccn.getId());
        followService.follow(charlie.getUsername(), alice.getId());
        followService.follow(charlie.getUsername(), bob.getId());

        // 샘플 게시글
        postRepository.save(new Post(ccn,     "안녕하세요! SNS를 시작합니다 🎉"));
        postRepository.save(new Post(alice,   "오늘 날씨가 정말 좋네요 ☀️"));
        postRepository.save(new Post(bob,     "오늘도 열심히 코딩 중입니다 💻"));
        postRepository.save(new Post(charlie, "Spring Boot 공부 중... JPA가 어렵네요 😅"));
        postRepository.save(new Post(ccn,     "점심으로 뭐 먹을까요? 오늘은 파스타 어떨까요?"));
        postRepository.save(new Post(alice,   "맛있는 커피 한 잔으로 하루를 시작합니다 ☕"));
        postRepository.save(new Post(bob,     "주말에 등산 다녀왔는데 경치가 최고였어요 🏔️"));
        postRepository.save(new Post(charlie, "드디어 첫 번째 프로젝트 완성! 뿌듯합니다 ✨"));
    }
}
