package it.corradolombardi.fabanking.model;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public class DateInterval {
    private final LocalDate from;
    private final LocalDate to;
    public static DateInterval of(LocalDate from, LocalDate to) {
        return new DateInterval(from, to);
    }

    public boolean isValid() {
        return to.isAfter(from);
    }
}
