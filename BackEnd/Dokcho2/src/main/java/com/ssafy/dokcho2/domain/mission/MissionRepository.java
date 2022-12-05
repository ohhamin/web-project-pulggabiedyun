package com.ssafy.dokcho2.domain.mission;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MissionRepository extends JpaRepository<Mission, Long> {
    Optional<Mission> findMissionByCharacters(String characters);
}
