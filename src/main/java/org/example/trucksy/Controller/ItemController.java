package org.example.trucksy.Controller;

import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiResponse;
import org.example.trucksy.Model.Item;
import org.example.trucksy.Model.User;
import org.example.trucksy.Service.ItemService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/item")
@RequiredArgsConstructor
public class ItemController {


    private final ItemService itemService;

    @GetMapping("/get/{truckId}")
    public ResponseEntity<?> getItems(@AuthenticationPrincipal User user,
                                                      @PathVariable Integer truckId) {
        return ResponseEntity.ok(itemService.getItemsByFoodTruck(user.getId(), truckId));
    }


    @PostMapping("/add/{truckId}")
    public ResponseEntity<ApiResponse> addItem(@AuthenticationPrincipal User user,
                                               @PathVariable Integer truckId,
                                               @RequestBody Item item) {
        itemService.addItemToFoodTruck(user.getId(), truckId, item);
        return ResponseEntity.ok(new ApiResponse("Item added to food truck"));
    }

    @PutMapping("/update/{truckId}/{itemId}")
    public ResponseEntity<ApiResponse> updateItem(@AuthenticationPrincipal User user,
                                                  @PathVariable Integer truckId,
                                                  @PathVariable Integer itemId,
                                                  @RequestBody Item item) {
        itemService.updateItemInFoodTruck(user.getId(), truckId, itemId, item);
        return ResponseEntity.ok(new ApiResponse("Item updated"));
    }

    @DeleteMapping("/delete/{truckId}/{itemId}")
    public ResponseEntity<ApiResponse> deleteItem(@AuthenticationPrincipal User user,
                                                  @PathVariable Integer truckId,
                                                  @PathVariable Integer itemId) {
        itemService.deleteItemFromFoodTruck(user.getId(), truckId, itemId);
        return ResponseEntity.ok(new ApiResponse("Item deleted"));
    }

    // عمليات إضافية
    @PutMapping("/setAvailable/{truckId}/{itemId}")
    public ResponseEntity<?> setAvailable(@AuthenticationPrincipal User user,
                                          @PathVariable Integer truckId,
                                          @PathVariable Integer itemId) {
        itemService.setAvailable(user.getId(), truckId , itemId);
        return ResponseEntity.status(200).body( new ApiResponse("Item availability updated ,it been true"));
    }

    @PutMapping("/setNotAvailable/{truckId}/{itemId}")
    public ResponseEntity<?> setNotAvailable(@AuthenticationPrincipal User user,
                                             @PathVariable Integer truckId,
                                             @PathVariable Integer itemId) {
        itemService.setNotAvailable(user.getId(), truckId , itemId);
        return ResponseEntity.status(200).body( new ApiResponse("Item availability updated , it been false"));
    }

    @PutMapping("/price/{truckId}/{itemId}/{newPrice}")
    public ResponseEntity<ApiResponse> updatePrice(@AuthenticationPrincipal User user,
                                                   @PathVariable Integer truckId,
                                                   @PathVariable Integer itemId,
                                                   @PathVariable Double newPrice) {
        itemService.updatePrice(user.getId(), truckId, itemId, newPrice);
        return ResponseEntity.ok(new ApiResponse("Item price updated"));
    }



    @GetMapping("/filterByPrice/{truckId}/{min}/{max}")
    public ResponseEntity<List<Item>> getItemsByPriceRange(@AuthenticationPrincipal User user,
                                                           @PathVariable Integer truckId,
                                                           @PathVariable Double min,
                                                           @PathVariable Double max) {
        return ResponseEntity.ok(itemService.getItemsByPriceRangeForClient(user.getId(), truckId, min, max));
    }


    @PostMapping(value = "/image/{truckId}/{itemId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadItemImage(@AuthenticationPrincipal User user, @PathVariable Integer truckId, @PathVariable Integer itemId, @RequestPart("file") MultipartFile file) {
        String url = itemService.uploadItemImage(user.getId(), truckId, itemId, file);
        return ResponseEntity.ok(Map.of("imageUrl", url));
    }




}
