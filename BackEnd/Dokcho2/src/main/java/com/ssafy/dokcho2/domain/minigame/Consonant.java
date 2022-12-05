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
@Table(name = "consonant")
public class Consonant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consonant_id")
    private Long consonantId;

    @Column(name = "question")
    private String question;

    @Column(name = "hint")
    private String hint;

    @Column(name = "right_answer")
    private String right_answer;

    @Column(name = "description")
    private String description;
}
