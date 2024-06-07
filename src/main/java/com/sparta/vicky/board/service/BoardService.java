package com.sparta.vicky.board.service;

import com.sparta.vicky.board.dto.BoardRequest;
import com.sparta.vicky.board.entity.Board;
import com.sparta.vicky.board.repository.BoardRepository;
import com.sparta.vicky.user.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    private BoardRepository boardRepository;

    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    @Transactional
    public Board createBoard(BoardRequest boardRequest, User user) {
        //request -> Entity
        Board board = new Board(boardRequest, user);
        return boardRepository.save(board);
    }

    public List<Board> getMyBoard(Long userId) {
        return boardRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
    }

    public Board getBoard(Long boardId) {
        return findBoard(boardId);
    }


    @Transactional
    public Board editBoard(Long boardId, BoardRequest boardRequest, User user) {
        Board board = findBoard(boardId);
        board.verify(user);
        return board.editBoard(boardRequest);
    }

    @Transactional
    public Long deleteBoard(Long boardId, User user) {
        Board board = findBoard(boardId);
        board.verify(user);
        boardRepository.deleteById(boardId);
        return boardId;

    }

    public Board findBoard(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("선택된 게시물은 존재하지 않습니다"));
    }
}
