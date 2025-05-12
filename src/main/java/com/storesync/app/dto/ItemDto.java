package com.storesync.app.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class ItemDto {


    @NotNull(message = "Item name is required")
    @Size(min = 1, max = 100, message = "Item name must be between 1 and 100 characters")
    private String itemName;              // Name of the item

    @Size(max = 500, message = "Description can't exceed 500 characters")
    private String description;           // Description of the item

    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private BigDecimal unitPrice;         // Price of the item


//
//    @PastOrPresent(message = "Creation date must be in the past or present")
//    private LocalDateTime createdAt;      // When the item was created
//
//    @PastOrPresent(message = "Updated date must be in the past or present")
//    private LocalDateTime updatedAt;      // When the item was last updated
}
