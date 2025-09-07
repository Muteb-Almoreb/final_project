package org.example.trucksy.Service;

import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiException;
import org.example.trucksy.Model.FoodTruck;
import org.example.trucksy.Model.User;
import org.example.trucksy.Repository.AuthRepository;
import org.example.trucksy.Repository.FoodTruckRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;
    private final FoodTruckRepository foodTruckRepository;

    // this is for admin

    public List<User> getAllUsers() {
        return authRepository.findAll();
    }

    public void deleteUser(Integer user_id) {
        User user = authRepository.findUserById(user_id);
        if (user == null) {
            throw new ApiException("User not found");
        }
        authRepository.deleteById(user_id);
    }


    public List<User> getAllOwners(){
        return authRepository.findAllUsersWithRoleOwner();
    }


    public List<User> getAllClients(){
        return authRepository.findAllUsersWithRoleClient();
    }

    public void deleteFoodTruck(Integer food_truck_id) {
        FoodTruck foodTruck = foodTruckRepository.findFoodTruckById(food_truck_id);
        if (foodTruck == null) {
            throw new ApiException("FoodTruck not found");
        }
        foodTruckRepository.deleteById(food_truck_id);
    }

}
