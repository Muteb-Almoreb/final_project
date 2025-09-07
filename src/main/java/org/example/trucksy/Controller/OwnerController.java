package org.example.trucksy.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiResponse;
import org.example.trucksy.DTO.OwnerDTO;
import org.example.trucksy.Model.User;
import org.example.trucksy.Service.OwnerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/update")
    public ResponseEntity<?> updateOwner(@AuthenticationPrincipal User user, @Valid @RequestBody OwnerDTO ownerDTO) {
        ownerService.updateOwner(user.getId(), ownerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Owner updated successfully"));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteOwner(@AuthenticationPrincipal User user) {
        ownerService.deleteOwner(user.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Owner deleted successfully"));
    }

    // Subscription payment endpoint - following the same style as addOrder
    @PostMapping("/subscribe") // todo after Security added remove ownerId from the path
    public ResponseEntity<?> subscribeOwner(@AuthenticationPrincipal User user) {
        // NOTE: Return service ResponseEntity directly to avoid double-wrapping.
        return ownerService.ownerSubscribePayment(user.getId());
    }

    //todo test the call manually and after deployment it will call directly from subscribe endpoint with the Callback Url
    //1) paymentId: POST /api/v1/owner/callback/{ownerId} use this method after deploy -> so this is for production env
    @PostMapping("/callback/{ownerId}")
    public ResponseEntity<?> subscriptionCallbackNoPid(@PathVariable Integer ownerId) {
        // NOTE: Controller path is /api/v1/owner/callback/{ownerId}
        return ownerService.handleSubscriptionPaymentCallback(ownerId, null);
    }

    // 2) paymentId: POST /api/v1/owner/callback/{ownerId}/{paymentId} -> and this for developer env
    @PostMapping("/callback/{ownerId}/{paymentId}")
    public ResponseEntity<?> subscriptionCallbackWithPid(
            @PathVariable Integer ownerId,
            @PathVariable String paymentId
    ) {
        // NOTE: Controller path is /api/v1/owner/callback/{ownerId}/{paymentId}
        return ownerService.handleSubscriptionPaymentCallback(ownerId, paymentId);
    }
}