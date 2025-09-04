package org.example.trucksy.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String status;

    @CreationTimestamp
    private LocalDate orderDate;

    private Double totalPrice;


    @ManyToOne
    @JsonIgnore
    private Client client;

    @ManyToOne
    @JsonIgnore
    private FoodTruck foodTruck;

//    //the Relation Edited to one to many instead of many to many
//    @OneToMany(cascade = CascadeType.ALL,mappedBy = "order")
//    private Set<Item> items;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "order",orphanRemoval = true)
    private Set<OrderLine> lines = new HashSet<>();

}
