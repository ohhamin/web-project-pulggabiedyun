package com.ssafy.dokcho2.domain.minigame;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
@Table(name = "words")
public class Words {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "words_id")
    private Long wordsId;
    @Column(name = "question")
    private String question;
    @Column(name = "answer1")
    private String answer1;
    @Column(name = "answer2")
    private String answer2;
    @Column(name = "answer3")
    private String answer3;
    @Column(name = "answer4")
    private String answer4;
    @Column(name = "right_answer")
    private String rightAnswer;
    @Column(name = "description")
    private String description;
}
