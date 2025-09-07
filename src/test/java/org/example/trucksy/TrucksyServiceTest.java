package org.example.trucksy;

import org.example.trucksy.Api.ApiException;
import org.example.trucksy.DTO.ClientDTO;
import org.example.trucksy.DTO.GeocodeResult;
import org.example.trucksy.DTO.LocationDTO;
import org.example.trucksy.Model.Client;
import org.example.trucksy.Model.User;
import org.example.trucksy.Repository.AuthRepository;
import org.example.trucksy.Repository.ClientRepository;
import org.example.trucksy.Service.ClientService;
import org.example.trucksy.Service.HereGeocodingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrucksyServiceTest {

    @InjectMocks
    ClientService clientService;

    @Mock
    ClientRepository clientRepository;

    @Mock
    AuthRepository authRepository;

    @Mock
    HereGeocodingService hereGeocodingService;

    Client client1;
    User user1;
    ClientDTO clientDTO;
    LocationDTO locationDTO;
    GeocodeResult geocodeResult;

    @BeforeEach
    void setUp() {
        // Initialize test user
        user1 = new User(1, "john_doe", "encodedPassword123", "john@example.com", "966512345678", "CLIENT", null, null, null);

        // Initialize test client
        client1 = new Client(1, "Riyadh", "Al Olaya", 24.7136, 46.6753, user1, null, null);

        // Initialize DTOs
        clientDTO = new ClientDTO();
        clientDTO.setUsername("john_doe");
        clientDTO.setPassword("password123");
        clientDTO.setEmail("john@example.com");
        clientDTO.setPhone("966512345678");
        clientDTO.setCity("Riyadh");
        clientDTO.setDistrict("Al Olaya");

        locationDTO = new LocationDTO();
        locationDTO.setCity("Jeddah");
        locationDTO.setDistrict("Al Balad");

        // Mock geocoding result
        geocodeResult = new GeocodeResult(21.5429, 39.1728, "Jeddah", "Al Balad", "Makkah Province", "SAU", "Jeddah, Al Balad, Saudi Arabia", "here:place123");

    }

    @Test
    public void registerClientTest() {
        // Given
        when(hereGeocodingService.geocodeCityDistrict("Riyadh", "Al Olaya", "SAU"))
                .thenReturn(new GeocodeResult(24.7136, 46.6753, "Riyadh", "Al Olaya", "Riyadh Province", "SAU", "Riyadh, Al Olaya, Saudi Arabia", "here:place456"));
        when(clientRepository.save(any(Client.class))).thenReturn(client1);

        // When
        clientService.registerClient(clientDTO);

        // Then
        verify(hereGeocodingService, times(1))
                .geocodeCityDistrict("Riyadh", "Al Olaya", "SAU");
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    public void updateClientTest() {
        // Given
        ClientDTO updateDTO = new ClientDTO();
        updateDTO.setUsername("john_updated");
        updateDTO.setPassword("newPassword123");
        updateDTO.setEmail("john.updated@example.com");
        updateDTO.setPhone("966587654321");

        when(clientRepository.findClientById(1)).thenReturn(client1);
        when(clientRepository.save(any(Client.class))).thenReturn(client1);

        // When
        clientService.updateClient(1, updateDTO);

        // Then
        verify(clientRepository, times(1)).findClientById(1);
        verify(clientRepository, times(1)).save(client1);

        // Verify that user fields were updated
        Assertions.assertEquals("john_updated", client1.getUser().getUsername());
        Assertions.assertEquals("john.updated@example.com", client1.getUser().getEmail());
        Assertions.assertEquals("966587654321", client1.getUser().getPhoneNumber());
    }

    @Test
    public void updateClientNotFoundTest() {
        // Given
        when(clientRepository.findClientById(999)).thenReturn(null);

        // When & Then
        ApiException exception = Assertions.assertThrows(ApiException.class, () -> {
            clientService.updateClient(999, clientDTO);
        });

        Assertions.assertEquals("Client not found", exception.getMessage());
        verify(clientRepository, times(1)).findClientById(999);
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    public void deleteClientTest() {
        // Given
        when(clientRepository.findClientById(1)).thenReturn(client1);
        doNothing().when(authRepository).delete(user1);
        doNothing().when(clientRepository).delete(client1);

        // When
        clientService.deleteClient(1);

        // Then
        verify(clientRepository, times(1)).findClientById(1);
        verify(authRepository, times(1)).delete(user1);
        verify(clientRepository, times(1)).delete(client1);
    }

    @Test
    public void deleteClientNotFoundTest() {
        // Given
        when(clientRepository.findClientById(999)).thenReturn(null);

        // When & Then
        ApiException exception = Assertions.assertThrows(ApiException.class, () -> {
            clientService.deleteClient(999);
        });

        Assertions.assertEquals("Client not found", exception.getMessage());
        verify(clientRepository, times(1)).findClientById(999);
        verify(authRepository, never()).delete(any(User.class));
        verify(clientRepository, never()).delete(any(Client.class));
    }

    @Test
    public void updateClientLocationTest() {
        // Given
        when(clientRepository.findClientById(1)).thenReturn(client1);
        when(hereGeocodingService.geocodeCityDistrict("Jeddah", "Al Balad", "SAU"))
                .thenReturn(geocodeResult);
        when(clientRepository.save(client1)).thenReturn(client1);

        // When
        clientService.updateClientLocation(1, locationDTO);

        // Then
        verify(clientRepository, times(1)).findClientById(1);
        verify(hereGeocodingService, times(1))
                .geocodeCityDistrict("Jeddah", "Al Balad", "SAU");
        verify(clientRepository, times(1)).save(client1);

        // Verify location was updated
        Assertions.assertEquals(21.5429, client1.getLatitude());
        Assertions.assertEquals(39.1728, client1.getLongitude());
    }

    @Test
    public void updateClientLocationNotFoundTest() {
        // Given
        when(clientRepository.findClientById(999)).thenReturn(null);

        // When & Then
        ApiException exception = Assertions.assertThrows(ApiException.class, () -> {
            clientService.updateClientLocation(999, locationDTO);
        });

        Assertions.assertEquals("Client not found", exception.getMessage());
        verify(clientRepository, times(1)).findClientById(999);
        verify(hereGeocodingService, never()).geocodeCityDistrict(anyString(), anyString(), anyString());
        verify(clientRepository, never()).save(any(Client.class));
    }

    // Additional edge case tests
    @Test
    public void registerClientWithGeocodingServiceTest() {
        // Given - Test the geocoding integration specifically
        GeocodeResult customResult = new GeocodeResult(24.9999, 46.9999, "Riyadh", "Al Olaya", "Riyadh Province", "SAU", "Custom Location Label", "here:custom123");
        when(hereGeocodingService.geocodeCityDistrict("Riyadh", "Al Olaya", "SAU"))
                .thenReturn(customResult);

        // When
        clientService.registerClient(clientDTO);

        // Then
        verify(hereGeocodingService, times(1))
                .geocodeCityDistrict("Riyadh", "Al Olaya", "SAU");
        verify(clientRepository, times(1)).save(argThat(client ->
                client.getLatitude().equals(24.9999) &&
                        client.getLongitude().equals(46.9999)
        ));
    }

    @Test
    public void updateClientPasswordEncodingTest() {
        // Given
        when(clientRepository.findClientById(1)).thenReturn(client1);
        when(clientRepository.save(any(Client.class))).thenReturn(client1);

        // When
        clientService.updateClient(1, clientDTO);

        // Then
        verify(clientRepository, times(1)).save(client1);
        // Verify that password was encoded (should be different from original)
        Assertions.assertNotEquals("password123", client1.getUser().getPassword());
        // Verify that BCrypt was used (encoded passwords start with $2a$)
        Assertions.assertTrue(client1.getUser().getPassword().startsWith("$2a$"));
    }
}