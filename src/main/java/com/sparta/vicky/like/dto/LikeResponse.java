package com.sparta.vicky.like.dto;

import com.sparta.vicky.like.entity.ContentType;
import com.sparta.vicky.like.entity.Like;
import com.sparta.vicky.like.entity.LikeStatus;
import lombok.Getter;

@Getter
public class LikeResponse {

    private final Long id;
    private final ContentType contentType;
    private final Long contentId;
    private final LikeStatus status; // 좋아요 상태 [LIKED, CANCELED]
    private final Long userId;

    public LikeResponse(Like like) {
        this.id = like.getId();
        this.contentType = like.getContentType();
        this.contentId = like.getContentId();
        this.status = like.getStatus();
        this.userId = like.getUser().getId();
    }

}
