package com.graczun.inflationCalculator.provider;

import java.time.YearMonth;
import java.util.Map;

public interface DataProvider extends AutoCloseable {

    Map<YearMonth, Double> getInflationRatesBetweenDates(YearMonth startYearMonth, YearMonth endYearMonth);
}
