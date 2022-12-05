package com.ssafy.dokcho2.domain.mission;

import com.ssafy.dokcho2.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserMissionRepository extends JpaRepository<UserMission, Long> {
    Optional<UserMission> findUserMissionByUserAndMission(User user, Mission mission);

    void deleteAllByUser(User user);
}
