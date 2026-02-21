package com.ccn.sns.sns_project.domain.follow;

import com.ccn.sns.sns_project.controller.dto.FollowCountResponse;
import com.ccn.sns.sns_project.controller.dto.FollowUserResponse;
import com.ccn.sns.sns_project.domain.user.User;
import com.ccn.sns.sns_project.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final FollowCountRepository followCountRepository;
    private final UserRepository userRepository;

    @Transactional
    public void follow(String currentUsername, Long followeeId) {
        User follower = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new FollowException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        if (follower.getId().equals(followeeId)) {
            throw new FollowException("자기 자신을 팔로우할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        if (!userRepository.existsById(followeeId)) {
            throw new FollowException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }

        Optional<Follow> existing = followRepository.findByFollowerIdAndFolloweeIdIncludingDeleted(
                follower.getId(), followeeId);

        if (existing.isPresent()) {
            Follow follow = existing.get();
            if (follow.getDeletedAt() == null) {
                throw new FollowException("이미 팔로우한 사용자입니다.", HttpStatus.CONFLICT);
            }
            // 소프트 딜리트된 팔로우 복원
            follow.restore();
            followRepository.save(follow);
        } else {
            User followee = userRepository.getReferenceById(followeeId);
            followRepository.save(new Follow(follower, followee));
        }

        ensureFollowCount(follower.getId());
        ensureFollowCount(followeeId);

        followCountRepository.incrementFolloweesCount(follower.getId());
        followCountRepository.incrementFollowersCount(followeeId);
    }

    @Transactional
    public void unfollow(String currentUsername, Long followeeId) {
        User follower = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new FollowException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        // @SQLRestriction 으로 deleted_at IS NULL 조건 자동 적용
        Follow follow = followRepository.findByFollower_IdAndFollowee_Id(follower.getId(), followeeId)
                .orElseThrow(() -> new FollowException("팔로우 관계가 존재하지 않습니다.", HttpStatus.NOT_FOUND));

        follow.delete();
        followRepository.save(follow);

        followCountRepository.decrementFolloweesCount(follower.getId());
        followCountRepository.decrementFollowersCount(followeeId);
    }

    public FollowCountResponse getFollowCount(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new FollowException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        FollowCount followCount = followCountRepository.findByUser_Id(user.getId())
                .orElseGet(() -> new FollowCount(user));

        return FollowCountResponse.from(followCount);
    }

    public List<FollowUserResponse> getFollowers(String currentUsername) {
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new FollowException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        return followRepository.findFollowersByFolloweeId(user.getId()).stream()
                .map(FollowUserResponse::from)
                .toList();
    }

    public List<FollowUserResponse> getFollowees(String currentUsername) {
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new FollowException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        return followRepository.findFolloweesByFollowerId(user.getId()).stream()
                .map(FollowUserResponse::from)
                .toList();
    }

    @Transactional
    public void followByUsername(String currentUsername, String targetUsername) {
        User followee = userRepository.findByUsername(targetUsername)
                .orElseThrow(() -> new FollowException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        follow(currentUsername, followee.getId());
    }

    @Transactional
    public void unfollowByUsername(String currentUsername, String targetUsername) {
        User followee = userRepository.findByUsername(targetUsername)
                .orElseThrow(() -> new FollowException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        unfollow(currentUsername, followee.getId());
    }

    public boolean isFollowing(String currentUsername, String targetUsername) {
        return userRepository.findByUsername(currentUsername)
                .flatMap(current -> userRepository.findByUsername(targetUsername)
                        .map(target -> followRepository.findByFollower_IdAndFollowee_Id(
                                current.getId(), target.getId()).isPresent()))
                .orElse(false);
    }

    private void ensureFollowCount(Long userId) {
        if (!followCountRepository.existsByUser_Id(userId)) {
            User user = userRepository.getReferenceById(userId);
            followCountRepository.save(new FollowCount(user));
        }
    }
}
