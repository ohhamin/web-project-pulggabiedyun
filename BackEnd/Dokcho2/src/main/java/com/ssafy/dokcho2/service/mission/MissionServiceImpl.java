package com.ssafy.dokcho2.service.mission;

import com.ssafy.dokcho2.domain.enums.MissionStatus;
import com.ssafy.dokcho2.domain.item.Item;
import com.ssafy.dokcho2.domain.item.ItemRepository;
import com.ssafy.dokcho2.domain.mission.*;
import com.ssafy.dokcho2.domain.monster.Monster;
import com.ssafy.dokcho2.domain.user.User;
import com.ssafy.dokcho2.domain.user.UserRepository;
import com.ssafy.dokcho2.domain.userItem.UserItem;
import com.ssafy.dokcho2.domain.userItem.UserItemRepository;
import com.ssafy.dokcho2.domain.userMonster.UserMonster;
import com.ssafy.dokcho2.domain.userMonster.UserMonsterRepository;
import com.ssafy.dokcho2.dto.exception.item.ItemNotFoundException;
import com.ssafy.dokcho2.dto.exception.mission.MissionNotFoundException;
import com.ssafy.dokcho2.dto.exception.monster.MonsterNotFoundException;
import com.ssafy.dokcho2.dto.exception.user.UserNotFoundException;
import com.ssafy.dokcho2.dto.mission.BossDto;
import com.ssafy.dokcho2.dto.mission.MissionDto;
import com.ssafy.dokcho2.dto.mission.QuizDto;
import com.ssafy.dokcho2.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MissionServiceImpl implements MissionService{

    private final UserRepository userRepository;
    private final MissionRepository missionRepository;
    private final UserMissionRepository userMissionRepository;
    private final ItemRepository itemRepository;
    private final UserItemRepository userItemRepository;
    private final UserMonsterRepository userMonsterRepository;
    private final BossRepository bossRepository;

    @Override
    public MissionDto getMissionInfo(Long missionId) {
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findByUsername).orElseThrow(UserNotFoundException::new);
        Mission mission = missionRepository.findById(missionId).orElseThrow(MissionNotFoundException::new);
        UserMission userMission = userMissionRepository.findUserMissionByUserAndMission(user, mission).orElseThrow(MissionNotFoundException::new);
        Item relic = itemRepository.findById(mission.getRelic()).orElseThrow(ItemNotFoundException::new);
        Item item = itemRepository.findById(mission.getItem()).orElseThrow(ItemNotFoundException::new);
        return MissionDto.from(mission, userMission.getStatus(), item, relic);
    }

    @Override
    public List<QuizDto> getQuizList(Long missionId) {
        Mission mission = missionRepository.findById(missionId).orElseThrow(MissionNotFoundException::new);
        return mission.getQuizList().stream().map(quiz -> QuizDto.from(quiz)).collect(Collectors.toList());
    }

    @Override
    public List<MissionDto> getMissionList() {
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findByUsername).orElseThrow(UserNotFoundException::new);
        List<MissionDto> list = new ArrayList<>();

        for(UserMission um : user.getUserMissionList()){
            Mission mission = um.getMission();
            Item item = itemRepository.findById(mission.getItem()).orElseThrow(ItemNotFoundException::new);
            Item relic = itemRepository.findById(mission.getRelic()).orElseThrow(ItemNotFoundException::new);
            list.add(MissionDto.from(mission, um.getStatus(), item, relic));
        }

        return list;
    }

    @Override
    public boolean changeMissionStatus() {
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findByUsername).orElseThrow(UserNotFoundException::new);
        Long missionId = user.getNowMissionId();
        Mission mission = missionRepository.findById(missionId).orElseThrow(MissionNotFoundException::new);
        UserMission userMission = userMissionRepository.findUserMissionByUserAndMission(user, mission).orElseThrow(MissionNotFoundException::new);
        MissionStatus nowStatus = userMission.getStatus();
        MissionStatus newStatus;
        boolean flag = false;

        if(nowStatus == MissionStatus.NOT_YET){
            newStatus = MissionStatus.READY;
        }else if(nowStatus == MissionStatus.READY) {
            newStatus = MissionStatus.STARTED;
        }else if(nowStatus == MissionStatus.STARTED){
            newStatus = MissionStatus.QUIZ_PASSED;
        }else if(nowStatus == MissionStatus.QUIZ_PASSED){
            newStatus = MissionStatus.BATTLE_WIN;
        }else if(nowStatus == MissionStatus.BATTLE_WIN){
            // 미션 종료, 경험치 & 아이템 지급, 다음 미션 시작
            Monster monster = user.getRepresentMonster();
            if(updateExp(user, monster, mission.getExp())) flag = true;

            Item relic = itemRepository.findById(mission.getRelic()).orElseThrow(ItemNotFoundException::new);
            Item item = itemRepository.findById(mission.getItem()).orElseThrow(ItemNotFoundException::new);
            userItemRepository.save(UserItem.builder().item(relic).user(user).count(1).build());
            userItemRepository.save(UserItem.builder().item(item).user(user).count(1).build());

            newStatus = MissionStatus.FINISHED;

            if(missionId < 8) {
                user.changeNowMissionId(missionId + 1);
                userRepository.save(user);

                Mission newMission = missionRepository.findById(missionId+1).orElseThrow(MissionNotFoundException::new);
                UserMission newUserMission = userMissionRepository.findUserMissionByUserAndMission(user, newMission).orElseThrow(MissionNotFoundException::new);

                newUserMission.changeStatus(MissionStatus.READY);
                userMissionRepository.save(newUserMission);
            }
        }else{
            newStatus = nowStatus;
        }

        userMission.changeStatus(newStatus);
        userMissionRepository.save(userMission);
        return flag;
    }

    @Override
    public boolean updateExp(Integer rewardExp){
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findByUsername).orElseThrow(UserNotFoundException::new);
        Monster monster = user.getRepresentMonster();

        return updateExp(user, monster, rewardExp);
    }

    public boolean updateExp(User user, Monster monster, Integer rewardExp){
        boolean flag = false;
        UserMonster um = userMonsterRepository.findByUserAndMonster(user, monster).orElseThrow(MonsterNotFoundException::new);
        Integer newLevel = um.getLevel();
        Integer newExp = um.getExp()+rewardExp;
        while (newLevel*100 <= newExp) {
            newExp -= newLevel*100;
            newLevel += 1;
            flag = true;
        }

        um.setExp(newExp);
        um.setLevel(newLevel);

        userMonsterRepository.save(um);
        return flag;
    }

    @Override
    public BossDto getBossInfo(Long missionId){
        return BossDto.from(bossRepository.findById(missionId).orElseThrow(MonsterNotFoundException::new));
    }
}
