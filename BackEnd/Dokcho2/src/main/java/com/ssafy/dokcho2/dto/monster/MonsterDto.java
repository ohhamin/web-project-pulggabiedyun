package com.ssafy.dokcho2.dto.monster;

import com.ssafy.dokcho2.domain.monster.Monster;
import com.ssafy.dokcho2.domain.userMonster.UserMonster;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "풀깨비 정보")
public class MonsterDto {
    private Long userId;
    private Long monsterId;
    private String name;
    private Integer hp;
    private Integer attack;
    private Integer defence;
    private Integer level;
    private Integer exp;

    public static MonsterDto from(UserMonster userMonster){
        Monster monster = userMonster.getMonster();

        return MonsterDto.builder()
                .userId(userMonster.getUser().getUserId())
                .monsterId(monster.getMonsterId())
                .name(monster.getName())
                .hp(monster.getHp() + userMonster.getLevel()* monster.getGrowthHp())
                .attack(monster.getAttack() + userMonster.getLevel()* monster.getGrowthAttack())
                .defence(monster.getDefence() + userMonster.getLevel()* monster.getGrowthDefence())
                .level(userMonster.getLevel())
                .exp(userMonster.getExp())
                .build();
    }

}
