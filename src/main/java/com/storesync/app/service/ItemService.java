package com.storesync.app.service;

import com.storesync.app.dto.ItemDto;
import com.storesync.app.entity.ItemEntity;
import com.storesync.app.exception.DuplicateItemException;
import com.storesync.app.exception.InvalidItemException;
import com.storesync.app.exception.ItemNotFoundException;
import com.storesync.app.repo.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService implements ItemServiceMiddleLayer {

    private final ItemRepository itemRepository;

    @Override
    public ItemDto storeItem(ItemDto itemDto) {
        // 1. Check if item name is empty/null
        if (itemDto.getItemName() == null || itemDto.getItemName().trim().isEmpty()) {
            throw new InvalidItemException("Item name must not be empty.");
        }

        // 2. Check for duplicate entry in DB (if item name must be unique)
        if (itemRepository.existsByItemName(itemDto.getItemName())) {
            throw new DuplicateItemException("Item already exists with name: " + itemDto.getItemName());
        }

        // 3. If validations pass, copy to entity and save
        ItemEntity itemEntity = new ItemEntity();

        itemEntity.setCreatedAt(LocalDateTime.now());
        itemEntity.setUpdatedAt(LocalDateTime.now());
        itemEntity.setSkuCode(UUID.randomUUID().toString());

        BeanUtils.copyProperties(itemDto, itemEntity);

        itemEntity = itemRepository.save(itemEntity);

        // 4. Prepare response DTO and return
        ItemDto savedItemDto = new ItemDto();
        BeanUtils.copyProperties(itemEntity, savedItemDto);

        return savedItemDto;
    }

    @Override
    public List<ItemDto> fetchAllItems() {
        List<ItemEntity> listOfProducts = itemRepository.findAll();
        // Convert each ItemEntity to ItemDto (assuming BeanUtils or a custom mapper is used)
        List<ItemDto> itemDtoList = listOfProducts.stream().map(item -> new ItemDto(item.getItemName(), item.getDescription(), item.getUnitPrice())).collect(Collectors.toList());
        return itemDtoList;
    }

    @Override
    public ItemDto fetchItemByName(String itemName) {
        // Fetch the item entity from the database using the repository
        ItemEntity entity = itemRepository.findByItemName(itemName);

        // Check if the entity is null (i.e., item not found)
        if (entity == null) {
            throw new ItemNotFoundException("Item not found with name: " + itemName);  // You can customize this exception
        }

        // Create a new ItemDto object to hold the converted data
        ItemDto itemDto = new ItemDto();

        // Use BeanUtils to copy properties from the entity to the DTO
        BeanUtils.copyProperties(entity, itemDto);

        return itemDto;
    }

    public ItemDto updateItem(String itemName, ItemDto itemDto) {
        try {
            // Check if item exists
            ItemEntity existingItem = itemRepository.findByItemName(itemName);

            // Validate ItemDto fields if needed (use a custom validation)
            if (itemDto.getDescription() == null || itemDto.getUnitPrice() == null) {
                throw new InvalidItemException("Invalid item data provided.");
            }

            // Update item details
            existingItem.setDescription(itemDto.getDescription());
            existingItem.setUnitPrice(itemDto.getUnitPrice());
            // Update other fields if needed

            // Save updated item
            ItemEntity updatedItem = itemRepository.save(existingItem);

            // Convert the updated item to DTO and return
            return new ItemDto(updatedItem.getItemName(), updatedItem.getDescription(), updatedItem.getUnitPrice());
        } catch (ItemNotFoundException e) {
            // Handle item not found
            throw e;  // Rethrow custom exception
        } catch (InvalidItemException e) {
            // Handle invalid data error
            throw e;  // Rethrow custom exception
        } catch (DataIntegrityViolationException e) {
            // Handle database constraint violation
            throw new RuntimeException("Database constraint violation while updating item.", e);
        } catch (Exception e) {
            // Catch any unexpected exceptions
            throw new RuntimeException("Unexpected error occurred while updating item.", e);
        }
    }


}
