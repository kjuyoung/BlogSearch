package com.young.blogsearch.repository;

import com.young.blogsearch.domain.KeywordEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class KeywordRepositoryTest {

    public static final String QUERY1 = "bank";
    public static final String QUERY2 = "blog";
    @Autowired
    private KeywordRepository keywordRepository;
    private KeywordEntity keywordEntity1;
    private KeywordEntity keywordEntity2;

    @BeforeEach
    void setUp() {
        keywordEntity1 = KeywordEntity.builder()
                .keyword(QUERY1)
                .count(new AtomicInteger(1))
                .build();
        keywordEntity2 = KeywordEntity.builder()
                .keyword(QUERY2)
                .count(new AtomicInteger(1))
                .build();
    }

    @Test
    void 검색키워드를_잘저장하는지_확인() {

        KeywordEntity savedKeyword = keywordRepository.save(keywordEntity1);
        assertThat(keywordEntity1).isSameAs(savedKeyword);
        assertThat(keywordEntity1.getKeyword()).isEqualTo(savedKeyword.getKeyword());
        assertThat(keywordEntity1.getCount()).isEqualTo(savedKeyword.getCount());
    }

    @Test
    void 저장된_검색키워드가_제대로_조회되는지_확인() {

        // given
        KeywordEntity savedKeyword1 = keywordRepository.save(keywordEntity1);
        KeywordEntity savedKeyword2 = keywordRepository.save(keywordEntity2);

        // when
        KeywordEntity findKeyword1 = keywordRepository.findByKeyword(savedKeyword1.getKeyword())
                .orElseThrow(() -> new IllegalArgumentException("Wrong Keyword:<" + savedKeyword1.getKeyword() + ">"));
        KeywordEntity findKeyword2 = keywordRepository.findByKeyword(savedKeyword2.getKeyword())
                .orElseThrow(() -> new IllegalArgumentException("Wrong Keyword:<" + savedKeyword2.getKeyword() + ">"));

        // then
        assertThat(keywordRepository.count()).isEqualTo(2);
        assertThat(findKeyword1.getKeyword()).isEqualTo(savedKeyword1.getKeyword());
        assertThat(findKeyword1.getCount().intValue()).isEqualTo(1);
        assertThat(findKeyword2.getKeyword()).isEqualTo(savedKeyword2.getKeyword());
        assertThat(findKeyword2.getCount().intValue()).isEqualTo(1);
    }
}