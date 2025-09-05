package org.example.trucksy.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Discount {

    @Id
    private Integer id;


    private String title;
    private String description;
    private Double percentage;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isActive;
    private LocalDate createDate;


    @Column(name = "original_price")
    private Double originalPrice;

    @OneToOne
    @MapsId
    @JsonIgnore
    private Item item;
}
