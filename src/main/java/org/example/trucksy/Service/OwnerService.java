package org.example.trucksy.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiException;
import org.example.trucksy.DTO.OwnerDTO;
import org.example.trucksy.Model.FoodTruck;
import org.example.trucksy.Model.Owner;
import org.example.trucksy.Model.User;
import org.example.trucksy.Repository.AuthRepository;
import org.example.trucksy.Repository.FoodTruckRepository;
import org.example.trucksy.Repository.OwnerRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final FoodTruckRepository foodTruckRepository;
    private final AuthRepository authRepository;
    private final FoodTruckService foodTruckService;


    public void registerOwner(OwnerDTO ownerDTO) { // todo
        User user = new User();
        user.setUsername(ownerDTO.getUsername());
        user.setPassword(ownerDTO.getPassword());
        user.setEmail(ownerDTO.getEmail());
        user.setPhoneNumber(ownerDTO.getPhone());
        user.setRole("OWNER");

        Owner owner = new Owner();
        owner.setSubscribed(false);
        owner.setUser(user);
        ownerRepository.save(owner);

        FoodTruck foodTruck = new FoodTruck();
        foodTruck.setName(ownerDTO.getName());
        foodTruck.setDescription(ownerDTO.getDescription());
        foodTruck.setCategory(ownerDTO.getCategory());
        foodTruck.setStatus("CLOSED");
        foodTruck.setOwner(owner);
        foodTruckRepository.save(foodTruck);
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
