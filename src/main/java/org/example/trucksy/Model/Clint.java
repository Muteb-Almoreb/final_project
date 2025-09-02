package org.example.trucksy.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Clint {

    @Id
    private Integer id;

    private Double latitude;
    private Double longitude;
}
