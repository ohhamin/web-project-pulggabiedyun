package com.ssafy.dokcho2.domain.userItem;

import com.ssafy.dokcho2.domain.item.Item;
import com.ssafy.dokcho2.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserItemRepository extends JpaRepository<UserItem, Long> {
    List<UserItem> findAllByUser(User user);

    Optional<UserItem> findByUserAndItem(User user, Item item);

    void deleteAllByUser(User user);
}
