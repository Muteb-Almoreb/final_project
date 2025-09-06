package org.example.trucksy.Service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiException;
import org.example.trucksy.DTO.ClientDTO;
import org.example.trucksy.DTO.GeocodeResult;
import org.example.trucksy.DTO.LocationDTO;
import org.example.trucksy.Model.Client;
import org.example.trucksy.Model.User;
import org.example.trucksy.Repository.AuthRepository;
import org.example.trucksy.Repository.ClientRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final AuthRepository authRepository;
    private final HereGeocodingService hereGeocodingService;

    public void registerClient(ClientDTO clientDTO) {
        System.out.println(clientDTO.getEmail());
        User user = new User();
        user.setUsername(clientDTO.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(clientDTO.getPassword()));
        user.setEmail(clientDTO.getEmail());
        user.setPhoneNumber(clientDTO.getPhone());
        user.setRole("CLIENT");

        Client client = new Client();
        GeocodeResult gr = hereGeocodingService.geocodeCityDistrict(clientDTO.getCity(), clientDTO.getDistrict(), "SAU");
        client.setLatitude(gr.lat());
        client.setLongitude(gr.lon());
        client.setUser(user);
        clientRepository.save(client);
    }


    public void updateClient(Integer id ,ClientDTO clientDTO) {
        Client client = clientRepository.findClientById(id);
        if(client == null) {
            throw new ApiException("Client not found");
        }
        User user = client.getUser();
        user.setUsername(clientDTO.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(clientDTO.getPassword()));
        user.setEmail(clientDTO.getEmail());
        user.setPhoneNumber(clientDTO.getPhone());
        clientRepository.save(client);
    }

    public void deleteClient(Integer id) {
        Client client = clientRepository.findClientById(id);
        if(client == null) {
            throw new ApiException("Client not found");
        }
        authRepository.delete(client.getUser());
        clientRepository.delete(client);
    }


    // this method to client if he went to change his location
    @Transactional
    public void updateClientLocation(Integer clientId, LocationDTO locationDTO) {
        Client client = clientRepository.findClientById(clientId);
        if(client == null) {
            throw new ApiException("Client not found");
        }
        GeocodeResult gr = hereGeocodingService.geocodeCityDistrict(locationDTO.getCity(), locationDTO.getDistrict(), "SAU");
        client.setLatitude(gr.lat());
        client.setLongitude(gr.lon());
        clientRepository.save(client);
    }

}
