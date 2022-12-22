package com.example.multi_datasources.controllers;

import com.example.multi_datasources.dto.UsersDto;
import com.example.multi_datasources.services.InsertUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AppController {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final InsertUserService insertUserService;

    @PostMapping("/insertValidUsers")
    public ResponseEntity<UsersDto> insertValidUsers(@RequestBody UsersDto dto) throws JsonProcessingException {
        log.info("insertValidUsers: Received request: {}", objectMapper.writeValueAsString(dto));
        return ResponseEntity.ok().body(insertUserService.insertValidUsers(dto));
    }

    @PostMapping("/insertInvalidUsers")
    public ResponseEntity<UsersDto> insertInvalidUsers(@RequestBody UsersDto dto) throws Exception {
        log.info("insertValidUsers: Received request: {}", objectMapper.writeValueAsString(dto));
        return ResponseEntity.ok().body(insertUserService.insertInvalidUsers(dto));
    }
}
