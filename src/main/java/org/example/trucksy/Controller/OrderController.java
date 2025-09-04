package org.example.trucksy.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiResponse;
import org.example.trucksy.DTO.OrderDtoIn;
import org.example.trucksy.DTO.OrderDtoOut;
import org.example.trucksy.Service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/add/{clientId}") // todo after Security added remove clientId from the path
    public ResponseEntity<?> addOrder(@PathVariable Integer clientId,
                                      @Valid @RequestBody OrderDtoIn orderDtoIn) {
        orderService.addOrder(clientId, orderDtoIn);
        return ResponseEntity.status(200).body(new ApiResponse("Order placed successfully"));
    }

    //todo بستشير الشباب اذا ننقلها للفود ترك سيرفس
    @GetMapping("/foodtruck/{foodTruckId}")
    public ResponseEntity<List<OrderDtoOut>> getOrdersForFoodTruck(@PathVariable Integer foodTruckId) {
        return ResponseEntity.status(200).body(orderService.getOrdersForFoodTruckDto(foodTruckId));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<OrderDtoOut>> getOrdersForClient(@PathVariable Integer clientId) {
        return ResponseEntity.status(200).body(orderService.getOrdersForClientDto(clientId));
    }

    @GetMapping("/foodtruck/{foodTruckId}/{orderId}")
    public ResponseEntity<OrderDtoOut> getOrderForFoodTruck(@PathVariable Integer foodTruckId,
                                                            @PathVariable Integer orderId) {
        return ResponseEntity.status(200).body(orderService.getOrderForFoodTruckDto(foodTruckId, orderId));
    }
}
