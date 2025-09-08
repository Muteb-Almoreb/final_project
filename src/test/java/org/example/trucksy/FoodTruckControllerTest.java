package org.example.trucksy;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.trucksy.Controller.FoodTruckController;
import org.example.trucksy.DTO.FoodTruckDTO;
import org.example.trucksy.DTO.LocationDTO;
import org.example.trucksy.DTOOut.NearbyTruckResponse;
import org.example.trucksy.Model.FoodTruck;
import org.example.trucksy.Model.Owner;
import org.example.trucksy.Service.FoodTruckService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class FoodTruckControllerTest {

    @InjectMocks
    FoodTruckController foodTruckController;

    @Mock
    FoodTruckService foodTruckService;

    MockMvc mockMvc;

    FoodTruck foodTruck1, foodTruck2;
    Owner owner;
    FoodTruckDTO foodTruckDTO;
    LocationDTO locationDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(foodTruckController).build();

        owner = new Owner();
        owner.setId(1);

        foodTruck1 = new FoodTruck();
        foodTruck1.setId(1);
        foodTruck1.setName("Taco Express");
        foodTruck1.setDescription("Mexican street food");
        foodTruck1.setCategory("Mexican");
        foodTruck1.setLatitude(25.7617);
        foodTruck1.setLongitude(50.4016);
        foodTruck1.setCity("Riyadh");
        foodTruck1.setDistrict("Downtown");
        foodTruck1.setStatus("OPEN");
        foodTruck1.setOwner(owner);

        foodTruck2 = new FoodTruck();
        foodTruck2.setId(2);
        foodTruck2.setName("Burger Palace");
        foodTruck2.setDescription("Gourmet burgers");
        foodTruck2.setCategory("American");
        foodTruck2.setLatitude(25.7618);
        foodTruck2.setLongitude(50.4017);
        foodTruck2.setCity("Riyadh");
        foodTruck2.setDistrict("Olaya");
        foodTruck2.setStatus("CLOSED");
        foodTruck2.setOwner(owner);

        foodTruckDTO = new FoodTruckDTO();
        foodTruckDTO.setName("Test Truck");
        foodTruckDTO.setDescription("Test description");
        foodTruckDTO.setCategory("Test category");

        locationDTO = new LocationDTO();
        locationDTO.setCity("Riyadh");
        locationDTO.setDistrict("Downtown");
    }

    @Test
    public void testDeleteFoodTruck() throws Exception {
        doNothing().when(foodTruckService).deleteFoodTruck(any(), anyInt());

        mockMvc.perform(delete("/api/v1/foodTruck/delete/{truck_id}", 1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Successfully deleted food truck"));

        verify(foodTruckService, times(1)).deleteFoodTruck(any(), eq(1));
    }

    @Test
    public void testGetAllFoodTrucksByCategory() throws Exception {
        // ✅ الخدمة تُرجِع List<FoodTruckDTO> حسب توقيعك، إذن غيّر الـstub ليعيد DTOs
        FoodTruckDTO mexicanDto = new FoodTruckDTO();
        mexicanDto.setName("Taco Express");
        mexicanDto.setDescription("Mexican street food");
        mexicanDto.setCategory("Mexican");

        List<FoodTruckDTO> mexicanDtos = Arrays.asList(mexicanDto);

        when(foodTruckService.getAllFoodTruckByCategory(eq("Mexican"))).thenReturn(mexicanDtos);

        mockMvc.perform(get("/api/v1/foodTruck/get-foodTrucks-by-category/{category}", "Mexican"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].category").value("Mexican"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Taco Express"));

        verify(foodTruckService, times(1)).getAllFoodTruckByCategory("Mexican");
    }
    @Test
    public void testUpdateFoodTruckLocation() throws Exception {
        doNothing().when(foodTruckService).updateFoodTruckLocation(any(), anyInt(), any(LocationDTO.class));

        mockMvc.perform(put("/api/v1/foodTruck/update-food-truck-location/{foodTruck_id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(locationDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Location updated Successfully"));

        verify(foodTruckService, times(1)).updateFoodTruckLocation(any(), eq(1), any(LocationDTO.class));
    }

    @Test
    public void testOpenFoodTruck() throws Exception {
        doNothing().when(foodTruckService).openFoodTruck(any(), anyInt());

        mockMvc.perform(put("/api/v1/foodTruck/open-foodTruck/{foodTruck_id}", 1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Your food truck has been opened"));

        verify(foodTruckService, times(1)).openFoodTruck(any(), eq(1));
    }

    @Test
    public void testCloseFoodTruck() throws Exception {
        doNothing().when(foodTruckService).closeFoodTruck(any(), anyInt());

        mockMvc.perform(put("/api/v1/foodTruck/close-foodTruck/{foodTruck_id}", 1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Your food truck has been closed"));

        verify(foodTruckService, times(1)).closeFoodTruck(any(), eq(1));
    }
}
