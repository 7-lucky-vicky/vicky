package com.sparta.vicky.like.dto;

import com.sparta.vicky.like.entity.ContentType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LikeRequest {

    @NotNull
    private ContentType contentType; // 컨텐츠 타입 [BOARD, COMMENT]

    @NotNull
    private Long contentId;

}
