package com.sparta.vicky.comment.controller;

import com.sparta.vicky.CommonResponse;
import com.sparta.vicky.comment.dto.CommentRequest;
import com.sparta.vicky.comment.dto.CommentResponse;
import com.sparta.vicky.comment.entity.Comment;
import com.sparta.vicky.comment.service.CommentService;
import com.sparta.vicky.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static com.sparta.vicky.comment.controller.ControllerUtils.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards/{boardId}/comments")
public class CommentController {

    private final CommentService commentService;

    /**
     * 새로운 댓글 생성 후 해당 일정에 추가
     */
    @PostMapping
    public ResponseEntity<CommonResponse<?>> createComment(
            @PathVariable Long scheduleId,
            @Valid @RequestBody CommentRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) throws IllegalArgumentException {
        // 바인딩 예외 처리
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "Failed to create comment");
        }
        try {
            verifyPathVariable(scheduleId, request);

            Comment comment = commentService.createComment(request, userDetails.getUser());
            CommentResponse response = new CommentResponse(comment);

            return getResponseEntity(response, "Comment created successfully");

        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 모든 댓글 조회
     */
    @GetMapping
    public ResponseEntity<CommonResponse<?>> getComments(
            @PathVariable Long scheduleId
    ) {
        List<CommentResponse> response = commentService.getComments(scheduleId)
                .stream().map(CommentResponse::new).toList();

        return getResponseEntity(response, "Retrieved all comments successfully");
    }

    /**
     * 댓글 조회
     */
    @GetMapping("/{commentId}")
    public ResponseEntity<CommonResponse<?>> getComment(
            @PathVariable Long scheduleId,
            @PathVariable Long commentId
    ) throws IllegalArgumentException {
        try {
            Comment comment = commentService.getComment(scheduleId, commentId);
            CommentResponse response = new CommentResponse(comment);

            return getResponseEntity(response, "Retrieved comment successfully");

        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 댓글 수정
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<CommonResponse<?>> updateComment(
            @PathVariable Long scheduleId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) throws IllegalArgumentException {
        // 바인딩 예외 처리
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "Failed to update comment");
        }
        try {
            verifyPathVariable(scheduleId, request);

            Comment comment = commentService.updateComment(scheduleId, commentId, request, userDetails.getUser());
            CommentResponse response = new CommentResponse(comment);

            return getResponseEntity(response, "Comment updated successfully");

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
    ) throws IllegalArgumentException {
        try {
            Long response = commentService.deleteComment(boardId, commentId, userDetails.getUser());

            return getResponseEntity(response, "Comment deleted successfully");

        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * PathVariable scheduleId가 RequestBody scheduleId와 같은지 검증
     */
    private static void verifyPathVariable(Long boardId, CommentRequest request) {
        if (!Objects.equals(boardId, request.getBoardId())) {
            throw new IllegalArgumentException("Schedule id does not match");
        }
    }

}