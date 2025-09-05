package org.example.trucksy.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiResponse;
import org.example.trucksy.DTOOut.TruckReviewsDTOOut;
import org.example.trucksy.Service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/add/{client_id}/{foodTruck_id}")
    public ResponseEntity<?> assignReview(@PathVariable Integer client_id, @PathVariable Integer foodTruck_id , @Valid@RequestBody TruckReviewsDTOOut truckReviewsDTOOut) {
        reviewService.assignReview(client_id,foodTruck_id , truckReviewsDTOOut);
        return ResponseEntity.status(200).body(new ApiResponse("Review Assigned"));
    }

    @GetMapping("/get-reviews-by-truck/{foodTruck_id}")
    public ResponseEntity<?> getReviewsByFoodTruck(@PathVariable Integer foodTruck_id) {
        return ResponseEntity.status(200).body(reviewService.getReviewsByFoodTruck(foodTruck_id));
    }

    @GetMapping("/get-reviews-by-client/{client_id}")
    public ResponseEntity<?> getReviewsByClient(@PathVariable Integer client_id) {
        return ResponseEntity.status(200).body(reviewService.getReviewsByClient(client_id));
    }

    @GetMapping("/get-truck-rating/{foodTruck_id}")
    public ResponseEntity<?> getAverageRatingForTruck(@PathVariable Integer foodTruck_id){
        return ResponseEntity.status(200).body(reviewService.getAverageRatingForTruck(foodTruck_id));
    }
}
