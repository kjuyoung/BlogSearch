package com.young.blogsearch.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class KeywordDto {

    private String keyword;
    private AtomicInteger count;

    @Builder
    public KeywordDto(String keyword, AtomicInteger count) {
        this.keyword = keyword;
        this.count = count;
    }

    public static KeywordDto toDto(KeywordEntity entity) {
        return KeywordDto.builder()
                        .keyword(entity.getKeyword())
                        .count(entity.getCount())
                        .build();
    }
}
