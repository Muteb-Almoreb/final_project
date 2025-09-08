package org.example.trucksy.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiResponse;
import org.example.trucksy.DTO.FoodTruckDTO;
import org.example.trucksy.DTO.LocationDTO;
import org.example.trucksy.DTOOut.NearbyTruckResponse;
import org.example.trucksy.Model.FoodTruck;
import org.example.trucksy.Model.User;
import org.example.trucksy.Service.FoodTruckService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/foodTruck")
@RequiredArgsConstructor
public class FoodTruckController {
    private final FoodTruckService foodTruckService;


    @PostMapping("/add")
    public ResponseEntity<?> addFoodTruck(@AuthenticationPrincipal User user, @Valid @RequestBody FoodTruckDTO foodTruckDTO) {
        foodTruckService.addFoodTruck(user.getId(), foodTruckDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Successfully added food truck"));
    }

    @PutMapping("/update/{truck_id}")
    public ResponseEntity<?> updateFoodTruck(@AuthenticationPrincipal User user , @PathVariable Integer truck_id , @Valid @RequestBody FoodTruckDTO foodTruckDTO) {
        foodTruckService.updateFoodTruck(user.getId(), truck_id , foodTruckDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Successfully updated food truck"));
    }

    @DeleteMapping("/delete/{truck_id}")
    public ResponseEntity<?> deleteFoodTruck(@AuthenticationPrincipal User user, @PathVariable Integer truck_id) {
        foodTruckService.deleteFoodTruck(user.getId(), truck_id);
        return ResponseEntity.status(200).body(new ApiResponse("Successfully deleted food truck"));
    }


    @GetMapping("/get-all-trucks-by-owner_id")
    public ResponseEntity<?> getAllFoodTrucksByOwnerId(@AuthenticationPrincipal User user) {
        return ResponseEntity.status(200).body(foodTruckService.getAllFoodTrucksByOwnerId(user.getId()));
    }

    @GetMapping("/get-foodTrucks-by-category/{category}")
    public ResponseEntity<?> getAllFoodTrucksByCategory(@PathVariable String category) {
        return ResponseEntity.status(200).body(foodTruckService.getAllFoodTruckByCategory(category));
    }

    // todo transfer this to client
    @GetMapping("/get-nearest")
    public ResponseEntity<?> topNearest(@AuthenticationPrincipal User user, @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.status(200).body(foodTruckService.findTopNearestTrucks(user.getId(), limit));
    }

    @PutMapping("/update-food-truck-location/{foodTruck_id}")
    public ResponseEntity<?> updateFoodTruckLocation(@AuthenticationPrincipal User user ,@PathVariable Integer foodTruck_id , @Valid @RequestBody LocationDTO locationDTO) {
        foodTruckService.updateFoodTruckLocation(user.getId() ,foodTruck_id , locationDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Location updated Successfully"));
    }

    @PutMapping("/open-foodTruck/{foodTruck_id}")
    public ResponseEntity<?> openFoodTruck(@AuthenticationPrincipal User user, @PathVariable Integer foodTruck_id) {
        foodTruckService.openFoodTruck(user.getId() , foodTruck_id);
        return ResponseEntity.status(200).body(new ApiResponse("Your food truck has been opened"));
    }

    @PutMapping("/close-foodTruck/{foodTruck_id}")
    public ResponseEntity<?> closeFoodTruck(@AuthenticationPrincipal User user, @PathVariable Integer foodTruck_id) {
        foodTruckService.closeFoodTruck(user.getId(), foodTruck_id);
        return ResponseEntity.status(200).body(new ApiResponse("Your food truck has been closed"));
    }

    @PostMapping(value = "/upload-image/{truck_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> uploadTruckImage(@AuthenticationPrincipal User user, @PathVariable Integer truck_id, @RequestPart("file") MultipartFile file) {
        String url = foodTruckService.uploadTruckImage(user.getId(), truck_id, file);
        return ResponseEntity.ok(Map.of("imageUrl", url));
    }

}
