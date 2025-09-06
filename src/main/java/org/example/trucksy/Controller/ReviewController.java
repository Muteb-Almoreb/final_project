package org.example.trucksy.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiResponse;
import org.example.trucksy.DTOOut.TruckReviewsDTOOut;
import org.example.trucksy.Model.User;
import org.example.trucksy.Service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/add/{foodTruck_id}")
    public ResponseEntity<?> assignReview(@AuthenticationPrincipal User user, @PathVariable Integer foodTruck_id , @Valid@RequestBody TruckReviewsDTOOut truckReviewsDTOOut) {
        reviewService.assignReview(user.getId(), foodTruck_id , truckReviewsDTOOut);
        return ResponseEntity.status(200).body(new ApiResponse("Review Assigned"));
    }

    @GetMapping("/get-reviews-by-truck/{foodTruck_id}")
    public ResponseEntity<?> getReviewsByFoodTruck(@PathVariable Integer foodTruck_id) {
        return ResponseEntity.status(200).body(reviewService.getReviewsByFoodTruck(foodTruck_id));
    }

    @GetMapping("/get-reviews-by-client")
    public ResponseEntity<?> getReviewsByClient(@AuthenticationPrincipal User user) {
        return ResponseEntity.status(200).body(reviewService.getReviewsByClient(user.getId()));
    }

    @GetMapping("/get-truck-rating/{foodTruck_id}")
    public ResponseEntity<?> getAverageRatingForTruck(@PathVariable Integer foodTruck_id){
        return ResponseEntity.status(200).body(reviewService.getAverageRatingForTruck(foodTruck_id));
    }
}
