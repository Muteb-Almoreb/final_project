package org.example.trucksy.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiResponse;
import org.example.trucksy.DTO.ClientDTO;
import org.example.trucksy.DTO.LocationDTO;
import org.example.trucksy.Model.User;
import org.example.trucksy.Service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PutMapping("/update") // todo after Security added remove ID from the path
    public ResponseEntity<?> updateClient(@AuthenticationPrincipal User user, @Valid @RequestBody ClientDTO clientDTO) {
        clientService.updateClient(user.getId(), clientDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Client updated successfully"));
    }


    @DeleteMapping("/delete") // todo after Security added remove ID from the path
    public ResponseEntity<?> deleteClient(@AuthenticationPrincipal User user) {
        clientService.deleteClient(user.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Client deleted successfully"));
    }


    @PutMapping("/update-client-location")
    public ResponseEntity<?> setClientLocation(@AuthenticationPrincipal User user, @Valid @RequestBody LocationDTO locationDTO) {
        clientService.updateClientLocation(user.getId(),locationDTO);
        return ResponseEntity.status(200).body(new ApiResponse("location updated successfully"));
    }
}
