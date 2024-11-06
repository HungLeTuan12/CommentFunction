package com.ecommerce.comment.request;

import com.ecommerce.comment.entity.Product;
import com.ecommerce.comment.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {
    @NotNull(message = "Content can not null")
    @Size(min = 10,max = 255,message = "Length of title must between 10 and 255")
    private String content;
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not be more than 5")
    private Integer rating;
    private LocalDateTime createdAt = LocalDateTime.now();
    boolean isApproved = false;
    private Long userId;
    private Long productId;
}
