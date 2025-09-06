package org.example.trucksy.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiResponse;
import org.example.trucksy.DTO.ClientDTO;
import org.example.trucksy.DTO.LocationDTO;
import org.example.trucksy.Service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @PostMapping("/add")
    public ResponseEntity<?> registerClient(@Valid @RequestBody ClientDTO clientDTO) {
        clientService.registerClient(clientDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Client registered successfully"));
    }

    @PutMapping("/update/{id}") // todo after Security added remove ID from the path
    public ResponseEntity<?> updateClient(@PathVariable Integer id, @Valid @RequestBody ClientDTO clientDTO) {
        clientService.updateClient(id, clientDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Client updated successfully"));
    }


    @DeleteMapping("/delete/{id}") // todo after Security added remove ID from the path
    public ResponseEntity<?> deleteClient(@PathVariable Integer id) {
        clientService.deleteClient(id);
        return ResponseEntity.status(200).body(new ApiResponse("Client deleted successfully"));
    }


    @PutMapping("/update-client-location/{client_id}")
    public ResponseEntity<?> setClientLocation(@PathVariable Integer client_id, @Valid @RequestBody LocationDTO locationDTO) {
        clientService.updateClientLocation(client_id,locationDTO);
        return ResponseEntity.status(200).body(new ApiResponse("location updated successfully"));
    }
}
