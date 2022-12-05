package com.ssafy.dokcho2.service.minigame;

import com.ssafy.dokcho2.domain.minigame.CharacterQuizRepository;
import com.ssafy.dokcho2.domain.minigame.ConsonantRepository;
import com.ssafy.dokcho2.domain.minigame.WordsRepository;
import com.ssafy.dokcho2.dto.minigame.CharacterQuizDto;
import com.ssafy.dokcho2.dto.minigame.ConsonantDto;
import com.ssafy.dokcho2.dto.minigame.WordsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MiniGameServiceImpl implements MiniGameService {

    private final WordsRepository wordsRepository;
    private final ConsonantRepository consonantRepository;
    private final CharacterQuizRepository characterQuizRepository;


    @Override
    public List<WordsDto> getWordsProblem(Integer count) {
        return wordsRepository.findWordsRandom(count).stream().map(w -> WordsDto.from(w)).collect(Collectors.toList());
    }

    @Override
    public List<ConsonantDto> getConsonantProblem(Integer count) {
        return consonantRepository.findConsonantRandom(count).stream().map(c -> ConsonantDto.from(c)).collect(Collectors.toList());
    }

    @Override
    public List<CharacterQuizDto> getCharacterQuizProblem(Integer count) {
        return characterQuizRepository.findCharacterQuizRandom(count).stream().map(c -> CharacterQuizDto.from(c)).collect(Collectors.toList());
    }
}
