package com.storesync.app.repo;

import com.storesync.app.entity.ItemEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity,Long> {
    boolean existsByItemName(@NotNull(message = "Item name is required") @Size(min = 1, max = 100, message = "Item name must be between 1 and 100 characters") String itemName);

    ItemEntity findByItemName(String itemName);
}
