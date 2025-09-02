package org.example.trucksy.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FoodTruckOwner {

    @Id
    private Integer id;

    private String businessName;

    private String iban;

    private boolean subscribed;

    @OneToOne
    @MapsId
    @JsonIgnore
    private User user;


    @OneToMany
    private Set<FoodTruck> foodTrucks;
}
