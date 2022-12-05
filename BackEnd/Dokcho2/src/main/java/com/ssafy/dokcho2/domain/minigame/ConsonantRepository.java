package com.ssafy.dokcho2.domain.minigame;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConsonantRepository extends JpaRepository<Consonant, Long> {
    @Query(nativeQuery = true, value = "select * from consonant order by RAND() limit :count")
    List<Consonant> findConsonantRandom(@Param("count") Integer count);
}
