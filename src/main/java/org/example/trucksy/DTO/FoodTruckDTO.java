package org.example.trucksy.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodTruckDTO {

    private String name;
    private String description;
    private String category;
    private Double latitude;
    private Double longitude;
}
