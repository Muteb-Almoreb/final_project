package org.example.trucksy.Service;

import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiException;
import org.example.trucksy.DTOOut.OwnerDashboardDTO;
import org.example.trucksy.Model.Dashboard;
import org.example.trucksy.Model.Owner;
import org.example.trucksy.Repository.DashboardRepository;
import org.example.trucksy.Repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardRepository dashboardRepository;
    private final OrderRepository orderRepository;



    public void refreshDashboard(Integer owner_id) {
        Dashboard dash = dashboardRepository.findDashboardById(owner_id);
        long total = orderRepository.countOrdersByOwner(owner_id);
        Double revenue = orderRepository.sumRevenueByOwner(owner_id);
//        String peak = computePeakHourLabel(ownerUserId);
//        String topItems = computeTopItems(ownerUserId);

        dash.setTotalOrders((int) total);
        dash.setTotalRevenue(revenue != null ? revenue : 0.0);
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
                dash.getPredictedOrders(),
                dash.getPeakOrders(),
                dash.getTopSellingItems(),
                dash.getUpdateDate()
        );
    }



}
