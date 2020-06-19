package com.sdet.code.exam;

import static io.restassured.RestAssured.get;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CountriesApiRestAssuredTest {

    @Test
    public void should_return_200() {
        get("https://restcountries.eu/rest/v2/name/USA").then().statusCode(200);
    }


    @Test
    public void should_return_valid_response() {
        String response = get("https://restcountries.eu/rest/v2/name/USA").then().statusCode(200).extract().asString();

        assertThat(response).isNotEmpty();
    }

    @Test
    public void should_return_valid_capital_for_usa() {
        get("https://restcountries.eu/rest/v2/name/USA").then().body(containsString("Washington, D.C."));
    }

    @Test
    public void should_return_capital_field_in_response() throws JsonMappingException, JsonProcessingException {
        String response = get("https://restcountries.eu/rest/v2/name/USA").then().statusCode(200).extract().body().asString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode actualObj =objectMapper.readTree(response);
        org.hamcrest.MatcherAssert.assertThat(actualObj.get(0).get("capital").toString(),is("\"Washington, D.C.\""));
    }

    @Test
    public void should_return_404_not_found() {
        get("https://restcountries.eu/rest/v2/name/1234").then().statusCode(404);
    }
}