package cl.duoc.fullstack.review_service_m7.service;

import cl.duoc.fullstack.review_service_m7.model.Review;
import cl.duoc.fullstack.review_service_m7.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public Review createReview(Review review) {
        log.info("Registrando nueva rese\u00f1a para el producto ID: " + review.getProductId());
        Review saved = reviewRepository.save(review);
        log.info("Rese\u00f1a registrada exitosamente con ID: " + saved.getId());
        return saved;
    }

    public List<Review> getReviewsByProduct(Long productId) {
        return reviewRepository.findByProductId(productId);
    }
}
