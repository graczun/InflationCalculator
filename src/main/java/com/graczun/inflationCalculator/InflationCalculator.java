package com.graczun.inflationCalculator;

import com.graczun.inflationCalculator.provider.DataProvider;
import lombok.RequiredArgsConstructor;

import java.time.YearMonth;
import java.util.Map;

@RequiredArgsConstructor
public class InflationCalculator implements AutoCloseable {

    private final DataProvider dataProvider;

    public double getInflationRateBetweenDates(YearMonth startYearMonth, YearMonth endYearMonth) {
        Map<YearMonth, Double> inflationInYearMonth = dataProvider.getInflationRatesBetweenDates(startYearMonth, endYearMonth);
        return inflationInYearMonth.values().stream().reduce(1.0, (a, b) -> a * b) - 1.0;
    }

    @Override
    public void close() throws Exception {
        dataProvider.close();
    }

}
