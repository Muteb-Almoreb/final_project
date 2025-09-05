package org.example.trucksy.Controller;

import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiResponse;
import org.example.trucksy.DTO.DiscountDTO;
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

    @GetMapping("/getBy/{id}")
    public ResponseEntity<?> getDiscountById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(discountService.getDiscountById(id));
    }

    @PostMapping("/add/{itemId}")
    public ResponseEntity<?> addDiscount(@PathVariable Integer itemId, @RequestBody DiscountDTO dto) {
        discountService.addDiscount(itemId, dto);
        return ResponseEntity.status(200).body (new ApiResponse("Discount added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateDiscount(@PathVariable Integer id, @RequestBody DiscountDTO dto) {
        discountService.updateDiscount(id, dto);
        return ResponseEntity.status(200).body (new ApiResponse("Discount updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDiscount(@PathVariable Integer id) {
        discountService.deleteDiscount(id);
        return ResponseEntity.status(200).body (new ApiResponse("Discount deleted successfully"));
    }


    @PutMapping("/activate/{id}")
    public ResponseEntity<?> activateDiscount(@PathVariable Integer id) {
        discountService.activateDiscount(id);
        return ResponseEntity.status(200).body (new ApiResponse("Discount activated"));
    }

    @PutMapping("/deactivate/{id}")
    public ResponseEntity<?> deactivateDiscount(@PathVariable Integer id) {
        discountService.deactivateDiscount(id);
        return ResponseEntity.status(200).body ( new ApiResponse("Discount deactivated"));
    }
}

