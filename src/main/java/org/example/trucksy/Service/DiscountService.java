package org.example.trucksy.Service;

import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiException;
import org.example.trucksy.DTO.DiscountDTO;
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

    public Discount getDiscountById(Integer id) {
        Discount discount = discountRepository.findDiscountById(id);
        if (discount == null)
            throw new ApiException("Discount not found");
        return discount;
    }


    public void addDiscount(Integer itemId, DiscountDTO dto) {
        if (dto == null) throw new ApiException("Discount body is required");

        Item item = itemRepository.findItemsById(itemId);
        if (item == null)
            throw new ApiException("Item id not found");



        if (item.getDiscount() != null) {
            throw new ApiException("Item already has a discount");
        }




        Discount discount = new Discount();
        discount.setTitle(dto.getTitle());
        discount.setDescription(dto.getDescription());
        discount.setPercentage(dto.getPercentage());
        discount.setStartDate(dto.getStartDate());
        discount.setEndDate(dto.getEndDate());
        discount.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        discount.setCreateDate(LocalDate.now());

        //        // ربط @MapsId

        //        discount.setId(item.getId());


        discount.setItem(item);
        //discount.setId(item.getId()); // مهم لأننا نستخدم @MapsId



        discountRepository.save(discount);

        // تحديث للأيتم
        item.setIsDiscounted(true);
        itemRepository.save(item);
    }

    public void updateDiscount(Integer id, DiscountDTO dto) {
        if (id == null) throw new ApiException("Discount id is required");
        Discount oldDiscount = discountRepository.findDiscountById(id);

        if (oldDiscount == null)
            throw new ApiException("Discount not found");



        oldDiscount.setPercentage(dto.getPercentage());
        oldDiscount.setStartDate(dto.getStartDate());
        oldDiscount.setEndDate(dto.getEndDate());
        oldDiscount.setIsActive(dto.getIsActive());

        discountRepository.save(oldDiscount);
    }


    public void deleteDiscount(Integer id) {
        Discount discount = discountRepository.findDiscountById(id);
        if (discount == null) throw new ApiException("Discount not found");

        Item item = discount.getItem();
        if (item != null) {
            item.setIsDiscounted(false);
            itemRepository.save(item);
        }

        discountRepository.delete(discount);
    }

    public void activateDiscount(Integer id) {
        Discount discount = discountRepository.findDiscountById(id);
        if (discount == null) throw new ApiException("Discount not found");

        discount.setIsActive(true);
        discountRepository.save(discount);
    }

    public void deactivateDiscount(Integer id) {
        Discount discount = discountRepository.findDiscountById(id);
        if (discount == null) throw new ApiException("Discount not found");

        discount.setIsActive(false);
        discountRepository.save(discount);

        // افصل حالة الخصم من الآيتم
        Item item = discount.getItem();
        if (item != null) {
            item.setIsDiscounted(false);
            itemRepository.save(item);
        }
    }


}
