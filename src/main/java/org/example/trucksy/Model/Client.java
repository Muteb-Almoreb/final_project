package org.example.trucksy.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Client {

    @Id
    private Integer id;
    private Double latitude;
    private Double longitude;

    @OneToOne
    @MapsId
    @JsonIgnore
    private User user;

    @OneToMany(cascade = CascadeType.ALL , mappedBy = "client")
    private Set<Order> orders;


    @OneToMany(cascade = CascadeType.ALL , mappedBy = "client")
    @JsonIgnore
    private Set<Review> reviews;

}
