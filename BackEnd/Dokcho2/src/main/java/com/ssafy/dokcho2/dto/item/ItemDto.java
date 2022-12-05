package com.ssafy.dokcho2.dto.item;

import com.ssafy.dokcho2.domain.enums.ItemType;
import com.ssafy.dokcho2.domain.userItem.UserItem;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "아이템 정보")
public class ItemDto {
    private Long userId;
    private Long itemId;
    private String itemName;
    private String description;
    private ItemType type;
    private Integer count;

    public static ItemDto from(UserItem userItem){
        return ItemDto.builder()
                .userId(userItem.getUser().getUserId())
                .itemId(userItem.getItem().getItemId())
                .itemName(userItem.getItem().getName())
                .description(userItem.getItem().getDescription())
                .type(userItem.getItem().getType())
                .count(userItem.getCount())
                .build();
    }
}
