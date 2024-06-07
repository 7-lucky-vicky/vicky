package com.sparta.vicky.user.entity;

import com.sparta.vicky.baseEntity.Timestamped;
import com.sparta.vicky.comment.entity.Comment;
import com.sparta.vicky.user.dto.SignupRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column
    private String email;

    @Column
    private String introduce;

    @Column
    @ColumnDefault("'정상'")
    private String statusCode;

    @Column
    private String refreshToken;

    @Column(nullable = false)
    private LocalDateTime statusUpdatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Comment> comments = new ArrayList<>();

    public User(SignupRequest requestDto) {
        this.username = requestDto.getUsername();
        this.password = requestDto.getPassword();
        this.name = requestDto.getName();
        this.email = requestDto.getEmail();
        this.introduce = requestDto.getIntroduce();
    }


    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setUser(this);
    }
}
