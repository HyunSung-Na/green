package com.green.demo.controller.review;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewUpdateDto {

    private String title;

    private String contents;

    @Builder
    public ReviewUpdateDto(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}
