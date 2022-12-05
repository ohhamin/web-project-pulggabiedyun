package com.ssafy.dokcho2.dto.minigame;

import com.ssafy.dokcho2.domain.minigame.CharacterQuiz;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ApiModel(value = "CharacterQuizDto", description = "인물 퀴즈 정보 응답 Dto")
public class CharacterQuizDto {
    private String question;
    private String right_answer;
    private String description;

    public static CharacterQuizDto from(CharacterQuiz entity){
        return CharacterQuizDto.builder()
                .question(entity.getQuestion())
                .right_answer(entity.getRight_answer())
                .description(entity.getDescription())
                .build();
    }
}
