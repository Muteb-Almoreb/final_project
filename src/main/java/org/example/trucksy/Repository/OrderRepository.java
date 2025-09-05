package org.example.trucksy.Repository;

import org.example.trucksy.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByFoodTruck_IdOrderByIdDesc(Integer foodTruckId);
    Optional<Order> findByIdAndFoodTruck_Id(Integer orderId, Integer foodTruckId);
    List<Order> findByClient_IdOrderByIdDesc(Integer clientId);

    @Query("""
   select coalesce(sum(o.totalPrice), 0)
   from Order o
   where o.foodTruck.owner.id = :ownerUserId
""")
    Double sumRevenueByOwner(@Param("ownerUserId") Integer ownerUserId);


    @Query("""
   select count(o)
   from Order o
   where o.foodTruck.owner.id = :ownerUserId
""")
    long countOrdersByOwner(@Param("ownerUserId") Integer ownerUserId);

}
