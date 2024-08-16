package com.ghtk.ecommercewebsite.services.comment;

import com.ghtk.ecommercewebsite.mapper.CommentMapper;
import com.ghtk.ecommercewebsite.models.dtos.CommentDTO;
import com.ghtk.ecommercewebsite.models.dtos.request.AddCommentRequestDTO;
import com.ghtk.ecommercewebsite.models.dtos.request.UpdateCommentRequestDTO;
import com.ghtk.ecommercewebsite.models.entities.Comment;
import com.ghtk.ecommercewebsite.models.entities.ProductItem;
import com.ghtk.ecommercewebsite.repositories.CommentRepository;
import com.ghtk.ecommercewebsite.repositories.ProductItemRepository;
import com.ghtk.ecommercewebsite.services.rate.RateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements ICommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final ProductItemRepository productItemRepository ;
    private final RateService rateService;

    @Override
    public CommentDTO addComment(AddCommentRequestDTO requestDTO) {
        Comment comment = Comment.builder()
                .content(requestDTO.getContent())
                .productItemId(requestDTO.getProductItemId())
                .userId(requestDTO.getUserId())
                .rateStars(requestDTO.getRateStars())
                .replyTo(requestDTO.getReplyTo())
                .status(Comment.CommentStatus.APPROVED)
                .build();

        ProductItem productItem = productItemRepository.findById(requestDTO.getProductItemId())
                .orElseThrow(() -> new IllegalArgumentException("ProductItem not found"));
        rateService.updateRate(productItem.getProductId()) ;


        Comment saved = commentRepository.save(comment);
        return commentMapper.toDto(saved);
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public List<CommentDTO> getCommentsByProductIdAndSortByDate(Long productId) {
        List<Comment> commentList = commentRepository.findCommentsByProductId(productId);

        return commentList.stream()
                .filter(comment -> "APPROVED".equals(comment.getStatus()))
                .sorted(Comparator.comparing(Comment::getCreatedAt).reversed())
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDTO> getCommentsByProductIdAndSortByRating(Long productId) {
        List<Comment> commentList = commentRepository.findCommentsByProductId(productId);
        return commentList.stream()
                .filter(comment -> "APPROVED".equals(comment.getStatus()))
                .sorted(Comparator.comparing(Comment::getRateStars).reversed())
                .map(commentMapper::toDto)
                .collect(Collectors.toList());

    }

    @Override
    public CommentDTO updateComment(Long id, UpdateCommentRequestDTO requestDTO) {
        Comment comment = commentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Comment not found"));
        comment.setContent(requestDTO.getContent());
        comment.setRateStars(requestDTO.getRateStars());
        comment.setModifiedAt(LocalDateTime.now());
        commentRepository.save(comment);
        return commentMapper.toDto(comment);
    }

    public Comment getCommentById(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Comment not found"));
    }

    @Override
    public Comment updateCommentStatus(Long id, Comment.CommentStatus status) {
        Comment comment = getCommentById(id);
        comment.setStatus(status);
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void softDeleteComment(Long commentId) {
        // Tìm kiếm bình luận dựa trên ID
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        // Cập nhật trạng thái bình luận thành REJECTED để xóa mềm
        comment.setStatus(Comment.CommentStatus.REJECTED);
        // Lưu lại thay đổi vào cơ sở dữ liệu
        commentRepository.save(comment);
    }


}