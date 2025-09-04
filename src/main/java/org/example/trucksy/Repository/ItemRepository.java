package org.example.trucksy.Repository;

import org.example.trucksy.Model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    Item findItemsById(Integer id);
    List<Item> findAllByFoodTruck_Id(Integer truckId);
    Item findItemById(Integer id);


}
