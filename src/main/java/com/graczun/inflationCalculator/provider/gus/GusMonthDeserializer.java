package com.graczun.inflationCalculator.provider.gus;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class GusMonthDeserializer extends JsonDeserializer<Integer> {

    private static final Integer MONTH_INDEX_SHIFT = 246;

    @Override
    public Integer deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        int originalType = jsonParser.getIntValue();
        return originalType - MONTH_INDEX_SHIFT;
    }
}
