package org.example.trucksy.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiException;
import org.example.trucksy.DTO.OwnerDTO;
import org.example.trucksy.Model.Dashboard;
import org.example.trucksy.Model.FoodTruck;
import org.example.trucksy.Model.Owner;
import org.example.trucksy.Model.User;
import org.example.trucksy.Repository.AuthRepository;
import org.example.trucksy.Repository.DashboardRepository;
import org.example.trucksy.Repository.FoodTruckRepository;
import org.example.trucksy.Repository.OwnerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final FoodTruckRepository foodTruckRepository;
    private final AuthRepository authRepository;
    private final FoodTruckService foodTruckService;
    private final DashboardService dashboardService;
    private final DashboardRepository dashboardRepository;


    public void registerOwner(OwnerDTO ownerDTO) { // todo
        User user = new User();
        user.setUsername(ownerDTO.getUsername());
        user.setPassword(ownerDTO.getPassword());
        user.setEmail(ownerDTO.getEmail());
        user.setPhoneNumber(ownerDTO.getPhone());
        user.setRole("OWNER");
        authRepository.save(user);

        Owner owner = new Owner();
        owner.setSubscribed(false);
        owner.setUser(user);
        ownerRepository.save(owner);

        // this is to create dashboard for owner
        Dashboard dash = new Dashboard();
        dash.setOwner(owner);
        dash.setTotalOrders(0);
        dash.setTotalRevenue(0.0);
        dash.setPredictedOrders(0);
        dash.setPeakOrders("N/A");
        dash.setTopSellingItems(null);
        dash.setUpdateDate(LocalDate.now());
        owner.setDashboard(dash);
        dashboardRepository.save(dash);
    }


    public void updateOwner(Integer id ,OwnerDTO ownerDTO) {
        Owner owner = ownerRepository.findOwnerById(id);
        if (owner == null) {
            throw new ApiException("Owner not found");
        }
        User user = owner.getUser();
        user.setUsername(ownerDTO.getUsername());
        user.setPassword(ownerDTO.getPassword());
        user.setEmail(ownerDTO.getEmail());
        user.setPhoneNumber(ownerDTO.getPhone());
        ownerRepository.save(owner);
    }


    public void deleteOwner(Integer id) {
        Owner owner = ownerRepository.findOwnerById(id);
        if (owner == null) {
            throw new ApiException("Owner not found");
        }
        authRepository.delete(owner.getUser());
    }
}
