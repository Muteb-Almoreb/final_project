package org.example.trucksy.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private Double price;
    private String description;
    private Boolean isAvailable;
    private Boolean isDiscounted;
    private LocalDate creationDate;
    private LocalDate updateDate;


    @OneToOne(cascade = CascadeType.ALL , mappedBy = "item")
    @PrimaryKeyJoinColumn
    private Discount discount;

    @ManyToMany
    @JsonIgnore
    private Set<Order> orders;
}
