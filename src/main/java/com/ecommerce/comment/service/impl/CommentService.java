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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

@Service
// Handle all logic of comment entity
public class CommentService implements ICommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
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
    // Function: check comment has sensitive words
    // Params: comment input
    public boolean containsSensitiveWords(String comment) {
        Set<String> sensitiveWords = SensitiveWords.getSensitiveWords();
        for(String word : sensitiveWords) {
            Pattern pattern = Pattern.compile("\\b" + Pattern.quote(word) + "\\b", Pattern.CASE_INSENSITIVE);
            if (pattern.matcher(comment).find()) {
                return true; // Nếu tìm thấy từ nhạy cảm, trả về true
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
    // Find comment by : product name, customer name
    // Pagination with each page has 3 comments
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
    // Delete comment by id
    @Override
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));
        comment.setApproved(false);
        commentRepository.deleteById(id);
    }
    // Get comment statistics by total comments, approved comments, and unapproved comments
    @Override
    public Map<String, Object> getCommentStatistics() {
        return null;
    }

    // Check comment if comment content has contains sensitive words, throw exception
    // If it hasn't contains exception, allow comment and shows for user
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
    // Show total approved comments
    @Override
    public Long countApprovedComments() {
        return commentRepository.countApprovedComments();
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
