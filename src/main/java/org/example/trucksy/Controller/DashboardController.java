package org.example.trucksy.Controller;

import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiResponse;
import org.example.trucksy.Service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @PutMapping("/refresh-dashboard/{owner_id}")
    public ResponseEntity<?> refreshDashboard(@PathVariable Integer owner_id){
        dashboardService.refreshDashboard(owner_id);
        return ResponseEntity.status(200).body(new ApiResponse("Dashboard refreshed successfully"));
    }


    @GetMapping("/get-owner-dashboard/{owner_id}")
    public ResponseEntity<?> getOwnerDashboard(@PathVariable Integer owner_id){
        return ResponseEntity.status(200).body(dashboardService.getOwnerDashboard(owner_id));
    }
}
