package com.ecommerce.comment.service.impl;

import com.ecommerce.comment.constant.SensitiveWords;
import com.ecommerce.comment.entity.Comment;
import com.ecommerce.comment.entity.Product;
import com.ecommerce.comment.entity.User;
import com.ecommerce.comment.exception.AppException;
import com.ecommerce.comment.exception.ResourceNotFoundException;
import com.ecommerce.comment.repository.CommentRepository;
import com.ecommerce.comment.repository.ProductRepository;
import com.ecommerce.comment.repository.UserRepository;
import com.ecommerce.comment.request.CommentRequest;
import com.ecommerce.comment.response.CommentResponse;
import com.ecommerce.comment.service.ICommentService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

@Service
/**
 * Handle all logic of comment entity
 */
public class CommentService implements ICommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private EntityManager entityManager;
    // Add new comment
    @Override
    public Comment addNewComment(CommentRequest commentRequest) {
        //Find user
        User user = userRepository.findById(commentRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + commentRequest.getUserId()));
        //Find product
        Product product = productRepository.findById(commentRequest.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + commentRequest.getProductId()));
        // Create new comment
        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        comment.setRating(commentRequest.getRating());
        comment.setUser(user);
        comment.setProduct(product);
        comment.setApproved(false);
        return commentRepository.save(comment);
    }

    /**
     * Function: check whether comment has sensitive words
     * @param comment: comment input from user
     */
    private boolean containsSensitiveWords(String comment) {
        Set<String> sensitiveWords = SensitiveWords.getSensitiveWords();
        for(String word : sensitiveWords) {
            Pattern pattern = Pattern.compile("\\b" + Pattern.quote(word) + "\\b", Pattern.CASE_INSENSITIVE);
            if (pattern.matcher(comment).find()) {
                return true; // if it contains sensitive words, return true
            }
        }
        return false;
    }
    // Get list all comments
    @Override
    public List<CommentResponse> findAllComment(Pageable pageable) {
        List<Comment> comments = commentRepository.findAllComments(pageable);
        List<CommentResponse> commentResponses = new ArrayList<>();
        for(Comment comment : comments) {
            CommentResponse commentResponse = convertEntityToResponse(comment);
            commentResponses.add(commentResponse);
        }
        return commentResponses;
    }
    // List of comment is unapproved

    /**
     * Function: Get List of comment is unapproved
     * @param pageable: pagination for return data
     * @return information of comment for user
     */
    @Override
    public List<CommentResponse> findAllCommentNotApproval(Pageable pageable) {
        List<Comment> comments = commentRepository.findCommentIsApprovedFalse(pageable);
        List<CommentResponse> commentResponses = new ArrayList<>();
        for(Comment comment : comments) {
            CommentResponse commentResponse = convertEntityToResponse(comment);
            commentResponses.add(commentResponse);
        }
        return commentResponses;
    }

    /**
     * Pagination with each page has 3 comments
     * @param productName: name of product
     * @param customerName: name of customer
     * @param pageable: pagination for data
     */
    @Override
    public List<CommentResponse> findCommentByField(String productName,
                                            String customerName,
                                            Pageable pageable) {
        List<Comment> comments = commentRepository.searchCommentsBy(productName, customerName, pageable);
        if(comments.isEmpty()) {
            throw new ResourceNotFoundException("Data is not exist, please try again !");
        }
        List<CommentResponse> commentResponses = new ArrayList<>();
        for(Comment comment : comments) {
            CommentResponse commentResponse = convertEntityToResponse(comment);
            commentResponses.add(commentResponse);
        }
        return commentResponses;
    }

    /**
     * This function is responsible for deleting comment by id of comment
     * @param id: id of comment
     * Ecxeption: Comment not found -> throw ResourceNotFoundException
     */
    @Override
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));
        comment.setApproved(false);
        commentRepository.deleteById(id);
    }
    // Get comment statistics by total comments, approved comments, and unapproved comments

    /**
     *
     * @param productId: id of product
     * @return map object contains information about total comments, Approved Comments, UnapprovedComments and rating quantity
     */
    public Map<String, Object> getCommentStatisticsForProduct(Long productId) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_comment_statistics");

        // Đăng ký tham số đầu vào
        query.registerStoredProcedureParameter("product_id", Integer.class, ParameterMode.IN);
        query.setParameter("product_id", productId);

        // Thực thi stored procedure
        query.execute();

        // Trích xuất kết quả từ stored procedure
        Map<String, Object> statistics = new HashMap<>();
        List<Object[]> ratingCounts = query.getResultList();

        // Giả sử kết quả trả về đầu tiên là tổng số comment, số comment đã duyệt và chưa duyệt
        Object[] totalStats = ratingCounts.get(0);
        statistics.put("TotalComments", totalStats[0]);
        statistics.put("ApprovedComments", totalStats[1]);
        statistics.put("UnapprovedComments", totalStats[2]);
        // Tạo một Map để lưu các số lượng theo từng rating
        Map<String, Integer> ratingCountsMap = new HashMap<>();
        // Đếm số lượng comment theo từng rating
        for (Object[] row : ratingCounts) {
            Integer rating = (Integer) row[0];
            Integer count = (Integer) row[1];
            ratingCountsMap.put("Rating " + rating + " sao", count);
        }

        statistics.put("RatingCounts", ratingCountsMap);

        return statistics;
    }
    /**
     * Check comment if comment content has contains sensitive words, throw exception
     * If it hasn't contains exception, allow comment and shows for user
     * @param commentId: id of comment
     * @return comment after checking with field isApproved(comment entity) of comment is true
     */
    @Override
    public Comment checkComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));
        if(containsSensitiveWords(comment.getContent())) {
            throw new AppException("This comments contains sensitive words, please try again !");
        }
        comment.setApproved(true);
        return commentRepository.save(comment);
    }
    // Convert entity to response
    public CommentResponse convertEntityToResponse(Comment comment) {
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setContent(comment.getContent());
        commentResponse.setRating(comment.getRating());
        commentResponse.setUsername(comment.getUser().getUsername());
        commentResponse.setProductName(comment.getProduct().getName());
        return commentResponse;
    }
}
