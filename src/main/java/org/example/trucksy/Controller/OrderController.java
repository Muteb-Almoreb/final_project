package org.example.trucksy.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiResponse;
import org.example.trucksy.DTO.LiensDtoIn;
import org.example.trucksy.DTO.OrderDtoIn;
import org.example.trucksy.DTO.OrderDtoOut;
import org.example.trucksy.Service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/add/{clientId}/{foodTruckId}") // todo after Security added remove clientId from the path
    public ResponseEntity<?> addOrder(@PathVariable Integer clientId,
                                      @PathVariable Integer foodTruckId,
                                      @Valid @RequestBody Set<LiensDtoIn> liensDtoIn) {

        // NOTE (assistant): Return service ResponseEntity directly to avoid double-wrapping.
        return orderService.addOrder(clientId, foodTruckId, liensDtoIn);
    }

    //todo test the call manually and after deployment it will call directly from addorder endpoint with the Callback Url
    //1) paymentId: POST /api/v1/payment/callback/{orderId} use this method after deploy -> so this is for production env
    @PostMapping("/callback/{orderId}")
    public ResponseEntity<?> callbackNoPid(@PathVariable Integer orderId) {
        // NOTE (assistant): Controller path is /api/v1/order/callback/{orderId} (not /api/v1/payment/...)
        return orderService.handlePaymentCallback(orderId, null);
    }

    // 2) paymentId: POST /api/v1/payment/callback/{orderId}/{paymentId} -> and this for developer env
    @PostMapping("/callback/{orderId}/{paymentId}")
    public ResponseEntity<?> callbackWithPid(
            @PathVariable Integer orderId,
            @PathVariable String paymentId
    ) {
        // NOTE (assistant): Controller path is /api/v1/order/callback/{orderId}/{paymentId}
        return orderService.handlePaymentCallback(orderId, paymentId);
    }

    // تغيير الحالة إلى READY (من Path Variables)
    @PutMapping("/status/ready/{ownerId}/{foodTruckId}/{orderId}")
    public ResponseEntity<ApiResponse> markOrderReady(@PathVariable("ownerId") Integer ownerId,
                                                      @PathVariable Integer foodTruckId,
                                                      @PathVariable Integer orderId) {
        orderService.changeOrderStatusToReady(ownerId, foodTruckId, orderId);
        return ResponseEntity.status(200).body(new ApiResponse(
                "Order #" + orderId + " marked as READY"
        ));
    }

    // تغيير الحالة إلى COMPLETED (من Path Variables)
    @PutMapping("/status/completed/{ownerId}/{foodTruckId}/{orderId}")
    public ResponseEntity<ApiResponse> markOrderCompleted(@PathVariable("ownerId") Integer ownerId,
                                                          @PathVariable Integer foodTruckId,
                                                          @PathVariable Integer orderId) {
        orderService.changeOrderStatusToCompleted(ownerId, foodTruckId, orderId);
        return ResponseEntity.status(200).body(new ApiResponse(
                "Order #" + orderId + " marked as COMPLETED"
        ));
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
