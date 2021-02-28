package com.green.demo.controller.post;

import com.green.demo.security.JwtAuthentication;
import com.green.demo.service.post.PostService;
import com.green.demo.util.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/post")
@RequiredArgsConstructor
public class PostRestController {

    private final PostService postService;

    @PostMapping("")
    public ApiResult<PostDto> write(@RequestBody PostCreateDto createDto, @AuthenticationPrincipal JwtAuthentication jwtAuthentication) {
        return ApiResult.OK(postService.write(createDto, jwtAuthentication.email));
    }

    @GetMapping("info/{postId}")
    public ApiResult<PostDto> postDetail(@PathVariable Long postId) {
        return ApiResult.OK(postService.postDetail(postId));
    }

    @GetMapping("info/list")
    public ApiResult<List<PostDto>> posts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ApiResult.OK(postService.posts(pageRequest));
    }

    @PutMapping("{postId}")
    public ApiResult<PostDto> updatePost(@PathVariable Long postId,
                                         @RequestBody PostUpdateDto updateDto, @AuthenticationPrincipal JwtAuthentication jwtAuthentication) {
        return ApiResult.OK(postService.updatePost(updateDto, jwtAuthentication.email, postId));
    }

    @DeleteMapping("{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId, @AuthenticationPrincipal JwtAuthentication jwtAuthentication) {
        postService.deletePost(postId, jwtAuthentication.email);
        return ResponseEntity.ok().build();
    }
}
