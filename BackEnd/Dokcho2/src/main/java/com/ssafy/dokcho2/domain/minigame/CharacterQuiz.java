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
@Table(name = "characterquiz")
public class CharacterQuiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "character_quiz_id")
    private Long characterQuizId;

    @Column(name = "question")
    private String question;

    @Column(name = "right_answer")
    private String right_answer;

    @Column(name = "description")
    private String description;
}
