package com.ceiba.tiendatecnologica.util;

import org.assertj.core.util.DateUtil;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Contiene métodos validados sobre el uso de fechas. Se implementan para verificar
 * el correcto funcionamiento de la lógica concerniente a fechas dentro
 *
 * @author William Torres
 * @since 2020
 * */
public class DateTestUtil {

    private DateTestUtil() {
    }

    public static boolean isSameDate(Date expect, Date actual) {
        return DateUtil.dayOfMonthOf(expect) == DateUtil.dayOfMonthOf(actual)
                && DateUtil.monthOf(expect) == DateUtil.monthOf(actual)
                && DateUtil.yearOf(expect) == DateUtil.yearOf(actual);
    }

    public static Date addDays(Date date, int days) {
        LocalDate result = convertToLocalDateViaInstant(date);
        result = result.plusDays(days);
        return convertToDateViaInstant(result);
    }

    public static Date addDaysSkippingMondays(Date date, int days) {
        LocalDate result = convertToLocalDateViaInstant(date);
        int addedDays = 0;
        while (addedDays < days) {
            result = result.plusDays(1);
            if (result.getDayOfWeek() != DayOfWeek.MONDAY) {
                ++addedDays;
            }
        }
        if(result.getDayOfWeek() == DayOfWeek.SUNDAY){
            result = result.plusDays(1);
        }
        return convertToDateViaInstant(result);
    }

    private static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private static Date convertToDateViaInstant(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

}
