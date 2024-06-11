package com.sparta.vicky.comment.entity;

import com.sparta.vicky.board.entity.Board;
import com.sparta.vicky.comment.dto.CommentRequest;
import com.sparta.vicky.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(nullable = false)
    private int likeCount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;


    /**
     * 생성자
     */
    public Comment(CommentRequest request, Board board, User user) {
        this.content = request.getContent();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.likeCount = 0;
        this.board = board;
        this.user = user;
    }

    /**
     * 검증 메서드
     */
    public void verifyBoard(Long boardId) {
        if (!this.board.getId().equals(boardId)) {
            throw new IllegalArgumentException("해당 댓글의 게시물이 아닙니다.");
        }
    }

    public void verifyUser(Long userId) {
        if (!this.user.getId().equals(userId)) {
            throw new IllegalArgumentException("해당 게시물의 작성자가 아닙니다.");
        }
    }

    /**
     * 수정 메서드
     */
    public void update(CommentRequest request) {
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