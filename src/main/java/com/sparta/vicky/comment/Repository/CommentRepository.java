package com.sparta.vicky.comment.Repository;

import com.sparta.vicky.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByBoardIdOrderByCreatedAt(Long schedule_id);

}