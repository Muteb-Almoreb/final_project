package org.example.trucksy.Service;



import kong.unirest.HttpResponse;
import kong.unirest.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.trucksy.Api.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Setter
@Getter
@Service
@RequiredArgsConstructor
public class WhatsAppService {

    @Value("${ultramsg.instance-id}")
    private String instanceId;

    @Value("${ultramsg.token}")
    private String token;

    @Value("${ultramsg.base-url}")
    private String baseUrl; // e.g. https://api.ultramsg.com

//    private void sendWhatsAppMessage(String to, String message) {
//        try {
//            Unirest.setTimeouts(0, 0);
//            HttpResponse<String> response =
//                    Unirest.post("https://api.ultramsg.com/instance103253/messages/chat")
//                                    .header("Content-Type",
//                                            "application/x-www-form-urlencoded")
//                                    .field("token", "66e2lh7c7hkrtj3o")
//                                    .field("to", to)
//                                    .field("body", message)
//                                    .asString();
//        } catch (Exception e) {
//            throw new ApiException("Failed to send WhatsApp message: " + e.getMessage());
//        }




    public void sendText(String to, String body) {
        if (to == null || to.isBlank())  throw new ApiException("to is required");
        if (body == null || body.isBlank()) throw new ApiException("body is required");

//        // مثال لضبط المهلات في Unirest (اختياري)
//        Unirest.config()
//                .connectTimeout(10_000)
//                .socketTimeout(10_000);

        String url = String.format("%s/%s/messages/chat", baseUrl, instanceId);

        try {
            HttpResponse<JsonNode> resp = Unirest.post(url)
                    .field("token", token)
                    .field("to", to)         // 9665XXXXXXXX بدون +
                    .field("body", body)
                    .asJson();

            int status = resp.getStatus();
            if (status < 200 || status >= 300) {
                throw new ApiException("UltraMsg error: HTTP " + status + " - " + resp.getBody());
            }

        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException("Failed to call UltraMsg: " + e.getMessage());
        }
    }


    @Data
    public static class SendTextRequest {
        private String to;     // 9665XXXXXXXX (بدون +)
        private String body;   // نص الرسالة
    }
}
