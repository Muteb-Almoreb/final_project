package org.example.trucksy.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.example.trucksy.Api.ApiResponse;
import org.example.trucksy.DTO.OwnerDTO;
import org.example.trucksy.Service.OwnerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/owner")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

    @PostMapping("/add")
    public ResponseEntity<?> registerOwner(@Valid@RequestBody OwnerDTO ownerDTO) {
        ownerService.registerOwner(ownerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Owner registered successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateOwner(@PathVariable Integer id, @Valid@RequestBody OwnerDTO ownerDTO) {
        ownerService.updateOwner(id, ownerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Owner updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOwner(@PathVariable Integer id) {
        ownerService.deleteOwner(id);
        return ResponseEntity.status(200).body(new ApiResponse("Owner deleted successfully"));
    }
}
