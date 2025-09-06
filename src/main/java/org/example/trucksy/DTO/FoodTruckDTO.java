package org.example.trucksy.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FoodTruckDTO {

    private String name;
    private String description;
    private String category;
    private String city;
    private String district;
}
