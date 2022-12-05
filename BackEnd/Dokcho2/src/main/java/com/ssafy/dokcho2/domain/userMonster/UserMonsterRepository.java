package com.ssafy.dokcho2.domain.userMonster;

import com.ssafy.dokcho2.domain.monster.Monster;
import com.ssafy.dokcho2.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserMonsterRepository extends JpaRepository<UserMonster, Long> {
    List<UserMonster> findAllByUser(User user);
    Optional<UserMonster> findByUserAndMonster(User user, Monster monster);

    void deleteAllByUser(User user);
}
