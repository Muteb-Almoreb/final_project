package org.example.trucksy.Controller;

import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiResponse;
import org.example.trucksy.Model.Item;
import org.example.trucksy.Service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/item")
@RequiredArgsConstructor
public class ItemController {


    private final ItemService itemService;

    @GetMapping("/get/{ownerId}/{truckId}")
    public ResponseEntity<?> getItems(@PathVariable Integer ownerId,
                                                      @PathVariable Integer truckId) {
        return ResponseEntity.ok(itemService.getItemsByFoodTruck(ownerId, truckId));
    }


    @PostMapping("/add/{ownerId}/{truckId}")
    public ResponseEntity<ApiResponse> addItem(@PathVariable Integer ownerId,
                                               @PathVariable Integer truckId,
                                               @RequestBody Item item) {
        itemService.addItemToFoodTruck(ownerId, truckId, item);
        return ResponseEntity.ok(new ApiResponse("Item added to food truck"));
    }

    @PutMapping("/update/{ownerId}/{truckId}/{itemId}")
    public ResponseEntity<ApiResponse> updateItem(@PathVariable Integer ownerId,
                                                  @PathVariable Integer truckId,
                                                  @PathVariable Integer itemId,
                                                  @RequestBody Item item) {
        itemService.updateItemInFoodTruck(ownerId, truckId, itemId, item);
        return ResponseEntity.ok(new ApiResponse("Item updated"));
    }

    @DeleteMapping("/delete/{ownerId}/{truckId}/{itemId}")
    public ResponseEntity<ApiResponse> deleteItem(@PathVariable Integer ownerId,
                                                  @PathVariable Integer truckId,
                                                  @PathVariable Integer itemId) {
        itemService.deleteItemFromFoodTruck(ownerId, truckId, itemId);
        return ResponseEntity.ok(new ApiResponse("Item deleted"));
    }

    // عمليات إضافية
    @PutMapping("/setAvailable/{ownerId}/{truckId}/{itemId}")
    public ResponseEntity<?> setAvailable(@PathVariable Integer ownerId,
                                          @PathVariable Integer truckId,
                                          @PathVariable Integer itemId) {
        itemService.setAvailable(ownerId , truckId , itemId);
        return ResponseEntity.status(200).body( new ApiResponse("Item availability updated ,it been true"));
    }

    @PutMapping("/setNotAvailable/{ownerId}/{truckId}/{itemId}")
    public ResponseEntity<?> setNotAvailable(@PathVariable Integer ownerId,
                                             @PathVariable Integer truckId,
                                             @PathVariable Integer itemId) {
        itemService.setNotAvailable(ownerId , truckId , itemId);
        return ResponseEntity.status(200).body( new ApiResponse("Item availability updated , it been false"));
    }

    @PutMapping("/price/{ownerId}/{truckId}/{itemId}/{newPrice}")
    public ResponseEntity<ApiResponse> updatePrice(@PathVariable Integer ownerId,
                                                   @PathVariable Integer truckId,
                                                   @PathVariable Integer itemId,
                                                   @PathVariable Double newPrice) {
        itemService.updatePrice(ownerId, truckId, itemId, newPrice);
        return ResponseEntity.ok(new ApiResponse("Item price updated"));
    }


}
