package com.ssafy.dokcho2.service.minigame;

import com.ssafy.dokcho2.dto.minigame.CharacterQuizDto;
import com.ssafy.dokcho2.dto.minigame.ConsonantDto;
import com.ssafy.dokcho2.dto.minigame.WordsDto;

import java.util.List;

public interface MiniGameService {

    List<WordsDto> getWordsProblem(Integer count);
    List<ConsonantDto> getConsonantProblem(Integer count);

    List<CharacterQuizDto> getCharacterQuizProblem(Integer count);
}
