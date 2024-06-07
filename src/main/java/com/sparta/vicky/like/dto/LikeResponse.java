package com.sparta.vicky.like.dto;

import com.sparta.vicky.like.entity.Like;
import lombok.Getter;

@Getter
public class LikeResponse {

    private final Long id;

    public LikeResponse(Like like) {
        this.id = like.getId();
    }

}
