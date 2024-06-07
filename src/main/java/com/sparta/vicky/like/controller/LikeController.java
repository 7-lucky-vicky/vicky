package com.sparta.vicky.like.controller;

import com.sparta.vicky.base.dto.CommonResponse;
import com.sparta.vicky.like.dto.LikeRequest;
import com.sparta.vicky.like.dto.LikeResponse;
import com.sparta.vicky.like.entity.Like;
import com.sparta.vicky.like.service.LikeService;
import com.sparta.vicky.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static com.sparta.vicky.util.ControllerUtil.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LikeController {

    private final LikeService likeService;

    /**
     * 게시물 좋아요 토글
     */
    @PostMapping("/boards/{boardId}/like")
    public ResponseEntity<CommonResponse<?>> toggleBoardLike(
            @PathVariable Long boardId,
            @Valid @RequestBody LikeRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "게시물 좋아요 토글 실패");
        }
        try {
            verifyPathVariable(boardId, request.getContentId());

            Like like = likeService.toggleLike(request, userDetails.getUser());
            LikeResponse response = new LikeResponse(like);

            return getResponseEntity(response, "게시물 좋아요 토글 성공");

        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 댓글 좋아요 토글
     */
    @PostMapping("/comments/{commentId}/like")
    public ResponseEntity<CommonResponse<?>> toggleCommentLike(
            @PathVariable Long commentId,
            @Valid @RequestBody LikeRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "댓글 좋아요 토글 실패");
        }
        try {
            verifyPathVariable(commentId, request.getContentId());

            Like like = likeService.toggleLike(request, userDetails.getUser());
            LikeResponse response = new LikeResponse(like);

            return getResponseEntity(response, "댓글 좋아요 토글 성공");

        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    private static void verifyPathVariable(Long contentId, Long requestId) {
        if (!Objects.equals(contentId, requestId)) {
            throw new IllegalArgumentException("PathVariable의 contentId와 RequestBody의 contentId가 다릅니다.");
        }
    }

}
