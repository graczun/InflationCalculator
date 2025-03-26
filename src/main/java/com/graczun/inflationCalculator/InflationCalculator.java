package com.graczun.inflationCalculator;

import com.graczun.inflationCalculator.provider.DataProvider;
import lombok.RequiredArgsConstructor;

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
        return (int) Math.floor(getInflationRateBetweenDates(yearMonth, YearMonth.now()) * value);
    }

    public Map<YearMonth, Integer> getInflationValueInDates(Integer value, YearMonth startYearMonth) {
        Map<YearMonth, Integer> result = new HashMap<>();
        double valueWithDigits = value.doubleValue();
        YearMonth currentYearMonth = YearMonth.now();
        Map<YearMonth, Double> inflationInYearMonth = dataProvider.getInflationRatesBetweenDates(startYearMonth, currentYearMonth);
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
