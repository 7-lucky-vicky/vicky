package com.sparta.vicky.board.service;

import com.sparta.vicky.board.dto.BoardRequest;
import com.sparta.vicky.board.entity.Board;
import com.sparta.vicky.board.repository.BoardRepository;
import com.sparta.vicky.user.entity.User;
import com.sparta.vicky.user.entity.UserStatus;
import com.sparta.vicky.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserService userService;

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
    public Page<Board> getAllBoards(Long userId, Pageable pageable) {
        if (userId == null) {
            return boardRepository.findAll(pageable);
        }

        User user = userService.findById(userId);
        // 사용자 탈퇴 여부 확인
        if (user.getStatus().equals(UserStatus.WITHDRAWN)) {
            throw new IllegalArgumentException("해당 사용자는 탈퇴 상태입니다.");
        }

        return boardRepository.findAllByUserId(userId, pageable);
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
