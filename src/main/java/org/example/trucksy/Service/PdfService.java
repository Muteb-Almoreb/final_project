package org.example.trucksy.Service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.example.trucksy.Model.Order;
import org.example.trucksy.Model.OrderLine;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PdfService {

    private final TemplateEngine templateEngine;

    public PdfService(TemplateEngine templateEngine){
        this.templateEngine = templateEngine;
    }

    // Generic HTML -> PDF
    public byte[] generatePdf(String templateName, Map<String,Object> data){
        try {
            Context context = new Context();
            if (data != null) context.setVariables(data);

            String html = templateEngine.process(templateName, context);

            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                PdfRendererBuilder builder = new PdfRendererBuilder();
                builder.useFastMode();
                builder.withHtmlContent(html, null);
                builder.toStream(baos);
                builder.run();
                return baos.toByteArray();
            }
        } catch (Exception e){
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }

    // Method specifically for generating invoice PDFs - matches OrderService call
    public byte[] generateInvoicePdf(Map<String, Object> templateVars) {
        return generatePdf("OrderInvoice", templateVars);
    }

    // NEW: Method specifically for generating subscription invoice PDFs
    public byte[] generateSubscriptionInvoicePdf(Map<String, Object> templateVars) {
        try {
            return generatePdf("SubscriptionInvoice", templateVars);
        } catch (RuntimeException e) {
            // Fallback: if SubscriptionInvoice template doesn't exist, use OrderInvoice as fallback
            if (e.getMessage().contains("template might not exist")) {
                System.err.println("SubscriptionInvoice template not found, using OrderInvoice as fallback");
                return generatePdf("OrderInvoice", templateVars);
            }
            throw e;
        }
    }

    // Build receipt for AI purchase (one-time) - keeping your existing method
    public byte[] buildAiPurchaseReceipt(String paymentId,
                                         String userName,
                                         String userEmail,
                                         double amount) {
        Map<String, Object> vars = Map.of(
                "subscriptionId", paymentId,
                "userName", userName,
                "userEmail", userEmail,
                "serviceName", "AI SubsTracker",
                "planName", "Lifetime",
                "period", "one-time",
                "priceFormatted", formatMoney(amount),
                "totalFormatted", formatMoney(amount),
                "date", LocalDate.now().toString()
        );
        return generatePdf("subscription-receipt", vars);
    }

    // Build Trucksy order invoice using Order object - alternative method
    public byte[] buildAiPurchaseReceipt(String paymentId,
                                         String customerName,
                                         String customerEmail,
                                         double totalPrice,
                                         Order order) {

        // Build order lines for the template
        List<Map<String, Object>> orderLines = new ArrayList<>();
        if (order.getLines() != null) {
            for (OrderLine line : order.getLines()) {
                Map<String, Object> lineData = new HashMap<>();
                lineData.put("itemName", line.getItem() != null ? line.getItem().getName() : "Unknown Item");
                lineData.put("quantity", line.getQuantity());
                lineData.put("unitPrice", formatMoney(line.getUnitPriceAtPurchase()));
                lineData.put("lineTotal", formatMoney(line.getUnitPriceAtPurchase() * line.getQuantity()));
                orderLines.add(lineData);
            }
        }

        // Prepare template variables to match OrderInvoice.html
        Map<String, Object> vars = new HashMap<>();
        vars.put("orderId", order.getId().toString());
        vars.put("paymentId", paymentId);
        vars.put("creationDate", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        vars.put("clientName", customerName);
        vars.put("customerEmail", customerEmail);
        vars.put("foodTruckName", order.getFoodTruck() != null ? order.getFoodTruck().getName() : "Unknown Food Truck");
        vars.put("totalPriceFormatted", formatMoney(totalPrice));
        vars.put("orderLines", orderLines);
        vars.put("orderStatus", order.getStatus());

        // Generate PDF using the OrderInvoice template
        return generatePdf("OrderInvoice", vars);
    }

    private static String formatMoney(double amount) {
        return String.format("%.2f SAR", amount);
    }
}