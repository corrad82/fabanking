package it.corradolombardi.fabanking.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static java.time.Month.DECEMBER;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DateIntervalTest {

    @Test
    void toAfterFromValidInterval() {
        DateInterval dateInterval = DateInterval.of(
                LocalDate.of(2020, DECEMBER, 17),
                LocalDate.of(2021, DECEMBER, 17));

        assertTrue(dateInterval.isValid());
    }

    @Test
    void toBeforeFromInValidInterval() {
        DateInterval dateInterval = DateInterval.of(
                LocalDate.of(2020, DECEMBER, 17),
                LocalDate.of(2019, DECEMBER, 17));

        assertFalse(dateInterval.isValid());
    }

    @Test
    void sameDayInvalidInterval() {
        DateInterval dateInterval = DateInterval.of(
                LocalDate.of(2020, DECEMBER, 17),
                LocalDate.of(2020, DECEMBER, 17));

        assertFalse(dateInterval.isValid());
    }
}