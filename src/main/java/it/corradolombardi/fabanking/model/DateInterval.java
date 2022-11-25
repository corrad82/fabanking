package it.corradolombardi.fabanking.model;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

import static java.time.LocalDate.now;
import static java.time.format.DateTimeFormatter.ISO_DATE;
import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
@ToString
public class DateInterval {
    private final LocalDate from;
    private final LocalDate to;
    public static DateInterval of(LocalDate from, LocalDate to) {
        return new DateInterval(from, to);
    }

    public static DateInterval of(String from, String to) {
        return of(LocalDate.parse(from), LocalDate.parse(to));
    }

    public boolean invalid() {
        return from.isAfter(now()) || !to.isAfter(from);
    }

    public String dateFrom() {
        return from.format(ISO_DATE);
    }

    public String dateTo() {
        return to.format(ISO_DATE);
    }
}
