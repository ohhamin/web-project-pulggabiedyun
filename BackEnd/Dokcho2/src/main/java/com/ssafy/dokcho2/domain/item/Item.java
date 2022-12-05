package com.ssafy.dokcho2.domain.item;

import com.ssafy.dokcho2.domain.enums.ItemType;
import com.ssafy.dokcho2.domain.userItem.UserItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
@Entity
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ItemType type;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<UserItem> userItemList = new ArrayList<>();

}
