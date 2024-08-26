package com.ghtk.ecommercewebsite.repositories;

import com.ghtk.ecommercewebsite.models.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Các phương thức truy vấn tùy chỉnh nếu cần

    @Query("SELECT c FROM Comment c WHERE c.productId = :productId")
    List<Comment> findCommentsByProductId(@Param("productId") Long productId);

}
