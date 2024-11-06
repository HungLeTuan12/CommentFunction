package com.ecommerce.comment.controller;

import com.ecommerce.comment.entity.Comment;
import com.ecommerce.comment.request.CommentRequest;
import com.ecommerce.comment.response.CommentResponse;
import com.ecommerce.comment.response.ErrorResponse;
import com.ecommerce.comment.response.SuccessResponse;
import com.ecommerce.comment.service.impl.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;
    // Add new comment, demo
    @PostMapping("/add-comment")
    public ResponseEntity<?> addNewComment(@RequestBody @Valid CommentRequest commentRequest) {
            Comment comment = commentService.addNewComment(commentRequest);
            CommentResponse commentResponse = convertEntityToResponse(comment);
            return ResponseEntity.ok(new SuccessResponse<>("Adding successfully",commentResponse));
    }
    // Get all comments
    @GetMapping("/findAll")
    public ResponseEntity<?> getAllComment(@RequestParam(value = "page",defaultValue = "1") int page,
                                           @RequestParam(value = "size",defaultValue = "100") int size) {
        Pageable pageable = PageRequest.of(page-1,size);
        List<CommentResponse> commentResponses = commentService.findAllComment(pageable);
        return ResponseEntity.ok(new SuccessResponse<>("Get success",commentResponses));
    }
    // Get list of unapproved comments
    // happen when isApproved = false
    @GetMapping("/pending")
    public ResponseEntity<?> getPendingReviews() {
        List<Comment> list = commentService.findAllCommentNotApproval();
        List<CommentResponse> commentResponses = new ArrayList<>();
        for(Comment comment : list) {
            CommentResponse commentResponse = convertEntityToResponse(comment);
            commentResponses.add(commentResponse);
        }
        return ResponseEntity.ok(new SuccessResponse<>("Get success", commentResponses));
    }
    // Check validation for comments: check whether comment contains sensitive words
    @PostMapping("/check-comments/{id}")
    public ResponseEntity<?> checkComments(@PathVariable("id") Long commentId) {
        Comment comment = commentService.checkComment(commentId);
        CommentResponse commentResponse = convertEntityToResponse(comment);
        return ResponseEntity.ok(new SuccessResponse<>("Adding successfully",commentResponse));
    }
    // Delete comment by comment id
    @DeleteMapping("/delete-comment/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable("id") Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(new SuccessResponse<>("Delete successfully"));
    }
    // Find comments by field
    // Scope: productName, product name, day of comment
    @GetMapping("/find-comments")
    public ResponseEntity<?> findCommentsBy(
            @RequestParam(value = "page",defaultValue = "1") int page,
            @RequestParam(value = "size",defaultValue = "100") int size,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Pageable pageable = PageRequest.of(page-1,size);
        List<CommentResponse> comments = commentService.findCommentByField(productName, customerName, startDate, endDate, pageable);
        return ResponseEntity.ok(new SuccessResponse<>("Get success", comments));
    }
    // Convert entity comment from database to comment response for user
    public CommentResponse convertEntityToResponse(Comment comment) {
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setContent(comment.getContent());
        commentResponse.setRating(comment.getRating());
        commentResponse.setUsername(comment.getUser().getUsername());
        commentResponse.setProductName(comment.getProduct().getName());
        return commentResponse;
    }
}
