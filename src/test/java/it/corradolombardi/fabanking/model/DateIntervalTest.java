package it.corradolombardi.fabanking.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static java.time.Month.DECEMBER;
import static org.junit.jupiter.api.Assertions.*;

class DateIntervalTest {

    @Test
    void toAfterFromValidInterval() {
        LocalDate from = LocalDate.of(2020, DECEMBER, 17);
        LocalDate to = LocalDate.of(2021, DECEMBER, 17);
        DateInterval.of(from, to);
    }
}