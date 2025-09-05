package org.example.trucksy.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDtoOut {
    // this is the main DTO that return back to the client or to the Food Truck
    private Integer id;
    private String status;
    private Double totalPrice;

    private ClientSummaryDtoOut client;
    private FoodTruckSummaryDtoOut foodTruck;

    private List<OrderLineDtoOut> lines;
}
