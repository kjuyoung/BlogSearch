package com.young.blogsearch.controller;

import com.young.blogsearch.service.KakaoBlogsearchService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class BlogsearchControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private KakaoBlogsearchService blogsearchService;

    @Test
    void searchBlogsByKeyword() throws Exception {

        // given
        final String TEST_URI = "/api/v1/blogs";
        final String QUERY = "query";
        final String KEYWORD = "kakaobank";

        mockMvc.perform(get(TEST_URI)
                        .queryParam(QUERY, KEYWORD)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void getPopularKeywords() throws Exception {

        // given
        final String TEST_URI = "/api/v1/popular-keywords";

        mockMvc.perform(get(TEST_URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}