package com.ssafy.dokcho2.domain.minigame;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CharacterQuizRepository extends JpaRepository<CharacterQuiz, Long> {
    @Query(nativeQuery = true, value = "select * from characterquiz order by RAND() limit :count")
    List<CharacterQuiz> findCharacterQuizRandom(@Param("count") Integer count);
}
