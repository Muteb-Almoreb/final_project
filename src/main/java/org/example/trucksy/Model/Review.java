package org.example.trucksy.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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


    //todo check the relation if user can review more than one or not
    @ManyToOne
    private Client client;


    @ManyToOne
    @JsonIgnore
    private FoodTruck foodTruck;

}
