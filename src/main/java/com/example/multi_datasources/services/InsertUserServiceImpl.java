package com.example.multi_datasources.services;

import com.example.multi_datasources.dto.UserCountDto;
import com.example.multi_datasources.dto.UsersDto;
import com.example.multi_datasources.model.PublicUser;
import com.example.multi_datasources.model.secondary.SecondaryUser;
import com.example.multi_datasources.repo.publicUser.PublicUserRepository;
import com.example.multi_datasources.repo.secondaryUser.SecondaryUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Component
@EnableTransactionManagement
@Service
@RequiredArgsConstructor
@Slf4j
public class InsertUserServiceImpl implements InsertUserService {
    private final PublicUserRepository publicUserRepo;
    private final SecondaryUserRepository secondaryUserRepo;

    @Override
    @Transactional(
            value = "chainedTransactionManager",
            rollbackFor = {Exception.class},
            isolation = Isolation.READ_UNCOMMITTED
    )
    public UsersDto insertValidUsers(UsersDto dto) {
        PublicUser pUser = publicUserRepo.save(dto.getPublicUser());
        SecondaryUser sUser = secondaryUserRepo.save(dto.getSecondaryUser());
        return new UsersDto(pUser, sUser);
    }

    @Override
    @Transactional(
            value = "chainedTransactionManager",
            rollbackFor = {Exception.class},
            isolation = Isolation.READ_UNCOMMITTED
    )
    public UsersDto insertInvalidUsers(UsersDto dto) throws Exception {
        PublicUser pUser = publicUserRepo.save(dto.getPublicUser());
        SecondaryUser sUSer;
        if (StringUtils.isNotEmpty(dto.getSecondaryUser().getName())) {
            sUSer = secondaryUserRepo.save(dto.getSecondaryUser());
        } else
            throw new Exception("insertInvalidUsers: Name parameter must be present in users to save. Rolling back transaction.");

        return new UsersDto(pUser, sUSer);
    }

    @Transactional
    public UsersDto insertUsers(UsersDto dto) {
        PublicUser pUser = publicUserRepo.save(dto.getPublicUser());
        SecondaryUser sUser = secondaryUserRepo.save(dto.getSecondaryUser());
        return new UsersDto(pUser, sUser);
    }

    @Override
    public UserCountDto countByName(String name) {
        return UserCountDto.builder()
                .publicUser(publicUserRepo.countByName(name))
                .secondaryUser(secondaryUserRepo.countByName(name))
                .build();
    }
}
