package com.green.demo.controller.item;

import com.green.demo.model.user.Email;
import com.green.demo.security.JwtAuthentication;
import com.green.demo.service.item.ItemService;
import com.green.demo.util.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/item")
@RequiredArgsConstructor
public class ItemRestController {

    private final ItemService itemService;

    @PostMapping("")
    public ApiResult<ItemDto> createItem(@RequestBody ItemCreateDto itemCreateDto) {
        return ApiResult.OK(itemService.createItem(itemCreateDto));
    }

    @GetMapping("info/{itemId}")
    public ApiResult<ItemDto> info(@PathVariable Long itemId) {
        return ApiResult.OK(itemService.detailItem(itemId));
    }

    @GetMapping("info/list")
    public ApiResult<List<ItemDto>> infoList(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return ApiResult.OK(itemService.items(pageRequest));
    }

    @PutMapping("{itemId}")
    public ApiResult<ItemDto> updateItem(@PathVariable Long itemId, ItemUpdateDto updateDto, @AuthenticationPrincipal JwtAuthentication jwtAuthentication) {
        return ApiResult.OK(itemService.updateItem(jwtAuthentication.email, itemId, updateDto));
    }

    @DeleteMapping("{itemId}")
    public ResponseEntity<?> deleteItem(@PathVariable Long itemId, @AuthenticationPrincipal JwtAuthentication jwtAuthentication) {
        itemService.deleteItem(jwtAuthentication.email, itemId);
        return ResponseEntity.ok().build();
    }
}
