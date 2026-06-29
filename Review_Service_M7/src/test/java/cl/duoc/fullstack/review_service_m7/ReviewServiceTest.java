package cl.duoc.fullstack.review_service_m7;

import cl.duoc.fullstack.review_service_m7.dto.ReviewRequest;
import cl.duoc.fullstack.review_service_m7.dto.ReviewResponse;
import cl.duoc.fullstack.review_service_m7.model.Review;
import cl.duoc.fullstack.review_service_m7.repository.ReviewRepository;
import cl.duoc.fullstack.review_service_m7.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        reviewService = new ReviewService(reviewRepository);
    }

    @Test
    void createReview_WithValidRequest_ShouldReturnReviewResponse() {
        // Given
        ReviewRequest request = new ReviewRequest(1L, 1L, 5, "Excelente producto");
        Review saved = new Review(1L, 1L, 1L, 5, "Excelente producto");
        when(reviewRepository.save(any())).thenReturn(saved);

        // When
        ReviewResponse response = reviewService.createReview(request);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.productId());
        assertEquals(1L, response.userId());
        assertEquals(5, response.rating());
        assertEquals("Excelente producto", response.comment());
        verify(reviewRepository).save(any());
    }

    @Test
    void createReview_ShouldSetAllFieldsCorrectly() {
        // Given
        ReviewRequest request = new ReviewRequest(2L, 3L, 3, "Producto regular");

        Review saved = new Review(2L, 2L, 3L, 3, "Producto regular");
        when(reviewRepository.save(any())).thenReturn(saved);

        // When
        ReviewResponse response = reviewService.createReview(request);

        // Then
        assertEquals(2L, response.productId());
        assertEquals(3L, response.userId());
        assertEquals(3, response.rating());
        assertEquals("Producto regular", response.comment());
    }

    @Test
    void createReview_WithNullComment_ShouldReturnNullComment() {
        // Given
        ReviewRequest request = new ReviewRequest(1L, 1L, 4, null);
        Review saved = new Review(3L, 1L, 1L, 4, null);
        when(reviewRepository.save(any())).thenReturn(saved);

        // When
        ReviewResponse response = reviewService.createReview(request);

        // Then
        assertNull(response.comment());
    }

    @Test
    void getReviewsByProduct_ShouldReturnListOfReviews() {
        // Given
        Review review1 = new Review(1L, 1L, 1L, 5, "Bueno");
        Review review2 = new Review(2L, 1L, 2L, 4, "Muy bueno");
        when(reviewRepository.findByProductId(1L)).thenReturn(List.of(review1, review2));

        // When
        List<ReviewResponse> responses = reviewService.getReviewsByProduct(1L);

        // Then
        assertEquals(2, responses.size());
        assertEquals(5, responses.get(0).rating());
        assertEquals(4, responses.get(1).rating());
    }
}
