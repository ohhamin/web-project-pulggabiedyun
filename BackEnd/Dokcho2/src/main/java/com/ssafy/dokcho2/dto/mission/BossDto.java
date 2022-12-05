package com.ssafy.dokcho2.dto.mission;

import com.ssafy.dokcho2.domain.mission.Boss;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "보스 정보")
public class BossDto {
    private String name;
    private Integer hp;
    private Integer attack;
    private Integer defence;

    public static BossDto from(Boss entity){
        return BossDto.builder()
                .name(entity.getName())
                .hp(entity.getHp())
                .attack(entity.getAttack())
                .defence(entity.getDefence())
                .build();
    }
}
