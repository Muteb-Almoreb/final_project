package org.example.trucksy.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDTO {

    private String title;
    private String description;
    private Double percentage;
    private LocalDate startDate;
    private LocalDate endDate;

   private Boolean isActive = true;
}
