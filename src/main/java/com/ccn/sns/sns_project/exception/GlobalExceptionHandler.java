package com.ccn.sns.sns_project.exception;

import com.ccn.sns.sns_project.domain.comment.CommentException;
import com.ccn.sns.sns_project.domain.follow.FollowException;
import com.ccn.sns.sns_project.domain.like.LikeException;
import com.ccn.sns.sns_project.domain.post.PostException;
import com.ccn.sns.sns_project.domain.repost.RepostException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FollowException.class)
    public ResponseEntity<Map<String, String>> handleFollowException(FollowException e) {
        return ResponseEntity.status(e.getStatus()).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(PostException.class)
    public ResponseEntity<Map<String, String>> handlePostException(PostException e) {
        return ResponseEntity.status(e.getStatus()).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(LikeException.class)
    public ResponseEntity<Map<String, String>> handleLikeException(LikeException e) {
        return ResponseEntity.status(e.getStatus()).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(RepostException.class)
    public ResponseEntity<Map<String, String>> handleRepostException(RepostException e) {
        return ResponseEntity.status(e.getStatus()).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(CommentException.class)
    public ResponseEntity<Map<String, String>> handleCommentException(CommentException e) {
        return ResponseEntity.status(e.getStatus()).body(Map.of("message", e.getMessage()));
    }
}
