package com.ccn.sns.sns_project.domain.follow;

import com.ccn.sns.sns_project.domain.BaseEntity;
import com.ccn.sns.sns_project.domain.user.User;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "follow_counts")
public class FollowCount extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    private long followersCount;

    private long followeesCount;

    protected FollowCount() {
    }

    public FollowCount(User user) {
        this.user = user;
        this.followersCount = 0;
        this.followeesCount = 0;
    }
}
