package com.ssafy.dokcho2.dto.user;

import com.ssafy.dokcho2.domain.enums.Role;
import com.ssafy.dokcho2.domain.user.User;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "UserResponseDto", description = "유저 정보 응답 Dto")
public class UserResponseDto {
    private Long user_id;
    private String username;
    private String nickname;
    private String email;
    private Long representMonster;
    private Role role;
    private String createDate;
    //현재 진행중인 미션 번호를 알기 위해 미션 번호 담아주기
    private Long nowMissionId;


    public static UserResponseDto from(User entity){
        String createDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(entity.getCreatedDate()).toString();

        return UserResponseDto.builder()
                .user_id(entity.getUserId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .nickname(entity.getNickname())
                .representMonster(entity.getRepresentMonster().getMonsterId())
                .role(entity.getRole())
                .createDate(createDate)
                .nowMissionId(entity.getNowMissionId())
                .build();
    }
}
