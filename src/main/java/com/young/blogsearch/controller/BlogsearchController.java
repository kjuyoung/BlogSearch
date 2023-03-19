package com.young.blogsearch.controller;

import com.young.blogsearch.domain.BlogDto;
import com.young.blogsearch.domain.KeywordDto;
import com.young.blogsearch.repository.KeywordRepository;
import com.young.blogsearch.service.KakaoBlogsearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BlogsearchController {

    private final KakaoBlogsearchService blogsearchService;

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
