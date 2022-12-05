package com.ssafy.dokcho2.service.item;

import com.ssafy.dokcho2.domain.item.Item;
import com.ssafy.dokcho2.domain.item.ItemRepository;
import com.ssafy.dokcho2.domain.user.User;
import com.ssafy.dokcho2.domain.user.UserRepository;
import com.ssafy.dokcho2.domain.userItem.UserItem;
import com.ssafy.dokcho2.domain.userItem.UserItemRepository;
import com.ssafy.dokcho2.dto.exception.item.ItemNotFoundException;
import com.ssafy.dokcho2.dto.exception.item.ItemNotUsable;
import com.ssafy.dokcho2.dto.exception.item.UserItemNotExistException;
import com.ssafy.dokcho2.dto.exception.user.UserNotFoundException;
import com.ssafy.dokcho2.dto.item.ItemDto;
import com.ssafy.dokcho2.dto.item.RawItemDto;
import com.ssafy.dokcho2.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ssafy.dokcho2.domain.enums.ItemType.USE_ITEM;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService{

    private final UserItemRepository userItemRepository;

    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    @Override
    public List<ItemDto> getItemList() {
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findByUsername).orElseThrow(UserNotFoundException::new);
        List<ItemDto> userItemList = userItemRepository.findAllByUser(user)
                .stream().map(s -> ItemDto.from(s)).collect(Collectors.toList());
        return userItemList;
    }

    @Override
    @Transactional
    public List<ItemDto> addItem(Long itemId) {
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findByUsername).orElseThrow(UserNotFoundException::new);
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);

        Optional<UserItem> o = userItemRepository.findByUserAndItem(user, item);

        UserItem userItem;

        if (o.isPresent()){
            userItem = o.get();
            userItem.setCount(userItem.getCount()+1);
        } else {
            userItem = UserItem.builder()
                    .user(user)
                    .item(item)
                    .count(1)
                    .build();
        }


        userItemRepository.save(userItem);

        List<ItemDto> userItemList = userItemRepository.findAllByUser(user)
                .stream().map(s -> ItemDto.from(s)).collect(Collectors.toList());

        return userItemList;
    }

    @Override
    @Transactional
    public List<ItemDto> useItem(Long itemId) {
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findByUsername).orElseThrow(UserNotFoundException::new);
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);

        //item이 스토리용 아이템이면 사용하는건 불가능해야함
        //스토리 상 사용된다면 그때 해결하자
        if (item.getType() != USE_ITEM){
            throw new ItemNotUsable();
        }

        UserItem userItem = userItemRepository.findByUserAndItem(user, item).orElseThrow(UserItemNotExistException::new);

        if (userItem.getCount()-1 <= 0){
            userItemRepository.delete(userItem);
        } else {
            userItem.setCount(userItem.getCount()-1);
            userItemRepository.save(userItem);
        }

        List<ItemDto> userItemList = userItemRepository.findAllByUser(user)
                .stream().map(s -> ItemDto.from(s)).collect(Collectors.toList());
        return userItemList;
    }

    @Override
    @Transactional
    public ItemDto addUseItem() {
        Item item = itemRepository.findRandomItem();
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findByUsername).orElseThrow(UserNotFoundException::new);

        Optional<UserItem> o = userItemRepository.findByUserAndItem(user, item);

        UserItem userItem;

        if (o.isPresent()){
            userItem = o.get();
            userItem.setCount(userItem.getCount()+1);
        } else {
            userItem = UserItem.builder()
                    .user(user)
                    .item(item)
                    .count(1)
                    .build();
        }
        userItemRepository.save(userItem);
        return ItemDto.from(userItem);
    }

    @Override
    public List<RawItemDto> getWholeItemList() {
        List<RawItemDto> rawItemDtoList = itemRepository.findAll().stream().map(s -> RawItemDto.from(s)).collect(Collectors.toList());

        return rawItemDtoList;
    }
}
