package com.ghtk.ecommercewebsite.services.comment;

import com.ghtk.ecommercewebsite.models.dtos.CommentDTO;
import com.ghtk.ecommercewebsite.models.dtos.request.AddCommentRequestDTO;
import com.ghtk.ecommercewebsite.models.dtos.request.UpdateCommentRequestDTO;
import com.ghtk.ecommercewebsite.models.entities.Comment;

import java.util.List;
import java.util.Optional;

public interface ICommentService {
    CommentDTO addComment(AddCommentRequestDTO requestDTO);

    Optional<Comment> findById(Long id);

    void deleteComment(Long id);

    List<CommentDTO> getCommentsByProductIdAndSortByDate(Long productId);

    List<CommentDTO> getCommentsByProductIdAndSortByRating(Long productId);

    CommentDTO updateComment(Long id, UpdateCommentRequestDTO requestDTO) ;

    Comment updateCommentStatus(Long id, Comment.CommentStatus status) ;

}
