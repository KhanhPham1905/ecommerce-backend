package com.ghtk.ecommercewebsite.services.rate;

import com.ghtk.ecommercewebsite.models.entities.Comment;
import com.ghtk.ecommercewebsite.models.entities.Rate;
import com.ghtk.ecommercewebsite.repositories.CommentRepository;
import com.ghtk.ecommercewebsite.repositories.RateRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RateServiceImpl implements RateService {


    private final RateRepository rateRepository;


    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public void updateRate(Long productId) {
        List<Comment> commentList = commentRepository.findCommentsByProductId(productId);
        long newQuantity = commentList.size();
        BigDecimal totalStars = commentList.stream()
                .map(Comment::getRateStars)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal newAverageStars = totalStars.divide(BigDecimal.valueOf(newQuantity), 2, RoundingMode.HALF_UP);

        Rate rate = rateRepository.findByProductId(productId);
        if (rate != null) {
            rate.setAverageStars(newAverageStars);
            rate.setQuantity(newQuantity);
        } else {
            rate = new Rate();
            rate.setProductId(productId);
            rate.setAverageStars(newAverageStars);
            rate.setQuantity(newQuantity);
        }

        rateRepository.save(rate);

    }
}
