package org.example.trucksy.Service;

import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiException;
import org.example.trucksy.Model.Client;
import org.example.trucksy.Model.FoodTruck;
import org.example.trucksy.Model.Item;
import org.example.trucksy.Model.Order;
import org.example.trucksy.Repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ItemRepository itemRepository;
    private final FoodTruckRepository foodTruckRepository;

    public void addOrder(Integer clientId, Integer foodTruckId, List<Item> items, Integer quantity ){
        FoodTruck foodTruck = foodTruckRepository.findFoodTruckById(foodTruckId);
        if(foodTruck == null){
            throw new ApiException("FoodTruck not found");
        }
        Client client = clientRepository.findClientById(clientId);
        if(client == null){
            throw new ApiException("Client not found");
        }
        //check the items id
        for(Item item : items){
            if(item == null){
                throw new ApiException("Item not found");
            }
            if(item.getIsAvailable() == false){
                throw new ApiException("Item is unavailable" + item.getName());
            }
        }

        Order order = new Order();

        //todo check payment from Moyasar and then continue the Order

        //todo send email WhatsApp notification to the food truck to tell him the Order

        order.setClient(client);
        order.setFoodTruck(foodTruck);
    }
}
