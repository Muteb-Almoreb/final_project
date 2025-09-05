package org.example.trucksy.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @NotEmpty(message = "Item name must not be empty")
    @Size(min = 2, max = 50, message = "Item name must be between 2 and 50 characters")
    @Column(columnDefinition = "varchar(50) not null")
    private String name;

    @NotNull(message = "Price must not be null")
    @Positive(message = "Price must be greater than 0")
    @Column(columnDefinition = "double not null")
    private Double price;

    @NotEmpty(message = "Description must not be empty")
    @Size(min = 5, max = 200, message = "Description must be between 5 and 200 characters")
    @Column(columnDefinition = "varchar(200) not null")
    private String description;

    @NotNull(message = "isAvailable must not be null")
    @Column(columnDefinition = "boolean not null")
    private Boolean isAvailable;

    @NotNull(message = "isDiscounted must not be null")
    @Column(columnDefinition = "boolean not null")
    private Boolean isDiscounted;

    @PastOrPresent(message = "Creation date cannot be in the future")
    @Column(columnDefinition = "date not null")
    private LocalDate creationDate;

    @PastOrPresent(message = "Update date cannot be in the future")
    private LocalDate updateDate;


    @OneToOne(cascade = CascadeType.ALL , mappedBy = "item")
    @PrimaryKeyJoinColumn
    private Discount discount;

    //the relation here will be undirect to Order by using liens.
//    //the Relation Edited to many to one instead of many to many
//    @ManyToOne
//    @JsonIgnore
//    private Order orders;

    @ManyToOne
    @JsonIgnore
    private FoodTruck foodTruck;
}
