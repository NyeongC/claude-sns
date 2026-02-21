package com.ccn.sns.sns_project.domain.comment;

import com.ccn.sns.sns_project.controller.dto.CommentCreateRequest;
import com.ccn.sns.sns_project.controller.dto.CommentResponse;
import com.ccn.sns.sns_project.domain.post.Post;
import com.ccn.sns.sns_project.domain.post.PostRepository;
import com.ccn.sns.sns_project.domain.user.User;
import com.ccn.sns.sns_project.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<CommentResponse> getComments(Long postId) {
        return commentRepository.findByPostIdWithAuthorOrderByCreatedAtAsc(postId).stream()
                .map(CommentResponse::from)
                .toList();
    }

    @Transactional
    public CommentResponse createComment(String username, Long postId, CommentCreateRequest request) {
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new CommentException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommentException("게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        Comment comment = commentRepository.save(request.toEntity(author, post));
        postRepository.incrementCommentCount(postId);
        return CommentResponse.from(comment);
    }
}
