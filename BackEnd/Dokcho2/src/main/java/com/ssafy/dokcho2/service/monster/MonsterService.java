package com.ssafy.dokcho2.service.monster;

import com.ssafy.dokcho2.dto.monster.MonsterDto;

import java.util.List;

public interface MonsterService{
    List<MonsterDto> getMonsterList();

    List<MonsterDto> addMonster(Long monsterId);

    MonsterDto getMonster(Long monsterId);
}
