package com.ecommerce.comment.service;

import com.ecommerce.comment.entity.Comment;
import com.ecommerce.comment.request.CommentRequest;
import com.ecommerce.comment.response.CommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ICommentService {
    public Comment addNewComment(CommentRequest commentRequest);
    // Get all comment from user
    public List<CommentResponse> findAllComment(Pageable pageable);
    // Get all comment is not approval
    public List<Comment> findAllCommentNotApproval();
    // Get all comment by product name or date
    public List<CommentResponse> findCommentByField(String productName, String customerName, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    // Delete comment
    // Params: id - comment id attribute
    public void deleteComment(Long id);
    public String commentStatistic();
    // Function: check validation of comment content
    public Comment checkComment(Long commentId);
}
