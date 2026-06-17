package com.smalaca.trainingcenter.sales.query.offer;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "OFFERS")
public class OfferView {
    @Id
    @Column(name = "offer_id")
    private UUID offerId;

    @Column(name = "cart_id")
    private UUID cartId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "OFFER_ITEMS", joinColumns = @JoinColumn(name = "offer_id"))
    private List<OfferItemView> items;

    private LocalDateTime createdAt;
    private LocalDateTime validTo;

    private OfferView() {}

    public UUID getOfferId() {
        return offerId;
    }

    public UUID getCartId() {
        return cartId;
    }

    public List<OfferItemView> getItems() {
        return items;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getValidTo() {
        return validTo;
    }
}
