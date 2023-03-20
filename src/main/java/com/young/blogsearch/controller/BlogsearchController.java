package com.young.blogsearch.controller;

import com.young.blogsearch.domain.BlogDto;
import com.young.blogsearch.domain.KeywordDto;
import com.young.blogsearch.service.KakaoBlogsearchService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.local.LocalBucketBuilder;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
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

    @GetMapping("/blogs")
    public ResponseEntity<?> searchBlogsByKeyword(@RequestParam(value = "query", required = true) String query,
                                      @RequestParam(value = "sort", required = false, defaultValue = "accuracy") String sort,
                                      @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                      @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        BlogDto blogs = blogsearchService.getBlogs(query, sort, page, size);
        return ResponseEntity.ok(blogs);
    }

    @GetMapping("/popular-keywords")
    public ResponseEntity<?> getPopularKeywords() {
        List<KeywordDto> popularKeywords = blogsearchService.getPopularKeywords();
        return ResponseEntity.ok(popularKeywords);
    }
}
