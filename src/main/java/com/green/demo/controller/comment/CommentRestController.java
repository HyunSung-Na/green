package com.green.demo.controller.comment;

import com.green.demo.security.JwtAuthentication;
import com.green.demo.service.comment.CommentService;
import com.green.demo.util.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/comment")
@RequiredArgsConstructor
public class CommentRestController {

    private final CommentService commentService;

    @PostMapping("")
    public ApiResult<CommentDto> write(@RequestBody CommentCreateDto createDto, @AuthenticationPrincipal JwtAuthentication jwtAuthentication) {
        return ApiResult.OK(commentService.write(createDto, jwtAuthentication.id));
    }

    @GetMapping("info/{reviewId}")
    public ApiResult<List<CommentDto>> reviewByIdWithComments(@PathVariable Long reviewId) {
        return ApiResult.OK(commentService.reviewDetail(reviewId));
    }

    @PutMapping("")
    public ApiResult<CommentDto> updateComment(@RequestBody CommentUpdateDto updateDto,
                                                @AuthenticationPrincipal JwtAuthentication jwtAuthentication) {
        return ApiResult.OK(commentService.update(updateDto, jwtAuthentication.id));
    }

    @DeleteMapping("{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal JwtAuthentication jwtAuthentication) {
        commentService.delete(commentId, jwtAuthentication.id);
        return ResponseEntity.ok().build();
    }
}
