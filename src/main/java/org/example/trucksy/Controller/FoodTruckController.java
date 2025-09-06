package org.example.trucksy.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiResponse;
import org.example.trucksy.DTO.FoodTruckDTO;
import org.example.trucksy.DTO.LocationDTO;
import org.example.trucksy.DTOOut.NearbyTruckResponse;
import org.example.trucksy.Model.FoodTruck;
import org.example.trucksy.Service.FoodTruckService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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


    @GetMapping("/get-all-trucks-by-owner_id/{owner_id}")
    public ResponseEntity<?> getAllFoodTrucksByOwnerId(@PathVariable Integer owner_id) {
        return ResponseEntity.status(200).body(foodTruckService.getAllFoodTrucksByOwnerId(owner_id));
    }

    @GetMapping("/get-foodTrucks-by-category/{category}")
    public ResponseEntity<?> getAllFoodTrucksByCategory(@PathVariable String category) {
        return ResponseEntity.status(200).body(foodTruckService.getAllFoodTruckByCategory(category));
    }

    @GetMapping("/get-nearest/{client_id}")
    public ResponseEntity<?> topNearest(@PathVariable Integer client_id, @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.status(200).body(foodTruckService.findTopNearestTrucks(client_id, limit));
    }

    @PutMapping("/update-food-truck-location/{foodTruck_id}")
    public ResponseEntity<?> updateFoodTruckLocation(@PathVariable Integer foodTruck_id , @Valid @RequestBody LocationDTO locationDTO) {
        foodTruckService.updateFoodTruckLocation(foodTruck_id , locationDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Location updated Successfully"));
    }

    @PutMapping("/open-foodTruck/{owner_id}/{foodTruck_id}")
    public ResponseEntity<?> openFoodTruck(@PathVariable Integer owner_id, @PathVariable Integer foodTruck_id) {
        foodTruckService.openFoodTruck(owner_id , foodTruck_id);
        return ResponseEntity.status(200).body(new ApiResponse("Your food truck has been opened"));
    }

    @PutMapping("/close-foodTruck/{owner_id}/{foodTruck_id}")
    public ResponseEntity<?> closeFoodTruck(@PathVariable Integer owner_id, @PathVariable Integer foodTruck_id) {
        foodTruckService.closeFoodTruck(owner_id , foodTruck_id);
        return ResponseEntity.status(200).body(new ApiResponse("Your food truck has been closed"));
    }
}
