package com.sparta.vicky.comment.service;

import com.sparta.vicky.comment.Repository.CommentRepository;
import com.sparta.vicky.comment.dto.CommentRequest;
import com.sparta.vicky.comment.entity.Comment;
import com.sparta.vicky.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final BoardService boardService;
    private final CommentRepository commentRepository;

    /**
     * 해당 일정에 새로운 댓글 추가
     */
    @Transactional
    public Comment createComment(CommentRequest request, User user) {
        Board board = boardService.getBoard(request.getBoardId());
        Comment comment = Comment.createComment(request, board, user);

        return commentRepository.save(comment);
    }

    /**
     * 해당 일정의 모든 댓글 조회
     */
    public List<Comment> getComments(Long boardId) {
        return commentRepository.findAllByScheduleIdOrderByCreatedDate(boardId);
    }

    /**
     * 댓글 조회
     */
    public Comment getComment(Long boardId, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new IllegalArgumentException("Comment with id " + commentId + " does not exist")
        );
        comment.verify(boardId);

        return comment;
    }

    /**
     * 댓글 수정
     */
    @Transactional
    public Comment updateComment(Long boardId, Long commentId, CommentRequest request, User user) {
        Comment comment = getComment(boardId, commentId);
        comment.verify(user);
        comment.update(request);

        return comment;
    }

    /**
     * 댓글 삭제
     */
    @Transactional
    public Long deleteComment(Long boardId, Long commentId, User user) {
        Comment comment = getComment(boardId, commentId);
        comment.verify(user);
        commentRepository.delete(comment);

        return commentId;
    }

}
