package com.sparta.vicky.board.dto;

import lombok.Getter;

@Getter
public class BoardRequest {
    private String title;
    private String region;
    private String address;
    private String content;
}
