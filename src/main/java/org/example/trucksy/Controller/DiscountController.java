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

    @GetMapping("/get")
    public List<Discount> getAllDiscounts() {
        return discountService.getAllDiscounts();
    }

    @GetMapping("/getBy/{ItemId}")
    public ResponseEntity<?> getDiscountById(@PathVariable Integer ItemId) {
        return ResponseEntity.status(200).body(discountService.getDiscountByItemId(ItemId));
    }

    @PostMapping("/add/{itemId}")
    public ResponseEntity<?> addDiscount(@PathVariable Integer itemId, @RequestBody Discount discount) {
        discountService.addDiscount(itemId, discount);
        return ResponseEntity.status(200).body (new ApiResponse("Discount added successfully"));
    }

    @PutMapping("/update/{discountId}")
    public ResponseEntity<?> updateDiscount(@PathVariable Integer discountId, @RequestBody Discount discount) {
        discountService.updateDiscount(discountId, discount);
        return ResponseEntity.status(200).body (new ApiResponse("Discount updated successfully"));
    }

    @DeleteMapping("/delete/{discountId}")
    public ResponseEntity<?> deleteDiscount(@PathVariable Integer discountId) {
        discountService.deleteDiscount(discountId);
        return ResponseEntity.status(200).body (new ApiResponse("Discount deleted successfully"));
    }


    @PutMapping("/activate/{discountId}")
    public ResponseEntity<?> activateDiscount(@PathVariable Integer discountId) {
        discountService.activateDiscount(discountId);
        return ResponseEntity.status(200).body (new ApiResponse("Discount activated"));
    }

    @PutMapping("/deactivate/{discountId}")
    public ResponseEntity<?> deactivateDiscount(@PathVariable Integer discountId) {
        discountService.deactivateDiscount(discountId);
        return ResponseEntity.status(200).body ( new ApiResponse("Discount deactivated"));
    }
}

