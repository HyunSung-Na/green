package com.green.demo.controller.review;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewCreateDto {

    private String title;

    private String contents;

    private Long itemId;

    @Builder
    public ReviewCreateDto(String title, String contents, Long itemId) {
        this.title = title;
        this.contents = contents;
        this.itemId = itemId;
    }
}
