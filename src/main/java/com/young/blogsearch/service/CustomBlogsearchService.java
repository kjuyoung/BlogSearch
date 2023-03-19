package com.young.blogsearch.service;

import com.young.blogsearch.domain.BlogDto;
import com.young.blogsearch.domain.KeywordDto;
import com.young.blogsearch.domain.KeywordEntity;
import com.young.blogsearch.domain.Response;
import com.young.blogsearch.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomBlogsearchService implements BlogsearchService{

    public static final String NAVER_HOST = "https://openapi.naver.com";
    public static final String URI_PATH = "/v1/search/blog.json";
    public static final String QUERY = "query";
    public static final String SORT = "sort";
    public static final String START = "start";
    public static final String DISPLAY = "display";
    public static final String CLIENT_ID = "X_NAVER_CLIENT_ID ";
    public static final String CLIENT_SECRET = "X_NAVER_CLIENT_SECRET";
    public static final String X_NAVER_CLIENT_ID = "YvYYiC6RVNwo53vLvoBY ";
    public static final String X_NAVER_CLIENT_SECRET = "ASLrpUAntF";

    private final Logger logger = LoggerFactory.getLogger(KakaoBlogsearchService.class);
    private final KeywordRepository keywordRepository;

    @Transactional
    public BlogDto getBlogs(String query, String sort, int page, int size) {
        Response response = getBlogsearchResult(query, sort, page, size);

        updateKeywordHistory(query);

        return BlogDto.from(response);
    }

    private static Response getBlogsearchResult(String query, String sort, int page, int size) {
        WebClient webClient = WebClient.builder()
                .baseUrl(NAVER_HOST)
                .build();

        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(URI_PATH)
                        .queryParam(QUERY, query)
                        .queryParam(SORT, sort)
                        .queryParam(START, page)
                        .queryParam(DISPLAY, size)
                        .build()
                )
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .header(CLIENT_ID, X_NAVER_CLIENT_ID)
                .header(CLIENT_SECRET, X_NAVER_CLIENT_SECRET)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .map(RuntimeException::new))
                .bodyToMono(new ParameterizedTypeReference<Response>() {})
                .block();
    }

    private void updateKeywordHistory(String query) {
        KeywordEntity keywordEntity = null;
        Optional<KeywordEntity> keyword = keywordRepository.findByKeyword(query);
        if(keyword.isPresent()) {
            keyword.get().getCount().getAndIncrement();
        } else {
            keywordEntity = KeywordEntity.builder()
                    .keyword(query)
                    .count(new AtomicInteger(1))
                    .build();
            keywordRepository.save(keywordEntity);
        }
    }

    public List<KeywordDto> getPopularKeywords() {
        List<KeywordEntity> keywordEntities = keywordRepository.findTop10ByOrderByCountDesc().orElseThrow(() -> new RuntimeException("검색된 블로그가 없습니다."));
        return keywordEntities.stream()
                .map(KeywordDto::toDto)
                .collect(Collectors.toList());
    }
}
