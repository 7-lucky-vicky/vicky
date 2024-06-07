package com.sparta.vicky.board.repository;

import com.sparta.vicky.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByUserIdOrderByCreatedAtDesc(Long userId);

    List<Board> findAllByOrderByCreatedAtDesc();
}
