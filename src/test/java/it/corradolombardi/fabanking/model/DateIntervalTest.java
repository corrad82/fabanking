package it.corradolombardi.fabanking.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class DateIntervalTest {

    @Test
    void toAfterFromValidInterval() {
        DateInterval dateInterval = DateInterval.of(
            LocalDate.parse("2020-12-17"),
            LocalDate.parse("2021-12-17"));

        assertFalse(dateInterval.invalid());
    }

    @Test
    void toBeforeFromInValidInterval() {
        DateInterval dateInterval = DateInterval.of(
            LocalDate.parse("2020-12-17"),
            LocalDate.parse("2019-12-17"));

        assertTrue(dateInterval.invalid());
    }

    @Test
    void sameDayInvalidInterval() {
        DateInterval dateInterval = DateInterval.of(
            LocalDate.parse("2020-12-17"),
            LocalDate.parse("2020-12-17"));

        assertTrue(dateInterval.invalid());
    }

    @Test
    void dateInFutureAreInvalid() {
        DateInterval dateInterval = DateInterval.of(
            LocalDate.now().plusDays(1),
            LocalDate.now().plusDays(10));

        assertTrue(dateInterval.invalid());
    }
}