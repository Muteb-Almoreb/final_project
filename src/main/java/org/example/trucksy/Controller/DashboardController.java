package org.example.trucksy.Controller;

import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiResponse;
import org.example.trucksy.DTOOut.DashBoardAnalyzerDtoOut;
import org.example.trucksy.DTOOut.ReviewAnalyzerDtoOut;
import org.example.trucksy.Model.User;
import org.example.trucksy.Service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @PutMapping("/refresh-dashboard")
    public ResponseEntity<?> refreshDashboard(@AuthenticationPrincipal User user) {
        dashboardService.refreshDashboard(user.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Dashboard refreshed successfully"));
    }


    @GetMapping("/get-owner-dashboard")
    public ResponseEntity<?> getOwnerDashboard(@AuthenticationPrincipal User user) {
        return ResponseEntity.status(200).body(dashboardService.getOwnerDashboard(user.getId()));
    }

    @GetMapping("/get-all-order-by-foodTruck/{foodTruck_id}")
    public ResponseEntity<?> getAllOrderByFoodTruck(@PathVariable Integer foodTruck_id){
        return ResponseEntity.status(200).body(dashboardService.getOrdersByFoodTruck(foodTruck_id));
    }
    @GetMapping("/analyze-reviews/{foodTruckId}")
    public ResponseEntity<ReviewAnalyzerDtoOut> analyzeReviews(@AuthenticationPrincipal User user ,@PathVariable Integer foodTruckId) {
        return dashboardService.reviewAnalyzer(user.getId(), foodTruckId);
    }

    @GetMapping("/analyze-dashboard/{ownerId}")
    public ResponseEntity<DashBoardAnalyzerDtoOut> analyzeDashboard(@PathVariable Integer ownerId) {
        return dashboardService.analyzeDashboard(ownerId);
    }
}