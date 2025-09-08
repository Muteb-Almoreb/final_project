package org.example.trucksy.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FoodTruck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;

    private String category;

    private Double latitude;

    private Double longitude;

    private String city;

    private String district;

    private String status;

    private String imageUrl;

    private String imageKey;

    @ManyToOne
    @JsonIgnore
    private Owner owner;


    @OneToMany(cascade = CascadeType.ALL , mappedBy = "foodTruck")
    private Set<Order> orders;


    //todo check the relation
    @OneToMany(cascade = CascadeType.ALL , mappedBy = "foodTruck")

    private Set<Review> reviews;


    // todo add relation with item and dashboard
    @OneToMany(cascade = CascadeType.ALL , mappedBy = "foodTruck")
    private Set<Item> item;

    //Add relation with Item
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "foodTruck")
    @JsonIgnore
    private Set<Item> items;

}
