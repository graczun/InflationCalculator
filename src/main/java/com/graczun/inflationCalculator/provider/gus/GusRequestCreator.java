package com.graczun.inflationCalculator.provider.gus;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GusRequestCreator {

    private static final String SCHEMA = "https";
    private static final String HOST = "api-sdp.stat.gov.pl";
    private static final int PORT = 443;
    private static final String INFLATION_PER_MONTH_PATH = "api/indicators/indicator-data-indicator";
    private static final String INFLATION_POINTER_PARAMETER_NAME = "id-wskaznik";
    private static final String YEAR_PARAMETER_NAME = "id-rok";
    private static final String LANG_PARAMETER_NAME = "lang";
    private static final String LANG_PARAMETER_VALUE = "pl";
    private static final int POINTER_ID = 639;

    static HttpGet createGetRequestForYear(int year) {
        URIBuilder ub = new URIBuilder()
                .setScheme(SCHEMA)
                .setHost(HOST)
                .setPort(PORT)
                .setPath(INFLATION_PER_MONTH_PATH)
                .addParameter(INFLATION_POINTER_PARAMETER_NAME, Integer.toString(POINTER_ID))
                .addParameter(YEAR_PARAMETER_NAME, Integer.toString(year))
                .addParameter(LANG_PARAMETER_NAME, LANG_PARAMETER_VALUE);
        HttpGet getRequest = new HttpGet(ub.toString());
        getRequest.addHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
        getRequest.addHeader(HttpHeaders.HOST, HOST);
        return getRequest;
    }
}
