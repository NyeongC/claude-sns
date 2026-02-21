package com.ccn.sns.sns_project.domain.follow;

import com.ccn.sns.sns_project.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    // @SQLRestriction 이 적용돼 deleted_at IS NULL 조건 자동 포함
    Optional<Follow> findByFollower_IdAndFollowee_Id(Long followerId, Long followeeId);

    // 소프트 딜리트된 레코드 포함 조회 (복원용, 네이티브 쿼리로 @SQLRestriction 우회)
    @Query(value = "SELECT * FROM follows WHERE follower_id = :followerId AND followee_id = :followeeId LIMIT 1",
            nativeQuery = true)
    Optional<Follow> findByFollowerIdAndFolloweeIdIncludingDeleted(
            @Param("followerId") Long followerId,
            @Param("followeeId") Long followeeId);

    @Query("SELECT f.follower FROM Follow f WHERE f.followee.id = :followeeId")
    List<User> findFollowersByFolloweeId(@Param("followeeId") Long followeeId);

    @Query("SELECT f.followee FROM Follow f WHERE f.follower.id = :followerId")
    List<User> findFolloweesByFollowerId(@Param("followerId") Long followerId);
}
