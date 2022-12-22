package com.example.multi_datasources.repo.publicUser;

import com.example.multi_datasources.model.PublicUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicUserRepository extends JpaRepository<PublicUser, Long> {
}
