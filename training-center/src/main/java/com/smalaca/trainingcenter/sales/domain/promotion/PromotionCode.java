package com.smalaca.trainingcenter.sales.domain.promotion;

public record PromotionCode(String code) {
    public boolean isValid() {
        return code != null && !code.isBlank();
    }
}
