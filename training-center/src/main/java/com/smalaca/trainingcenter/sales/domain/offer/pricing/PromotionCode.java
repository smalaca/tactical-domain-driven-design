package com.smalaca.trainingcenter.sales.domain.offer.pricing;

import com.smalaca.annotations.architecture.DomainDrivenDesign;

@DomainDrivenDesign.ValueObject
public record PromotionCode(String code) {
    public static final PromotionCode NO_CODE = new PromotionCode(null);

    public static PromotionCode of(String code) {
        return new PromotionCode(code);
    }

    public static PromotionCode none() {
        return NO_CODE;
    }

    public boolean isValid() {
        return code != null && !code.isBlank();
    }
}
