/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.root101.trmi_eltoque;

import dev.root101.trmi_eltoque.model.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Yo
 */
@Service
public class Client {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")
            .withZone(ZoneId.of("GMT-5"));

    @Value("${trmi.eltoque.auth_token}")
    private String elToqueToken;

    @Value("${trmi.eltoque.minutes_before}")
    private int minutesBefore;

    @Value("${trmi.eltoque.url}")
    private String url;

    @Value("${trmi.eltoque.url.header.date_from}")
    private String hedaer_dateFrom;

    @Value("${trmi.eltoque.url.header.date_to}")
    private String hedaer_dateTo;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<ElToque_Response> trmi() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(elToqueToken);

        HttpEntity entity = new HttpEntity(headers);

        Instant now = Instant.now();
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                ElToque_Response.class,
                Map.of(
                        hedaer_dateFrom, DATE_FORMATTER.format(
                                now.minus(
                                        minutesBefore,
                                        ChronoUnit.MINUTES
                                )
                        ),
                        hedaer_dateTo, DATE_FORMATTER.format(now)
                )
        );
    }
}
