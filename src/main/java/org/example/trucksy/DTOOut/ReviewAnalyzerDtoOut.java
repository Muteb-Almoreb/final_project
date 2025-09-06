package org.example.trucksy.DTOOut;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewAnalyzerDtoOut {
    private Integer positiveComments;
    private Integer negativeComments;
    private Integer trueNegativeReviews;//بناء على تطابق الرايتنق مع الكومنت
    private Integer truePositiveReviews;
    private String  adviceBasedOnReviews;//نصيحه من الAI بناء على المراجعات كلها
    private String mostComplaintPoint;//اكثر نقطه تم الشكوى منها
}
