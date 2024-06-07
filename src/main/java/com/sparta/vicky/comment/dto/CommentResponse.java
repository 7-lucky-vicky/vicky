package com.sparta.vicky.comment.dto;

import com.sparta.vicky.comment.entity.Comment;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CommentResponse {

    private final Long commentId;
    private final Long scheduleId;
    private final Long userId;
    private final String content;
    private final LocalDate createdAt;
    private final LocalDate updatedAt;

    public CommentResponse(Comment comment) {
        this.commentId = comment.getId();
        this.scheduleId = comment.getBoard().getId();
        this.userId = comment.getUser().getId();
        this.content = comment.getContent();
        this.createdAt = LocalDate.from(comment.getCreatedAt());
        this.updatedAt = LocalDate.from(comment.getUpdatedAt());
    }
}
