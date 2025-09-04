package org.example.trucksy.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiResponse;
import org.example.trucksy.DTO.OwnerDTO;
import org.example.trucksy.Service.OwnerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/owner")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

    @PostMapping("/add")
    public ResponseEntity<?> registerOwner(@Valid @RequestBody OwnerDTO ownerDTO) {
        ownerService.registerOwner(ownerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Owner registered successfully"));
    }
}
