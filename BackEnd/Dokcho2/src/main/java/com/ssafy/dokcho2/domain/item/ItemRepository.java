package com.ssafy.dokcho2.domain.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(nativeQuery = true, value = "select * from item i where i.type = 'USE_ITEM' order by RAND() limit 1")
    Item findRandomItem();
}
