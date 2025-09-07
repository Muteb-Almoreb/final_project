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


    @Query("SELECT r FROM Review r WHERE r.foodTruck.id = :foodTruckId AND r.comment IS NOT NULL AND r.comment != ''")
    List<Review> findByFoodTruckIdWithComments(@Param("foodTruckId") Integer foodTruckId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.foodTruck.id = :foodTruckId")
    Integer countReviewsByFoodTruckId(@Param("foodTruckId") Integer foodTruckId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.foodTruck.id = :foodTruckId")
    Double getAverageRatingByFoodTruckId(@Param("foodTruckId") Integer foodTruckId);
}
