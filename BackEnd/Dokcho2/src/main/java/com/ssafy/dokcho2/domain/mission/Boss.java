package com.ssafy.dokcho2.domain.mission;

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
@Table(name = "boss")
public class Boss {
    @Id
    @Column(name = "mission_id")
    private Long missionId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "mission_id", referencedColumnName = "mission_id")
    private Mission mission;

    @Column(name = "name")
    private String name;
    @Column(name = "hp")
    private Integer hp;
    @Column(name = "attack")
    private Integer attack;
    @Column(name = "defence")
    private Integer defence;
}
