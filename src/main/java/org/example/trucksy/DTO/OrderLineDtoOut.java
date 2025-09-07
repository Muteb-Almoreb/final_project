package org.example.trucksy.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineDtoOut {
    private Integer itemId;
    private String itemName;
    private Integer quantity;
    private Double unitPriceAtPurchase;
}
