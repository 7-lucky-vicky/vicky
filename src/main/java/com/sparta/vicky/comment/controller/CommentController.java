package com.sparta.vicky.comment.controller;

import com.sparta.vicky.comment.dto.CommentRequest;
import com.sparta.vicky.comment.dto.CommentResponse;
import com.sparta.vicky.comment.entity.Comment;
import com.sparta.vicky.comment.service.CommentService;
import com.sparta.vicky.base.dto.CommonResponse;
import com.sparta.vicky.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static com.sparta.vicky.util.ControllerUtil.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards/{boardId}/comments")
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 작성
     */
    @PostMapping
    public ResponseEntity<CommonResponse<?>> createComment(
            @PathVariable Long boardId,
            @Valid @RequestBody CommentRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "댓글 작성 실패");
        }
        try {
            verifyPathVariable(boardId, request);

            Comment comment = commentService.createComment(request, userDetails.getUser());
            CommentResponse response = new CommentResponse(comment);

            return getResponseEntity(response, "댓글 작성 성공");

        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 특정 게시물의 전체 댓글 조회
     */
    @GetMapping
    public ResponseEntity<CommonResponse<?>> getAllComments(
            @PathVariable Long boardId
    ) {
        List<CommentResponse> response = commentService.getAllComments(boardId).stream()
                .map(CommentResponse::new).toList();

        return getResponseEntity(response, "게시물 전체 댓글 조회 성공");
    }

    /**
     * 특정 댓글 조회
     */
    @GetMapping("/{commentId}")
    public ResponseEntity<CommonResponse<?>> getComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId
    ) {
        try {
            Comment comment = commentService.getComment(boardId, commentId);
            CommentResponse response = new CommentResponse(comment);

            return getResponseEntity(response, "댓글 조회 성공");

        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 댓글 수정
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<CommonResponse<?>> updateComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "댓글 수정 실패");
        }
        try {
            verifyPathVariable(boardId, request);

            Comment comment = commentService.updateComment(boardId, commentId, request, userDetails.getUser());
            CommentResponse response = new CommentResponse(comment);

            return getResponseEntity(response, "댓글 수정 성공");

        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommonResponse<?>> deleteComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try {
            Long response = commentService.deleteComment(boardId, commentId, userDetails.getUser());
            return getResponseEntity(response, "댓글 삭제 성공");

        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    private static void verifyPathVariable(Long boardId, CommentRequest request) {
        if (!Objects.equals(boardId, request.getBoardId())) {
            throw new IllegalArgumentException("PathVariable boardId가 RequestBody boardId와 다릅니다.");
        }
    }

}