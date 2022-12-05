package com.ssafy.dokcho2.dto.minigame;

import com.ssafy.dokcho2.domain.minigame.Words;
import com.ssafy.dokcho2.domain.mission.Quiz;
import com.ssafy.dokcho2.dto.mission.QuizDto;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "WordsDto", description = "사자성어 정보 응답 Dto")
public class WordsDto {
    private String question;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String right_answer;
    private String description;

    public static WordsDto from(Words entity){
        return WordsDto.builder()
                .question(entity.getQuestion())
                .answer1(entity.getAnswer1())
                .answer2(entity.getAnswer2())
                .answer3(entity.getAnswer3())
                .answer4(entity.getAnswer4())
                .right_answer(entity.getRightAnswer())
                .description(entity.getDescription())
                .build();
    }
}
