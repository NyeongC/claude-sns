package com.ccn.sns.sns_project.domain.post;

import com.ccn.sns.sns_project.domain.BaseEntity;
import com.ccn.sns.sns_project.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "posts")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User author;

    @Column(nullable = false, length = 280)
    private String content;

    private long likeCount;

    private long repostCount;

    private long commentCount;

    protected Post() {
    }

    public Post(User author, String content) {
        this.author = author;
        this.content = content;
        this.likeCount = 0;
        this.repostCount = 0;
        this.commentCount = 0;
    }
}
