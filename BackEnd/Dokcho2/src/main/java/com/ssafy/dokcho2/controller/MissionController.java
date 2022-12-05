package com.ssafy.dokcho2.controller;

import com.ssafy.dokcho2.domain.enums.MissionStatus;
import com.ssafy.dokcho2.dto.item.ItemDto;
import com.ssafy.dokcho2.dto.mission.BossDto;
import com.ssafy.dokcho2.dto.mission.MissionDto;
import com.ssafy.dokcho2.dto.mission.QuizDto;
import com.ssafy.dokcho2.service.item.ItemService;
import com.ssafy.dokcho2.service.mission.MissionService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mission")
public class MissionController {
    private final MissionService missionService;
    private final ItemService itemService;

    @GetMapping("/{missionId}")
    @ApiOperation(value = "캐릭터와 상호작용 시 미션 정보 주기")
    public ResponseEntity<MissionDto> getMissionInfo(@PathVariable Long missionId){
        return new ResponseEntity<>(missionService.getMissionInfo(missionId), HttpStatus.OK);
    }

    @GetMapping("/quiz/{missionId}")
    @ApiOperation(value = "미션의 퀴즈 리스트 주기")
    public ResponseEntity<List<QuizDto>> getQuizList(@PathVariable Long missionId){
        return new ResponseEntity<>(missionService.getQuizList(missionId), HttpStatus.OK);
    }

    @GetMapping("/")
    @ApiOperation(value = "전체 미션 리스트")
    public ResponseEntity<List<MissionDto>> getMissionList(){
        return new ResponseEntity<>(missionService.getMissionList(), HttpStatus.OK);
    }

    @PutMapping("/")
    @ApiOperation(value = "미션 상태 바꾸기")
    public ResponseEntity<Boolean> changeMissionStatus(){

        return new ResponseEntity<>(missionService.changeMissionStatus(), HttpStatus.OK);
    }

    @GetMapping("/boss/{missionId}")
    @ApiOperation(value = "보스 정보")
    public ResponseEntity<BossDto> getBossInfo(@PathVariable Long missionId){
        return new ResponseEntity<>(missionService.getBossInfo(missionId), HttpStatus.OK);
    }

    @PutMapping("/mini")
    @ApiOperation(value = "미니게임 경험치 저장")
    public ResponseEntity<Map<String, Object>> completeMiniGame(@RequestParam Integer rewardExp){
        Map<String, Object> map = new HashMap<>();
        map.put("levelup", missionService.updateExp(rewardExp));
        ItemDto itemDto = itemService.addUseItem();
        map.put("itemDto", itemDto);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
