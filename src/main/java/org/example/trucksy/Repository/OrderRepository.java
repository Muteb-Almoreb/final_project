package org.example.trucksy.Repository;

import org.example.trucksy.Model.Order;
import org.springframework.data.domain.Pageable;
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
    Integer countOrdersByOwner(@Param("ownerUserId") Integer ownerUserId);

    boolean existsByClientIdAndFoodTruckId(Integer clientId, Integer foodTruckId);

    @Query("""
       select count(o)
       from Order o
       where o.foodTruck.owner.id = :ownerId
         and o.status = 'COMPLETED'
    """)
    Integer countCompletedOrdersByOwner(@Param("ownerId") Integer ownerId);

//    @Query("""
//       select count(o)
//       from Order o
//       where o.foodTruck.owner.id = :ownerId
//         and o.status = 'PLACED'
//    """)
//    Integer countPLACEDOrdersByOwner(@Param("ownerId") Integer ownerId);

    @Query("""
        select o from Order o 
        where o.foodTruck.owner.id = :ownerId 
        order by o.orderDate desc
    """)
    List<Order> findByOwnerIdOrderByOrderDateDesc(@Param("ownerId") Integer ownerId, Pageable pageable);

    @Query("""
        select o from Order o 
        where o.foodTruck.owner.id = :ownerId 
        order by o.orderDate desc
    """)
    List<Order> findByOwnerIdOrderByOrderDateDesc(@Param("ownerId") Integer ownerId);


    List<Order> findAllByStatus(String status);

}