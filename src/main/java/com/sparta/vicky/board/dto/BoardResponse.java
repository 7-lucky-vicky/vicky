package com.sparta.vicky.board.dto;

import com.sparta.vicky.board.entity.Board;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardResponse {
    private Long id;
    private Long userId;
    private String title;
    private String region;
    private String address;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
//    좋아요

    public BoardResponse(Board board) {
        this.id = board.getId();
        this.userId = board.getUser().getId();
        this.title = board.getTitle();
        this.region = board.getRegion();
        this.address = board.getAddress();
        this.content = board.getContent();
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();
    }

}
