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
@Table(name = "likes")
public class Like extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
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
    public Like(LikeRequest request, User user) {
        this.contentType = request.getContentType();
        this.contentId = request.getContentId();
        this.status = LikeStatus.CANCELED; // 좋아요 로직 수행 전에는 취소 상태
        this.user = user;
    }

    /**
     * 좋아요 토글 메서드
     */
    public void doLike(Long userId) {
        this.verifyUser(userId);
        this.status = LikeStatus.LIKED;
    }

    public void cancelLike(Long userId) {
        this.verifyUser(userId);
        this.status = LikeStatus.CANCELED;
    }

    /**
     * 사용자 검증 메서드
     */
    public void verifyUser(Long userId) {
        if (!userId.equals(this.user.getId())) {
            throw new IllegalArgumentException("사용자가 해당 좋아요의 주인이 아닙니다.");
        }
    }

}
