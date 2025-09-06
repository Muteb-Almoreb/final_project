package org.example.trucksy.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiException;
import org.example.trucksy.DTO.FoodTruckDTO;
import org.example.trucksy.DTO.GeocodeResult;
import org.example.trucksy.DTO.LocationDTO;
import org.example.trucksy.DTOOut.NearbyTruckResponse;
import org.example.trucksy.Model.Client;
import org.example.trucksy.Model.FoodTruck;
import org.example.trucksy.Model.Owner;
import org.example.trucksy.Repository.ClientRepository;
import org.example.trucksy.Repository.FoodTruckRepository;
import org.example.trucksy.Repository.OwnerRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FoodTruckService {
    private final FoodTruckRepository foodTruckRepository;
    private final OwnerRepository ownerRepository;
    private final HereGeocodingService hereGeocodingService;
    private final ClientRepository clientRepository;
    private final DistanceService distanceService;

    public void addFoodTruck(Integer owner_id ,FoodTruckDTO foodTruckDTO) {
        Owner owner = ownerRepository.findOwnerById(owner_id);
        if(owner == null){
            throw new ApiException("Owner not found");
        }
        FoodTruck foodTruck = new FoodTruck();
        GeocodeResult gr = hereGeocodingService.geocodeCityDistrict(foodTruckDTO.getCity(), foodTruckDTO.getDistrict(), "SAU");
        foodTruck.setLatitude(gr.lat());
        foodTruck.setLongitude(gr.lon());
        foodTruck.setName(foodTruckDTO.getName());
        foodTruck.setDescription(foodTruckDTO.getDescription());
        foodTruck.setCategory(foodTruckDTO.getCategory());
        foodTruck.setStatus("CLOSED");
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


    // Ex

    public List<FoodTruck> getAllFoodTrucksByOwnerId(Integer owner_id) {
        Owner owner = ownerRepository.findOwnerById(owner_id);
        if(owner == null){
            throw new ApiException("Owner not found");
        }
        return foodTruckRepository.findFoodTruckByOwnerId(owner_id);
    }


    public List<FoodTruckDTO> getAllFoodTruckByCategory(String category) {
        List<FoodTruck> foodTrucks = foodTruckRepository.findFoodTruckByCategory(category);

        return foodTrucks.stream()
                .map(r -> new FoodTruckDTO(
                        r.getName(),
                        r.getDescription(),
                        r.getCategory(),
                        r.getCity(),
                        r.getDistrict(),
                        r.getLatitude(),//ماتحتاجها بس حطيتها عشان اشيل الerror
                        r.getLongitude()//ماتحتاجها بس حطيتها عشان اشيل الerror
                )).toList();
    }


    public List<NearbyTruckResponse> findTopNearestTrucks(Integer clientId, int limit) {
        Client client = clientRepository.findClientById(clientId);
        if (client == null) throw new ApiException("Client not found");
        if (client.getLatitude() == null || client.getLongitude() == null)
            throw new ApiException("Client location not set");

        double cLat = client.getLatitude();
        double cLon = client.getLongitude();

        // todo if we want to get just open truck
        List<FoodTruck> trucks = foodTruckRepository.findAll();

        return trucks.stream()
                .map(ft -> new NearbyTruckResponse(
                        ft.getId(), ft.getName(), ft.getDescription(), ft.getCategory(),
                        ft.getLatitude(), ft.getLongitude(),
                        distanceService.km(cLat, cLon, ft.getLatitude(), ft.getLongitude())
                ))
                .sorted(Comparator.comparingDouble(NearbyTruckResponse::distanceKm))
                .limit(Math.max(1, limit))
                .toList();
    }


    // this method to foodTruck Owner if he went to change his truck location
    @Transactional
    public void updateFoodTruckLocation(Integer foodTruck_id, LocationDTO locationDTO) {
        FoodTruck foodTruck = foodTruckRepository.findFoodTruckById(foodTruck_id);
        if(foodTruck == null) {
            throw new ApiException("food Truck not found");
        }
        GeocodeResult gr = hereGeocodingService.geocodeCityDistrict(locationDTO.getCity(), locationDTO.getDistrict(), "SAU");
        foodTruck.setLatitude(gr.lat());
        foodTruck.setLongitude(gr.lon());
        foodTruckRepository.save(foodTruck);
    }

}
