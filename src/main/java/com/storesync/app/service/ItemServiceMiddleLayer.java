package com.storesync.app.service;

import com.storesync.app.dto.ItemDto;

import java.util.List;

public interface ItemServiceMiddleLayer {
    ItemDto storeItem(ItemDto itemDto);

    List<ItemDto> fetchAllItems();

    ItemDto fetchItemByName(String itemName);

    ItemDto updateItem(String itemName, ItemDto itemDto);
}
