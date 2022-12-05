package com.ssafy.dokcho2.controller;

import com.ssafy.dokcho2.domain.monster.Monster;
import com.ssafy.dokcho2.dto.monster.MonsterDto;
import com.ssafy.dokcho2.service.monster.MonsterService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1/monster")
public class MonsterController {
    private final MonsterService monsterService;

    @GetMapping()
    @ApiOperation(value = "보유한 풀깨비 리스트 불러오기")
    public ResponseEntity<List<MonsterDto>> getMonsters(){
        List<MonsterDto> monsterDtoList = monsterService.getMonsterList();
        return new ResponseEntity<>(monsterDtoList, HttpStatus.OK);
    }

    @PostMapping("/{monsterId}")
    @ApiOperation(value = "풀깨비 습득 후 갱신된 풀깨비 리스트 반환")
    public ResponseEntity<List<MonsterDto>> addMonster(@PathVariable Long monsterId){
        List<MonsterDto> monsterDtoList = monsterService.addMonster(monsterId);
        return new ResponseEntity<>(monsterDtoList, HttpStatus.OK);
    }

    @GetMapping("/{monsterId}")
    @ApiOperation(value = "보유한 풀깨비 중 특정 풀깨비의 정보 불러오기")
    public ResponseEntity<MonsterDto> getMonster(@PathVariable Long monsterId){
        MonsterDto monsterDto = monsterService.getMonster(monsterId);

        return new ResponseEntity<>(monsterDto, HttpStatus.OK);
    }
}
