package com.sparta.vicky.like.service;

import com.sparta.vicky.board.service.BoardService;
import com.sparta.vicky.comment.service.CommentService;
import com.sparta.vicky.like.dto.LikeRequest;
import com.sparta.vicky.like.entity.ContentType;
import com.sparta.vicky.like.entity.Like;
import com.sparta.vicky.like.entity.LikeStatus;
import com.sparta.vicky.like.repository.LikeRepository;
import com.sparta.vicky.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private final BoardService boardService;
    private final CommentService commentService;
    private final LikeRepository likeRepository;

    /**
     * 좋아요 토글
     */
    @Transactional
    public Like toggleLike(LikeRequest request, User user) {
        ContentType contentType = request.getContentType();
        Long contentId = request.getContentId();

        Like like = likeRepository.findByContentTypeAndContentId(contentType, contentId)
                .orElse(createLike(request, user));

        if (!like.getStatus().equals(LikeStatus.CANCELED)) {
            doLike(like, user); // 취소된 좋아요이거나 신규 좋아요인 경우 좋아요
        } else {
            cancelLike(like, user); // 이미 좋아요 상태인 경우 좋아요 취소
        }

        return like;
    }

    /**
     * 신규 좋아요 생성
     */
    public Like createLike(LikeRequest request, User user) {
        Long contentId = request.getContentId();
        User contentUser = null;

        switch (request.getContentType()) {
            case BOARD -> contentUser = boardService.getBoard(contentId).getUser();
            case COMMENT -> contentUser = commentService.findById(contentId).getUser();
        }

        if (contentUser.equals(user)) {
            throw new IllegalArgumentException("본인이 작성한 컨텐츠에 좋아요를 남길 수 없습니다.");
        }
        return Like.createLike(request, user);
    }

    /**
     * 좋아요
     */
    private void doLike(Like like, User user) {
        like.doLike(user);

        // 해당 컨텐츠의 좋아요 수 늘리기
        Long contentId = like.getContentId();
        switch (like.getContentType()) {
            case BOARD -> boardService.getBoard(contentId).increaseLikeCount();
            case COMMENT -> commentService.findById(contentId).increaseLikeCount();
        }
    }

    /**
     * 좋아요 취소
     */
    private void cancelLike(Like like, User user) {
        like.cancelLike(user);

        // 해당 컨텐츠의 좋아요 수 줄이기
        Long contentId = like.getContentId();
        switch (like.getContentType()) {
            case BOARD -> boardService.getBoard(contentId).decreaseLikeCount();
            case COMMENT -> commentService.findById(contentId).decreaseLikeCount();
        }
    }

}
