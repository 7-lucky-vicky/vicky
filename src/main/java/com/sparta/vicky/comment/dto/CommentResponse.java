package com.sparta.vicky.comment.dto;

import com.sparta.vicky.comment.entity.Comment;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CommentResponse {

    private final Long commentId;
    private final Long boardId;
    private final Long userId;
    private final String content;
    private final int likes;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public CommentResponse(Comment comment) {
        this.commentId = comment.getId();
        this.boardId = comment.getBoard().getId();
        this.userId = comment.getUser().getId();
        this.content = comment.getContent();
        this.likes = comment.getLikes();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getCreatedAt();
    }
}
