package org.example.trucksy.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
//فكرته بربطه مع الاوردر بحيث اعرف كل اوردر ايش الايتمز مع عددها الموجوده في الاوردر
public class OrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Item item;     // بدل itemId

    private Integer quantity;

    //item price at the moment
    private Double unitPriceAtPurchase;

    @ManyToOne
    @JsonIgnore
    private Order order;
}
