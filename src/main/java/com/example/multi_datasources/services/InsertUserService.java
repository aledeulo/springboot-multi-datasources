package com.example.multi_datasources.services;

import com.example.multi_datasources.dto.UsersDto;

public interface InsertUserService {
    UsersDto insertValidUsers(UsersDto dto);
    UsersDto insertInvalidUsers(UsersDto dto) throws Exception;
}
