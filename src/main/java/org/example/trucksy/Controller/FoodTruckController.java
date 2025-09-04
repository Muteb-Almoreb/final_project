package org.example.trucksy.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiResponse;
import org.example.trucksy.DTO.FoodTruckDTO;
import org.example.trucksy.Model.FoodTruck;
import org.example.trucksy.Service.FoodTruckService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/foodTruck")
@RequiredArgsConstructor
public class FoodTruckController {
    private final FoodTruckService foodTruckService;


    @PostMapping("/add/{id}")
    public ResponseEntity<?> addFoodTruck(@PathVariable Integer id , @Valid @RequestBody FoodTruckDTO foodTruckDTO) {
        foodTruckService.addFoodTruck(id, foodTruckDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Successfully added food truck"));
    }

    @PutMapping("/update/{owner_id}/{truck_id}")
    public ResponseEntity<?> updateFoodTruck(@PathVariable Integer owner_id , @PathVariable Integer truck_id , @Valid @RequestBody FoodTruckDTO foodTruckDTO) {
        foodTruckService.updateFoodTruck(owner_id , truck_id , foodTruckDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Successfully updated food truck"));
    }

    @DeleteMapping("/delete/{owner_id}/{truck_id}")
    public ResponseEntity<?> deleteFoodTruck(@PathVariable Integer owner_id , @PathVariable Integer truck_id) {
        foodTruckService.deleteFoodTruck(owner_id , truck_id);
        return ResponseEntity.status(200).body(new ApiResponse("Successfully deleted food truck"));
    }
}
