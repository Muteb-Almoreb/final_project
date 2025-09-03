package org.example.trucksy.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    private Integer rating;

    private String comment;

    private LocalDate createdDate; // todo check if we need time


    @ManyToOne
    private Client client;


    @ManyToOne
    private FoodTruck foodTruck;


}
