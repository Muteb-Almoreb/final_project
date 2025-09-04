package org.example.trucksy.Service;

import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiException;
import org.example.trucksy.DTO.ItemDTO;
import org.example.trucksy.DTO.ItemViewDTO;
import org.example.trucksy.Model.Discount;
import org.example.trucksy.Model.FoodTruck;
import org.example.trucksy.Model.Item;
import org.example.trucksy.Model.Owner;
import org.example.trucksy.Repository.FoodTruckRepository;
import org.example.trucksy.Repository.ItemRepository;
import org.example.trucksy.Repository.OwnerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final FoodTruckRepository foodTruckRepository;
    private final OwnerRepository ownerRepository;



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


    private boolean isDiscountActive(Discount d, LocalDate today) {
        if (d == null || Boolean.FALSE.equals(d.getIsActive())) return false;
        if (d.getStartDate() != null && today.isBefore(d.getStartDate())) return false;
        if (d.getEndDate() != null && today.isAfter(d.getEndDate())) return false;
        return true;
    }

    private double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    private ItemViewDTO toView(Item item) {
        ItemViewDTO dto = new ItemViewDTO();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setPrice(item.getPrice());
        dto.setIsAvailable(item.getIsAvailable());
        dto.setIsDiscounted(item.getIsDiscounted());

        Discount d = item.getDiscount();
        if (d != null && isDiscountActive(d, LocalDate.now()) && d.getPercentage() != null && d.getPercentage() > 0) {
            dto.setDiscountPercentage(d.getPercentage());
            dto.setEffectivePrice(round2(item.getPrice() * (1 - d.getPercentage() / 100.0)));
        } else {
            dto.setEffectivePrice(item.getPrice());
        }
        return dto;
    }


    public List<ItemViewDTO> getItemsByFoodTruck(Integer ownerId, Integer truckId) {
        mustOwnTruck(ownerId, truckId);

        List<Item> items = itemRepository.findAllByFoodTruck_Id(truckId);
        List<ItemViewDTO> result = new ArrayList<>();

        for (Item item : items) {
            result.add(toView(item)); // toView هي نفس الهيلبر اللي يحسب effectivePrice
        }

        return result;
    }




    public void addItemToFoodTruck(Integer ownerId, Integer truckId, ItemDTO dto) {
            FoodTruck truck = mustOwnTruck(ownerId, truckId);

            Item item = new Item();
            item.setName(dto.getName());
            item.setPrice(dto.getPrice());
            item.setDescription(dto.getDescription());
            item.setIsAvailable(dto.getIsAvailable() != null ? dto.getIsAvailable() : true);
            item.setIsDiscounted(false);
            item.setCreationDate(LocalDate.now());
            item.setFoodTruck(truck);

            itemRepository.save(item);
        }



    public void updateItemInFoodTruck(Integer ownerId, Integer truckId, Integer itemId, ItemDTO dto) {
        mustOwnTruck(ownerId, truckId);

        Item item = itemRepository.findItemsById(itemId);
        if (item == null || item.getFoodTruck() == null || !item.getFoodTruck().getId().equals(truckId))
            throw new ApiException("Item not found in this FoodTruck");

         item.setName(dto.getName());
         item.setDescription(dto.getDescription());
         item.setPrice(dto.getPrice());
         item.setIsAvailable(dto.getIsAvailable());

        item.setUpdateDate(LocalDate.now());
        itemRepository.save(item);
    }






        public void deleteItemFromFoodTruck(Integer ownerId, Integer truckId, Integer itemId) {
            mustOwnTruck(ownerId, truckId);

            Item item = itemRepository.findItemsById(itemId);
            if (item == null || item.getFoodTruck() == null || !item.getFoodTruck().getId().equals(truckId))
                throw new ApiException("Item not found in this FoodTruck");

            // لو تبغى تمنع الحذف إذا عليه طلبات، تقدر تضيف شرط هنا
            // if (db.getOrders()!=null && !db.getOrders().isEmpty())
            //     throw new ApiException("Cannot delete item linked to orders");

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


}
