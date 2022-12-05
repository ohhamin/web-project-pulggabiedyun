package com.ssafy.dokcho2.domain.mission;

import com.ssafy.dokcho2.domain.enums.MissionStatus;
import com.ssafy.dokcho2.domain.user.User;
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
@Table(name = "users_mission")
public class UserMission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_mission_id")
    private Long usersMissionId;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private MissionStatus status;
    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "mission_id")
    private Mission mission;

    public void changeStatus(MissionStatus newStatus){
        this.status = newStatus;
    }
}
