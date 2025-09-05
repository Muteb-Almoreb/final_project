package org.example.trucksy.Repository;

import org.example.trucksy.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findByFoodTruck_Id(Integer foodTruckId);

    List<Review> findByClient_Id(Integer clientId);

    @Query("select avg(r.rating) from Review r where r.foodTruck.id = :foodTruckId")
    Double findAverageRatingByFoodTruckId(@Param("foodTruckId") Integer foodTruckId);
}
