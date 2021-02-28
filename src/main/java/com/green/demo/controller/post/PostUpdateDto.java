package com.green.demo.controller.post;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostUpdateDto {

    private String title;

    private String contents;

    public PostUpdateDto(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}
