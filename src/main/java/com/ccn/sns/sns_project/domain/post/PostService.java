package com.ccn.sns.sns_project.domain.post;

import com.ccn.sns.sns_project.controller.dto.PostCreateRequest;
import com.ccn.sns.sns_project.controller.dto.PostResponse;
import com.ccn.sns.sns_project.domain.like.PostLikeRepository;
import com.ccn.sns.sns_project.domain.repost.RepostRepository;
import com.ccn.sns.sns_project.domain.user.User;
import com.ccn.sns.sns_project.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;
    private final RepostRepository repostRepository;

    public PostResponse createPost(String username, PostCreateRequest request) {
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new PostException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        Post post = postRepository.save(request.toEntity(author));
        return PostResponse.from(post, false, false);
    }

    public List<PostResponse> getPosts(String username, String sort) {
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new PostException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        List<Post> posts = "popular".equals(sort)
                ? postRepository.findAllWithAuthorOrderByLikeCountDesc()
                : postRepository.findAllWithAuthorOrderByCreatedAtDesc();

        return toResponseList(posts, currentUser.getId());
    }

    public List<PostResponse> getPostsByUser(String currentUsername, String profileUsername) {
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new PostException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        User author = userRepository.findByUsername(profileUsername)
                .orElseThrow(() -> new PostException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        List<Post> posts = postRepository.findByAuthorWithAuthorOrderByCreatedAtDesc(author.getId());
        return toResponseList(posts, currentUser.getId());
    }

    private List<PostResponse> toResponseList(List<Post> posts, Long currentUserId) {
        if (posts.isEmpty()) return List.of();

        List<Long> postIds = posts.stream().map(Post::getId).toList();
        Set<Long> likedIds = postLikeRepository.findLikedPostIds(currentUserId, postIds);
        Set<Long> repostedIds = repostRepository.findRepostedPostIds(currentUserId, postIds);

        return posts.stream()
                .map(p -> PostResponse.from(p, likedIds.contains(p.getId()), repostedIds.contains(p.getId())))
                .toList();
    }
}
