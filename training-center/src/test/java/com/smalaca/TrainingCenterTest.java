package com.smalaca;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TrainingCenterTest {
    @Test
    void shouldRecognizeTrainingCenterIsWorking() {
        boolean actual = new TrainingCenter().isWorking();

        assertThat(actual).isTrue();
    }
}