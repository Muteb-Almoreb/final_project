package org.example.trucksy.DTOOut;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDashboardDTOOut {

    private Integer orderId;
    private LocalDate Date;
    private String customerName;
    private Double amount;
    private String status;
}
