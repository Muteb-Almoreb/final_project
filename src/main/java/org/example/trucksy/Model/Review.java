package org.example.trucksy.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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


    @NotNull(message = "Rating must not be null")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not be more than 5")
    @Column(columnDefinition = "int not null")
    private Integer rating;

    @NotEmpty(message = "Comment must not be empty")
    @Size(min = 3, max = 200, message = "Comment must be between 3 and 200 characters")
    @Column(columnDefinition = "varchar(200) not null")
    private String comment;

    private LocalDate createdDate; // todo check if we need time


    //todo check the relation if user can review more than one or not
    @ManyToOne
    private Client client;


    @ManyToOne
    @JsonIgnore
    private FoodTruck foodTruck;

}
