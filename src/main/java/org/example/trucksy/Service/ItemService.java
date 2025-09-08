package org.example.trucksy.Service;

import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiException;
import org.example.trucksy.Model.*;
import org.example.trucksy.Repository.ClientRepository;
import org.example.trucksy.Repository.FoodTruckRepository;
import org.example.trucksy.Repository.ItemRepository;
import org.example.trucksy.Repository.OwnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final FoodTruckRepository foodTruckRepository;
    private final OwnerRepository ownerRepository;
    private final ClientRepository clientRepository;
    private final StorageService storage;



    // helper Methods

    private FoodTruck mustOwnTruck(Integer ownerId, Integer truckId) {
        Owner owner = ownerRepository.findOwnerById(ownerId);
        if (owner == null) throw new ApiException("Owner not found");

        FoodTruck truck = foodTruckRepository.findFoodTruckById(truckId);
        if (truck == null) throw new ApiException("FoodTruck not found");

        if (truck.getOwner() == null || !truck.getOwner().getId().equals(owner.getId()))
            throw new ApiException("You don't own this food truck");

        return truck;
    }




    public List<Item> getItemsByFoodTruck(Integer ownerId, Integer truckId) {
        mustOwnTruck(ownerId, truckId);
        return itemRepository.findAllByFoodTruck_Id(truckId);
    }




    public void addItemToFoodTruck(Integer ownerId, Integer truckId, Item newItem) {
        FoodTruck truck = mustOwnTruck(ownerId, truckId);

        Item item = new Item();
        item.setName(newItem.getName());
        item.setPrice(newItem.getPrice());
        item.setDescription(newItem.getDescription());
        item.setIsAvailable(newItem.getIsAvailable() != null ? newItem.getIsAvailable() : true);
        item.setIsDiscounted(false);
        item.setCreationDate(LocalDate.now());
        item.setFoodTruck(truck);

        itemRepository.save(item);
    }



    public void updateItemInFoodTruck(Integer ownerId, Integer truckId, Integer itemId, Item item) {
        mustOwnTruck(ownerId, truckId);

        Item oldItem = itemRepository.findItemsById(itemId);
        if (oldItem == null || oldItem.getFoodTruck() == null || !oldItem.getFoodTruck().getId().equals(truckId))
            throw new ApiException("Item not found in this FoodTruck");

        oldItem.setName(item.getName());
        oldItem.setDescription(item.getDescription());
        oldItem.setPrice(item.getPrice());
        oldItem.setIsAvailable(item.getIsAvailable());

        oldItem.setUpdateDate(LocalDate.now());
        itemRepository.save(oldItem);
    }





    public void deleteItemFromFoodTruck(Integer ownerId, Integer truckId, Integer itemId) {
        mustOwnTruck(ownerId, truckId);

        Item item = itemRepository.findItemsById(itemId);
        if (item == null || item.getFoodTruck() == null || !item.getFoodTruck().getId().equals(truckId))
            throw new ApiException("Item not found in this FoodTruck");

        itemRepository.delete(item);
    }



    public void updatePrice(Integer ownerId, Integer truckId, Integer itemId, Double newPrice) {
        mustOwnTruck(ownerId, truckId);

        Item item = itemRepository.findItemsById(itemId);
        if (item == null || item.getFoodTruck() == null || !item.getFoodTruck().getId().equals(truckId))
            throw new ApiException("Item not found in this FoodTruck");

        item.setPrice(newPrice);
        item.setUpdateDate(LocalDate.now());
        itemRepository.save(item);
    }

    public void setAvailable(Integer ownerId, Integer truckId, Integer itemId) {
        mustOwnTruck(ownerId, truckId);

        Item item = itemRepository.findItemsById(itemId);
        if (item == null) throw new ApiException("Item id not found");

        item.setIsAvailable(true);
        item.setUpdateDate(LocalDate.now());
        itemRepository.save(item);
    }

    public void setNotAvailable(Integer ownerId, Integer truckId, Integer itemId) {
        mustOwnTruck(ownerId, truckId);

        Item item = itemRepository.findItemsById(itemId);
        if (item == null) throw new ApiException("Item id not found");

        item.setIsAvailable(false);
        item.setUpdateDate(LocalDate.now());
        itemRepository.save(item);
    }


    public List<Item> getItemsByPriceRangeForClient(Integer clientId, Integer truckId, Double min, Double max) {
        if (clientId == null) throw new ApiException("ClientId is required");
        if (truckId == null) throw new ApiException("TruckId is required");
        if (min == null || max == null) throw new ApiException("Min and Max prices are required");
        if (min < 0 || max < 0) throw new ApiException("Price must be >= 0");
        if (min > max) throw new ApiException("Min price cannot be greater than Max");

        FoodTruck truck = foodTruckRepository.findFoodTruckById(truckId);
        if (truck == null) throw new ApiException("FoodTruck not found");


        Client client = clientRepository.findClientById(clientId);
         if(client == null) throw new ApiException("Client not found");

        return itemRepository.findByFoodTruck_IdAndIsAvailableTrueAndPriceBetween(truckId, min, max);
    }


    public String uploadItemImage(Integer ownerId, Integer truckId, Integer itemId, MultipartFile file) {
        FoodTruck truck = mustOwnTruck(ownerId, truckId);
        Item item = itemRepository.findItemsById(itemId);
        if (item == null || item.getFoodTruck() == null || !item.getFoodTruck().getId().equals(truckId))
            throw new ApiException("Item not found in this FoodTruck");

        storage.deleteIfExists(item.getImageKey());

        String key = StorageService.buildKey(
                "items/%d/%d/%d".formatted(ownerId, truckId, itemId),
                file.getOriginalFilename(),
                "jpg"
        );
        StorageService.UploadResult ur = storage.upload(file, key);

        item.setImageKey(ur.key());
        item.setImageUrl(ur.url());
        item.setUpdateDate(LocalDate.now());
        itemRepository.save(item);

        return ur.url();
    }


}
