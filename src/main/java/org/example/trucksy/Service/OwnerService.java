package org.example.trucksy.Service;

import lombok.RequiredArgsConstructor;
import org.example.trucksy.DTO.OwnerDTO;
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

    public void registerOwner(OwnerDTO ownerDTO) {
        System.out.println(ownerDTO.getEmail());
        System.out.println(ownerDTO.getUsername());
        System.out.println(ownerDTO.getPassword());
        System.out.println(ownerDTO.getPhone());

        User user = new User();
        user.setUsername(ownerDTO.getUsername());
        user.setPassword(ownerDTO.getPassword());
        user.setEmail(ownerDTO.getEmail());
        user.setPhoneNumber(ownerDTO.getPhone());
        user.setRole("OWNER");

        Owner owner = new Owner();
        owner.setUser(user);

        ownerRepository.save(owner);

        //        FoodTruck foodTruck = new FoodTruck();
        //        foodTruck.setName(OwnerDTO.getName());
        //        foodTruck.setDescription(OwnerDTO.getDescription());
        //        foodTruck.setCategory(OwnerDTO.getCategory());
        //        foodTruck.setStatus("Closed");
        //        foodTruck.setOwner(owner);
        //        foodTruckRepository.save(foodTruck);
    }



}
