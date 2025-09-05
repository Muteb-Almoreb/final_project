package org.example.trucksy.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ItemViewDTO {

    private Integer id;
    private String name;
    private String description;
    private Double price;          // الأساسي
    private Double effectivePrice; // بعد الخصم (إن وجد)
    private Boolean isAvailable;
    private Boolean isDiscounted;
    private Double discountPercentage; // للعلم
}
