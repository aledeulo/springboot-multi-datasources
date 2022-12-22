package com.example.multi_datasources.dbConfig;

import com.example.multi_datasources.model.PublicUser;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.example.multi_datasources.repo.secondaryUser",
        entityManagerFactoryRef = "secondaryUserEntityManagerFactory",
        transactionManagerRef= "secondaryUserTransactionManager"
)
public class SecondaryDataSourceConfig {
    @Bean
    @ConfigurationProperties("spring.datasource.secondary")
    public DataSourceProperties secondaryUserDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource secondaryUserDataSource() {
        return secondaryUserDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean(name = "secondaryUserEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean secondaryUserEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(secondaryUserDataSource())
                .packages(PublicUser.class)
                .build();
    }

    @Bean
    public PlatformTransactionManager secondaryUserTransactionManager(
            final @Qualifier("secondaryUserEntityManagerFactory") LocalContainerEntityManagerFactoryBean secondaryUserEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(secondaryUserEntityManagerFactory.getObject()));
    }
}
