package com.graczun.inflationCalculator.provider.gus;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graczun.inflationCalculator.provider.DataProvider;
import com.graczun.inflationCalculator.provider.gus.dto.GusInflationRatesResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.lineSeparator;
import static java.nio.charset.Charset.defaultCharset;
import static java.util.stream.Collectors.toMap;
import static org.apache.http.impl.client.HttpClients.createDefault;

@RequiredArgsConstructor
public class GusDataProvider implements DataProvider {

    private final CloseableHttpClient httpClient = createDefault();
    private final ObjectMapper mapper;

    @Override
    @SneakyThrows
    public Map<YearMonth, Double> getInflationRatesBetweenDates(YearMonth startYearMonth, YearMonth endYearMonth) {
        if (startYearMonth.isAfter(endYearMonth) || startYearMonth.equals(endYearMonth)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
        Map<YearMonth, Double> result = new HashMap<>();
        result.put(startYearMonth, 1.0);
        YearMonth secondMonth = startYearMonth.plusMonths(1);
        AtomicInteger year = new AtomicInteger(startYearMonth.getYear());
        do {
            getInflationPerMonthForYear(year.get()).entrySet()
                    .stream()
                    .map(entry -> Map.entry(YearMonth.of(year.get(), entry.getKey()), entry.getValue()))
                    .filter(entry -> entry.getKey().isBefore(endYearMonth) && !entry.getKey().isBefore(secondMonth))
                    .forEach(entry -> result.put(entry.getKey(), entry.getValue()));
        } while (year.getAndIncrement() < endYearMonth.getYear());

        return result;
    }

    private Map<Integer, Double> getInflationPerMonthForYear(int year) throws IOException {
        HttpGet request = GusRequestCreator.createGetRequestForYear(year);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            String body = getResponseContent(response, defaultCharset().name());
            if (response.getStatusLine().getStatusCode() > 300) {
                throw new IllegalStateException(String.format("Unable to get date from GUS - status code %d - [%s]", response.getStatusLine().getStatusCode(), body));
            }
            List<GusInflationRatesResponse> responses = mapper.readValue(body, new TypeReference<>() {
            });
            return responses.stream().collect(toMap(GusInflationRatesResponse::getMonth, GusInflationRatesResponse::getPercentageValue));
        }
    }


    @Override
    @SneakyThrows
    public void close() {
        httpClient.close();
    }

    private static String getResponseContent(CloseableHttpResponse response, String charset) throws IOException {
        StringBuilder responseMessage;
        try (BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), charset))) {
            String line;
            responseMessage = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                responseMessage.append(line).append(lineSeparator());
            }
        }
        return responseMessage.toString();
    }
}
