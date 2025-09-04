package org.example.trucksy.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Owner {

    @Id
    private Integer id;

    private boolean subscribed;

    @OneToOne
    @MapsId
//    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;


    @OneToMany(cascade = CascadeType.ALL , mappedBy = "owner")
    private Set<FoodTruck> foodTrucks;
}
