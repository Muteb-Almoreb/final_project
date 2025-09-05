package org.example.trucksy.Repository;

import org.example.trucksy.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByFoodTruck_IdOrderByIdDesc(Integer foodTruckId);
    Optional<Order> findByIdAndFoodTruck_Id(Integer orderId, Integer foodTruckId);
    List<Order> findByClient_IdOrderByIdDesc(Integer clientId);
}
