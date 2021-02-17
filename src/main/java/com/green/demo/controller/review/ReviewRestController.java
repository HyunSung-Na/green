package com.green.demo.controller.review;

import com.green.demo.security.JwtAuthentication;
import com.green.demo.service.review.ReviewService;
import com.green.demo.util.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewRestController {

    private final ReviewService reviewService;

    @PostMapping("")
    public ApiResult<ReviewDto> writeReview(@RequestBody ReviewCreateDto createDto, @AuthenticationPrincipal JwtAuthentication jwtAuthentication) {
        return ApiResult.OK(reviewService.writeReview(createDto, jwtAuthentication.email));
    }

    @GetMapping("info/{reviewId}")
    public ApiResult<ReviewDto> reviewDetail(@PathVariable Long reviewId) {
        return ApiResult.OK(reviewService.reviewDetail(reviewId));
    }

    @GetMapping("info/list")
    public ApiResult<List<ReviewDto>> reviews(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ApiResult.OK(reviewService.reviews(pageRequest));
    }

    @PutMapping("{reviewId}")
    public ApiResult<ReviewDto> updateReview(@PathVariable Long reviewId, @RequestBody ReviewUpdateDto updateDto, @AuthenticationPrincipal JwtAuthentication jwtAuthentication) {
        return ApiResult.OK(reviewService.updateReview(updateDto, jwtAuthentication.email, reviewId));
    }

    @DeleteMapping("{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId, @AuthenticationPrincipal JwtAuthentication jwtAuthentication) {
        reviewService.deleteReview(reviewId, jwtAuthentication.email);
        return ResponseEntity.ok().build();
    }
}
