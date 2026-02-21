package com.ccn.sns.sns_project.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p JOIN FETCH p.author ORDER BY p.createdAt DESC")
    List<Post> findAllWithAuthorOrderByCreatedAtDesc();

    @Query("SELECT p FROM Post p JOIN FETCH p.author ORDER BY p.likeCount DESC")
    List<Post> findAllWithAuthorOrderByLikeCountDesc();

    @Query("SELECT p FROM Post p JOIN FETCH p.author WHERE p.author.id = :authorId ORDER BY p.createdAt DESC")
    List<Post> findByAuthorWithAuthorOrderByCreatedAtDesc(@Param("authorId") Long authorId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Post p SET p.likeCount = p.likeCount + 1 WHERE p.id = :postId")
    void incrementLikeCount(@Param("postId") Long postId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Post p SET p.likeCount = p.likeCount - 1 WHERE p.id = :postId")
    void decrementLikeCount(@Param("postId") Long postId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Post p SET p.repostCount = p.repostCount + 1 WHERE p.id = :postId")
    void incrementRepostCount(@Param("postId") Long postId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Post p SET p.repostCount = p.repostCount - 1 WHERE p.id = :postId")
    void decrementRepostCount(@Param("postId") Long postId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Post p SET p.commentCount = p.commentCount + 1 WHERE p.id = :postId")
    void incrementCommentCount(@Param("postId") Long postId);
}
