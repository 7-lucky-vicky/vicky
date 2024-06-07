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
    private int likes;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 생성자
     */
    public static Comment createComment(CommentRequest request, Board board, User user) {
        Comment comment = new Comment(request.getContent());
        comment.setBoard(board);
        comment.setUser(user);

        return comment;
    }

    private Comment(String content) {
        this.content = content;
        this.likes = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 연관관계 편의 메서드
     */
    public void setUser(User user) {
        this.user = user;
        user.addComment(this);
    }

    public void setBoard(Board board) {
        this.board = board;
        board.addComment(this);
    }

    /**
     * 검증 메서드
     */
    public void verifyBoard(Long boardId) {
        if (!this.board.getId().equals(boardId)) {
            throw new IllegalArgumentException("해당 댓글의 게시물이 아닙니다.");
        }
    }

    public void verifyUser(User user) {
        if (!this.user.equals(user)) {
            throw new IllegalArgumentException("해당 댓글의 작성자가 아닙니다.");
        }
    }

    /**
     * 수정 메서드
     */
    public void update(CommentRequest request) {
        this.content = request.getContent();
        this.updatedAt = LocalDateTime.now();
    }

}