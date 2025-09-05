package org.example.trucksy.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
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

    // todo check if we want this object of item
    private String topSellingItems;

    private LocalDate updateDate;

    @OneToOne
    @MapsId
    @JsonIgnore
    private Owner owner;
}
