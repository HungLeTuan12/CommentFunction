package com.ecommerce.comment.repository;

import com.ecommerce.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllComments(Pageable pageable);
    // Show all comments is not approved
    List<Comment> findByIsApprovedFalse();
    // If has condition field is null, skip this field and find the next field
    // If field is not null, will find all of comments match with this condition
    @Query("SELECT c FROM Comment c " +
            "WHERE (:productName IS NULL OR c.product.name LIKE %:productName%) " +
            "AND (:username IS NULL OR c.user.username LIKE %:username%) " +
            "AND (:startDate IS NULL OR :endDate IS NULL OR c.createdAt BETWEEN :startDate AND :endDate) " +
            "order by c.createdAt"
    )
    List<Comment> searchCommentsBy(@Param("productName") String productName,
                                   @Param("username") String username,
                                   @Param("startDate") LocalDateTime startDate,
                                   @Param("endDate") LocalDateTime endDate,
                                   Pageable pageable
                                   );
}
