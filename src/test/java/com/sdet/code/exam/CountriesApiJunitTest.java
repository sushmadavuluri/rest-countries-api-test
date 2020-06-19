package com.sdet.code.exam;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

/**
 * Unit test for Countries RESTful App.
 */
class CountriesApiJunitTest {

    /**
     * Happy Path.
     */
    @Test
    public void should_return_success_and_get_capital_for_usa() {
        Map<String, Object> responseMap = invokeWebService("https://restcountries.eu/rest/v2/name/USA");
        JsonNode actualObj = (JsonNode) responseMap.get("responseObj");
        assertNotNull(actualObj);
        assertThat(responseMap.get("status").toString(), is("200"));
        assertEquals("\"Washington, D.C.\"", actualObj.get(0).get("capital").toString());
    }

    /**
     * Sad Path.
     */
    @Test
    public void should_return_not_found_for_invalid_input() {
        Map<String, Object> responseMap = invokeWebService("https://restcountries.eu/rest/v2/name/111");
        JsonNode actualObj = (JsonNode) responseMap.get("responseObj");
        assertNotNull(actualObj);
        assertThat(responseMap.get("status").toString(), is("404"));
    }

    /**
     * Consumer code to call Countries RESTful API for the given endPoint
     * 
     * @param endPoint -- which represent the unique request locator for accessing
     *                 counties info
     * @return
     */
    private Map<String, Object> invokeWebService(String endPoint) {
        JsonNode actualObj = null;
        Map<String, Object> responseHashMap = new HashMap<>();
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet request = new HttpGet(endPoint);
            CloseableHttpResponse response = httpClient.execute(request);

            ObjectMapper objectMapper = new ObjectMapper();

            actualObj = objectMapper.readTree(EntityUtils.toString(response.getEntity()));

            responseHashMap.put("responseObj", actualObj);
            responseHashMap.put("status", response.getStatusLine().getStatusCode());
        } catch (IOException ioException) {
            System.out.println("Error while invoking the countries web service" + ioException);
        }
        return responseHashMap;
    }
}
