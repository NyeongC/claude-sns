package com.ccn.sns.sns_project.controller.dto;

import com.ccn.sns.sns_project.domain.follow.FollowCount;

public record FollowCountResponse(long followersCount, long followeesCount) {

    public static FollowCountResponse from(FollowCount followCount) {
        return new FollowCountResponse(followCount.getFollowersCount(), followCount.getFolloweesCount());
    }
}
