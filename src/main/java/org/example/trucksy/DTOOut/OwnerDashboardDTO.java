package org.example.trucksy.DTOOut;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnerDashboardDTO {

    private String ownerName;
    private Integer totalOrders;
    private Double totalRevenue;
    private Integer predictedOrders;
    private String peakOrders;
    private String topSellingItems;
    private LocalDate updateDate;
}
