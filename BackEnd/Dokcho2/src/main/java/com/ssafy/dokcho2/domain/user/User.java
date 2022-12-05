package com.ssafy.dokcho2.domain.user;

import com.ssafy.dokcho2.domain.basetime.BaseTime;
import com.ssafy.dokcho2.domain.enums.Role;
import com.ssafy.dokcho2.domain.mission.UserMission;
import com.ssafy.dokcho2.domain.monster.Monster;
import com.ssafy.dokcho2.domain.userItem.UserItem;
import com.ssafy.dokcho2.domain.userMonster.UserMonster;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_id")
    private Long userId;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "nickname")
    private String nickname;
    @Column(name = "email")
    private String email;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(name = "token")
    private String token;

    @Column(name = "now_mission_id")
    private Long nowMissionId;

    @ManyToOne
    @JoinColumn(name = "monster_id")
    private Monster representMonster;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserItem> userItemList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserMonster> userMonsterList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserMission> userMissionList = new ArrayList<>();


    public void saveToken(String token) {
        this.token = token;
    }
    public void changePW(String password){this.password = password;}
    public void setNickname(String nickname){this.nickname = nickname;}

    public void changeRepresentMonster(Monster monster){this.representMonster = monster;}

    public void changeNowMissionId(Long missionId) {this.nowMissionId = missionId;}
    @PrePersist
    public void prePersist(){
        this.role = this.role == null ? Role.ROLE_USER : this.role;
        this.nowMissionId = this.nowMissionId == null ? 1 : this.nowMissionId;
    }
}
