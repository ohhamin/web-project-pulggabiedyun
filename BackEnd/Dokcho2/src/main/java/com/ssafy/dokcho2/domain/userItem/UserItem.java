package com.ssafy.dokcho2.domain.userItem;

import com.ssafy.dokcho2.domain.item.Item;
import com.ssafy.dokcho2.domain.user.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users_item")
public class UserItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_item_id")
    private Long usersItemId;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "count")
    private Integer count;

    public void setCount(Integer c){
        this.count = c;
    }

    @PrePersist
    public void prePersist() {
        this.count = this.count == null ? 1 : this.count;
    }
}
