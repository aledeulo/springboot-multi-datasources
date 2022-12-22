package com.example.multi_datasources.dto;

import com.example.multi_datasources.model.PublicUser;
import com.example.multi_datasources.model.SecondaryUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsersDto {
    private PublicUser publicUser;
    private SecondaryUser secondaryUser;
}
