package com.sparta.vicky.board.dto;

import com.sparta.vicky.board.entity.Board;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardResponse {

    private final Long id;
    private final String title;
    private final String region;
    private final String address;
    private final String content;
    private final Long userId;
    private final int likes;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public BoardResponse(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.region = board.getRegion();
        this.address = board.getAddress();
        this.content = board.getContent();
        this.userId = board.getUser().getId();
        this.likes = board.getLikeCount();
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();
    }

}
