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

	//Scenario 1: Sending Country name  and checking status code
    @Test
    public void should_return_200() {
        get("https://restcountries.eu/rest/v2/name/USA").then().statusCode(200);
    }

  //Scenario 2: Sending country name and checking response have data or not
    @Test
    public void should_return_valid_response() {
        String response = get("https://restcountries.eu/rest/v2/name/USA").then().statusCode(200).extract().asString();

        assertThat(response).isNotEmpty();
    }

  //Scenario 3: Sending country name  and checking of response contains capital or not
    @Test
    public void should_return_valid_capital_for_usa() {
        get("https://restcountries.eu/rest/v2/name/USA").then().body(containsString("Washington, D.C."));
    }
    
  //Scenario 4: Sending country name  and checking if it returned correct capital name or not
    @Test
    public void should_return_capital_field_in_response() throws JsonMappingException, JsonProcessingException {
        String response = get("https://restcountries.eu/rest/v2/name/USA").then().statusCode(200).extract().body().asString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode actualObj =objectMapper.readTree(response);
        org.hamcrest.MatcherAssert.assertThat(actualObj.get(0).get("capital").toString(),is("\"Washington, D.C.\""));
    }

    
    //Scenario 5: Sending country code  and checking if it returned correct capital name or not
    @Test
    public void should_return_capital_field_in_response_for_countrycode() throws JsonMappingException, JsonProcessingException {
        String response = get("https://restcountries.eu/rest/v2/alpha/FR").then().statusCode(200).extract().body().asString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode actualObj =objectMapper.readTree(response);
        org.hamcrest.MatcherAssert.assertThat(actualObj.get("capital").toString(),is("\"Paris\""));
    }
    

    //Scenario 6: Sending invalid input and checking if it is returning correct status code or not
    @Test
    public void should_return_404_not_found() {
        get("https://restcountries.eu/rest/v2/name/1234").then().statusCode(404);
    }
}