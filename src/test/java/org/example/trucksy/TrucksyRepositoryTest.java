package org.example.trucksy;

import org.example.trucksy.Model.*;
import org.example.trucksy.Repository.AuthRepository;
import org.example.trucksy.Repository.FoodTruckRepository;
import org.example.trucksy.Repository.ItemRepository;
import org.example.trucksy.Repository.OwnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class TrucksyRepositoryTest {
    @Autowired
    ItemRepository itemRepository;

    @Autowired
    FoodTruckRepository foodTruckRepository;

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    AuthRepository authRepository;

    private Owner owner;
    private FoodTruck truck;

    @BeforeEach
    void setUp() {

        User u = new User();
        u.setUsername("owner_test");
        u.setEmail("owner_test@example.com");
        u.setPassword("pass123");
        u.setPhoneNumber("966500000001");
        u.setRole("OWNER");
        u = authRepository.save(u);


        owner = new Owner();
        owner.setUser(u);
        owner.setSubscribed(false);
        owner = ownerRepository.save(owner);


        truck = new FoodTruck();
        truck.setOwner(owner);
        truck.setName("T1");
        truck.setDescription("Yummy truck");
        truck.setCategory("BBQ");
        truck.setStatus("OPEN");
        truck.setCity("Riyadh");
        truck.setDistrict("Al Olaya");
        truck.setLatitude(24.7136);
        truck.setLongitude(46.6753);
        truck = foodTruckRepository.save(truck);
    }

    // (1) ItemRepository: findAllByFoodTruck_Id
    @Test
    void findAllByFoodTruckId_returnsOnlyItemsOfThatTruck() {
        Item a = new Item();
        a.setName("Burger");
        a.setPrice(18.0);
        a.setDescription("Burger desc");
        a.setIsAvailable(true);
        a.setIsDiscounted(false);
        a.setCreationDate(LocalDate.now());
        a.setFoodTruck(truck);

        Item b = new Item();
        b.setName("Fries");
        b.setPrice(7.0);
        b.setDescription("Fries desc");
        b.setIsAvailable(true);
        b.setIsDiscounted(false);
        b.setCreationDate(LocalDate.now());
        b.setFoodTruck(truck);

        itemRepository.save(a);
        itemRepository.save(b);

        List<Item> items = itemRepository.findAllByFoodTruck_Id(truck.getId());

        assertEquals(2, items.size());
        assertTrue(items.stream().allMatch(i -> i.getFoodTruck().getId().equals(truck.getId())));
    }

    // (2) ItemRepository: findByFoodTruck_IdAndIsAvailableTrueAndPriceBetween
    @Test
    void priceBetween_availableOnly_filtersCorrectly() {
        Item cheap = new Item();
        cheap.setName("Cheap");
        cheap.setPrice(5.0);
        cheap.setDescription("cheap item");
        cheap.setIsAvailable(true);
        cheap.setIsDiscounted(false);
        cheap.setCreationDate(LocalDate.now());
        cheap.setFoodTruck(truck);

        Item mid = new Item();
        mid.setName("Mid");
        mid.setPrice(12.0);
        mid.setDescription("mid item ok");
        mid.setIsAvailable(true);
        mid.setIsDiscounted(false);
        mid.setCreationDate(LocalDate.now());
        mid.setFoodTruck(truck);

        Item exp = new Item();
        exp.setName("Exp");
        exp.setPrice(30.0);
        exp.setDescription("expensive item");
        exp.setIsAvailable(true);
        exp.setIsDiscounted(false);
        exp.setCreationDate(LocalDate.now());
        exp.setFoodTruck(truck);

        Item na = new Item();
        na.setName("NA");
        na.setPrice(12.0);
        na.setDescription("not available");
        na.setIsAvailable(false);
        na.setIsDiscounted(false);
        na.setCreationDate(LocalDate.now());
        na.setFoodTruck(truck);

        itemRepository.save(cheap);
        itemRepository.save(mid);
        itemRepository.save(exp);
        itemRepository.save(na);

        List<Item> items =
                itemRepository.findByFoodTruck_IdAndIsAvailableTrueAndPriceBetween(truck.getId(), 10.0, 20.0);

        assertEquals(1, items.size());
        assertEquals("Mid", items.get(0).getName());
        assertTrue(items.get(0).getIsAvailable());
        assertTrue(items.get(0).getPrice() >= 10.0 && items.get(0).getPrice() <= 20.0);
    }

    // (3) ItemRepository: findItemById
    @Test
    void findItemById_returnsSavedItem() {
        Item taco = new Item();
        taco.setName("Taco");
        taco.setPrice(14.0);
        taco.setDescription("taco item");
        taco.setIsAvailable(true);
        taco.setIsDiscounted(false);
        taco.setCreationDate(LocalDate.now());
        taco.setFoodTruck(truck);

        Item saved = itemRepository.save(taco);

        Item found = itemRepository.findItemById(saved.getId());
        assertEquals(saved.getId(), found.getId());
        assertEquals("Taco", found.getName());
        assertEquals(truck.getId(), found.getFoodTruck().getId());
    }

    // (4) FoodTruckRepository: findFoodTruckByCategory
    @Test
    void foodTruck_findByCategory_returnsOnlyThatCategory() {
        FoodTruck vegan = new FoodTruck();
        vegan.setOwner(owner);
        vegan.setName("GreenBite");
        vegan.setDescription("GreenBite desc");
        vegan.setCategory("Vegan");
        vegan.setStatus("OPEN");
        vegan.setCity("Riyadh");
        vegan.setDistrict("Al Olaya");
        vegan.setLatitude(24.7136);
        vegan.setLongitude(46.6753);
        foodTruckRepository.save(vegan);

        FoodTruck bbq2 = new FoodTruck();
        bbq2.setOwner(owner);
        bbq2.setName("FlameIt");
        bbq2.setDescription("FlameIt desc");
        bbq2.setCategory("BBQ");
        bbq2.setStatus("OPEN");
        bbq2.setCity("Riyadh");
        bbq2.setDistrict("Al Olaya");
        bbq2.setLatitude(24.7136);
        bbq2.setLongitude(46.6753);
        foodTruckRepository.save(bbq2);

        List<FoodTruck> bbq = foodTruckRepository.findFoodTruckByCategory("BBQ");

        // المتوقع: T1 + FlameIt
        assertEquals(2, bbq.size());
        assertTrue(bbq.stream().allMatch(t -> "BBQ".equals(t.getCategory())));
    }

    // (5) FoodTruckRepository: findFoodTruckByOwnerId
    @Test
    void foodTruck_findByOwnerId_returnsOnlyOwnersTrucks() {
        // Owner إضافي للتأكد
        User u2 = new User();
        u2.setUsername("owner2");
        u2.setEmail("owner2@test.com");
        u2.setPassword("pass123");
        u2.setPhoneNumber("966500000002");
        u2.setRole("OWNER");
        u2 = authRepository.save(u2);

        Owner owner2 = new Owner();
        owner2.setUser(u2);
        owner2.setSubscribed(false);
        owner2 = ownerRepository.save(owner2);

        FoodTruck tOwner2 = new FoodTruck();
        tOwner2.setOwner(owner2);
        tOwner2.setName("Other");
        tOwner2.setDescription("Other desc");
        tOwner2.setCategory("Vegan");
        tOwner2.setStatus("OPEN");
        tOwner2.setCity("Riyadh");
        tOwner2.setDistrict("Al Olaya");
        tOwner2.setLatitude(24.7136);
        tOwner2.setLongitude(46.6753);
        foodTruckRepository.save(tOwner2);

        FoodTruck bbq2 = new FoodTruck();
        bbq2.setOwner(owner);
        bbq2.setName("FlameIt");
        bbq2.setDescription("FlameIt desc");
        bbq2.setCategory("BBQ");
        bbq2.setStatus("OPEN");
        bbq2.setCity("Riyadh");
        bbq2.setDistrict("Al Olaya");
        bbq2.setLatitude(24.7136);
        bbq2.setLongitude(46.6753);
        foodTruckRepository.save(bbq2);

        List<FoodTruck> owner1Trucks = foodTruckRepository.findFoodTruckByOwnerId(owner.getId());

        // المتوقع: T1 + FlameIt
        assertEquals(2, owner1Trucks.size());
        assertTrue(owner1Trucks.stream().allMatch(t -> t.getOwner().getId().equals(owner.getId())));
    }

}
