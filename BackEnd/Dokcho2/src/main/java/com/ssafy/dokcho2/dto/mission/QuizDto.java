package com.ssafy.dokcho2.dto.mission;

import com.ssafy.dokcho2.domain.mission.Quiz;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "QuizDto", description = "퀴즈 정보 응답 Dto")
public class QuizDto {
    private String question;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String img;
    private String right_answer;

    public static QuizDto from(Quiz entity){
        return QuizDto.builder()
                .question(entity.getQuestion())
                .answer1(entity.getAnswer1())
                .answer2(entity.getAnswer2())
                .answer3(entity.getAnswer3())
                .answer4(entity.getAnswer4())
                .img(entity.getImg())
                .right_answer(entity.getRightAnswer())
                .build();
    }
}
