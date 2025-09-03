package org.example.trucksy.Service;

import lombok.RequiredArgsConstructor;
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

//    public void registerOwner(OwnerDTO OwnerDTO) { // todo
//        User user = new User();
//        user.setUsername(OwnerDTO.getUsername());
//        user.setPassword(OwnerDTO.getPassword());
//        user.setEmail(OwnerDTO.getEmail());
//        user.setPhoneNumber(OwnerDTO.getPhone());
//        user.setRole("Owner");
//        authRepository.save(user);
//
//        Owner owner = new Owner();
//        owner.setUser(user);
//        ownerRepository.save(owner);
//
//
//        FoodTruck foodTruck = new FoodTruck();
//        foodTruck.setName(OwnerDTO.getName());
//        foodTruck.setDescription(OwnerDTO.getDescription());
//        foodTruck.setCategory(OwnerDTO.getCategory());
//        foodTruck.setStatus("Closed");
//        foodTruck.setOwner(owner);
//        foodTruckRepository.save(foodTruck);
//    }
}
