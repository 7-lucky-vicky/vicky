package com.sparta.vicky.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommentRequest {

    @NotNull
    private Long boardId;

    @NotBlank
    private String content;

}
