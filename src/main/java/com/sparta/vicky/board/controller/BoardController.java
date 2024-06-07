package com.sparta.vicky.board.controller;

import com.sparta.vicky.board.dto.BoardRequest;
import com.sparta.vicky.board.dto.BoardResponse;
import com.sparta.vicky.board.entity.Board;
import com.sparta.vicky.board.service.BoardService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BoardController {

    private BoardService boardService;

    //전체게시물 조회
    @GetMapping("/boards")
    public ResponseEntity<CommonResponse<?>> getAllBoards() {
        List<BoardResponse> response = boardService.getAllBoards().stream().map(BoardResponse::new).toList();
        return getResponseEntity(response, "Retrieved all schedules successfully");
    }

    //게시물 작성
    @PostMapping("/boards")
    public BoardResponse createBoard(@Valid @RequestBody BoardRequest request,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails,
                                     BindingResult bindingResult) throws IllegalArgumentException {
        if (bindingResult.hasErrors())
            return getFieldErrorResponseEntity(bindingResult, "게시물을 작성하는데 실패하였습니다!");
        try {
            Board board = boardService.createBoard(request, userDetails.getUser());
            BoardResponse response = new BoardResponse(board);
            return getResponseEntity(response, "게시물 작성이 완료되었습니다!");
        } catch (Exception e) {
            return getBadRequestEntity(e);
        }

    }

    //내 게시물 조회
    @GetMapping("/boards/{userId}")
    public ResponseEntity<CommonResponse<?>> getMyBoard(@PathVariable Long userId) {
        List<BoardResponse> response = boardService.getMyBoard(userId).stream().map(BoardResponse::new).toList();
        return getResponseEntity(response, "나의 게시물이 성공적으로 조회되었습니다!");
    }

    //특정 게시물 조회
    @GetMapping("/boards/{boardId}")
    public ResponseEntity<CommonResponse<?>> getBoard(@PathVariable Long boardId) throws IllegalArgumentException {
        try {
            BoardResponse response = new BoardResponse(boardService.getBoard(boardId));
            return getResponseEntity(response, "해당 게시물이 성공적으로 조회되었습니다!");
        } catch (Exception e) {
            return getBadRequestResponseException(e);
        }
    }

    //게시물 수정
    @PutMapping("/boards/{boardId}")
    public BoardResponse editBoard(@PathVariable Long boardId,
                                   @Valid @RequestBody BoardRequest request,
                                   @AuthenticationPrincipal UserDetailsImpl userDetails,
                                   BindingResult bindingResult) throws IllegalArgumentException {
        if (bindingResult.hasErrors())
            return getFieldErrorResponseEntity(bindingResult, "게시물을 수정하는데 실패하였습니다!");
        try {
            Board board = boardService.editBoard(boardId, request, userDetails.getUser());
            BoardResponse response = new BoardResponse(board);
            return getResponseEntity(response, "Schedule updated successfully");
        } catch (Exception e) {
            return getBadRequestResponseException(e);
        }

    }

    //게시물 삭제
    @DeleteMapping("/boards/{boardId}")
    public ResponseEntity<CommonResponse<?>> deleteBoard(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IllegalArgumentException {
        try {
            Long response = boardService.deleteBoard(boardId, userDetails.getUser());

            return getResponseEntity(response, "게시물이 성공적으로 삭제되었습니다");
        } catch (Exception e) {
            return getBadRequestResponseException(e);
        }
    }
}
