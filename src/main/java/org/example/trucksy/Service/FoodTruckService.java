package org.example.trucksy.Service;

import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiException;
import org.example.trucksy.DTO.FoodTruckDTO;
import org.example.trucksy.Model.FoodTruck;
import org.example.trucksy.Model.Owner;
import org.example.trucksy.Repository.FoodTruckRepository;
import org.example.trucksy.Repository.OwnerRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FoodTruckService {
    private final FoodTruckRepository foodTruckRepository;
    private final OwnerRepository ownerRepository;

    public void addFoodTruck(Integer owner_id ,FoodTruckDTO foodTruckDTO) {
        Owner owner = ownerRepository.findOwnerById(owner_id);
        if(owner == null){
            throw new ApiException("Owner not found");
        }
        FoodTruck foodTruck = new FoodTruck();
        foodTruck.setName(foodTruckDTO.getName());
        foodTruck.setDescription(foodTruckDTO.getDescription());
        foodTruck.setCategory(foodTruckDTO.getCategory());
        foodTruck.setLatitude(foodTruckDTO.getLatitude());
        foodTruck.setLongitude(foodTruckDTO.getLongitude());
        foodTruck.setOwner(owner);
        foodTruckRepository.save(foodTruck);
    }


    public void updateFoodTruck(Integer owner_id , Integer id ,FoodTruckDTO foodTruckDTO) {
        Owner owner = ownerRepository.findOwnerById(owner_id);
        if(owner == null){
            throw new ApiException("Owner not found");
        }
        FoodTruck foodTruck = foodTruckRepository.findFoodTruckById(id);
        if(foodTruck == null){
            throw new ApiException("FoodTruck not found");
        }
        if (!Objects.equals(foodTruck.getOwner().getId(), owner.getId())) {
            throw new ApiException("You don't own this food truck");
        }
        foodTruck.setName(foodTruckDTO.getName());
        foodTruck.setDescription(foodTruckDTO.getDescription());
        foodTruck.setCategory(foodTruckDTO.getCategory());
        foodTruck.setLatitude(foodTruckDTO.getLatitude());
        foodTruck.setLongitude(foodTruckDTO.getLongitude());
        foodTruckRepository.save(foodTruck);
    }

    public void deleteFoodTruck(Integer owner_id , Integer id) {
        Owner owner = ownerRepository.findOwnerById(owner_id);
        if(owner == null){
            throw new ApiException("Owner not found");
        }
        FoodTruck foodTruck = foodTruckRepository.findFoodTruckById(id);
        if(foodTruck == null){
            throw new ApiException("FoodTruck not found");
        }
        if (!Objects.equals(foodTruck.getOwner().getId(), owner.getId())) {
            throw new ApiException("You don't own this food truck");
        }
        foodTruckRepository.delete(foodTruck);
    }
}
