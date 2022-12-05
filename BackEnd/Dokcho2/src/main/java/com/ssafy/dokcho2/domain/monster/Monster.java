package com.ssafy.dokcho2.domain.monster;

import com.ssafy.dokcho2.domain.userMonster.UserMonster;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
@Table(name = "monster")
public class Monster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "monster_id")
    private Long monsterId;

    @Column(name = "name")
    private String name;

    @Column(name = "hp")
    private Integer hp;
    @Column(name = "growth_hp")
    private Integer growthHp;
    @Column(name = "attack")
    private Integer attack;
    @Column(name = "growth_attack")
    private Integer growthAttack;
    @Column(name = "defence")
    private Integer defence;
    @Column(name = "growth_defence")
    private Integer growthDefence;

    @OneToMany(mappedBy = "monster", cascade = CascadeType.ALL)
    private List<UserMonster> userMonsterList = new ArrayList<>();

}
