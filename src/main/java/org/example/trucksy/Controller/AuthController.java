package org.example.trucksy.Controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiResponse;
import org.example.trucksy.Service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // this service for admin

    @GetMapping("/get")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.status(200).body(authService.getAllUsers());
    }

    @DeleteMapping("/delete/{user_id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer user_id) {
        authService.deleteUser(user_id);
        return ResponseEntity.status(200).body(new ApiResponse("User deleted successfully"));
    }

    @GetMapping("/get-all-owners")
    public ResponseEntity<?> getAllOwners() {
        return ResponseEntity.status(200).body(authService.getAllOwners());
    }
}
