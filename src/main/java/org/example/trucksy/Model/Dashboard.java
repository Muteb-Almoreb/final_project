package org.example.trucksy.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Dashboard {

    @Id
    private Integer id;

    private Integer totalOrders;

    private Double totalRevenue;

    private Integer predictedOrders;

    // todo this is pattern 'morning , night '
    private String peakOrders;

    private String topSellingItems;

    private LocalDate updateDate;
}
