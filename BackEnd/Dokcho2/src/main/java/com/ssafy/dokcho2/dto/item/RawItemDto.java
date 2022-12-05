package com.ssafy.dokcho2.dto.item;

import com.ssafy.dokcho2.domain.enums.ItemType;
import com.ssafy.dokcho2.domain.item.Item;
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
public class RawItemDto {
    private Long itemId;
    private String itemName;
    private String description;
    private ItemType type;

    public static RawItemDto from(Item item){
        return RawItemDto.builder()
                .itemId(item.getItemId())
                .itemName(item.getName())
                .description(item.getDescription())
                .type(item.getType())
                .build();
    }
}
