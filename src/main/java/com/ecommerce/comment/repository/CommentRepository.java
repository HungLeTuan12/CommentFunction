package com.ecommerce.comment.repository;

import com.ecommerce.comment.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c")
    List<Comment> findAllComments(Pageable pageable);
    // Show all comments is not approved
    @Query("SELECT c FROM Comment c WHERE c.isApproved = false")
    List<Comment> findCommentIsApprovedFalse(Pageable pageable);
    // If has condition field is null, skip this field and find the next field
    // If field is not null, will find all of comments match with this condition
    // Param: productName: find by productName has contains string which user enter data
    @Query("SELECT c FROM Comment c " +
            "WHERE (:productName IS NULL OR c.product.name LIKE %:productName%) " +
            "AND (:username IS NULL OR c.user.username LIKE %:username%) " +
            "order by c.createdAt"
    )
    List<Comment> searchCommentsBy(@Param("productName") String productName,
                                   @Param("username") String username,
                                   Pageable pageable
                                   );
    // Count approved comments
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.isApproved = true")
    Long countApprovedComments();
    // Gọi stored procedure `sp_comment_statistics`
    @Procedure(name = "sp_comment_statistics")
    List<Object[]> getCommentStatistics(@Param("product_id") Long productId);
}
