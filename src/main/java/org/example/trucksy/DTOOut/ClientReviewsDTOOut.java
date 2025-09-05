package org.example.trucksy.DTOOut;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientReviewsDTOOut {

    private String foodTruckName;
    private Integer rating;
    private String comment;
}
