package org.example.trucksy.DTOOut;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashBoardAnalyzerDtoOut {

    // Backward compatibility
    private String bestSpotToPark;
    private String adviceBasedOnTheDashboard;
    private String adviceOnItemDescription;

    // KPIs
    private Integer totalOrders;
    private Integer totalCompletedOrders;
    private Integer predictedOrders;
    private Double totalRevenue;
    private Double avgOrderValue;
    private Double grossMarginPct;
    private Double repeatCustomerRate;
    private Double conversionRate;
    private Double cancelRate;
    private Double avgPrepTimeSec;
    private Double queueLenAvg;
    private Double tipsTotal;

    // Impact indices
    private Double weatherImpactIndex;
    private Double eventImpactIndex;

    // Confidence & context
    private Double confidence;
    private Boolean riyadhOnly;
    private String analysisPeriodFrom;
    private String analysisPeriodTo;
}
