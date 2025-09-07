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
import org.springframework.web.multipart.MultipartFile;

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
    private final StorageService storage;

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
        FoodTruck foodTruck = mustOwnTruck(owner_id, id);
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
                        r.getLongitude(),//ماتحتاجها بس حطيتها عشان اشيل الerror
                        r.getImageUrl()
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
                        distanceService.km(cLat, cLon, ft.getLatitude(), ft.getLongitude()) ,ft.getImageUrl()
                ))
                .sorted(Comparator.comparingDouble(NearbyTruckResponse::distanceKm))
                .limit(Math.max(1, limit))
                .toList();
    }


    // this method to foodTruck Owner if he went to change his truck location
    @Transactional
    public void updateFoodTruckLocation(Integer owner_id , Integer foodTruck_id, LocationDTO locationDTO) {
        Owner owner = ownerRepository.findOwnerById(owner_id);
        if(owner == null){
            throw new ApiException("Owner not found");
        }
        FoodTruck foodTruck = foodTruckRepository.findFoodTruckById(foodTruck_id);
        if(foodTruck == null) {
            throw new ApiException("food Truck not found");
        }
        if (!Objects.equals(foodTruck.getOwner().getId(), owner.getId())) {
            throw new ApiException("You don't own this food truck");
        }
        GeocodeResult gr = hereGeocodingService.geocodeCityDistrict(locationDTO.getCity(), locationDTO.getDistrict(), "SAU");
        foodTruck.setLatitude(gr.lat());
        foodTruck.setLongitude(gr.lon());
        foodTruckRepository.save(foodTruck);
    }


    public void openFoodTruck(Integer owner_id , Integer foodTruck_id) {
        FoodTruck foodTruck = mustOwnTruck(owner_id, foodTruck_id);
        foodTruck.setStatus("OPEN");
        foodTruckRepository.save(foodTruck);
    }


    public void closeFoodTruck(Integer owner_id , Integer foodTruck_id) {
        FoodTruck foodTruck = mustOwnTruck(owner_id, foodTruck_id);
        foodTruck.setStatus("CLOSE");
        foodTruckRepository.save(foodTruck);
    }


    private FoodTruck mustOwnTruck(Integer ownerId, Integer truckId) {
        Owner owner = ownerRepository.findOwnerById(ownerId);
        if (owner == null) throw new ApiException("Owner not found");
        FoodTruck truck = foodTruckRepository.findFoodTruckById(truckId);
        if (truck == null) throw new ApiException("FoodTruck not found");
        if (truck.getOwner() == null || !truck.getOwner().getId().equals(owner.getId()))
            throw new ApiException("You don't own this food truck");
        return truck;
    }


    public String uploadTruckImage(Integer ownerId, Integer truckId, MultipartFile file) {
        FoodTruck truck = mustOwnTruck(ownerId, truckId);
        // امسح القديمة إن وجدت
        storage.deleteIfExists(truck.getImageKey());

        String key = StorageService.buildKey(
                "foodtrucks/%d/%d".formatted(ownerId, truckId),
                file.getOriginalFilename(),
                "jpg"
        );
        StorageService.UploadResult ur = storage.upload(file, key);

        truck.setImageKey(ur.key());
        truck.setImageUrl(ur.url());
        foodTruckRepository.save(truck);
        return ur.url();
    }
}
