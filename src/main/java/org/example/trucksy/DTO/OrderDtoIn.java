package org.example.trucksy.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDtoIn {
    private Integer foodTruckId;
    private Set<LiensDtoIn> liensDtoIns;//every item with its quantity
}
