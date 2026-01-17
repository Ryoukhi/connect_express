package com.eadl.connect_backend.infrastructure.adapter.in.web.controller.review;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.eadl.connect_backend.application.dto.ReviewDto;
import com.eadl.connect_backend.application.mapper.ReviewMapper;
import com.eadl.connect_backend.domain.model.review.Review;
import com.eadl.connect_backend.domain.model.review.Rating;
import com.eadl.connect_backend.domain.port.in.review.ReviewService;
import com.eadl.connect_backend.domain.port.out.security.CurrentUserProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReviewService reviewService;

    @Mock
    private ReviewMapper reviewMapper;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private ReviewController reviewController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    }

    @Test
    void createReview_ShouldReturnCreatedReview() throws Exception {
        // Arrange
        ReviewDto inputDto = new ReviewDto();
        inputDto.setIdReservation(10L);
        inputDto.setRating(Rating.of(5));
        inputDto.setComment("Great job");
        Long clientId = 1L;
        Long reservationId = 10L;
        Review createdReview = new Review();
        ReviewDto outputDto = new ReviewDto();
        outputDto.setIdReview(1L);
        outputDto.setIdReservation(reservationId);
        outputDto.setRating(Rating.of(5));

        when(currentUserProvider.getCurrentUserId()).thenReturn(clientId);
        when(reviewService.createReview(clientId, reservationId, Rating.of(5), "Great job")).thenReturn(createdReview);
        when(reviewMapper.toDto(createdReview)).thenReturn(outputDto);

        // Act & Assert
        mockMvc.perform(post("/api/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rating.value").value(5));
    }

    @Test
    void getReviewById_ShouldReturnReview_WhenFound() throws Exception {
        // Arrange
        Long id = 1L;
        Review review = new Review();
        ReviewDto dto = new ReviewDto();
        dto.setIdReview(id);

        when(reviewService.getReviewById(id)).thenReturn(Optional.of(review));
        when(reviewMapper.toDto(review)).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(get("/api/reviews/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReview").value(id));
    }

    @Test
    void getMyReviews_ShouldReturnListOfReviews() throws Exception {
        // Arrange
        Long clientId = 1L;
        Review review = new Review();
        ReviewDto dto = new ReviewDto();
        dto.setIdReview(1L);

        when(currentUserProvider.getCurrentUserId()).thenReturn(clientId);
        when(reviewService.getClientReviews(clientId)).thenReturn(List.of(review));
        when(reviewMapper.toDto(review)).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(get("/api/reviews/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idReview").value(1));
    }

    @Test
    void getClientReviews_ShouldReturnListOfReviews() throws Exception {
        // Arrange
        Long clientId = 1L;
        Review review = new Review();
        ReviewDto dto = new ReviewDto();
        dto.setIdReview(1L);

        when(reviewService.getClientReviews(clientId)).thenReturn(List.of(review));
        when(reviewMapper.toDto(review)).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(get("/api/reviews/client/{clientId}", clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idReview").value(1));
    }

    @Test
    void updateReview_ShouldReturnUpdatedReview() throws Exception {
        // Arrange
        Long id = 1L;
        ReviewDto dto = new ReviewDto();
        dto.setComment("Updated");
        Review review = new Review();
        Review updatedReview = new Review();
        ReviewDto outputDto = new ReviewDto();
        outputDto.setIdReview(id);
        outputDto.setComment("Updated");

        when(reviewMapper.toModel(any(ReviewDto.class))).thenReturn(review);
        when(reviewService.updateReview(eq(id), any(Review.class))).thenReturn(updatedReview);
        when(reviewMapper.toDto(updatedReview)).thenReturn(outputDto);

        // Act & Assert
        mockMvc.perform(put("/api/reviews/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment").value("Updated"));
    }

    @Test
    void deleteReview_ShouldReturnNoContent() throws Exception {
        // Arrange
        Long id = 1L;
        Long adminId = 1L;
        String reason = "Spam";

        when(currentUserProvider.getCurrentUserId()).thenReturn(adminId);
        doNothing().when(reviewService).deleteReview(adminId, id, reason);

        // Act & Assert
        mockMvc.perform(delete("/api/reviews/{id}", id)
                .param("reason", reason))
                .andExpect(status().isNoContent());
    }

    @Test
    void countReviews_ShouldReturnCount() throws Exception {
        // Arrange
        Long count = 50L;
        when(reviewService.countReviews()).thenReturn(count);

        // Act & Assert
        mockMvc.perform(get("/api/reviews/stats/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(count));
    }

    @Test
    void getReviewForReservation_ShouldReturnReview() throws Exception {
        // Arrange
        Long reservationId = 1L;
        Review review = new Review();
        ReviewDto dto = new ReviewDto();
        dto.setIdReview(1L);
        dto.setIdReservation(reservationId);

        when(reviewService.getReviewByReservationId(reservationId)).thenReturn(Optional.of(review));
        when(reviewMapper.toDto(review)).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(get("/api/reviews/reservation/{reservationId}", reservationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReview").value(1));
    }
}
