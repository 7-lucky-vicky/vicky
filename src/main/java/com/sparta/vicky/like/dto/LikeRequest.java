package com.sparta.vicky.like.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class LikeRequest {

    @Pattern(
            regexp = "^(Board|Comment)$",
            message = "Invalid value. Only 'Board' or 'Comment' are allowed."
    )
    private String contentType;

    @NotNull(message = "contentId cannot be null")
    private Long contentId;

}
