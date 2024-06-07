package com.sparta.vicky.board.entity;

import com.sparta.vicky.baseEntity.Timestamped;
import com.sparta.vicky.board.dto.BoardRequest;
import com.sparta.vicky.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;


@Getter
@Entity
public class Board extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String region;

    @Column
    private String address;

    @Column(nullable = false)
    private String content;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Comment> comments = new ArrayList<>();


//    Long likes;

    protected Board() {

    }

    public Board(BoardRequest boardRequest, User user) {
        this.title = boardRequest.getTitle();
        this.region = boardRequest.getRegion();
        this.address = boardRequest.getAddress();
        this.content = boardRequest.getContent();
        this.user = user;
    }

    /****연관관계 편의 메서드****/
    public void setUser(User user) {
        this.user = user;
        user.addBoard(this);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setBoard(this);
    }


    public void verify(User user) {
        if (!this.user.equals(user))
            throw new IllegalArgumentException("이 게시물의 작성자가 아닙니다!");
    }

    public Board editBoard(BoardRequest request) {
        this.title = request.getTitle();
        this.region = request.getRegion();
        this.address = request.getAddress();
        this.content = request.getContent();
        return this;
    }

}
