package com.green.demo.controller.post;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostCreateDto {

    private String title;

    private String contents;

    @Builder
    public PostCreateDto(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}
