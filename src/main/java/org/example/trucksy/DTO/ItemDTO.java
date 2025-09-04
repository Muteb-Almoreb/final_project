package org.example.trucksy.DTO;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {

    private String name;
    private Double price;
    private String description;

    private Boolean isAvailable = true;
}
