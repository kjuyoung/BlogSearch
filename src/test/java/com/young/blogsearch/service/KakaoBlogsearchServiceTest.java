package com.young.blogsearch.service;

import com.young.blogsearch.domain.KeywordDto;
import com.young.blogsearch.domain.KeywordEntity;
import com.young.blogsearch.repository.KeywordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class KakaoBlogsearchServiceTest {

    private static final String QUERY = "bank";
    private static final String SORT = "accuracy";
    private static final int page = 1;
    private static final int size = 10;

    @InjectMocks
    private KakaoBlogsearchService kakaoBlogsearchService;
    @Mock
    private KeywordRepository keywordRepository;
    private KeywordDto keywordDto;
    private KeywordEntity keywordEntity;

    @BeforeEach
    void setUp() {
        keywordDto = KeywordDto.builder()
                .keyword(QUERY)
                .count(new AtomicInteger(1))
                .build();
        keywordEntity = KeywordEntity.builder()
                .keyword(keywordDto.getKeyword())
                .count(new AtomicInteger(1))
                .build();
    }

    @Test
    void 블로그_검색_단위테스트() {
        // given
        given(keywordRepository.save(any())).willReturn(keywordEntity);
        given(keywordRepository.findByKeyword(QUERY)).willReturn(Optional.ofNullable(keywordEntity));

        // when
        kakaoBlogsearchService.getBlogs(QUERY, SORT, page, size);

        // then
        KeywordEntity findKeyword = keywordRepository.findByKeyword(QUERY).get();
        assertEquals(keywordEntity.getKeyword(), findKeyword.getKeyword());
        assertEquals(keywordEntity.getCount(), findKeyword.getCount());
        then(keywordRepository).should(atLeast(1)).findByKeyword(QUERY);
    }

    @Test
    void 검색키워드_제공_단위테스트() {
        // given
        List<KeywordEntity> keywordEntities = new ArrayList<>();
        keywordEntities.add(keywordEntity);
        given(keywordRepository.save(any())).willReturn(keywordEntity);
        given(keywordRepository.findTop10ByOrderByCountDesc()).willReturn(Optional.of(keywordEntities));

        // when
        kakaoBlogsearchService.getBlogs(QUERY, SORT, page, size);

        // then
        Optional<List<KeywordEntity>> top10Keywords = keywordRepository.findTop10ByOrderByCountDesc();
        assertEquals(top10Keywords.get().size(), 1);
        then(keywordRepository).should(atLeast(1)).findTop10ByOrderByCountDesc();
    }
}