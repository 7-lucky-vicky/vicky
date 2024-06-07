package com.sparta.vicky.board.controller;

import com.sparta.vicky.board.dto.BoardRequest;
import com.sparta.vicky.board.dto.BoardResponse;
import com.sparta.vicky.board.entity.Board;
import com.sparta.vicky.board.service.BoardService;
import com.sparta.vicky.base.dto.CommonResponse;
import com.sparta.vicky.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.vicky.util.ControllerUtil.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    /**
     * 게시물 작성
     */
    @PostMapping
    public ResponseEntity<CommonResponse<?>> createBoard(
            @Valid @RequestBody BoardRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "게시물 작성 실패");
        }
        try {
            Board board = boardService.createBoard(request, userDetails.getUser());
            BoardResponse response = new BoardResponse(board);

            return getResponseEntity(response, "게시물 작성 성공");

        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 전체 게시물 조회
     */
    @GetMapping
    public ResponseEntity<CommonResponse<?>> getAllBoards() {
        List<Board> boardList = boardService.getAllBoards();
        if (boardList.isEmpty()) {
            return getResponseEntity(null, "먼저 작성하여 소식을 알려보세요!");
        }

        List<BoardResponse> response = boardList.stream()
                .map(BoardResponse::new).toList();

        return getResponseEntity(response, "전체 게시물 조회 성공");
    }

    /**
     * 특정 사용자의 전체 게시물 조회
     */
    @GetMapping
    public ResponseEntity<CommonResponse<?>> getUserBoards(
            @RequestParam Long userId
    ) {
        try {
            List<BoardResponse> response = boardService.getUserBoards(userId).stream()
                    .map(BoardResponse::new).toList();

            return getResponseEntity(response, "사용자 전체 게시물 조회 성공");

        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 특정 게시물 조회
     */
    @GetMapping("/{boardId}")
    public ResponseEntity<CommonResponse<?>> getBoard(
            @PathVariable Long boardId
    ) {
        try {
            Board board = boardService.getBoard(boardId);
            BoardResponse response = new BoardResponse(board);

            return getResponseEntity(response, "게시물 조회 성공");

        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 게시물 수정
     */
    @PutMapping("/{boardId}")
    public ResponseEntity<CommonResponse<?>> updateBoard(
            @PathVariable Long boardId,
            @Valid @RequestBody BoardRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "게시물 수정 실패");
        }
        try {
            Board board = boardService.updateBoard(boardId, request, userDetails.getUser());
            BoardResponse response = new BoardResponse(board);

            return getResponseEntity(response, "게시물 수정 성공");

        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }

    }

    /**
     * 게시물 삭제
     */
    @DeleteMapping("/boards/{boardId}")
    public ResponseEntity<CommonResponse<?>> deleteBoard(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try {
            Long response = boardService.deleteBoard(boardId, userDetails.getUser());

            return getResponseEntity(response, "게시물 삭제 성공");

        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

}
