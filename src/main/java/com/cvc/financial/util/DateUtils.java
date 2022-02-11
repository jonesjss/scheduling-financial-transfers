package com.cvc.financial.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

public class DateUtils {
    private DateUtils() {}

    public static long differenceInDays(Temporal temp1, Temporal temp2) {
        LocalDate from = LocalDate.from(temp1);
        LocalDate until = LocalDate.from(temp2);

        return ChronoUnit.DAYS.between(from, until);
    }
}
