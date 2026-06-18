package com.smalaca.trainingcenter.sales.application.offer;

import com.smalaca.trainingcenter.sales.domain.cart.CartId;
import com.smalaca.trainingcenter.sales.domain.clock.Clock;
import com.smalaca.trainingcenter.sales.domain.money.Money;
import com.smalaca.trainingcenter.sales.domain.offer.Offer;
import com.smalaca.trainingcenter.sales.domain.offer.OfferAssertion;
import com.smalaca.trainingcenter.sales.domain.offer.OfferId;
import com.smalaca.trainingcenter.sales.domain.offer.OfferRepository;
import com.smalaca.trainingcenter.sales.domain.offer.acceptance.OfferExpiredException;
import com.smalaca.trainingcenter.sales.domain.offer.acceptance.OfferWithoutItemsException;
import com.smalaca.trainingcenter.sales.domain.offer.acceptance.TrainingAlreadyStartedException;
import com.smalaca.trainingcenter.sales.domain.opentrainingservice.OpenTrainingService;
import com.smalaca.trainingcenter.sales.domain.order.Order;
import com.smalaca.trainingcenter.sales.domain.order.OrderAssertion;
import com.smalaca.trainingcenter.sales.domain.order.OrderRepository;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

class OfferApplicationServiceTest {
    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final TrainingId TRAINING_ID_1 = new TrainingId(UUID.randomUUID());
    private static final TrainingId TRAINING_ID_2 = new TrainingId(UUID.randomUUID());
    private static final Money PRICE = new Money(new BigDecimal("10.00"));

    private final OfferRepository offerRepository = mock(OfferRepository.class);
    private final OrderRepository orderRepository = mock(OrderRepository.class);
    private final Clock clock = mock(Clock.class);
    private final OpenTrainingService openTrainingService = mock(OpenTrainingService.class);
    private final OfferApplicationService service = new OfferApplicationServiceFactory()
            .offerApplicationService(offerRepository, orderRepository, clock, openTrainingService);

    @Test
    void shouldAcceptOffer() {
        OfferId offerId = existingOffer(TRAINING_ID_1, TRAINING_ID_2);
        given(clock.now()).willReturn(NOW);

        service.accept(new AcceptOfferCommand(offerId.value()));

        thenOfferSaved().hasOfferId(offerId).isAccepted();
    }

    private OfferAssertion thenOfferSaved() {
        ArgumentCaptor<Offer> captor = ArgumentCaptor.forClass(Offer.class);
        then(offerRepository).should().save(captor.capture());
        Offer actual = captor.getValue();

        return OfferAssertion.assertThat(actual);
    }

    @Test
    void shouldCreateOrderOnceOfferAccepted() {
        OfferId offerId = existingOffer(TRAINING_ID_1, TRAINING_ID_2);
        given(clock.now()).willReturn(NOW);

        service.accept(new AcceptOfferCommand(offerId.value()));

        thenOrderSaved()
                .hasOfferId(offerId)
                .hasItems(2)
                .hasItem(TRAINING_ID_1, PRICE)
                .hasItem(TRAINING_ID_2, PRICE)
                .hasOrderId();
    }

    private OrderAssertion thenOrderSaved() {
        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        then(orderRepository).should().save(captor.capture());
        Order savedOrder = captor.getValue();

        return OrderAssertion.assertThat(savedOrder);
    }

    @Test
    void shouldNotAcceptWhenOfferExpired() {
        OfferId offerId = existingOffer(TRAINING_ID_1);
        given(clock.now()).willReturn(NOW.plusDays(11));
        AcceptOfferCommand command = new AcceptOfferCommand(offerId.value());

        OfferExpiredException actual = assertThrows(OfferExpiredException.class, () -> service.accept(command));

        assertThat(actual).hasMessage("Offer: " + offerId + " expired.");
        thenOrderNotSaved();
        thenOfferNotSaved();
    }

    @Test
    void shouldNotAcceptWhenTrainingAlreadyStarted() {
        OfferId offerId = existingOffer(TRAINING_ID_1);
        given(clock.now()).willReturn(NOW);
        given(openTrainingService.hasAlreadyStarted(TRAINING_ID_1)).willReturn(true);
        AcceptOfferCommand command = new AcceptOfferCommand(offerId.value());

        TrainingAlreadyStartedException actual = assertThrows(TrainingAlreadyStartedException.class, () -> service.accept(command));

        assertThat(actual).hasMessage("Training: " + TRAINING_ID_1 + " already started.");
        thenOrderNotSaved();
        thenOfferNotSaved();
    }

    @Test
    void shouldNotAcceptWhenOfferWithoutItems() {
        OfferId offerId = existingOffer();
        AcceptOfferCommand command = new AcceptOfferCommand(offerId.value());

        OfferWithoutItemsException actual = assertThrows(OfferWithoutItemsException.class, () -> service.accept(command));

        assertThat(actual).hasMessage("Offer: " + offerId + " without items.");
        thenOrderNotSaved();
        thenOfferNotSaved();
    }

    private OfferId existingOffer(TrainingId... trainingIds) {
        Offer offer = offerWithItems(trainingIds);
        OfferId offerId = offer.getOfferId();
        given(offerRepository.findBy(offerId)).willReturn(offer);

        return offerId;
    }

    private void thenOrderNotSaved() {
        then(orderRepository).should(never()).save(any());
    }

    private void thenOfferNotSaved() {
        then(offerRepository).should(never()).save(any());
    }


    private Offer offerWithItems(TrainingId... trainingIds) {
        given(clock.now()).willReturn(NOW);
        Offer.Builder builder = new Offer.Builder(new CartId(UUID.randomUUID()), clock);
        for (TrainingId trainingId : trainingIds) {
            builder.item(trainingId, PRICE);
        }
        return builder.build();
    }
}
