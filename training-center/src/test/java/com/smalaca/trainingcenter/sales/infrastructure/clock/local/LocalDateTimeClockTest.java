package com.smalaca.trainingcenter.sales.infrastructure.clock.local;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class LocalDateTimeClockTest {
    @Test
    void shouldReturnCurrentLocalDateTime() {
        LocalDateTimeClock clock = new LocalDateTimeClock();

        LocalDateTime actual = clock.now();

        assertThat(actual)
                .isBeforeOrEqualTo(LocalDateTime.now())
                .isAfter(LocalDateTime.now().minusSeconds(1));
    }
}
