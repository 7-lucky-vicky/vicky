package com.sparta.vicky.like.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Stream;

public enum ContentType {

    BOARD, COMMENT;

    @JsonCreator
    public static ContentType parsing(String inputValue) {
        return Stream.of(ContentType.values())
                .filter(contentType -> contentType.toString().equals(inputValue.toUpperCase()))
                .findFirst()
                .orElse(null);
    }

}
