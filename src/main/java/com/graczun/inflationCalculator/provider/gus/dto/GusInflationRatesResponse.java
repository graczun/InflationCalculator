package com.graczun.inflationCalculator.provider.gus.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.graczun.inflationCalculator.provider.gus.GusMonthDeserializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GusInflationRatesResponse {
    @JsonAlias("wartosc")
    private double value;
    @JsonAlias("id-daty")
    private int year;
    @JsonAlias("id-okres")
    @JsonDeserialize(using = GusMonthDeserializer.class)
    private int month;

    public double getPercentageValue() {
        return value / 100.0;
    }
}
