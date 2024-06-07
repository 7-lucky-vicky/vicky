package com.sparta.vicky.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentRequest {
    @NotBlank
    private Long boardId;

    @NotBlank
    private String content;
}
