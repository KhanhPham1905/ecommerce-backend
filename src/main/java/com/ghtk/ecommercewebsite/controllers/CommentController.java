package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.models.dtos.CommentDTO;
import com.ghtk.ecommercewebsite.models.dtos.request.AddCommentRequestDTO;
import com.ghtk.ecommercewebsite.models.dtos.request.UpdateCommentRequestDTO;
import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.services.comment.ICommentService;
import com.ghtk.ecommercewebsite.services.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final ICommentService commentService;

    private final UserService userService;

    @PostMapping("/add_comment")
    public CommonResult<CommentDTO> addComment(@Valid @RequestBody AddCommentRequestDTO requestDTO) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            CommentDTO commentDTO = commentService.addComment(requestDTO, user.getId(), user.getFullName());
            return CommonResult.success(commentDTO, "Comment added successfully");
        } catch (Exception e) {
            return CommonResult.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }


    @DeleteMapping("/delete_comment/{id}")
    public CommonResult<String> deleteComment(@PathVariable Long id) {
        return commentService.findById(id)
                .map(comment -> {
                    commentService.deleteComment(id);
                    return CommonResult.success("Comment with ID " + id + " has been deleted.");
                }).orElse(CommonResult.error(404, "Comment not found"));
    }





    @GetMapping("/product/{productId}/sort-by-date")
    public CommonResult<List<CommentDTO>> getCommentsByProductIdSortedByDate(@PathVariable("productId") Long productId) {
        try {
            List<CommentDTO> comments = commentService.getCommentsByProductIdAndSortByDate(productId);
            return CommonResult.success(comments, "Comments sorted by date retrieved successfully");
        } catch (Exception e) {
            return CommonResult.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred while retrieving comments");
        }
    }

    @GetMapping("/product/{productId}/sort-by-rating")
    public CommonResult<List<CommentDTO>> getCommentsByProductIdSortedByRating(@PathVariable("productId") Long productId) {
        try {
            List<CommentDTO> comments = commentService.getCommentsByProductIdAndSortByRating(productId);
            return CommonResult.success(comments, "Comments sorted by rating retrieved successfully");
        } catch (Exception e) {
            return CommonResult.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred while retrieving comments");
        }
    }

    @PutMapping("/update_comment/{id}")
    public CommonResult<CommentDTO> updateComment(@PathVariable("id") Long id, @Valid @RequestBody UpdateCommentRequestDTO requestDTO) {
        try {
            CommentDTO commentDTO = commentService.updateComment(id, requestDTO);
            return CommonResult.success(commentDTO, "Comment updated successfully");
        } catch (IllegalArgumentException e) {
            return CommonResult.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            return CommonResult.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred while updating the comment");
        }
    }


}

