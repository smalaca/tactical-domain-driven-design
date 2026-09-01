package com.smalaca.trainingcenter.sales.infrastructure.promotion.dummy;

import com.smalaca.annotations.architecture.PortsAndAdaptersArchitecture;
import com.smalaca.trainingcenter.sales.domain.promotion.PromotionCode;
import com.smalaca.trainingcenter.sales.domain.promotion.PromotionService;
import org.springframework.stereotype.Service;

@PortsAndAdaptersArchitecture.DrivenAdapter
@Service
public class DummyPromotionService implements PromotionService {
    @Override
    public boolean isAvailable(PromotionCode promotionCode) {
        return false;
    }
}
