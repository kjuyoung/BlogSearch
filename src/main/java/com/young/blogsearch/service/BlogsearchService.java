package com.young.blogsearch.service;

import com.young.blogsearch.domain.BlogDto;
import com.young.blogsearch.domain.KeywordDto;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.util.List;

public interface BlogsearchService {

    public BlogDto getBlogs(String query, String sort, int page, int size) throws MissingServletRequestParameterException;

    public List<KeywordDto> getPopularKeywords();
}
