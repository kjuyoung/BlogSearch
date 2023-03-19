package com.young.blogsearch.service;

import com.young.blogsearch.repository.KeywordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KakaoBlogsearchServiceTest {

    @Autowired
    KeywordRepository keywordRepository;

    @Test
    void getBlogs() {
    }

    @Test
    void getPopularKeywords() {
    }
}