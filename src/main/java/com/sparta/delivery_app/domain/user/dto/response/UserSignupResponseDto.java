package com.sparta.delivery_app.domain.user.dto.response;


import com.sparta.delivery_app.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSignupResponseDto {
    private String nickname;
    private String name;

    public static UserSignupResponseDto of(User user) {
        return UserSignupResponseDto.builder()
                .nickname(user.getNickName())
                .name(user.getName())
                .build();
    }
}
