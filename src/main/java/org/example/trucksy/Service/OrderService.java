package org.example.trucksy.Service;


import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiException;
import org.example.trucksy.DTO.*;
import org.example.trucksy.Model.*;
import org.example.trucksy.Repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final ClientRepository clientRepository;
    private final ItemRepository itemRepository;
    private final FoodTruckRepository foodTruckRepository;
    private final AuthRepository authRepository;
    private final OrderRepository orderRepository;

    @Transactional
//todo make it select by path variable instead of Dto then add it to the dto by code.
    public void addOrder(Integer clientId, Integer foodTruckId, Set<LiensDtoIn> liensDtoIns) {
        FoodTruck foodTruck = foodTruckRepository.findFoodTruckById(foodTruckId);
        if(foodTruck == null )
            throw new ApiException("FoodTruck not found");

        if(foodTruck.getStatus().equals("CLOSED"))
            throw new ApiException("food truck is already closed");

        Client client = clientRepository.findClientById(clientId);
        if(client == null)
            throw new ApiException("Client not found");

        //check if the user have a card or not ! before payment -> if not cancel the Order.
        //the user id is the same as client ID (MapsId) so I can check if the user have card from its client ID
        User user = authRepository.findUserById(clientId);
        if (user == null)
            throw new ApiException("User not found for this client");

        if(user.getBankCard() == null)
            throw new ApiException("Client does not have Bank Card");

        if (liensDtoIns == null || liensDtoIns.isEmpty())
            throw new ApiException("Order must contain at least one item");

        //check the items id
        Set<LiensDtoIn> liens = liensDtoIns;// a set of items and it's quantity

        Order order = new Order();
        order.setStatus("PLACED");//it will be preparing if the client paid
        order.setClient(client);
        order.setFoodTruck(foodTruck);

        Set<OrderLine> lines = new LinkedHashSet<>();
        Double totalPrice = 0.0;//سعر الطلب

        // ===== this loop is designed to merge the duplicated items =====
        for(LiensDtoIn lien : liens){
            Integer qty = lien.getQuantity();
            if (qty == null || qty <= 0)
                throw new ApiException("Quantity must be > 0");

            Item item = itemRepository.findItemById(lien.getItemId());
            if(item == null){
                throw new ApiException("one of the items id is not found");
            }
            if(Boolean.FALSE.equals(item.getIsAvailable())){
                throw new ApiException("one of the items is not available right now");
            }

            // تحقق أن هذا الـ item يتبع نفس الـ FoodTruck القادم من الهيدر/الباث
            if (item.getFoodTruck() == null || item.getFoodTruck().getId() == null
                    || !item.getFoodTruck().getId().equals(foodTruckId)) {
                throw new ApiException("one of the items does not belong to the selected FoodTruck");
            }//مثلا طالب شاهي من فود ترك يبيع برقر

            // if any order was duplicated this line will detect it .
            OrderLine existing = null;
            for (OrderLine ol : lines) {
                if (ol.getItem().getId().equals(item.getId())) {
                    existing = ol;
                    break;
                }
            }

            if (existing == null) {
                double unitPrice = item.getPrice();
                OrderLine line = new OrderLine();
                line.setItem(item);
                line.setQuantity(qty);
                line.setUnitPriceAtPurchase(unitPrice);
                line.setOrder(order);

                lines.add(line);
                totalPrice += unitPrice * qty; // adding the total
            } else {
                //adding continuity and calculate the total price
                existing.setQuantity(existing.getQuantity() + qty);
                totalPrice += existing.getUnitPriceAtPurchase() * qty;
            }
        }

        order.setTotalPrice(totalPrice);
        order.setLines(lines);
        //todo check payment from Moyasar and then continue the Order (assume it's paid) -> another end point take 2 param (client id and Order id) then go to payment process
        //todo after the payment complete from moyasar 3rd secure the status of the order will be Placed
        //todo food truck will make the order preparing and continue with the status of the Order -> ready -> if the user come to the food truck and take the order -> completed
        //todo every one of the status should send a notification to the user or to the food truck as the flows said.
        //todo send email WhatsApp notification to the food truck to tell him the Order

        order.setClient(client);
        order.setFoodTruck(foodTruck);

        orderRepository.save(order);
    }



    @Transactional(readOnly = true)
    public List<OrderDtoOut> getOrdersForFoodTruckDto(Integer foodTruckId) {
        if (foodTruckRepository.findFoodTruckById(foodTruckId) == null)
            throw new ApiException("FoodTruck not found");

        List<Order> orders = orderRepository.findByFoodTruck_IdOrderByIdDesc(foodTruckId);
        List<OrderDtoOut> result = new ArrayList<>();

        for (Order o : orders) {
            // نعرض العميل فقط (client) ونخفي معلومات الفود ترك
            OrderDtoOut dto = mapOrderToDtoOut(o, /*includeClient*/ true, /*includeFoodTruck*/ false);
            result.add(dto);
        }

        return result;
    }

    @Transactional(readOnly = true)
    public List<OrderDtoOut> getOrdersForClientDto(Integer clientId) {
        if (clientRepository.findClientById(clientId) == null)
            throw new ApiException("Client not found");

        List<Order> orders = orderRepository.findByClient_IdOrderByIdDesc(clientId);
        List<OrderDtoOut> result = new ArrayList<>();

        for (Order o : orders) {
            // نعرض الفود ترك فقط ونخفي معلومات العميل
            OrderDtoOut dto = mapOrderToDtoOut(o, /*includeClient*/ false, /*includeFoodTruck*/ true);
            result.add(dto);
        }

        return result;
    }

    @Transactional(readOnly = true)
    public OrderDtoOut getOrderForFoodTruckDto(Integer foodTruckId, Integer orderId) {
        if (foodTruckRepository.findFoodTruckById(foodTruckId) == null)
            throw new ApiException("FoodTruck not found");

        Order order = orderRepository.findByIdAndFoodTruck_Id(orderId, foodTruckId)
                .orElseThrow(() -> new ApiException("Order not found for this FoodTruck"));

        // نعرض العميل فقط ونخفي الفود ترك
        return mapOrderToDtoOut(order, /*includeClient*/ true, /*includeFoodTruck*/ false);
    }

    //Helper methods
    private OrderDtoOut mapOrderToDtoOut(Order order, boolean includeClient, boolean includeFoodTruck) {
        OrderDtoOut dto = new OrderDtoOut();
        dto.setId(order.getId());
        dto.setStatus(order.getStatus());
        dto.setTotalPrice(order.getTotalPrice());

        // ----- ClientSummaryDtoOut -----
        if (includeClient) {
            ClientSummaryDtoOut clientDto = null;
            Client client = order.getClient();
            if (client != null) {
                clientDto = new ClientSummaryDtoOut();
                clientDto.setId(client.getId());

                // MapsId: بيانات المستخدم داخل client.getUser()
                User u = client.getUser();
                if (u != null) {
                    clientDto.setUsername(u.getUsername());
                    clientDto.setEmail(u.getEmail());
                    clientDto.setPhone(u.getPhoneNumber());
                }
            }
            dto.setClient(clientDto);
        } else {
            dto.setClient(null);
        }

        // ----- FoodTruckSummaryDtoOut -----
        if (includeFoodTruck) {
            FoodTruckSummaryDtoOut truckDto = null;
            if (order.getFoodTruck() != null) {
                truckDto = new FoodTruckSummaryDtoOut();
                truckDto.setId(order.getFoodTruck().getId());
                truckDto.setName(order.getFoodTruck().getName());
                truckDto.setCategory(order.getFoodTruck().getCategory());
                truckDto.setStatus(order.getFoodTruck().getStatus());
            }
            dto.setFoodTruck(truckDto);
        } else {
            dto.setFoodTruck(null);
        }

        // ----- Lines -> List<OrderLineDtoOut> -----
        List<OrderLineDtoOut> lineDtos = new ArrayList<>();
        if (order.getLines() != null) {
            for (OrderLine ol : order.getLines()) {
                OrderLineDtoOut ld = new OrderLineDtoOut();
                if (ol.getItem() != null) {
                    ld.setItemId(ol.getItem().getId());
                    ld.setItemName(ol.getItem().getName());
                }
                ld.setQuantity(ol.getQuantity());
                ld.setUnitPriceAtPurchase(ol.getUnitPriceAtPurchase());
                lineDtos.add(ld);
            }
        }
        dto.setLines(lineDtos);

        return dto;
    }
}
