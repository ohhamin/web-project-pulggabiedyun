package com.ssafy.dokcho2.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND(404, "U001", "회원 정보를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(400, "U002", "이미 존재하는 이메일입니다."),
    DUPLICATE_NICKNAME(400, "U003", "이미 존재하는 닉네임입니다."),
    DUPLICATE_USERNAME(400, "U004", "이미 존재하는 계정입니다."),
    INVALID_PARAMETER(400, "X001", "잘못된 요청입니다."),

    MISSION_NOT_FOUND(404, "M001", "미션 정보를 찾을 수 없습니다."),

    ITEM_NOT_EXIST(400, "I001", "존재하지 않는 아이템입니다."),

    ITEM_NOT_USABLE(400, "I003", "사용할 수 없는 아이템입니다."),

    ITEM_NOT_IN_INVENTORY(400, "I002", "사용하는 아이템이 인벤토리에 없습니다."),

    MONSTER_NOT_FOUND(400, "M001", "데이터베이스 또는 인벤토리에 존재하지 않는 풀깨비입니다."),
    
    MONSTER_ALREADY_EXIST(400, "M002", "이미 보유한 풀깨비입니다.")
    ;

    private final int status;
    private final String code;
    private final String message;
}
