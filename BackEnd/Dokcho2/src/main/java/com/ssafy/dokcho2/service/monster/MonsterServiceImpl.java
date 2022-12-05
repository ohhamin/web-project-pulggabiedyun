package com.ssafy.dokcho2.service.monster;

import com.ssafy.dokcho2.domain.monster.Monster;
import com.ssafy.dokcho2.domain.monster.MonsterRepository;
import com.ssafy.dokcho2.domain.user.User;
import com.ssafy.dokcho2.domain.user.UserRepository;
import com.ssafy.dokcho2.domain.userMonster.UserMonster;
import com.ssafy.dokcho2.domain.userMonster.UserMonsterRepository;
import com.ssafy.dokcho2.dto.exception.monster.MonsterAlreadyExist;
import com.ssafy.dokcho2.dto.exception.monster.MonsterNotFoundException;
import com.ssafy.dokcho2.dto.exception.user.UserNotFoundException;
import com.ssafy.dokcho2.dto.monster.MonsterDto;
import com.ssafy.dokcho2.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MonsterServiceImpl implements MonsterService {

    private final MonsterRepository monsterRepository;

    private final UserMonsterRepository userMonsterRepository;

    private final UserRepository userRepository;

    @Override
    public List<MonsterDto> getMonsterList() {
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findByUsername).orElseThrow(UserNotFoundException::new);
        List<MonsterDto> monsterDtoList = userMonsterRepository.findAllByUser(user)
                .stream().map(s -> MonsterDto.from(s)).collect(Collectors.toList());;

        return monsterDtoList;
    }

    @Override
    @Transactional
    public List<MonsterDto> addMonster(Long monsterId) {
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findByUsername).orElseThrow(UserNotFoundException::new);
        Monster monster = monsterRepository.findById(monsterId).orElseThrow(MonsterNotFoundException::new);

        Optional<UserMonster> o = userMonsterRepository.findByUserAndMonster(user, monster);

        if (o.isPresent()) {
            throw new MonsterAlreadyExist();
        } else {
            UserMonster userMonster = UserMonster.builder()
                    .user(user)
                    .monster(monster)
                    .level(1)
                    .exp(0)
                    .build();
            userMonsterRepository.save(userMonster);
        }

        List<MonsterDto> monsterDtoList = userMonsterRepository.findAllByUser(user)
                .stream().map(s -> MonsterDto.from(s)).collect(Collectors.toList());

        return monsterDtoList;
    }

    @Override
    public MonsterDto getMonster(Long monsterId) {
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findByUsername).orElseThrow(UserNotFoundException::new);

        Monster monster = monsterRepository.findById(monsterId).orElseThrow(MonsterNotFoundException::new);

        UserMonster userMonster = userMonsterRepository.findByUserAndMonster(user, monster).orElseThrow(MonsterNotFoundException::new);

        MonsterDto monsterDto = MonsterDto.from(userMonster);

        return monsterDto;
    }
}
