package com.sparta.vicky.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentRequest {
    @NotNull
    private Long boardId;

    @NotBlank
    private String content;
}
