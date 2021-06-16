package com.green.demo.service.post;

import com.green.demo.controller.post.PostCreateDto;
import com.green.demo.controller.post.PostDto;
import com.green.demo.controller.post.PostUpdateDto;
import com.green.demo.error.NotFoundException;
import com.green.demo.error.UnauthorizedException;
import com.green.demo.model.post.Post;
import com.green.demo.model.user.Email;
import com.green.demo.model.user.User;
import com.green.demo.repository.post.PostRepository;
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
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    public PostDto write(PostCreateDto createDto, Email email) {
        checkNotNull(email, "email must be provided.");

        User user = userService.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(Email.class, email));

        Post post = new Post(createDto.getTitle(), createDto.getContents(), user.getName(), user);
        return PostDto.of(postRepository.save(post));
    }

    @Transactional(readOnly = true)
    public PostDto postDetail(Long postId) {
        checkNotNull(postId, "postId must be provided.");

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(Post.class, postId));

        return PostDto.of(post);
    }

    @Transactional(readOnly = true)
    public List<PostDto> posts(PageRequest pageRequest) {
        return postRepository.findAll(pageRequest).stream()
                .map(PostDto::of)
                .collect(Collectors.toList());
    }

    public PostDto updatePost(PostUpdateDto updateDto, Email email, Long postId) {
        checkNotNull(email, "email must be provided.");
        checkNotNull(postId, "postId must be provided.");

        User user = userService.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(Email.class, email));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(Post.class, postId));

        if (!post.getUser().equals(user)) {
            throw new UnauthorizedException("AuthenticationFailed");
        }

        post.apply(updateDto.getTitle(), updateDto.getContents());
        return PostDto.of(postRepository.save(post));
    }

    public void deletePost(Long postId, Email email) {
        checkNotNull(email, "email must be provided.");
        checkNotNull(postId, "postId must be provided.");

        User user = userService.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(Email.class, email));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(Post.class, postId));

        if (!post.getUser().equals(user)) {
            throw new UnauthorizedException("AuthenticationFailed");
        }

        postRepository.delete(post);
        postRepository.flush();
    }

    @Transactional(readOnly = true)
    public Post findByPostTitle(String title) {
        return postRepository.findByTitle(title);
    }
}
