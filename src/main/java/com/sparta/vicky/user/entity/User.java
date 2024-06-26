package com.sparta.vicky.user.entity;

import com.sparta.vicky.base.entity.Timestamped;
import com.sparta.vicky.board.entity.Board;
import com.sparta.vicky.comment.entity.Comment;
import com.sparta.vicky.like.entity.Like;
import com.sparta.vicky.user.dto.SignupRequest;
import com.sparta.vicky.user.dto.ProfileRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // 사용자 ID

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name; // 사용자 이름

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String introduce; // 소개

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Like> likes = new ArrayList<>();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status; // 사용자 상태 [JOINED, WITHDRAWN]

    @Column(nullable = false)
    private LocalDateTime statusUpdatedAt;

    /**
     * 생성자
     */
    public User(SignupRequest request, String encodedPassword) {
        this.username = request.getUsername();
        this.password = encodedPassword;
        this.name = request.getName();
        this.email = request.getEmail();
        this.introduce = request.getIntroduce();
        this.status = UserStatus.JOINED;
        this.statusUpdatedAt = LocalDateTime.now();
    }

    /**
     * 회원 탈퇴
     */
    public void withdraw() {
        this.status = UserStatus.WITHDRAWN;
        this.statusUpdatedAt = LocalDateTime.now();
    }

    /**
     * 프로필 수정
     */
    public void updateProfile(ProfileRequest request) {
        this.name = request.getName();
        this.email = request.getEmail();
        this.introduce = request.getIntroduce();
    }

    /**
     * 비밀번호 수정
     */
    public void updatePassword(String encodedNewPassword) {
        this.password = encodedNewPassword;
    }

    /**
     * 검증 메서드
     */
    public void verifyUser(Long id) {
        if (!this.id.equals(id)) {
            throw new IllegalArgumentException("User with id " + id + " does not belong to user with id " + this.id);
        }
    }

}
