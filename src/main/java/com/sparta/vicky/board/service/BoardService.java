package com.sparta.vicky.board.service;

import com.sparta.vicky.board.dto.BoardRequest;
import com.sparta.vicky.board.entity.Board;
import com.sparta.vicky.board.repository.BoardRepository;
import com.sparta.vicky.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    /**
     * 게시물 작성
     */
    @Transactional
    public Board createBoard(BoardRequest request, User user) {
        Board board = Board.createBoard(request, user);
        return boardRepository.save(board);
    }

    /**
     * 전체 게시물 조회
     */
    public List<Board> getAllBoards() {
        return boardRepository.findAllByOrderByCreatedAtDesc();
    }

    /**
     * 특정 사용자의 전체 게시물 조회
     */
    public List<Board> getUserBoards(Long userId) {
        return boardRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
    }

    /**
     * 특정 게시물 조회
     */
    public Board getBoard(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() ->
                new IllegalArgumentException("boardId " + boardId + " 에 해당하는 게시물이 존재하지 않습니다."));
    }

    /**
     * 게시물 수정
     */
    @Transactional
    public Board updateBoard(Long boardId, BoardRequest request, User user) {
        Board board = getBoard(boardId);
        board.verifyUser(user);
        board.update(request);

        return board;
    }

    /**
     * 게시물 삭제
     */
    @Transactional
    public Long deleteBoard(Long boardId, User user) {
        Board board = getBoard(boardId);
        board.verifyUser(user);
        boardRepository.delete(board);

        return boardId;
    }

}
