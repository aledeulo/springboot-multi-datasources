package com.example.multi_datasources.services;

import com.example.multi_datasources.dto.UserCountDto;
import com.example.multi_datasources.dto.UsersDto;

public interface InsertUserService {
    UsersDto insertUsers(UsersDto dto);

    UserCountDto countByName(String name);
}
