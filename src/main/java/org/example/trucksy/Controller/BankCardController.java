package org.example.trucksy.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiResponse;
import org.example.trucksy.Model.BankCard;
import org.example.trucksy.Service.BankCardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bankcard")
@RequiredArgsConstructor
public class BankCardController {

    private final BankCardService bankCardService;

    @PostMapping("/add/{userId}") // todo after Security added remove userId from the path
    public ResponseEntity<?> addBankCard(@PathVariable Integer userId,
                                         @Valid @RequestBody BankCard bankCard) {
        bankCardService.addBankCard(userId, bankCard);
        return ResponseEntity.status(200).body(new ApiResponse("BankCard added successfully"));
    }

    @GetMapping("/get/{userId}") // todo after Security added remove userId from the path
    public ResponseEntity<?> getBankCardByUserId(@PathVariable Integer userId) {
        return ResponseEntity.status(200).body(bankCardService.getBankCardByUserId(userId));
    }
}
