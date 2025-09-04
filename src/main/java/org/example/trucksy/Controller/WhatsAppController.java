package org.example.trucksy.Controller;

import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiException;
import org.example.trucksy.Api.ApiResponse;
import org.example.trucksy.Service.WhatsAppService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/whatsApp")
@RequiredArgsConstructor
public class WhatsAppController {

    private final WhatsAppService whatsAppService;


    @PostMapping("/send-text")
    public ResponseEntity<?> sendText(@RequestBody WhatsAppService.SendTextRequest req) {
        if (req == null) throw new ApiException("body is required");
        whatsAppService.sendText(req.getTo(), req.getBody());
        return ResponseEntity.ok(new ApiResponse("WhatsApp text sent"));
    }

}
