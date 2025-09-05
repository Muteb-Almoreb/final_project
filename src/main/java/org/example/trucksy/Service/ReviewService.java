package org.example.trucksy.Service;

import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiException;
import org.example.trucksy.DTOOut.ClientReviewsDTOOut;
import org.example.trucksy.DTOOut.TruckReviewsDTOOut;
import org.example.trucksy.Model.Client;
import org.example.trucksy.Model.FoodTruck;
import org.example.trucksy.Model.Review;
import org.example.trucksy.Repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final AuthRepository authRepository;
    private final FoodTruckRepository foodTruckRepository;
    private final ClientRepository clientRepository;
    public final OrderRepository orderRepository;

    public void assignReview(Integer client_id,Integer foodTruck_id, TruckReviewsDTOOut truckReviewsDTOOut) {
       Client client = clientRepository.findClientById(client_id);
        if (client == null) {
            throw new ApiException("User not found");
        }
        FoodTruck foodTruck = foodTruckRepository.findFoodTruckById(foodTruck_id);
        if (foodTruck == null) {
            throw new ApiException("FoodTruck not found");
        }
        boolean hasOrder = orderRepository.existsByClientIdAndFoodTruckId(client_id,foodTruck_id);
        if (!hasOrder) {
            throw new ApiException("You can't assign a review to a food truck before trying it");
        }
        Review review = new Review();
        review.setRating(truckReviewsDTOOut.getRating());
        review.setComment(truckReviewsDTOOut.getComment());
        review.setClient(client);
        review.setFoodTruck(foodTruck);
        review.setCreatedDate(LocalDate.now());
        reviewRepository.save(review);
    }

    // this method for food truck reviews
    public List<TruckReviewsDTOOut> getReviewsByFoodTruck(Integer foodTruck_id) {
        List<Review> reviews = reviewRepository.findByFoodTruck_Id(foodTruck_id);

        return reviews.stream()
                .map(r -> new TruckReviewsDTOOut(
                        r.getClient().getUser().getUsername(),
                        r.getRating(),
                        r.getComment()
                )).toList();
    }


    // this method for client reviews
    public List<ClientReviewsDTOOut> getReviewsByClient(Integer client_id) {
        List<Review> reviews = reviewRepository.findByClient_Id(client_id);

        return reviews.stream()
                .map(r -> new ClientReviewsDTOOut(
                        r.getFoodTruck().getName(),
                        r.getRating(),
                        r.getComment()
                )).toList();
    }


    // this method to get food truck rating
    public Double getAverageRatingForTruck(Integer foodTruckId) {
        Double avg = reviewRepository.findAverageRatingByFoodTruckId(foodTruckId);
        return avg != null ? avg : 0.0;
    }

}
