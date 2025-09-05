package org.example.trucksy.Service;

import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiException;
import org.example.trucksy.Model.Discount;
import org.example.trucksy.Model.Item;
import org.example.trucksy.Repository.DiscountRepository;
import org.example.trucksy.Repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountRepository discountRepository;
    private final ItemRepository itemRepository;







    public List<Discount> getAllDiscounts() {
        return discountRepository.findAll();
    }

    public Discount getDiscountByItemId(Integer itemId) {
        Item item = itemRepository.findItemById(itemId);
        if (item == null) throw new ApiException("Item not found");
        return item.getDiscount();
    }

    //تقريب عشري
    private double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }




    public void addDiscount(Integer itemId, Discount discount) {
        if (discount == null) throw new ApiException("Discount body is required");

        Item item = itemRepository.findItemsById(itemId);
        if (item == null) throw new ApiException("Item id not found");
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
        d.setOriginalPrice(item.getPrice());


        if (Boolean.TRUE.equals(d.getIsActive()) && d.getPercentage() != null) {
            double newPrice = d.getOriginalPrice() * (1 - d.getPercentage() / 100.0);
            item.setPrice(round2(newPrice));
            item.setIsDiscounted(true);
            item.setUpdateDate(LocalDate.now());
            itemRepository.save(item);
        }

        discountRepository.save(d);
        item.setDiscount(d);
        itemRepository.save(item);
    }


    public void updateDiscount(Integer id, Discount discount) {
        if (id == null) throw new ApiException("Discount id is required");

        Discount d = discountRepository.findDiscountById(id);
        if (d == null) throw new ApiException("Discount not found");

        Item item = d.getItem();
        if (item == null) throw new ApiException("Linked item not found");

        d.setTitle(discount.getTitle());
        d.setDescription(discount.getDescription());
        d.setStartDate(discount.getStartDate());
        d.setEndDate(discount.getEndDate());
        d.setIsActive(discount.getIsActive());
        d.setPercentage(discount.getPercentage());

        if (Boolean.TRUE.equals(d.getIsActive()) && d.getPercentage() != null) {
            Double base = d.getOriginalPrice() != null ? d.getOriginalPrice() : item.getPrice();
            double newPrice = base * (1 - d.getPercentage() / 100.0);
            item.setPrice(round2(newPrice));
            item.setIsDiscounted(true);
            item.setUpdateDate(LocalDate.now());
            itemRepository.save(item);
        }

        discountRepository.save(d);
    }


    public void activateDiscount(Integer id) {
        Discount d = discountRepository.findDiscountById(id);
        if (d == null) throw new ApiException("Discount not found");

        Item item = d.getItem();
        if (item == null) throw new ApiException("Linked item not found");

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


    public void deactivateDiscount(Integer id) {
        Discount d = discountRepository.findDiscountById(id);
        if (d == null) throw new ApiException("Discount not found");

        Item item = d.getItem();
        if (item == null) throw new ApiException("Linked item not found");

        if (d.getOriginalPrice() != null) {
            item.setPrice(round2(d.getOriginalPrice()));
        }
        item.setIsDiscounted(false);
        item.setUpdateDate(LocalDate.now());
        itemRepository.save(item);

        d.setIsActive(false);
        discountRepository.save(d);
    }


    public void deleteDiscount(Integer id) {
        Discount d = discountRepository.findDiscountById(id);
        if (d == null) throw new ApiException("Discount not found");

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
