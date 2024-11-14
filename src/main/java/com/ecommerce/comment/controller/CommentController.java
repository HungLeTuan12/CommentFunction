package com.ecommerce.comment.controller;

import com.ecommerce.comment.entity.Comment;
import com.ecommerce.comment.dto.request.CommentRequest;
import com.ecommerce.comment.dto.response.CommentResponse;
import com.ecommerce.comment.response.SuccessResponse;
import com.ecommerce.comment.service.impl.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    // Get all comments function
    @Operation(summary = "Retrieve all comments with pagination",
            description = "Get a paginated list of all comments. Use the 'page' and 'size' parameters to customize pagination.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of comments"),
            @ApiResponse(responseCode = "404", description = "Cannot find by any fields"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/findAll")
    public ResponseEntity<?> getAllComment(@Parameter(description = "Page number for pagination, starting from 1", example = "1")
                                               @RequestParam(value = "page",defaultValue = "1") int page,
                                           @Parameter(description = "Number of comments per page", example = "100")
                                           @RequestParam(value = "size",defaultValue = "100") int size) {
            Pageable pageable = PageRequest.of(page-1,size);
            List<CommentResponse> commentResponses = commentService.findAllComment(pageable);
            return ResponseEntity.ok(new SuccessResponse<>("Get success",commentResponses));
    }
    // Find comments by field
    // Scope: productName, username, day of comment
    @Operation(summary = "Find comments with optional filters and pagination",
            description = "Retrieve a list of comments based on optional filters (product name, customer name, date range) and pagination parameters (page and size).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of comments"),
            @ApiResponse(responseCode = "404", description = "Cannot find the entity by fields"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/find-comments")
    public ResponseEntity<?> findCommentsBy(
            @Parameter(description = "Page number for pagination, starting from 1", example = "1")
            @RequestParam(value = "page", defaultValue = "1") int page,

            @Parameter(description = "Number of comments per page", example = "100")
            @RequestParam(value = "size", defaultValue = "100") int size,

            @Parameter(description = "Optional filter by product name", example = "Product A")
            @RequestParam(required = false) String productName,

            @Parameter(description = "Optional filter by customer name", example = "John Doe")
            @RequestParam(required = false) String customerName
    ) {
        Pageable pageable = PageRequest.of(page-1,size);
        List<CommentResponse> comments = commentService.findCommentByField(productName, customerName, pageable);
        return ResponseEntity.ok(new SuccessResponse<>("Get success", comments));
    }
    // Get list of unapproved comments
    // happen when isApproved = false
    @Operation(summary = "Retrieve pending comments with pagination",
            description = "Get a paginated list of comments that are pending approval. Use the 'page' and 'size' parameters to customize pagination.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of pending comments"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/pending")
    public ResponseEntity<?> getPendingReviews(
            @Parameter(description = "Page number for pagination, starting from 1", example = "1")
            @RequestParam(value = "page",defaultValue = "1") int page,
            @Parameter(description = "Number of comments per page", example = "100")
            @RequestParam(value = "size",defaultValue = "100") int size) {
            Pageable pageable = PageRequest.of(page-1,size);
            List<CommentResponse> commentResponses = commentService.findAllCommentNotApproval(pageable);
            return ResponseEntity.ok(new SuccessResponse<>("Get success",commentResponses));
    }

    // Check validation for comments: check whether comment contains sensitive words
    @Operation(summary = "Check a comment by ID",
            description = "Retrieve a comment by its ID and check its details. The 'id' parameter is required to identify the comment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the comment"),
            @ApiResponse(responseCode = "404", description = "Comment not found"),
            @ApiResponse(responseCode = "400", description = "Comment is not valid"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/check-comments/{id}")
    public ResponseEntity<?> checkComments(
            @Parameter(description = "ID of the comment to check", required = true, example = "123")
            @PathVariable("id") Long commentId) {
            Comment comment = commentService.checkComment(commentId);
            CommentResponse commentResponse = convertEntityToResponse(comment);
            return ResponseEntity.ok(new SuccessResponse<>("Adding successfully",commentResponse));
    }

    // Delete comment by comment id
    @Operation(summary = "Delete comment by ID",
            description = "Retrieve a comment by its ID and check its details. The 'id' parameter is required to identify the comment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the comment"),
            @ApiResponse(responseCode = "404", description = "Comment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/delete-comment/{id}")
    public ResponseEntity<?> deleteComment(
            @Parameter(description = "ID of the comment to check", required = true, example = "123")
            @PathVariable("id") Long commentId) {
            commentService.deleteComment(commentId);
            return ResponseEntity.ok(new SuccessResponse<>("Delete successfully"));
    }

    // Get comment statistics
    @Operation(summary = "Get comment statistics",
            description = "Retrieve various statistics for comments, including counts, averages, and other relevant data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved comment statistics"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/statistics/{productId}")
    public ResponseEntity<Map<String, Object>> getCommentStatistics(@Parameter(description = "ID of the product")
                                                                        @PathVariable("productId") Long productId) {
        Map<String, Object> statistics = commentService.getCommentStatisticsForProduct(productId);
        return ResponseEntity.ok(statistics);
    }
    // Add comment function
    @Operation(summary = "Add new comment")
    @Parameter(description = "Request payload to add a new comment", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Add new comment successfully"),
            @ApiResponse(responseCode = "500", description = "Adding comment failed")
    })
    // Add new comment, demo
    @PostMapping("/add-comment")
    public ResponseEntity<?> addNewComment(@RequestBody @Valid CommentRequest commentRequest) {
        Comment comment = commentService.addNewComment(commentRequest);
        CommentResponse commentResponse = convertEntityToResponse(comment);
        return ResponseEntity.ok(new SuccessResponse<>("Adding successfully",commentResponse));
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
