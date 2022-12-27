package com.example.multi_datasources;

import com.example.multi_datasources.dto.UserCountDto;
import com.example.multi_datasources.dto.UsersDto;
import com.example.multi_datasources.model.PublicUser;
import com.example.multi_datasources.model.secondary.SecondaryUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MultiDatasourcesApplicationTests {
    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper om;

    private final HttpClient http = HttpClient.newHttpClient();

    @Test
    void contextLoads() throws IOException, InterruptedException {
        String username = Instant.now().toString();

        HttpResponse<String> resInsertUsers = insertUsers(username, username);
        assertEquals(200, resInsertUsers.statusCode());

        HttpResponse<String> resCountByName = countByName(username);
        UserCountDto resBodyCountByName = om.readValue(resCountByName.body(), UserCountDto.class);
        assertEquals(1, resBodyCountByName.getPublicUser());
        assertEquals(1, resBodyCountByName.getSecondaryUser());

        resInsertUsers = insertUsers(null, username);
        assertEquals(500, resInsertUsers.statusCode());

        resCountByName = countByName(username);
        resBodyCountByName = om.readValue(resCountByName.body(), UserCountDto.class);
        assertEquals(1, resBodyCountByName.getPublicUser());
        assertEquals(1, resBodyCountByName.getSecondaryUser());

        resInsertUsers = insertUsers(username, null);
        assertEquals(500, resInsertUsers.statusCode());

        resCountByName = countByName(username);
        resBodyCountByName = om.readValue(resCountByName.body(), UserCountDto.class);
        assertEquals(1, resBodyCountByName.getPublicUser());
        assertEquals(1, resBodyCountByName.getSecondaryUser());
    }

    HttpResponse<String> insertUsers(String publicName, String secondaryName) throws IOException, InterruptedException {
        PublicUser publicUser = new PublicUser();
        publicUser.setName(publicName);

        SecondaryUser secondaryUser = new SecondaryUser();
        secondaryUser.setName(secondaryName);

        UsersDto body = new UsersDto();
        body.setPublicUser(publicUser);
        body.setSecondaryUser(secondaryUser);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(uri("/api/insertUsers"))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(json(body)))
                .build();
        return http.send(req, HttpResponse.BodyHandlers.ofString());
    }

    HttpResponse<String> countByName(String name) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(uri("/api/countByName?name=" + name))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .GET()
                .build();
        return http.send(req, HttpResponse.BodyHandlers.ofString());
    }

    @SneakyThrows
    String json(Object obj) {
        return om.writeValueAsString(obj);
    }

    URI uri(String path) {
        return URI.create("http://localhost:" + port + path);
    }
}
