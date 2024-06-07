package com.sparta.vicky.like.entity;

import com.sparta.vicky.base.entity.Timestamped;
import com.sparta.vicky.like.dto.LikeRequest;
import com.sparta.vicky.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ContentType contentType; // 컨텐츠 타입 [BOARD, COMMENT]

    @Column(nullable = false)
    private Long contentId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LikeStatus status; // 좋아요 상태 [LIKED, CANCELED]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * 생성자
     */
    public static Like createLike(LikeRequest request, User user) {
        Like like = new Like(request);
        like.setUser(user);

        return like;
    }

    private Like(LikeRequest request) {
        this.contentType = request.getContentType();
        this.contentId = request.getContentId();
        this.status = LikeStatus.CANCELED; // 좋아요 로직 수행 전에는 취소 상태
    }

    /**
     * 연관관계 편의 메서드
     */
    public void setUser(User user) {
        this.user = user;
        user.addLike(this);
    }

    /**
     * 좋아요 토글 메서드
     */
    public void doLike(User user) {
        this.verifyUser(user);
        this.status = LikeStatus.LIKED;
    }

    public void cancelLike(User user) {
        this.verifyUser(user);
        this.status = LikeStatus.CANCELED;
    }

    /**
     * 사용자 검증 메서드
     */
    public void verifyUser(User user) {
        if (!this.user.equals(user)) {
            throw new IllegalArgumentException("사용자가 해당 좋아요의 주인이 아닙니다.");
        }
    }

}
