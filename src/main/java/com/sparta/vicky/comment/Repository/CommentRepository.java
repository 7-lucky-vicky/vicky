package com.sparta.vicky.comment.Repository;

import com.sparta.vicky.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByScheduleIdOrderByCreatedDate(Long schedule_id);
}