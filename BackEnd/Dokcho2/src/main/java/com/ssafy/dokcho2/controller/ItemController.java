package com.ssafy.dokcho2.controller;

import com.ssafy.dokcho2.domain.item.Item;
import com.ssafy.dokcho2.dto.item.ItemDto;
import com.ssafy.dokcho2.dto.item.RawItemDto;
import com.ssafy.dokcho2.service.item.ItemService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/item")
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @GetMapping()
    @ApiOperation(value = "보유한 아이템 리스트 불러오기")
    public ResponseEntity<List<ItemDto>> getInventory(){
        List<ItemDto> itemDtoList = itemService.getItemList();

        return new ResponseEntity<>(itemDtoList, HttpStatus.OK);
    }

    @PostMapping()
    @ApiOperation(value = "아이템 습득 후 갱신된 아이템 리스트 반환")
    public ResponseEntity<List<ItemDto>> addItem(@RequestBody Long itemId) {

        log.info("요청받음");
        List<ItemDto> itemDtoList = itemService.addItem(itemId);

        return new ResponseEntity<>(itemDtoList, HttpStatus.OK);
    }

    @DeleteMapping("/{itemId}")
    @ApiOperation(value = "아이템 사용 후 갱신된 아이템 리스트 반환")
    public ResponseEntity<List<ItemDto>> useItem(@PathVariable Long itemId){

        List<ItemDto> itemDtoList = itemService.useItem(itemId);

        return new ResponseEntity<>(itemDtoList, HttpStatus.OK);
    }

    @GetMapping("/itemList")
    @ApiOperation(value = "전체 아이템 리스트 반환")
    public ResponseEntity<List<RawItemDto>> wholeItem() {
        List<RawItemDto> rawItemDtoList = itemService.getWholeItemList();

        return new ResponseEntity<>(rawItemDtoList, HttpStatus.OK);
    }

}
