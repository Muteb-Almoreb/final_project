package org.example.trucksy.Service;

import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiException;
import org.example.trucksy.Model.Discount;
import org.example.trucksy.Model.FoodTruck;
import org.example.trucksy.Model.Item;
import org.example.trucksy.Model.Owner;
import org.example.trucksy.Repository.DiscountRepository;
import org.example.trucksy.Repository.FoodTruckRepository;
import org.example.trucksy.Repository.ItemRepository;
import org.example.trucksy.Repository.OwnerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountRepository discountRepository;
    private final ItemRepository itemRepository;
    private final FoodTruckRepository foodTruckRepository;
    private final OwnerRepository ownerRepository;


    private FoodTruck mustOwnTruck(Integer ownerId, Integer truckId) {
        Owner owner = ownerRepository.findOwnerById(ownerId);
        if (owner == null) throw new ApiException("Owner not found");

        FoodTruck truck = foodTruckRepository.findFoodTruckById(truckId);
        if (truck == null) throw new ApiException("FoodTruck not found");

        if (truck.getOwner() == null || !truck.getOwner().getId().equals(owner.getId()))
            throw new ApiException("You don't own this food truck");

        return truck;
    }

    private void mustBelongToTruck(Item item, Integer truckId) {
        if (item == null || item.getFoodTruck() == null || !item.getFoodTruck().getId().equals(truckId))
            throw new ApiException("Item not found in this FoodTruck");
    }

    private void mustBelongToTruck(Discount d, Integer truckId) {
        if (d == null) throw new ApiException("Discount not found");
        Item item = d.getItem();
        if (item == null || item.getFoodTruck() == null || !item.getFoodTruck().getId().equals(truckId))
            throw new ApiException("Discount not linked to an item in this FoodTruck");
    }

    private double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    // ===== Queries =====
    public List<Discount> getAllDiscountsByTruck(Integer ownerId, Integer truckId) {
        mustOwnTruck(ownerId, truckId); // نفس الهيلبر المستخدم عندك

        List<Item> items = itemRepository.findAllByFoodTruck_Id(truckId);
        List<Discount> out = new ArrayList<>();

        for (Item it : items) {
            if (it.getDiscount() != null) {
                out.add(it.getDiscount());
            }
        }
        return out;
    }


    public Discount getDiscountByItemId(Integer ownerId, Integer truckId, Integer itemId) {
        mustOwnTruck(ownerId, truckId);
        Item item = itemRepository.findItemsById(itemId); // <== أصلحنا الاسم
        mustBelongToTruck(item, truckId);
        return item.getDiscount();
    }


    public void addDiscount(Integer ownerId, Integer truckId, Integer itemId, Discount discount) {
        if (discount == null) throw new ApiException("Discount body is required");

        FoodTruck truck = mustOwnTruck(ownerId, truckId);

        Item item = itemRepository.findItemsById(itemId);
        mustBelongToTruck(item, truck.getId());

        if (item.getDiscount() != null) throw new ApiException("Item already has a discount");

        Discount d = new Discount();
        d.setTitle(discount.getTitle());
        d.setDescription(discount.getDescription());
        d.setPercentage(discount.getPercentage());
        d.setStartDate(discount.getStartDate());
        d.setEndDate(discount.getEndDate());
        d.setIsActive(discount.getIsActive() != null ? discount.getIsActive() : true);
        d.setCreateDate(LocalDate.now());

        d.setItem(item);
        d.setOriginalPrice(item.getPrice()); // خزّن السعر قبل الخصم

        if (Boolean.TRUE.equals(d.getIsActive()) && d.getPercentage() != null) {
            double newPrice = d.getOriginalPrice() * (1 - d.getPercentage() / 100.0);
            item.setPrice(round2(newPrice));
            item.setIsDiscounted(true);
            item.setUpdateDate(LocalDate.now());
            itemRepository.save(item);
        }

        discountRepository.save(d);
        item.setDiscount(d); // (اختياري) تثبيت الربط
        itemRepository.save(item);
    }


    public void updateDiscount(Integer ownerId, Integer truckId, Integer discountId, Discount req) {
        if (discountId == null) throw new ApiException("Discount id is required");

        mustOwnTruck(ownerId, truckId);
        Discount d = discountRepository.findDiscountById(discountId);
        mustBelongToTruck(d, truckId);

        Item item = d.getItem();

        if (req.getTitle() != null)       d.setTitle(req.getTitle());
        if (req.getDescription() != null) d.setDescription(req.getDescription());
        if (req.getStartDate() != null)   d.setStartDate(req.getStartDate());
        if (req.getEndDate() != null)     d.setEndDate(req.getEndDate());
        if (req.getIsActive() != null)    d.setIsActive(req.getIsActive());
        if (req.getPercentage() != null)  d.setPercentage(req.getPercentage());

        // لو Active نعيد حساب السعر دائمًا من originalPrice
        if (Boolean.TRUE.equals(d.getIsActive()) && d.getPercentage() != null) {
            Double base = d.getOriginalPrice() != null ? d.getOriginalPrice() : item.getPrice();
            if (d.getOriginalPrice() == null) d.setOriginalPrice(base); // احتياط
            double newPrice = base * (1 - d.getPercentage() / 100.0);
            item.setPrice(round2(newPrice));
            item.setIsDiscounted(true);
            item.setUpdateDate(LocalDate.now());
            itemRepository.save(item);
        }

        discountRepository.save(d);
    }

    // Activate (by discountId)
    public void activateDiscount(Integer ownerId, Integer truckId, Integer discountId) {
        mustOwnTruck(ownerId, truckId);
        Discount d = discountRepository.findDiscountById(discountId);
        mustBelongToTruck(d, truckId);

        Item item = d.getItem();

        if (d.getOriginalPrice() == null) d.setOriginalPrice(item.getPrice());
        if (d.getPercentage() != null) {
            double newPrice = d.getOriginalPrice() * (1 - d.getPercentage() / 100.0);
            item.setPrice(round2(newPrice));
        }
        item.setIsDiscounted(true);
        item.setUpdateDate(LocalDate.now());
        itemRepository.save(item);

        d.setIsActive(true);
        discountRepository.save(d);
    }

    // Deactivate (by discountId)
    public void deactivateDiscount(Integer ownerId, Integer truckId, Integer discountId) {
        mustOwnTruck(ownerId, truckId);
        Discount d = discountRepository.findDiscountById(discountId);
        mustBelongToTruck(d, truckId);

        Item item = d.getItem();

        if (d.getOriginalPrice() != null) {
            item.setPrice(round2(d.getOriginalPrice()));
        }
        item.setIsDiscounted(false);
        item.setUpdateDate(LocalDate.now());
        itemRepository.save(item);

        d.setIsActive(false);
        discountRepository.save(d);
    }

    // Delete (by discountId)
    public void deleteDiscount(Integer ownerId, Integer truckId, Integer discountId) {
        mustOwnTruck(ownerId, truckId);
        Discount d = discountRepository.findDiscountById(discountId);
        mustBelongToTruck(d, truckId);

        Item item = d.getItem();
        if (item != null) {
            if (d.getOriginalPrice() != null) {
                item.setPrice(round2(d.getOriginalPrice()));
            }
            item.setIsDiscounted(false);
            item.setDiscount(null);
            item.setUpdateDate(LocalDate.now());
            itemRepository.save(item);
        }

        d.setItem(null);
        discountRepository.delete(d);
    }

}
