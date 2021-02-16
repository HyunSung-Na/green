package com.green.demo.service.review;

import com.green.demo.controller.review.ReviewCreateDto;
import com.green.demo.controller.review.ReviewDto;
import com.green.demo.controller.review.ReviewUpdateDto;
import com.green.demo.error.NotFoundException;
import com.green.demo.error.UnauthorizedException;
import com.green.demo.model.review.Review;
import com.green.demo.model.user.Email;
import com.green.demo.model.user.User;
import com.green.demo.repository.ReviewRepository;
import com.green.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final UserService userService;

    private final ReviewRepository reviewRepository;

    public ReviewDto writeReview(ReviewCreateDto createDto, Email email) {
        checkNotNull(email, "email must be provided.");

        User user = userService.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(Email.class, email));

        Review review = new Review(createDto.getTitle(), createDto.getContents(), user.getName());
        review.setUser(user);

        user.addReview(review);
        userService.insert(user);

        return ReviewDto.of(reviewRepository.save(review));
    }

    @Transactional(readOnly = true)
    public ReviewDto reviewDetail(Long reviewId) {
        checkNotNull(reviewId, "reviewId must be provided.");

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException(Review.class, reviewId));

        return ReviewDto.of(review);
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> reviews(PageRequest pageRequest) {
        return reviewRepository.findAll(pageRequest).stream()
                .map(ReviewDto::of)
                .collect(Collectors.toList());
    }

    public ReviewDto updateReview(ReviewUpdateDto updateDto, Email email, Long reviewId) {
        checkNotNull(email, "email must be provided.");
        checkNotNull(reviewId, "reviewId must be provided.");

        User user = userService.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(Email.class, email));
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException(Review.class, reviewId));

        if (!review.getUser().equals(user)) {
            throw new UnauthorizedException("AuthenticationFailed");
        }

        review.apply(updateDto.getTitle(), updateDto.getContents());
        return ReviewDto.of(reviewRepository.save(review));
    }

    public void deleteReview(Long reviewId, Email email) {
        checkNotNull(email, "email must be provided.");
        checkNotNull(reviewId, "reviewId must be provided.");

        User user = userService.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(Email.class, email));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException(Review.class, reviewId));

        if (!review.getUser().equals(user)) {
            throw new UnauthorizedException("AuthenticationFailed");
        }

        reviewRepository.deleteById(reviewId);
    }
}
