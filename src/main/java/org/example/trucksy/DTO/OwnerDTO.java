package org.example.trucksy.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OwnerDTO {
    private String username;
    private String email;
    private String password;
    private String phone;

    // this is for foodTruck
    private String name;
    private String description;
    private String category;
}
