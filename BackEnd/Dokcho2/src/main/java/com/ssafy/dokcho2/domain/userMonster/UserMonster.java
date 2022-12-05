package com.ssafy.dokcho2.domain.userMonster;

import com.ssafy.dokcho2.domain.monster.Monster;
import com.ssafy.dokcho2.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "users_monster")
public class UserMonster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_monster_id")
    private Long usersMonsterId;

    @Column(name = "level")
    private Integer level;

    @Column(name = "exp")
    private Integer exp;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "monster_id")
    private Monster monster;

    public void setLevel(Integer level) { this.level = level; }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    @PrePersist
    public void prePersist() {
        this.level = 1;
        this.exp = 0;
    }
}
