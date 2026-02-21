package com.ccn.sns.sns_project.domain.repost;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RepostRepository extends JpaRepository<Repost, Long> {

    boolean existsByUser_IdAndPost_Id(Long userId, Long postId);

    Optional<Repost> findByUser_IdAndPost_Id(Long userId, Long postId);

    @Query("SELECT r.post.id FROM Repost r WHERE r.user.id = :userId AND r.post.id IN :postIds")
    Set<Long> findRepostedPostIds(@Param("userId") Long userId, @Param("postIds") List<Long> postIds);
}
