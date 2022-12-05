package com.ssafy.dokcho2.controller;

import com.ssafy.dokcho2.dto.minigame.CharacterQuizDto;
import com.ssafy.dokcho2.dto.minigame.ConsonantDto;
import com.ssafy.dokcho2.dto.minigame.WordsDto;
import com.ssafy.dokcho2.service.minigame.MiniGameService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/game")
public class MiniGameController {

    private final MiniGameService miniGameService;

    @GetMapping("/words/auth/{count}")
    @ApiOperation(value = "사자성어 문제 리스트 받기")
    public ResponseEntity<List<WordsDto>> getWordsProblem(@PathVariable Integer count){
        return new ResponseEntity<>(miniGameService.getWordsProblem(count), HttpStatus.OK);
    }

    @GetMapping("/consonant/auth/{count}")
    @ApiOperation(value = "초성 퀴즈 문제 리스트 받기")
    public ResponseEntity<List<ConsonantDto>> getConsonantProblem(@PathVariable Integer count){
        return new ResponseEntity<>(miniGameService.getConsonantProblem(count), HttpStatus.OK);
    }

    @GetMapping("/characterquiz/auth/{count}")
    @ApiOperation(value = "인물 퀴즈 문제 리스트 받기")
    public ResponseEntity<List<CharacterQuizDto>> getCharacterQuizProblem(@PathVariable Integer count){
        return new ResponseEntity<>(miniGameService.getCharacterQuizProblem(count), HttpStatus.OK);
    }
}
