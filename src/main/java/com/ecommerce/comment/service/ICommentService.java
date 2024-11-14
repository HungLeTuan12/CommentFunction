package com.ecommerce.comment.service;

import com.ecommerce.comment.entity.Comment;
import com.ecommerce.comment.dto.request.CommentRequest;
import com.ecommerce.comment.dto.response.CommentResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

// This interface will provide for user all functions for comments entity
public interface ICommentService {
    // Get all comment from user
    public List<CommentResponse> findAllComment(Pageable pageable);

    /**
     * Function: Get List of comment is unapproved
     * @param pageable: pagination for return data
     * @return information of comment for user
     */
    public List<CommentResponse> findAllCommentNotApproval(Pageable pageable);

    /**
     * Pagination with each page has 3 comments
     * @param productName: name of product
     * @param customerName: name of customer
     * @param pageable: pagination for data
     */
    public List<CommentResponse> findCommentByField(String productName, String customerName,  Pageable pageable);
    /**
     * This function is responsible for deleting comment by id of comment
     * @param id: id of comment
     * Ecxeption: Comment not found -> throw ResourceNotFoundException
     */
    public void deleteComment(Long id);
    /**
     *
     * @param productId: id of product
     * @return map object contains information about total comments, Approved Comments, UnapprovedComments and rating quantity
     */
    public Map<String, Object> getCommentStatisticsForProduct(Long productId);
    /**
     * Check comment if comment content has contains sensitive words, throw exception
     * If it hasn't contains exception, allow comment and shows for user
     * @param commentId: id of comment
     * @return comment after checking with field isApproved(comment entity) of comment is true
     */
    public Comment checkComment(Long commentId);
    public Comment addNewComment(CommentRequest commentRequest);
}
