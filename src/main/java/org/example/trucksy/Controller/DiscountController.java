package org.example.trucksy.Controller;

import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiResponse;
import org.example.trucksy.Model.Discount;
import org.example.trucksy.Service.DiscountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/discount")
@RequiredArgsConstructor
public class DiscountController {


    private final DiscountService discountService;

    @GetMapping("/getAll/{ownerId}/{truckId}")
    public ResponseEntity<?> getAllDiscounts(@PathVariable Integer ownerId,
                                                          @PathVariable Integer truckId) {
        return ResponseEntity.ok(discountService.getAllDiscountsByTruck(ownerId, truckId));
    }




    // جلب الخصم المرتبط بآيتم معيّن
    @GetMapping("/get/{ownerId}/{truckId}/{itemId}")
    public ResponseEntity<?> getDiscountByItemId(@PathVariable Integer ownerId,
                                                 @PathVariable Integer truckId,
                                                 @PathVariable Integer itemId) {
        return ResponseEntity.ok(discountService.getDiscountByItemId(ownerId, truckId, itemId));
    }

    // إضافة خصم جديد لآيتم معيّن
    @PostMapping("/add/{ownerId}/{truckId}/{itemId}")
    public ResponseEntity<?> addDiscount(@PathVariable Integer ownerId,
                                                   @PathVariable Integer truckId,
                                                   @PathVariable Integer itemId,
                                                   @RequestBody Discount discount) {
        discountService.addDiscount(ownerId, truckId, itemId, discount);
        return ResponseEntity.ok(new ApiResponse("Discount added successfully"));
    }

    // تحديث خصم
    @PutMapping("/update/{ownerId}/{truckId}/{discountId}")
    public ResponseEntity<?> updateDiscount(@PathVariable Integer ownerId,
                                                      @PathVariable Integer truckId,
                                                      @PathVariable Integer discountId,
                                                      @RequestBody Discount discount) {
        discountService.updateDiscount(ownerId, truckId, discountId, discount);
        return ResponseEntity.ok(new ApiResponse("Discount updated successfully"));
    }

    // حذف خصم
    @DeleteMapping("/delete/{ownerId}/{truckId}/{discountId}")
    public ResponseEntity<?> deleteDiscount(@PathVariable Integer ownerId,
                                                      @PathVariable Integer truckId,
                                                      @PathVariable Integer discountId) {
        discountService.deleteDiscount(ownerId, truckId, discountId);
        return ResponseEntity.ok(new ApiResponse("Discount deleted successfully"));
    }

    // تفعيل خصم
    @PutMapping("/activate/{ownerId}/{truckId}/{discountId}")
    public ResponseEntity<?> activateDiscount(@PathVariable Integer ownerId,
                                                        @PathVariable Integer truckId,
                                                        @PathVariable Integer discountId) {
        discountService.activateDiscount(ownerId, truckId, discountId);
        return ResponseEntity.ok(new ApiResponse("Discount activated"));
    }

    // تعطيل خصم
    @PutMapping("/deactivate/{ownerId}/{truckId}/{discountId}")
    public ResponseEntity<?> deactivateDiscount(@PathVariable Integer ownerId,
                                                          @PathVariable Integer truckId,
                                                          @PathVariable Integer discountId) {
        discountService.deactivateDiscount(ownerId, truckId, discountId);
        return ResponseEntity.ok(new ApiResponse("Discount deactivated"));
    }
}

