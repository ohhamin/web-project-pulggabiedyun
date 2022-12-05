package com.ssafy.dokcho2.domain.minigame;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WordsRepository extends JpaRepository<Words, Long> {

    @Query(nativeQuery = true, value = "select * from words order by RAND() limit :count")
    List<Words> findWordsRandom(@Param("count") Integer count);
}
