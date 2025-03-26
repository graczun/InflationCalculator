package com.graczun.inflationCalculator;

import com.graczun.inflationCalculator.provider.DataProvider;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class InflationCalculator implements AutoCloseable {

    private final DataProvider dataProvider;

    public double getInflationRateBetweenDates(YearMonth startYearMonth, YearMonth endYearMonth) {
        Map<YearMonth, Double> inflationInYearMonth = dataProvider.getInflationRatesBetweenDates(startYearMonth, endYearMonth);
        return inflationInYearMonth.values().stream().reduce(1.0, (a, b) -> a * b);
    }

    public Integer getCurrentValue(Integer value, YearMonth yearMonth) {
        LocalDate date = LocalDate.now();
        return (int) Math.floor(getInflationRateBetweenDates(yearMonth, YearMonth.of(date.getYear(), date.getMonthValue())) * value);
    }

    public Map<YearMonth, Integer> getInflationValueInDates(Integer value, YearMonth startYearMonth) {
        Map<YearMonth, Integer> result = new HashMap<>();
        LocalDate date = LocalDate.now();
        double valueWithDigits = value.doubleValue();
        YearMonth currentYearMonth = YearMonth.of(date.getYear(), date.getMonthValue());
        Map<YearMonth, Double> inflationInYearMonth = dataProvider.getInflationRatesBetweenDates(startYearMonth, YearMonth.of(date.getYear(), date.getMonthValue()));
        while (!currentYearMonth.isBefore(startYearMonth)) {
            valueWithDigits = valueWithDigits * inflationInYearMonth.getOrDefault(startYearMonth, 1.0);
            result.put(startYearMonth, (int) Math.floor(valueWithDigits));
            startYearMonth = startYearMonth.plusMonths(1);
        }
        return result;
    }

    @Override
    public void close() throws Exception {
        dataProvider.close();
    }

}
