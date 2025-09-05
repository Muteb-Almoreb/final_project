package org.example.trucksy.Repository;

import org.example.trucksy.Model.FoodTruck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodTruckRepository extends JpaRepository<FoodTruck, Integer> {
    FoodTruck findFoodTruckById(Integer id);

    List<FoodTruck> findFoodTruckByOwnerId(Integer id);
}
