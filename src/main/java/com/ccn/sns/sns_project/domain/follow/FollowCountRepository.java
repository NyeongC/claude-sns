package com.ccn.sns.sns_project.domain.follow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface FollowCountRepository extends JpaRepository<FollowCount, Long> {

    Optional<FollowCount> findByUser_Id(Long userId);

    boolean existsByUser_Id(Long userId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE FollowCount fc SET fc.followersCount = fc.followersCount + 1 WHERE fc.user.id = :userId")
    void incrementFollowersCount(@Param("userId") Long userId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE FollowCount fc SET fc.followersCount = fc.followersCount - 1 WHERE fc.user.id = :userId")
    void decrementFollowersCount(@Param("userId") Long userId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE FollowCount fc SET fc.followeesCount = fc.followeesCount + 1 WHERE fc.user.id = :userId")
    void incrementFolloweesCount(@Param("userId") Long userId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE FollowCount fc SET fc.followeesCount = fc.followeesCount - 1 WHERE fc.user.id = :userId")
    void decrementFolloweesCount(@Param("userId") Long userId);
}
