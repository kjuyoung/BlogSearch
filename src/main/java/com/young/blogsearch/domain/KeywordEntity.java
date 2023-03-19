package com.young.blogsearch.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Entity
@Table(name = "popular_keywords")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KeywordEntity extends BasetimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String keyword;
    private AtomicInteger count;

    @Builder
    public KeywordEntity(String keyword, AtomicInteger count) {
        this.keyword = keyword;
        this.count = count;
    }
}
