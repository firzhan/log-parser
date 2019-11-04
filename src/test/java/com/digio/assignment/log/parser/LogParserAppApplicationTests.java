package com.digio.assignment.log.parser;

import com.digio.assignment.log.parser.model.LogStat;
import com.digio.assignment.log.parser.utils.LogParserUtils;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {LogParserAppApplication.class}, webEnvironment =
        SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class LogParserAppApplicationTests {

    private static final String API = "http://localhost:8080/log";

    @Test
    void contextLoads() {

    }

    @Test
    void uniqueIPsTest_WithinTimeRange() {

        createLogStat("177.71.128.21", "url1");
        createLogStat("177.71.128.21", "url2");

        String payload = "{\n" + "  \"startTime\": 10,\n" + "  \"endTime\": " +
                "10000000000000\n" + "}";
        given().contentType(ContentType.JSON).body(payload).post(API + "/ip" +
                "/unique").then().statusCode(HttpStatus.OK.value()).body(
                        "count", equalTo(1));
        ;
    }

    @Test
    void uniqueIPsTest_OutOfTimeRange() {

        createLogStat("177.71.128.21", "url1");
        createLogStat("177.71.128.21", "url2");

        //get(API + "/ip/unique").then().assertThat().statusCode(HttpStatus
        // .OK.value()).body("count", equalTo(1));
        String payload = "{\n" + "  \"startTime\": 10000000000000,\n" + "  " +
                "\"endTime\": 10\n" + "}";
        given().contentType(ContentType.JSON).body(payload).post(API + "/ip" +
                "/unique").then().statusCode(HttpStatus.OK.value()).body(
                        "count", equalTo(0));
    }

    @Test
    void mostVisitedURLTest() {

        createLogStat("177.71.128.21", "url1");
        createLogStat("177.71.128.21", "url2");
        createLogStat("177.71.128.21", "url3");
        createLogStat("177.71.128.21", "url4");
        createLogStat("177.71.128.21", "url1");
        createLogStat("177.71.128.21", "url2");
        createLogStat("177.71.128.21", "url3");
        createLogStat("177.71.128.21", "url4");
        createLogStat("177.71.128.21", "url1");
        createLogStat("177.71.128.21", "url2");
        createLogStat("177.71.128.21", "url3");
        createLogStat("177.71.128.21", "url4");
        createLogStat("177.71.128.21", "url1");
        createLogStat("177.71.128.21", "url2");
        createLogStat("177.71.128.21", "url3");

        String payload = "{\n" + "  \"startTime\": 10,\n" + "  \"endTime\": " +
                "10000000000000,\n" + "  \"count\": 3\n" + "}";
        List<String> urlList =
                given().contentType(ContentType.JSON).body(payload).post(API + "/url/unique").then().extract().jsonPath().getList("urls.url");
        assertFalse(urlList.contains("url4"));
    }

    @Test
    void activeIPsTest() {

        createLogStat("177.71.128.21", "url1");
        createLogStat("177.71.128.22", "url2");
        createLogStat("177.71.128.23", "url3");
        createLogStat("177.71.128.24", "url4");
        createLogStat("177.71.128.21", "url1");
        createLogStat("177.71.128.22", "url2");
        createLogStat("177.71.128.23", "url3");
        createLogStat("177.71.128.24", "url4");
        createLogStat("177.71.128.21", "url1");
        createLogStat("177.71.128.22", "url2");
        createLogStat("177.71.128.23", "url3");
        createLogStat("177.71.128.24", "url4");
        createLogStat("177.71.128.21", "url1");
        createLogStat("177.71.128.22", "url2");
        createLogStat("177.71.128.23", "url3");

        String payload = "{\n" + "  \"startTime\": 10,\n" + "  \"endTime\": " +
                "10000000000000,\n" + "  \"count\": 3\n" + "}";
        List<String> ipList =
                given().contentType(ContentType.JSON).body(payload).post(API + "/ip/active").then().extract().jsonPath().getList("ips.ip");

        assertFalse(ipList.contains("177.71.128.24"));
        assertTrue(ipList.contains("177.71.128.23"));
    }

    private void createLogStat(String ip, String url) {

        LogStat logStat = new LogStat();
        logStat.setIp(ip);
        logStat.setUrl(url);
        try {
            logStat.setEventTime(LogParserUtils.getCurrentEpochTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Response response =
                given().contentType(MediaType.APPLICATION_JSON_VALUE).body(logStat).post(API);
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
    }

}
