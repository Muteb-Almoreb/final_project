package org.example.trucksy.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiException;
import org.example.trucksy.DTO.OwnerDTO;
import org.example.trucksy.Model.*;
import org.example.trucksy.Repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final FoodTruckRepository foodTruckRepository;
    private final AuthRepository authRepository;
    private final FoodTruckService foodTruckService;
    private final DashboardService dashboardService;
    private final DashboardRepository dashboardRepository;
    private final BankCardRepository bankCardRepository;
    private final PdfService pdfService;
    private final PdfMailService pdfMailService;

    @Value("${moyasar.api.key}")
    private String apiKey;
    private static final String MOYASAR_API_URL = "https://api.moyasar.com/v1/payments/";

    public void registerOwner(OwnerDTO ownerDTO) {
        User user = new User();
        user.setUsername(ownerDTO.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(ownerDTO.getPassword()));
        user.setEmail(ownerDTO.getEmail());
        user.setPhoneNumber(ownerDTO.getPhone());
        user.setRole("OWNER");
        authRepository.save(user);

        Owner owner = new Owner();
        owner.setSubscribed(false);
        owner.setUser(user);
        ownerRepository.save(owner);

        // this is to create dashboard for owner
        Dashboard dash = new Dashboard();
        dash.setOwner(owner);
        dash.setTotalOrders(0);
        dash.setTotalRevenue(0.0);
        dash.setPredictedOrders(0);
        dash.setPeakOrders("N/A");
        dash.setTopSellingItems(null);
        dash.setUpdateDate(LocalDate.now());
        owner.setDashboard(dash);
        dashboardRepository.save(dash);
    }

    public void updateOwner(Integer id ,OwnerDTO ownerDTO) {
        Owner owner = ownerRepository.findOwnerById(id);
        if (owner == null) {
            throw new ApiException("Owner not found");
        }
        User user = owner.getUser();
        user.setUsername(ownerDTO.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(ownerDTO.getPassword()));
        user.setEmail(ownerDTO.getEmail());
        user.setPhoneNumber(ownerDTO.getPhone());
        ownerRepository.save(owner);
    }

    public void deleteOwner(Integer id) {
        Owner owner = ownerRepository.findOwnerById(id);
        if (owner == null) {
            throw new ApiException("Owner not found");
        }
        authRepository.delete(owner.getUser());
    }

    @Transactional
    public ResponseEntity<?> ownerSubscribePayment(Integer OwnerId){
        //سعر الاشتراك الشهري 30 ريال
        Owner owner = ownerRepository.findOwnerById(OwnerId);
        if (owner == null) {
            throw new ApiException("Owner not found");
        }
        User user = owner.getUser();
        if (user.getBankCard() == null || (authRepository.findUserById(owner.getId())== null)) {
            throw new ApiException("Add Bank card to continue Payment ");
        }
        if(user.getBankCard().getAmount() < 30){
            throw new ApiException("Insufficient funds. Required: " + 30 + " SAR for Subscription");
        }
        String url = "https://api.moyasar.com/v1/payments/";
        String callbackUrl = "http://localhost:8080/api/v1/owner/callback/" + owner.getId();

        String requestBody = String.format(
                "source[type]=card" +
                        "&source[name]=%s" +
                        "&source[number]=%s" +
                        "&source[cvc]=%s" +
                        "&source[month]=%s" +
                        "&source[year]=%s" +
                        "&amount=%d" +
                        "&currency=%s" +
                        "&callback_url=%s",
                user.getBankCard().getName(),
                user.getBankCard().getNumber(),
                user.getBankCard().getCvc(),
                user.getBankCard().getMonth(),
                user.getBankCard().getYear(),
                (int) Math.round(30 * 100),
                user.getBankCard().getCurrency(),
                callbackUrl
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(apiKey, "");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(response.getBody());

            String paymentId = root.path("id").asText(null);
            String transactionUrl = root.path("source").path("transaction_url").asText(null);

            if (paymentId == null || transactionUrl == null) {
                throw new ApiException("Payment response missing required fields");
            }

            BankCard pr = user.getBankCard();
            pr.setPaymentUserId(paymentId);
            pr.setRedirectToCompletePayment(transactionUrl);
            bankCardRepository.save(pr);

            Map<String, String> result = new HashMap<>();
            result.put("payment_user_id", paymentId);
            result.put("transaction_url", transactionUrl);

            return ResponseEntity.status(response.getStatusCode()).body(result);

        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new ApiException("Failed to parse payment response JSON");
        }
    }

    @Transactional
    public ResponseEntity<?> handleSubscriptionPaymentCallback(Integer ownerId, String paymentIdFromQuery) {
        if (ownerId == null) {
            throw new ApiException("ownerId is required");
        }

        Owner owner = ownerRepository.findOwnerById(ownerId);
        if (owner == null) {
            throw new ApiException("Owner not found");
        }

        User user = owner.getUser();
        if (user == null || user.getBankCard() == null) {
            throw new ApiException("User/BankCard not found");
        }

        String paymentId = paymentIdFromQuery;
        if (paymentId == null || paymentId.isBlank()) {
            paymentId = user.getBankCard().getPaymentUserId();
        }
        if (paymentId == null || paymentId.isBlank()) {
            throw new ApiException("paymentId not provided and not found on BankCard");
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(apiKey, "");
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> resp = restTemplate.exchange(
                MOYASAR_API_URL + paymentId,
                HttpMethod.GET,
                entity,
                String.class
        );

        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(resp.getBody());

            String status = root.path("status").asText("");

            if (!"paid".equalsIgnoreCase(status)) {
                throw new ApiException("Payment not paid (status=" + status + ")");
            }

            double current = user.getBankCard().getAmount();
            double subscriptionFee = 30.0;
            double newAmount = current - subscriptionFee;
            if (newAmount < -0.001) {
                throw new ApiException("Insufficient funds at callback");
            }
            user.getBankCard().setAmount(newAmount);
            bankCardRepository.save(user.getBankCard());

            // Set owner as subscribed
            owner.setSubscribed(true);
            ownerRepository.save(owner);

            // Send subscription invoice email
            try {
                String ownerName = (user.getUsername() != null) ? user.getUsername() : "Owner";
                String ownerEmail = user.getEmail();

                if (ownerEmail != null && !ownerEmail.isBlank()) {
                    // Create template variables for subscription PDF generation
                    Map<String, Object> templateVars = new HashMap<>();

                    // Basic subscription info
                    templateVars.put("subscriptionId", "SUB-" + ownerId + "-" + LocalDate.now().getYear());
                    templateVars.put("subscriptionDate", LocalDate.now().toString());
                    templateVars.put("paymentId", paymentId);
                    templateVars.put("subscriptionStatus", "ACTIVE");

                    // Owner info
                    templateVars.put("ownerName", ownerName);
                    templateVars.put("ownerEmail", ownerEmail);

                    // Subscription details
                    templateVars.put("subscriptionPlan", "Monthly Premium");
                    templateVars.put("subscriptionFee", String.format("%.2f SAR", subscriptionFee));
                    templateVars.put("nextBillingDate", LocalDate.now().plusMonths(1).toString());

                    // Benefits
                    templateVars.put("benefits", "Premium Dashboard Analytics, Priority Support, Advanced Reporting");

                    // Generate subscription PDF
                    byte[] pdf = pdfService.generateSubscriptionInvoicePdf(templateVars);

                    String filename = "Trucksy-Subscription-Invoice-" + ownerId + ".pdf";
                    String subject = "Your Trucksy Subscription Invoice - Welcome to Premium!";

                    String html = String.format("""
                        <div style="font-family:Arial,Helvetica,sans-serif">
                          <h2 style="margin:0 0 8px 0;color:#ff6b35">Welcome to Trucksy Premium!</h2>
                          <p style="margin:0 0 12px 0">Your subscription payment has been processed successfully.</p>
                          <p style="margin:0 0 12px 0">Subscription ID: <b>SUB-%s-%s</b></p>
                          <p style="margin:0 0 12px 0">Amount paid: <b>%.2f SAR</b></p>
                          <p style="margin:0 0 12px 0">Next billing date: <b>%s</b></p>
                          <p style="margin:0 0 12px 0">We've attached your subscription invoice as a PDF.</p>
                          <h3 style="color:#ff6b35;margin:20px 0 8px 0">Premium Benefits:</h3>
                          <ul style="margin:0 0 12px 20px">
                            <li>Advanced dashboard analytics</li>
                            <li>Priority customer support</li>
                            <li>Detailed reporting features</li>
                            <li>Enhanced food truck management tools</li>
                          </ul>
                          <p style="color:#6b7280;font-size:12px;margin:16px 0 0 0">
                            If you didn't authorize this payment, please contact support immediately.
                          </p>
                        </div>
                        """, ownerId, LocalDate.now().getYear(), subscriptionFee, LocalDate.now().plusMonths(1).toString());

                    pdfMailService.sendHtmlEmailWithAttachment(
                            ownerEmail,
                            subject,
                            html,
                            filename,
                            pdf
                    );
                }
            } catch (Exception mailErr) {
                // Don't break the success flow if email fails
                System.err.println("Failed to send subscription invoice email: " + mailErr.getMessage());
            }

            Map<String, Object> result = new HashMap<>();
            result.put("ownerId", owner.getId());
            result.put("paymentId", paymentId);
            result.put("status", "subscribed");
            result.put("subscriptionActive", true);
            return ResponseEntity.ok(result);

        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new ApiException("Failed to parse Moyasar payment GET response");
        }
    }
}