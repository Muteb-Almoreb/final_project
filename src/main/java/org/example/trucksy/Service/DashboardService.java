package org.example.trucksy.Service;

import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiException;
import org.example.trucksy.DTOOut.OrderDashboardDTOOut;
import org.example.trucksy.DTOOut.OwnerDashboardDTO;
import org.example.trucksy.DTOOut.ReviewAnalyzerDtoOut;
import org.example.trucksy.Model.Dashboard;
import org.example.trucksy.Model.Order;
import org.example.trucksy.Model.Owner;
import org.example.trucksy.Repository.DashboardRepository;
import org.example.trucksy.Repository.OrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardRepository dashboardRepository;
    private final OrderRepository orderRepository;
    private final AiReviewAnalyzerService aiReviewAnalyzerService;


    public void refreshDashboard(Integer owner_id) {
        Dashboard dash = dashboardRepository.findDashboardById(owner_id);
        Integer total = orderRepository.countOrdersByOwner(owner_id);
        Integer totalCompletedOrder = orderRepository.countCompletedOrdersByOwner(owner_id);
        Double revenue = orderRepository.sumRevenueByOwner(owner_id);
//        String peak = computePeakHourLabel(ownerUserId);
//        String topItems = computeTopItems(ownerUserId);

        dash.setTotalOrders((int) total);
        dash.setTotalRevenue(revenue != null ? revenue : 0.0);
        dash.setTotalCompletedOrders((int) totalCompletedOrder);
//        dash.setPredictedOrders(predict(total));
//        dash.setPeakOrders(peak);
//        dash.setTopSellingItems(topItems);

        dashboardRepository.save(dash);
    }


    public OwnerDashboardDTO getOwnerDashboard(Integer ownerId) {
        Dashboard dash = dashboardRepository.findDashboardById(ownerId);
        if (dash == null) {
            throw new ApiException("Dashboard not found");
        }

        //this from method refresh
        refreshDashboard(ownerId);

        Owner owner = dash.getOwner();

        return new OwnerDashboardDTO(
                owner.getUser().getUsername(),
                dash.getTotalOrders(),
                dash.getTotalRevenue(),
                dash.getTotalCompletedOrders(),
                dash.getPredictedOrders(),
                dash.getPeakOrders(),
                dash.getTopSellingItems(),
                dash.getUpdateDate()
        );
    }


    public List<OrderDashboardDTOOut> getOrdersByFoodTruck(Integer foodTruckId) {
        List<Order> orders = orderRepository.findByFoodTruck_IdOrderByIdDesc(foodTruckId);

        return orders.stream()
                .map(r -> new OrderDashboardDTOOut(
                        r.getId(),
                        r.getOrderDate(),
                        r.getClient().getUser().getUsername(),
                        r.getTotalPrice(),
                        r.getStatus()
                )).toList();
    }

    public ResponseEntity<ReviewAnalyzerDtoOut> reviewAnalyzer(Integer ownerId, Integer foodTruckId) {
        try {
            ReviewAnalyzerDtoOut analysis = aiReviewAnalyzerService.analyzeReviewsByFoodTruckId(ownerId, foodTruckId);
            return ResponseEntity.ok(analysis);
        } catch (ApiException e) {
            throw e; // Re-throw API exceptions as-is
        } catch (Exception e) {
            throw new ApiException("Failed to analyze reviews: " + e.getMessage());
        }
    }
}
