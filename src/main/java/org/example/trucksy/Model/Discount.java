package org.example.trucksy.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Discount {

    @Id
    private Integer id;


    @NotEmpty(message = "Title must not be empty")
    @Size(min = 3, max = 50, message = "Title must be between 3 and 50 characters")
    @Column(columnDefinition = "varchar(50) not null")
    private String title;

    @NotEmpty(message = "Description must not be empty")
    @Size(min = 5, max = 200, message = "Description must be between 5 and 200 characters")
    @Column(columnDefinition = "varchar(200) not null")
    private String description;

    @NotNull(message = "Percentage must not be null")
    @Column(columnDefinition = "double not null")
    private Double percentage;

    @NotNull(message = "Start date must not be null")
    @FutureOrPresent(message = "Start date cannot be in the past")
    @Column(columnDefinition = "date not null")
    private LocalDate startDate;

    @NotNull(message = "End date must not be null")
    @Future(message = "End date must be in the future")
    private LocalDate endDate;

    @NotNull(message = "isActive must not be null")
    @Column(columnDefinition = "boolean not null")
    private Boolean isActive;

    @CreationTimestamp
    private LocalDate createDate;


    @Column(name = "original_price")
    private Double originalPrice;

    @OneToOne
    @MapsId
    @JsonIgnore
    private Item item;
}
