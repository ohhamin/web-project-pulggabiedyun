package com.ssafy.dokcho2.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum MissionStatus {
    NOT_YET, READY, STARTED, QUIZ_PASSED, BATTLE_WIN, FINISHED;
    // 아직 시작하지 않음, 시작할 수 있음, 시작함, 퀴즈 통과함, 배틀 승리함, 끝남

    @JsonCreator
    public static MissionStatus from(String value) {
        return MissionStatus.valueOf(value.toUpperCase());
    }
}
