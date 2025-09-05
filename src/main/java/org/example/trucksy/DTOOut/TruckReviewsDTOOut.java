package org.example.trucksy.DTOOut;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TruckReviewsDTOOut {

    private String clientName;
    private Integer rating;
    private String comment;

}
