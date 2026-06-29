package cl.duoc.fullstack.review_service_m7.service;

import cl.duoc.fullstack.review_service_m7.dto.ReviewRequest;
import cl.duoc.fullstack.review_service_m7.dto.ReviewResponse;
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

    public ReviewResponse createReview(ReviewRequest request) {
        log.info("Registrando nueva resena para el producto ID: {}", request.productId());

        Review review = new Review();
        review.setProductId(request.productId());
        review.setUserId(request.userId());
        review.setRating(request.rating());
        review.setComment(request.comment());

        Review saved = reviewRepository.save(review);
        log.info("Resena registrada exitosamente con ID: {}", saved.getId());
        return toResponse(saved);
    }

    public List<ReviewResponse> getReviewsByProduct(Long productId) {
        return reviewRepository.findByProductId(productId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ReviewResponse toResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getProductId(),
                review.getUserId(),
                review.getRating(),
                review.getComment()
        );
    }
}
