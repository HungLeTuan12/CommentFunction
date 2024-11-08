package com.ecommerce.comment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// A Comment entity typically represents a comment on a product
// One user has many comments for one product
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    // rating start from 1 and end is 5
    private Integer rating;
    private LocalDateTime createdAt = LocalDateTime.now();
    // false: comment is unapproved
    // true: comment is approved
    boolean isApproved = false;
    // One user has many comments
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;
    // One product has many comments
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private Product product;
}
