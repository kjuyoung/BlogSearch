package com.young.blogsearch.repository;

import com.young.blogsearch.domain.KeywordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KeywordRepository extends JpaRepository<KeywordEntity, Long> {
    Optional<KeywordEntity> findByKeyword(String query);

    Optional<List<KeywordEntity>> findTop10ByOrderByCountDesc();
}
