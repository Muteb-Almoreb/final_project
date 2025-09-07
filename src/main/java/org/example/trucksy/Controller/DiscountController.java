package org.example.trucksy.Controller;

import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiResponse;
import org.example.trucksy.Model.Discount;
import org.example.trucksy.Model.User;
import org.example.trucksy.Service.DiscountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/discount")
@RequiredArgsConstructor
public class DiscountController {


    private final DiscountService discountService;

    @GetMapping("/getAll/{truckId}")
    public ResponseEntity<?> getAllDiscounts(@AuthenticationPrincipal User user,
                                                          @PathVariable Integer truckId) {
        return ResponseEntity.ok(discountService.getAllDiscountsByTruck(user.getId(), truckId));
    }




    // جلب الخصم المرتبط بآيتم معيّن
    @GetMapping("/get/{truckId}/{itemId}")
    public ResponseEntity<?> getDiscountByItemId(@AuthenticationPrincipal User user,
                                                 @PathVariable Integer truckId,
                                                 @PathVariable Integer itemId) {
        return ResponseEntity.ok(discountService.getDiscountByItemId(user.getId(), truckId, itemId));
    }

    // إضافة خصم جديد لآيتم معيّن
    @PostMapping("/add/{truckId}/{itemId}")
    public ResponseEntity<?> addDiscount(@AuthenticationPrincipal User user,
                                                   @PathVariable Integer truckId,
                                                   @PathVariable Integer itemId,
                                                   @RequestBody Discount discount) {
        discountService.addDiscount(user.getId(), truckId, itemId, discount);
        return ResponseEntity.ok(new ApiResponse("Discount added successfully"));
    }

    // تحديث خصم
    @PutMapping("/update/{truckId}/{discountId}")
    public ResponseEntity<?> updateDiscount(@AuthenticationPrincipal User user,
                                                      @PathVariable Integer truckId,
                                                      @PathVariable Integer discountId,
                                                      @RequestBody Discount discount) {
        discountService.updateDiscount(user.getId(), truckId, discountId, discount);
        return ResponseEntity.ok(new ApiResponse("Discount updated successfully"));
    }

    // حذف خصم
    @DeleteMapping("/delete/{truckId}/{discountId}")
    public ResponseEntity<?> deleteDiscount(@AuthenticationPrincipal User user,
                                                      @PathVariable Integer truckId,
                                                      @PathVariable Integer discountId) {
        discountService.deleteDiscount(user.getId(), truckId, discountId);
        return ResponseEntity.ok(new ApiResponse("Discount deleted successfully"));
    }

    // تفعيل خصم
    @PutMapping("/activate/{truckId}/{discountId}")
    public ResponseEntity<?> activateDiscount(@AuthenticationPrincipal User user,
                                                        @PathVariable Integer truckId,
                                                        @PathVariable Integer discountId) {
        discountService.activateDiscount(user.getId(), truckId, discountId);
        return ResponseEntity.ok(new ApiResponse("Discount activated"));
    }

    // تعطيل خصم
    @PutMapping("/deactivate/{truckId}/{discountId}")
    public ResponseEntity<?> deactivateDiscount(@AuthenticationPrincipal User user,
                                                          @PathVariable Integer truckId,
                                                          @PathVariable Integer discountId) {
        discountService.deactivateDiscount(user.getId(), truckId, discountId);
        return ResponseEntity.ok(new ApiResponse("Discount deactivated"));
    }
}

