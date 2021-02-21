package com.green.demo.service.item;

import com.green.demo.controller.item.ItemCreateDto;
import com.green.demo.controller.item.ItemDto;
import com.green.demo.controller.item.ItemUpdateDto;
import com.green.demo.error.NotFoundException;
import com.green.demo.error.UnauthorizedException;
import com.green.demo.model.item.Item;
import com.green.demo.model.user.Email;
import com.green.demo.model.user.User;
import com.green.demo.repository.item.ItemRepository;
import com.green.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;

    public ItemDto createItem(ItemCreateDto createDto, Email email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() ->new NotFoundException(User.class, email));

        Item newItem = createDto.of();
        newItem.setUser(user);

        return ItemDto.of(itemRepository.save(newItem));
    }

    @Transactional(readOnly = true)
    public ItemDto detailItem(Long itemId) {
        checkNotNull(itemId, "itemId must be provided.");

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(Item.class, itemId));

        return ItemDto.of(item);
    }

    @Transactional(readOnly = true)
    public List<ItemDto> items(PageRequest pageRequest) {
        return itemRepository.findAll(pageRequest).stream()
                .map(ItemDto::of)
                .collect(Collectors.toList());
    }

    public ItemDto updateItem(Email email, Long itemId, ItemUpdateDto updateDto) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(User.class, email));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(Item.class, itemId));

        if (!item.getUser().equals(user)) {
            throw new UnauthorizedException("AuthenticationFailed");
        }

        item.update(updateDto);
        return ItemDto.of(itemRepository.save(item));
    }

    public void deleteItem(Email email, Long itemId) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(User.class, email));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(Item.class, itemId));

        if (!item.getUser().equals(user)) {
            throw new UnauthorizedException("AuthenticationFailed");
        }

        itemRepository.deleteById(itemId);
    }

    public Item findByName(String itemName) {
        return itemRepository.findByItemName(itemName);
    }

    public Item findById(Long itemId) {
        checkNotNull(itemId, "itemId must be provided.");

        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(Item.class, itemId));
    }

    public void insert(Item item) {
        itemRepository.save(item);
    }
}
