package org.example.trucksy.Service;

import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiException;
import org.example.trucksy.DTO.ClientDTO;
import org.example.trucksy.Model.Client;
import org.example.trucksy.Model.User;
import org.example.trucksy.Repository.AuthRepository;
import org.example.trucksy.Repository.ClientRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final AuthRepository authRepository;

    public void registerClient(ClientDTO clientDTO) {
        System.out.println(clientDTO.getEmail());
        User user = new User();
        user.setUsername(clientDTO.getUsername());
        user.setPassword(clientDTO.getPassword()); // todo make it Encrypt
        user.setEmail(clientDTO.getEmail());
        user.setPhoneNumber(clientDTO.getPhone());
        user.setRole("CLIENT");

        Client client = new Client();
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
        user.setPassword(clientDTO.getPassword()); //todo make the password Encrypt
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
}
