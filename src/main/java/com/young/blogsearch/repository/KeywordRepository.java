package com.young.blogsearch.repository;

import com.young.blogsearch.domain.KeywordEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface KeywordRepository extends JpaRepository<KeywordEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<KeywordEntity> findByKeyword(String query);

    Optional<List<KeywordEntity>> findTop10ByOrderByCountDesc();
}
