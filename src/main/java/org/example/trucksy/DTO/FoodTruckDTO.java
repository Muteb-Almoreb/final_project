package org.example.trucksy.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FoodTruckDTO {


    @NotEmpty(message = "Name must not be empty")
    @Size(min = 3, max = 30, message = "Name must be between 3 and 30 characters")
    private String name;

    @NotEmpty(message = "Description must not be empty")
    @Size(min = 10, max = 200, message = "Description must be between 10 and 200 characters")
    private String description;

    @NotEmpty(message = "Category must not be empty")
    @Pattern(
            regexp = "(?i)^(Burger|Pizza|Coffee|Dessert|Other|breakfast|Lunch|Dinner)$",
            message = "Category must be one of: Burger, Pizza, Coffee, Dessert, breakfast ,Lunch,Dinner,Other"
    )
    private String category;
    private String city;
    private String district;

    private Double latitude = null;
    private Double longitude = null;

    private String imageUrl;
}
