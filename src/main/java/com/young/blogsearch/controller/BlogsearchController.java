package com.young.blogsearch.controller;

import com.young.blogsearch.domain.BlogDto;
import com.young.blogsearch.domain.KeywordDto;
import com.young.blogsearch.service.KakaoBlogsearchService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.local.LocalBucketBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.bucket4j.Bucket;
import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BlogsearchController {

    private final KakaoBlogsearchService blogsearchService;
    private final Bucket bucket;

    public BlogsearchController(KakaoBlogsearchService blogsearchService) {
        // 1초에 5개 요청 허용, 1분에 50개 요청 허용
        Bandwidth minuteBandwidth = Bandwidth.simple(50, Duration.ofMinutes(1));
        Bandwidth secondBandwidth = Bandwidth.simple(5, Duration.ofSeconds(1));

        LocalBucketBuilder bucketBuilder = Bucket.builder();
        bucketBuilder.addLimit(minuteBandwidth);
        bucketBuilder.addLimit(secondBandwidth);

        this.bucket = bucketBuilder.build();

        this.blogsearchService = blogsearchService;
    }

    @Operation(summary = "오픈 API를 이용한 블로그 검색", description = "오픈 API를 이용하여 블로그 검색을 합니다.", tags = { "Blog search" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = BlogDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/blogs")
    public ResponseEntity<?> searchBlogsByKeyword(@RequestParam(value = "query", required = true) String query,
                                      @RequestParam(value = "sort", required = false, defaultValue = "accuracy") String sort,
                                      @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                      @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        BlogDto blogs = blogsearchService.getBlogs(query, sort, page, size);
        return ResponseEntity.ok(blogs);
    }

    @Operation(summary = "검색 키워드 제공", description = "사람들이 많이 검색한 순서대로, 최대 10개의 검색 키워드를 제공합니다.", tags = { "Top 10 popular keyword" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = KeywordDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/popular-keywords")
    public ResponseEntity<?> getPopularKeywords() {
        List<KeywordDto> popularKeywords = blogsearchService.getPopularKeywords();
        return ResponseEntity.ok(popularKeywords);
    }
}
