package com.ccn.sns.sns_project.domain.repost;

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
public class RepostService {

    private final RepostRepository repostRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void repost(String username, Long postId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RepostException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        if (!postRepository.existsById(postId)) {
            throw new RepostException("게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }

        if (repostRepository.existsByUser_IdAndPost_Id(user.getId(), postId)) {
            throw new RepostException("이미 리포스트한 게시글입니다.", HttpStatus.CONFLICT);
        }

        Post post = postRepository.getReferenceById(postId);
        repostRepository.save(new Repost(user, post));
        postRepository.incrementRepostCount(postId);
    }

    @Transactional
    public void unrepost(String username, Long postId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RepostException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        Repost repost = repostRepository.findByUser_IdAndPost_Id(user.getId(), postId)
                .orElseThrow(() -> new RepostException("리포스트 기록을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        repostRepository.delete(repost);
        postRepository.decrementRepostCount(postId);
    }
}
