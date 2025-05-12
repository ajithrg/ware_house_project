package com.storesync.app.controller;

import com.storesync.app.dto.ItemDto;
import com.storesync.app.exception.ItemNotFoundException;
import com.storesync.app.service.ItemServiceMiddleLayer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("api/warehouse/itemapi")
@RestController
@RequiredArgsConstructor
public class ItemController {

    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);
    private final ItemServiceMiddleLayer serviceLayer;

    @PostMapping("/store")
    public ResponseEntity<ItemDto> storeItem(@RequestBody @Validated ItemDto itemDto) {
        logger.info("Received request to store item: {}", itemDto);
        ItemDto Item = serviceLayer.storeItem(itemDto);
        logger.info("Item successfully saved with ID: {}", Item.getItemName());

        return ResponseEntity.status(HttpStatus.CREATED).body(Item);
    }

    @GetMapping("/fetchAllProducts")
    public ResponseEntity<List<ItemDto>> fetchAllProducts() {
        logger.info("Received request to fetch all items.");

        try {
            List<ItemDto> itemList = serviceLayer.fetchAllItems();

            if (itemList.isEmpty()) {
                logger.info("No items found.");
                return ResponseEntity.noContent().build();  // HTTP 204 No Content if list is empty
            }

            logger.info("Successfully fetched {} items.", itemList.size());
            return ResponseEntity.ok(itemList);  // Simplified HTTP 200 OK response

        } catch (Exception e) {
            logger.error("Error occurred while fetching items: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);  // Return HTTP 500 on error
        }


    }

    //Get the Data By its name
    @GetMapping("/fetchItemByName/{itemName}")
    public ResponseEntity<ItemDto> fetchItemByName(@PathVariable String itemName) {
        logger.info("Received request to fetch item by name: {}", itemName);

        ItemDto item = serviceLayer.fetchItemByName(itemName);

        if (item == null) {
            // Throw ItemNotFoundException if item is not found
            throw new ItemNotFoundException("Item not found with name: " + itemName);
        }

        logger.info("Successfully fetched item: {}", itemName);
        return ResponseEntity.ok(item);  // Return item details with HTTP 200 OK
    }


    @PutMapping("/updateItem/{itemName}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable String itemName, @RequestBody @Validated ItemDto itemDto) {
        logger.info("Received request to update item: {}", itemName);

        ItemDto updatedItem = serviceLayer.updateItem(itemName, itemDto);

        if (updatedItem == null) {
            logger.error("Item not found for update: {}", itemName);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        logger.info("Successfully updated item: {}", itemName);
        return ResponseEntity.ok(updatedItem);  // Return the updated item with HTTP 200 OK
    }



}
