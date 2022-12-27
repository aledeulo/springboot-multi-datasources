package com.example.multi_datasources.repo.secondaryUser;

import com.example.multi_datasources.model.secondary.SecondaryUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecondaryUserRepository extends JpaRepository<SecondaryUser, Long> {
}
