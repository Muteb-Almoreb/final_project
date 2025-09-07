package org.example.trucksy.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiResponse;
import org.example.trucksy.Model.BankCard;
import org.example.trucksy.Model.User;
import org.example.trucksy.Service.BankCardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bankcard")
@RequiredArgsConstructor
public class BankCardController {

    private final BankCardService bankCardService;

    @PostMapping("/add") // todo after Security added remove userId from the path
    public ResponseEntity<?> addBankCard(@AuthenticationPrincipal User user,
                                         @Valid @RequestBody BankCard bankCard) {
        bankCardService.addBankCard(user.getId(), bankCard);
        return ResponseEntity.status(200).body(new ApiResponse("BankCard added successfully"));
    }

    @GetMapping("/get") // todo after Security added remove userId from the path
    public ResponseEntity<?> getBankCardByUserId(@AuthenticationPrincipal User user) {
        return ResponseEntity.status(200).body(bankCardService.getBankCardByUserId(user.getId()));
    }
}
