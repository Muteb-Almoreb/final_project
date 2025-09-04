package org.example.trucksy.Service;

import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiException;
import org.example.trucksy.DTO.ItemDTO;
import org.example.trucksy.Model.Item;
import org.example.trucksy.Repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;


    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }




    public void addItem(ItemDTO dto){

        if (dto == null) throw new ApiException("Item body is required");

        Item item = new Item();
        item.setName(dto.getName());
        item.setPrice(dto.getPrice());
        item.setDescription(dto.getDescription());
        item.setIsAvailable(dto.getIsAvailable() != null ? dto.getIsAvailable() : true);
        item.setIsDiscounted(false);
        item.setCreationDate(LocalDate.now());

        itemRepository.save(item);
    }


    public void updateItem(Integer id, ItemDTO dto) {

        if (id == null) throw new ApiException("Item id is required");
        if (dto == null) throw new ApiException("Item body is required");

        Item olditem = itemRepository.findItemsById(id);
        if (olditem == null)
            throw new ApiException("Item id not found");


        // Patch-style update
        olditem.setName(dto.getName());
        olditem.setDescription(dto.getDescription());
        olditem.setPrice(dto.getPrice());
        olditem.setIsAvailable(dto.getIsAvailable());




        olditem.setUpdateDate(LocalDate.now());
        itemRepository.save(olditem);
    }


    public void deleteItem(Integer id) {
        Item item = itemRepository.findItemsById(id);
        if (item == null)
            throw new ApiException("Item id not found");

        // لو تبغى تمنع الحذف إذا عليه طلبات، تقدر تضيف شرط هنا
        // if (db.getOrders()!=null && !db.getOrders().isEmpty())
        //     throw new ApiException("Cannot delete item linked to orders");

//        // افصل الخصم إن وجد (عشان سلامة العلاقات)
//        if (db.getDiscount() != null) {
//            removeDiscount(id);
//        }

        itemRepository.delete(item);
    }



    public void updatePrice(Integer id, Double newPrice) {
        if (newPrice == null || newPrice <= 0)
            throw new ApiException("newPrice must be > 0");

        Item item = itemRepository.findItemsById(id);
        if (item == null)
            throw new ApiException("Item id not found");

        item.setPrice(newPrice);
        item.setUpdateDate(LocalDate.now());
        itemRepository.save(item);
    }



    public void setAvailable(Integer id) {

        Item item = itemRepository.findItemsById(id);
        if (item == null) throw new ApiException("Item id not found");

        item.setIsAvailable(true);
        item.setUpdateDate(LocalDate.now());
        itemRepository.save(item);
    }

    public void setNotAvailable(Integer id) {

        Item item = itemRepository.findItemsById(id);
        if (item == null) throw new ApiException("Item id not found");

        item.setIsAvailable(false);
        item.setUpdateDate(LocalDate.now());
        itemRepository.save(item);
    }


}
