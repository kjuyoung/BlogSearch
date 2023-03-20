package com.young.blogsearch.controller;

import com.young.blogsearch.service.KakaoBlogsearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class BlogsearchControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private KakaoBlogsearchService blogsearchService;

    @Test
    void 키워드를통해_블로그검색이_잘되는지_확인() throws Exception {

        // given
        final String TEST_URI = "http://localhost:8080/api/v1/blogs";
        final String QUERY = "query";
        final String KEYWORD = "kakaobank";

        // when & then
        mockMvc.perform(get(TEST_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam(QUERY, KEYWORD))
                .andExpect(status().isOk());
    }

    @Test
    void 사용자들이_많이_검색한_순서대로_최대_10개의_검색키워드를_제공하는지_확인() throws Exception {

        // given
        final String TEST_URI = "/api/v1/popular-keywords";

        mockMvc.perform(get(TEST_URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}