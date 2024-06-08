package com.sparta.vicky.board.entity;

import com.sparta.vicky.board.dto.BoardRequest;
import com.sparta.vicky.comment.entity.Comment;
import com.sparta.vicky.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Comment> comments = new ArrayList<>();

    @Column(nullable = false)
    private int likeCount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 생성자
     */
    public Board(BoardRequest boardRequest, User user) {
        this.title = boardRequest.getTitle();
        this.region = boardRequest.getRegion();
        this.address = boardRequest.getAddress();
        this.content = boardRequest.getContent();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.likeCount = 0;
        this.user = user;
    }

    /**
     * 연관관계 편의 메서드
     */
    public void setUser(User user) {
        this.user = user;
        user.getBoards().add(this);
    }

    /**
     * 검증 메서드
     */
    public void verifyUser(User user) {
        if (!this.user.equals(user))
            throw new IllegalArgumentException("해당 게시물의 작성자가 아닙니다.");
    }

    /**
     * 수정 메서드
     */
    public void update(BoardRequest request) {
        this.title = request.getTitle();
        this.region = request.getRegion();
        this.address = request.getAddress();
        this.content = request.getContent();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 좋아요 비즈니스 로직
     */
    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        this.likeCount--;
    }

}
