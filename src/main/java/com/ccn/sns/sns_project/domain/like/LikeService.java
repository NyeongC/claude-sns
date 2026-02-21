package com.ccn.sns.sns_project.domain.like;

import com.ccn.sns.sns_project.domain.post.Post;
import com.ccn.sns.sns_project.domain.post.PostRepository;
import com.ccn.sns.sns_project.domain.user.User;
import com.ccn.sns.sns_project.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void like(String username, Long postId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new LikeException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        if (!postRepository.existsById(postId)) {
            throw new LikeException("게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }

        if (postLikeRepository.existsByUser_IdAndPost_Id(user.getId(), postId)) {
            throw new LikeException("이미 좋아요한 게시글입니다.", HttpStatus.CONFLICT);
        }

        Post post = postRepository.getReferenceById(postId);
        postLikeRepository.save(new PostLike(user, post));
        postRepository.incrementLikeCount(postId);
    }

    @Transactional
    public void unlike(String username, Long postId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new LikeException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        PostLike postLike = postLikeRepository.findByUser_IdAndPost_Id(user.getId(), postId)
                .orElseThrow(() -> new LikeException("좋아요 기록을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        postLikeRepository.delete(postLike);
        postRepository.decrementLikeCount(postId);
    }
}
