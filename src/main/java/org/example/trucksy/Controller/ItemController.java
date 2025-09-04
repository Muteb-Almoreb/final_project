package org.example.trucksy.Controller;

import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiResponse;
import org.example.trucksy.DTO.ItemDTO;
import org.example.trucksy.Model.Item;
import org.example.trucksy.Service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/item")
@RequiredArgsConstructor
public class ItemController {


    private final ItemService itemService;

    @GetMapping("/get")
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addItem(@RequestBody ItemDTO dto) {
        itemService.addItem(dto);
        return ResponseEntity.status(200).body(new ApiResponse("Item added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateItem(@PathVariable Integer id, @RequestBody ItemDTO dto) {
        itemService.updateItem(id, dto);
        return ResponseEntity.status(200).body(new ApiResponse("Item updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public  ResponseEntity<?> deleteItem(@PathVariable Integer id) {
        itemService.deleteItem(id);
        return ResponseEntity.status(200).body(new ApiResponse("Item deleted successfully"));
    }

    // عمليات إضافية
    @PutMapping("/setAvailable/{id}")
    public ResponseEntity<?> setAvailable(@PathVariable Integer id) {
        itemService.setAvailable(id);
        return ResponseEntity.status(200).body( new ApiResponse("Item availability updated it been true"));
    }

    @PutMapping("/setNotAvailable/{id}")
    public ResponseEntity<?> setNotAvailable(@PathVariable Integer id) {
        itemService.setNotAvailable(id);
        return ResponseEntity.status(200).body( new ApiResponse("Item availability updated it been false"));
    }

    @PutMapping("/update-price/{id}/{newPrice}")
    public ResponseEntity<?>  updatePrice(@PathVariable Integer id, @PathVariable Double newPrice) {
        itemService.updatePrice(id, newPrice);
        return ResponseEntity.status(200).body(new ApiResponse("Item price updated"));
    }


}
